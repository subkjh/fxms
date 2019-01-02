package com.fxms.ui.node.dxeditor;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.bas.vo.ui.UiGroupVo;
import com.fxms.ui.bas.vo.ui.UiPropertyVo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

public class DxEditor extends BorderPane {

	private AnchorPane dxPane;
	private DxItemEvent event;
	private DxEditorContextMenu menu;
	private final ToggleGroup btnGroup;
	private UiGroupVo groupVo;
	private final TextField txtGroupName;
	private FxCallback<String> saveCallback;

	public DxEditor() {
		this(-1, null);
	}

	public DxEditor(int uiGroupNo, FxCallback<String> saveCallback) {

		this.saveCallback = saveCallback;

		dxPane = new AnchorPane();
		menu = new DxEditorContextMenu(this);
		event = new DxItemEvent(this);
		txtGroupName = new TextField();
		txtGroupName.setPromptText("화면이름을 입력하세요");

		btnGroup = new ToggleGroup();

		ScrollPane sp = new ScrollPane();

		sp.setContent(dxPane);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setContextMenu(menu);

		setTop(makeTop(uiGroupNo));
		setCenter(sp);

		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		setPrefSize(bounds.getWidth(), bounds.getHeight() * .8);

		setUiGroupNo(uiGroupNo);

	}

	public void addNode(Node node) {

		dxPane.getChildren().add(node);

		if (node instanceof DxEditItem) {
			event.addEvent((DxEditItem) node);

			((Node) node).setOnContextMenuRequested((ContextMenuEvent event) -> {
				menu.setNode((DxEditItem) node);
			});
			
			((DxEditItem) node).setText();
		}

	}

	public DxEditorContextMenu getMenu() {
		return menu;
	}

	public UiGroupVo getUiGroupVo() {

		if (groupVo == null) {
			groupVo = new UiGroupVo();
			groupVo.setUiGroupNo((int) btnGroup.getSelectedToggle().getUserData());
			groupVo.setUiGroupName("index-" + groupVo.getUiGroupNo());
		} else {
			groupVo.getChildren().clear();
		}

		if (txtGroupName.getText().length() > 0) {
			groupVo.setUiGroupName(txtGroupName.getText());
		}

		int index = 1;
		UiBasicVo vo;
		DxEditItem item;
		for (Node node : dxPane.getChildren()) {
			if (node instanceof DxEditItem) {
				item = (DxEditItem) node;
				vo = new UiBasicVo(item.getBounds());
				vo.setOpNo(item.getOpCode().getOpNo());
				vo.setUiNo(index++);
				vo.setSeqBy(vo.getUiNo());
				vo.setUiStyle("NONE");
				vo.setUiTitle(item.getOpCode().getOpTitle());

				for (String key : item.getDxItemProperties().keySet()) {
					vo.getChildren().add(new UiPropertyVo(groupVo.getUiGroupNo(), vo.getUiNo(), key,
							item.getDxItemProperties().get(key) + ""));
				}

				groupVo.getChildren().add(vo);
			}
		}

		return groupVo;
	}

	public void removeNode(Node node) {
		dxPane.getChildren().remove(node);
	}

	private Node makeTop(int uiGroupNo) {

		HBox hBox = new HBox(5);
		hBox.setPadding(new Insets(10, 10, 10, 10));

		for (int i = 1; i <= 8; i++) {
			ToggleButton tb = new ToggleButton(String.valueOf(i));
			tb.setPrefWidth(50);
			tb.setUserData(i);
			tb.setToggleGroup(btnGroup);
			tb.setSelected(i == uiGroupNo);
			hBox.getChildren().add(tb);
		}

		btnGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if (new_toggle != null) {
					setUiGroupNo((int) new_toggle.getUserData());
				}
			}
		});

		Button bSave = new Button("Save");
		bSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				UiGroupVo data = getUiGroupVo();

				UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.UserUiSet);
				DxAsyncSelector.getSelector().callMethod(bSave, opCode, data.getSaveMap());

				CodeMap.getMap().reloadUi();

				if (saveCallback != null) {
					saveCallback.onCallback("");
				}
			}
		});

		Button bSaveAs = new Button("Save as");
		bSaveAs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				UiGroupVo data = getUiGroupVo();

				UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.UserUiSet);
				DxAsyncSelector.getSelector().callMethod(bSaveAs, opCode, data.getSaveMap());

				CodeMap.getMap().reloadUi();

			}
		});

		Button bPreview = new Button("Preview");
		bPreview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FxDialog.showDialog(bPreview, new DxPane(getUiGroupVo()), "Preview DX");
			}
		});

		hBox.getChildren().add(new Separator(Orientation.VERTICAL));
		hBox.getChildren().add(txtGroupName);
		hBox.getChildren().add(new Separator(Orientation.VERTICAL));
		hBox.getChildren().add(bSave);
		hBox.getChildren().add(bSaveAs);
		hBox.getChildren().add(new Separator(Orientation.VERTICAL));
		hBox.getChildren().add(bPreview);

		return hBox;
	}

	private void setUiGroupNo(int uiGroupNo) {

		dxPane.getChildren().clear();

		UiOpCodeVo opcode;

		for (UiGroupVo vo : CodeMap.getMap().getUiList()) {
			if (vo.getUiGroupNo() == uiGroupNo) {

				txtGroupName.setText(vo.getUiGroupName());

				for (UiBasicVo ui : vo.getChildren()) {
					opcode = CodeMap.getMap().getOpCode(ui.getOpNo());
					if (opcode != null) {
						DxEditItem node = new DxEditItem(opcode, ui.getUiX(), ui.getUiY(), ui.getUiWidth(),
								ui.getUiHeight());
						node.getDxItemProperties().putAll(ui.getProperties());
						addNode(node);
					}
				}

				break;
			}
		}
	}
}

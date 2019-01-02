package com.fxms.ui.biz.pane;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeAttrVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.editor.EditorCallback;
import com.fxms.ui.bas.editor.FxEditor;
import com.fxms.ui.bas.editor.FxListEditor;
import com.fxms.ui.bas.editor.QidEditor;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.pane.SearchFilterPane;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class MoGridPane extends BorderPane implements FxUi, EditorCallback<Object> {

	private UiOpCodeVo opcode;
	private final SearchFilterPane filterPane;
	private final TabPane tabPane;

	public MoGridPane() {
		tabPane = new TabPane();
		filterPane = new SearchFilterPane();

		showText("no datas");

		setTop(filterPane);
		setCenter(tabPane);
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opcode;
	}

	@Override
	public void init(UiOpCodeVo opcode) {
		this.opcode = opcode;
		List<Node> nodeList = filterPane.makeFilters(opcode);
		for (Node node : nodeList) {
			if (node instanceof FxListEditor) {
				((FxListEditor) node).setCallback(this);
			}
		}
	}

	@Override
	public void initData(Map<String, Object> data) {

	}

	private void fillMo(long moNo) {

		tabPane.getTabs().clear();

		DxAsyncSelector.getSelector().getMoList(moNo, null, new FxCallback<List<Mo>>() {
			@Override
			public void onCallback(List<Mo> data) {
				Platform.runLater(new Runnable() {
					public void run() {

						if (data.size() == 0) {
							showText("no children");
						} else {
							for (Mo mo : data) {
								addMo(mo);
							}
						}
					}
				});
			}
		});
	}

	private void addMo(Mo mo) {
		Tab tab = null;
		FlowPane flow = null;

		for (Tab e : tabPane.getTabs()) {
			if (e.getText().equals(mo.getMoClass())) {
				tab = e;
				flow = (FlowPane) tab.getContent();
				break;
			}
		}

		if (tab == null) {

			flow = new FlowPane();
			flow.setPadding(new Insets(5, 0, 5, 0));
			flow.setVgap(4);
			flow.setHgap(4);

			tab = new Tab();
			tab.setClosable(false);
			tab.setText(mo.getMoClass());
			tab.setContent(flow);
			tabPane.getTabs().add(tab);
		}

		Button button = new Button(mo.getMoName());
		button.getStyleClass().add("mo-grid-pane-child");
		button.setPrefWidth(200);

		flow.getChildren().add(button);

	}

	private void showText(String text) {
		Tab tab = new Tab();
		tab.setClosable(false);
		tab.setText("...");
		Label label = new Label(text);
		label.getStyleClass().add("message");
		tab.setContent(label);
		tabPane.getTabs().add(tab);
	}

	@Override
	public void onSelected(FxEditor editor, Object data) {

		UiOpCodeAttrVo attr = (UiOpCodeAttrVo) ((Node) editor).getUserData();

		if (attr.getAttrName().equals("moClass")) {
			String moClass = editor.getAttrId();
			FxEditor node = filterPane.getFxEditor("moNo");
			if (node instanceof QidEditor) {
				((QidEditor) node).initList("SELECT_" + moClass.toUpperCase() + "_LIST");
			}

		} else if (attr.getAttrName().equals("moNo")) {
			try {
				long moNo = Double.valueOf(editor.getAttrId()).longValue();
				fillMo(moNo);
			} catch (Exception e) {
			}
		} else {

			System.out.println(attr.getAttrName() + "=" + data);

		}

	}

}

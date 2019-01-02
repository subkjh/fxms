package com.fxms.ui.node.dxeditor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.WindowEvent;

public class DxEditorContextMenu extends ContextMenu {

	private DxEditItem node;
	private DxEditor editor;
	private MenuItem removeMi;
	private MenuItem editMi;

	public DxEditorContextMenu(DxEditor editor) {

		this.editor = editor;

		removeMi = new MenuItem(Lang.getText(Lang.Type.menu, "Remove Selected Component"));
		removeMi.setDisable(true);
		removeMi.setOnAction((ActionEvent e) -> {
			editor.removeNode((DxEditItem) node);
		});

		editMi = new MenuItem(Lang.getText(Lang.Type.menu, "Edit Selected Component"));
		editMi.setDisable(true);
		editMi.setOnAction((ActionEvent e) -> {
			DxEditItem item = node;
			FxDialog.showEditorDialog(editor, item.getOpCode(), "Set Properties", "Set", "Cancel",
					new FxCallback<Map<String, Object>>() {
						@Override
						public void onCallback(Map<String, Object> data) {
							item.getDxItemProperties().putAll(data);
							item.setText();
						}
					}, item.getDxItemProperties());
		});

		getItems().add(getAddComponent());
		getItems().add(new SeparatorMenuItem());
		getItems().add(removeMi);
		getItems().add(new SeparatorMenuItem());
		getItems().add(editMi);

		this.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent e) {
				setNode(null);
			}
		});

	}

	public void setNode(DxEditItem node) {
		this.node = node;
		removeMi.setDisable(node == null);
		editMi.setDisable(node == null);
		if (node != null) {
			editMi.setDisable(node.getOpCode().getChildren().size() == 0);
		}
	}

	private Menu getAddComponent() {

		Menu menu = new Menu(Lang.getText(Lang.Type.menu, "Add New Component"));

		List<UiOpCodeVo> opcodeList = CodeMap.getMap().getOpCodes4Dx();

		opcodeList.sort(new Comparator<UiOpCodeVo>() {
			@Override
			public int compare(UiOpCodeVo arg0, UiOpCodeVo arg1) {
				return arg0.getSeqBy() - arg1.getSeqBy();
			}
		});

		for (UiOpCodeVo groupOp : opcodeList) {

			if (groupOp.getUpperOpNo() == 2000) {
				Menu groupMenu = new Menu(groupOp.getOpTitle());
				menu.getItems().add(groupMenu);

				for (UiOpCodeVo op : opcodeList) {
					if (op.getUpperOpNo() == groupOp.getOpNo()) {
						MenuItem mi = new MenuItem(op.getOpTitle());
						mi.setUserData(op);
						mi.setOnAction((ActionEvent e) -> {
							final DxEditItem item = new DxEditItem(op, getNodeX(), getNodeY(), 240, 48);
							if (op.getChildren().size() > 0) {
								FxDialog.showEditorDialog(editor, op, "Set Properties", "Set", "Cancel",
										new FxCallback<Map<String, Object>>() {
											@Override
											public void onCallback(Map<String, Object> data) {
												item.getDxItemProperties().putAll(data);
												editor.addNode(item);
											}
										}, null);
							} else {
								editor.addNode(item);
							}
						});

						groupMenu.getItems().add(mi);
					}
				}
			}
		}

		return menu;
	}

	private double getNodeX() {
		return DiagNodeBounds.makeGridUnit(
				getAnchorX() - editor.getScene().getWindow().getX() - editor.getScene().getX() - editor.getLayoutX());
	}

	private double getNodeY() {
		return DiagNodeBounds.makeGridUnit(
				getAnchorY() - editor.getScene().getWindow().getY() - editor.getScene().getY() - editor.getLayoutY());
	}

}

package com.fxms.ui.biz.pane;

import java.util.List;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.biz.action.FxContextMenu;
import com.fxms.ui.biz.action.FxDataMenuItem;
import com.fxms.ui.dx.DxCallback;

import fxms.client.ObjectUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class OpTreeTablePane extends ScrollPane implements FxUi {

	private TreeItem<UiOpCodeVo> root;
	private TreeTableView<UiOpCodeVo> treeTableView;
	private UiOpCodeVo opCode;
	private TreeItem<UiOpCodeVo> selectedItem;

	public OpTreeTablePane() {
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public void init(UiOpCodeVo opcode) {
		this.opCode = opcode;

		root = new TreeItem<>(new UiOpCodeVo());
		root.setExpanded(true);

		treeTableView = new TreeTableView<>(root);
		FxContextMenu<Map<String, Object>> contextMenu = makeContextMenu(treeTableView);
		treeTableView.setContextMenu(contextMenu);
		treeTableView.setShowRoot(false);

		treeTableView.setTableMenuButtonVisible(true);
		treeTableView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TreeItem<UiOpCodeVo>>() {
					@Override
					public void changed(ObservableValue<? extends TreeItem<UiOpCodeVo>> observable,
							TreeItem<UiOpCodeVo> old_val, TreeItem<UiOpCodeVo> new_val) {

						if (new_val != null) {
							selectedItem = new_val;
							UiOpCodeVo opcode = new_val.getValue();
							contextMenu.onSelectedData(ObjectUtil.toMap(opcode));
						}

					}
				});

		TreeTableColumn<UiOpCodeVo, String> item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "OP_NAME"));
		item.setPrefWidth(200);
		item.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<UiOpCodeVo, String> param) -> new ReadOnlyStringWrapper(
						param.getValue().getValue().getOpName()));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "OP_TITLE"));
		item.setPrefWidth(250);
		item.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<UiOpCodeVo, String> param) -> new ReadOnlyStringWrapper(
						param.getValue().getValue().getOpTitle()));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "OP_DESCR"));
		item.setPrefWidth(400);
		item.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<UiOpCodeVo, String> param) -> new ReadOnlyStringWrapper(
						param.getValue().getValue().getOpDescr()));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "ACCESS_UGRP_NAME"));
		item.setPrefWidth(150);
		item.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<UiOpCodeVo, String> param) -> new ReadOnlyStringWrapper(
						CodeMap.getMap().getUserGroupName(param.getValue().getValue().getUgrpNo())));
		treeTableView.getColumns().add(item);

		this.setContent(treeTableView);
		this.setFitToHeight(true);
		this.setFitToWidth(true);

		List<UiOpCodeVo> opcodeList = CodeMap.getMap().getOpCodeAll();

		makeTree(root, -1, opcodeList);
	}

	@Override
	public void initData(Map<String, Object> data) {

	}

	private FxContextMenu<Map<String, Object>> makeContextMenu(TreeTableView<UiOpCodeVo> treeTableView) {

		FxContextMenu<Map<String, Object>> menu = new FxContextMenu<>();
		FxDataMenuItem menuItem = new FxDataMenuItem(OpTreeTablePane.this, OP_NAME.UserOpChange, new DxCallback() {

			@Override
			public void onCallback(Map<String, Object> data) {
				Object includeSubYn = data.get("includeSubYn");
				if ("y".equalsIgnoreCase(includeSubYn == null ? "n" : includeSubYn.toString())) {
					resetVo(root, CodeMap.getMap().reloadOpCode());
				} else {
					UiOpCodeVo vo = new UiOpCodeVo();
					ObjectUtil.toObject(data, vo);
					selectedItem.getValue().setUgrpNo(vo.getUgrpNo());
				}
				treeTableView.refresh();
			}
		});
		menu.getItems().add(menuItem);

		return menu;
	}

	private void makeTree(TreeItem<UiOpCodeVo> parent, int opcodeNo, List<UiOpCodeVo> opcodeList) {

		for (UiOpCodeVo opcode : opcodeList) {
			if (opcode.getUpperOpNo() == opcodeNo) {
				TreeItem<UiOpCodeVo> item = new TreeItem<>(opcode);
				parent.getChildren().add(item);
				makeTree(item, opcode.getOpNo(), opcodeList);
			}
		}

	}

	private void resetVo(TreeItem<UiOpCodeVo> item, Map<Integer, UiOpCodeVo> opcodeMap) {
		UiOpCodeVo vo = opcodeMap.get(item.getValue().getOpNo());
		if (vo != null) {
			item.setValue(vo);
		}
		if (item.getChildren() != null) {
			for (TreeItem<UiOpCodeVo> child : item.getChildren()) {
				resetVo(child, opcodeMap);
			}
		}
	}

}

package com.fxms.ui.bas.pane;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.pane.list.ListPaneBase;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.folder.UiUserTreeVo;
import com.fxms.ui.biz.action.FxDataMenuItem;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import fxms.client.log.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TreeCfgPane extends BorderPane implements FxUi, DxCallback {

	private EditorPane cfgPane;
	private ListPaneBase listPane;
	private TreeView<Object> treeView;
	private TreeItem<Object> rootNode;
	private ContextMenu contextMenu;
	private TreeItem<Object> selectedItem;
	private FxDataMenuItem miTreeAdd;
	private FxDataMenuItem treeAttrAddMenuItem;
	private UiOpCodeVo opCode;
	private FxDataMenuItem miTreeDelete;

	public TreeCfgPane() {
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public void init(UiOpCodeVo opcode) {
		this.opCode = opcode;

		VBox vBox = new VBox(5);
		vBox.setPadding(new Insets(10, 10, 10, 10));

		cfgPane = makeCfg(CodeMap.getMap().getOpCode(OP_NAME.TreeUpdate));
		cfgPane.setDisable(true);

		listPane = makeMemList();
		listPane.setDisable(true);

		vBox.getChildren().add(cfgPane);
		vBox.getChildren().add(listPane);

		setLeft(makeTree());
		setCenter(vBox);

	}

	@Override
	public void initData(Map<String, Object> data) {

		cfgPane.initData(data);

		if (data != null) {
			listPane.getDefaultData().put("treeNo", data.get("treeNo"));
			listPane.doSearch();
		}

	}

	@Override
	public void onCallback(Map<String, Object> data) {
		refreshRoot();
	}

	private void addChildren(TreeItem<Object> upperItem, UiUserTreeVo upper, List<UiUserTreeVo> list) {

		upperItem.setExpanded(false);

		for (UiUserTreeVo folder : list) {

			if (folder.getUpperTreeNo() == upper.getTreeNo()) {
				TreeItem<Object> folderTreeItem = new TreeItem<>(folder);
				upperItem.getChildren().add(folderTreeItem);
				addChildren(folderTreeItem, folder, list);
			}
		}

	}

	private ContextMenu createMenu() {

		contextMenu = new ContextMenu();

		miTreeDelete = new FxDataMenuItem(TreeCfgPane.this, OP_NAME.TreeDelete, new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				selectedItem.getParent().getChildren().remove(selectedItem);
			}
		});

		miTreeAdd = new FxDataMenuItem(TreeCfgPane.this, OP_NAME.TreeAdd, this);

		contextMenu.getItems().add(miTreeDelete);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(miTreeAdd);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(new FxDataMenuItem(TreeCfgPane.this, OP_NAME.TreeTopAdd, this));

		return contextMenu;
	}

	// private EditorPane makeAttrPane() {
	// CdOp opcode = CodeMap.getMap().getCdOp(OP_NAME.TreeAttrAddMo);
	//
	// EditorPane memMo = new EditorPane(new DxCallback<Map<String, Object>>() {
	// @Override
	// public void onCallback(Map<String, Object> data) {
	// listPane.getParameters().put("treeNo", data.get("treeNo"));
	// listPane.doSearch();
	// }
	// }, opcode.getButtonText(), null, true);
	// memMo.init(opcode);
	//
	// return memMo;
	// }

	private EditorPane makeCfg(UiOpCodeVo cfgOpcode) {

		EditorPane cfg = new EditorPane(cfgOpcode.getOpTypeText(), null, true);
		cfg.setCallback(this);
		cfg.init(cfgOpcode);
		cfg.setPrefWidth(680);

		return cfg;

	}

	private ListPaneBase makeMemList() {

		ListPaneBase listPane = new ListPaneBase();
		listPane.init(CodeMap.getMap().getOpCode(OP_NAME.TreeAttrList));
		listPane.setPadding(new Insets(10, 10, 10, 10));

		treeAttrAddMenuItem = new FxDataMenuItem(listPane, OP_NAME.TreeAttrAddMo, listPane.getSearchCallback());

		listPane.getTableContextMenu().getItems()
				.add(new FxDataMenuItem(listPane, OP_NAME.TreeAttrDel, listPane.getSearchCallback()));
		listPane.getTableContextMenu().getItems().add(treeAttrAddMenuItem);

		return listPane;
	}

	private TreeView<Object> makeTree() {
		rootNode = new TreeItem<>("My Tree");
		treeView = new TreeView<Object>(rootNode);
		treeView.setEditable(false);
		treeView.setShowRoot(false);
		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Object>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Object>> observable, TreeItem<Object> old_val,
					TreeItem<Object> new_val) {

				selectedItem = new_val;

				if (selectedItem != null) {

					miTreeAdd.getParameters().clear();

					if (selectedItem.getValue() instanceof UiUserTreeVo) {

						UiUserTreeVo vo = (UiUserTreeVo) selectedItem.getValue();
						Map<String, Object> data = ObjectUtil.toMap(vo);

						miTreeDelete.onSelectedData(data);

						miTreeAdd.getParameters().put("upperTreeNo", vo.getTreeNo());
						miTreeAdd.getParameters().put("upperTreeName", vo.getTreeName());
						miTreeAdd.getParameters().put("targetMoClass", vo.getTargetMoClass());

						cfgPane.initData(data);
						cfgPane.setDisable(false);

						treeAttrAddMenuItem.getParameters().put("treeNo", vo.getTreeNo());

						listPane.getDefaultData().put("treeNo", vo.getTreeNo());
						listPane.doSearch();
					}

				}

			}
		});

		treeView.setContextMenu(createMenu());

		refreshRoot();

		return treeView;
	}

	@SuppressWarnings("unchecked")
	private void refreshRoot() {

		Map<String, Object> map;
		try {

			rootNode.getChildren().clear();

			map = DxAsyncSelector.getSelector().callMethod(TreeCfgPane.this,
					CodeMap.getMap().getOpCode(OP_NAME.TreeGetList), null);

			List<UiUserTreeVo> treeList = DxAsyncSelector.convert(UiUserTreeVo.class,
					(List<Map<String, Object>>) map.get("tree-list"));

			treeList.sort(new Comparator<UiUserTreeVo>() {

				@Override
				public int compare(UiUserTreeVo a1, UiUserTreeVo a2) {
					if (a1.getOrderBy() == a2.getOrderBy()) {
						return a1.getTreeName().compareTo(a2.getTreeName());
					}
					return a1.getOrderBy() - a2.getOrderBy();
				}
			});

			for (UiUserTreeVo tree : treeList) {
				if (tree.getUpperTreeNo() <= 0) {
					TreeItem<Object> treeItem = new TreeItem<>(tree);
					rootNode.getChildren().add(treeItem);
					addChildren(treeItem, tree, treeList);
				}
			}

		} catch (Exception e1) {
			Logger.logger.error(e1);
			return;
		}
	}

}
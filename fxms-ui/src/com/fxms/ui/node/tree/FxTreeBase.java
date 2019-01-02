package com.fxms.ui.node.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.lang.Lang.Type;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.bas.vo.folder.UiUserTreeVo;
import com.fxms.ui.biz.action.FxDataMenuItem;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.tree.vo.TreeItemVo;
import com.fxms.ui.node.tree.vo.TreeMoVo;

import fxms.client.log.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public abstract class FxTreeBase extends BorderPane {

	protected final TreeView<TreeItemVo> treeView;
	private TreeItem<TreeItemVo> rootNode;
	private TreeItem<TreeItemVo> selectedItem;
	private ContextMenu contextMenu;
	private FxDataMenuItem miMoTree;
	private List<TreeItem<TreeItemVo>> foundList = new ArrayList<TreeItem<TreeItemVo>>();
	private int foundIndex = -1;
	private TextField tfFind;
	private Button btnFind;
	protected final Map<Long, TreeMoVo> moMap = new HashMap<Long, TreeMoVo>();
	private String moClass;

	public FxTreeBase(String moClass) {

		this.moClass = moClass;

		rootNode = new TreeItem<>(new TreeItemVo("My Tree"));

		treeView = new TreeView<TreeItemVo>(rootNode);
		treeView.setEditable(false);
		treeView.setShowRoot(false);

		setTop(makeTop());
		setCenter(treeView);
		setStyle("-fx-background-color: transparent;");

		refreshRoot();

		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<TreeItemVo>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<TreeItemVo>> observable,
					TreeItem<TreeItemVo> old_val, TreeItem<TreeItemVo> new_val) {

				onSelected(new_val == null ? null : new_val.getValue());

				selectedItem = new_val;

				if (selectedItem != null && selectedItem.getValue() instanceof TreeMoVo) {
					miMoTree.onSelectedData(ObjectUtil.toMap(((TreeMoVo) selectedItem.getValue()).getMo()));
					miMoTree.setDisable(false);
				} else {
					miMoTree.setDisable(true);
				}
			}
		});

		treeView.setContextMenu(createMenu());
		treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	@SuppressWarnings("unchecked")
	private void addItems(TreeItem<TreeItemVo> upperTreeItem, Map<String, Object> map) {

		List<Map<String, Object>> children = (List<Map<String, Object>>) map.get("children");
		if (children != null && children.size() > 0) {
			for (Map<String, Object> e : children) {
				TreeItem<TreeItemVo> treeItem = addItem(upperTreeItem, e, UiUserTreeVo.class);
				addItems(treeItem, e);
			}
		}

		Map<String, Map<String, Object>> locationMap = (Map<String, Map<String, Object>>) map.get("locationMap");
		if (locationMap != null && locationMap.size() > 0) {
			for (Map<String, Object> e : locationMap.values()) {
				TreeItem<TreeItemVo> treeItem = addItem(upperTreeItem, e, LocationVo.class);
				addItems(treeItem, e);
			}
		}

		List<Map<String, Object>> moList = (List<Map<String, Object>>) map.get("moList");
		if (moList != null && moList.size() > 0) {

			if (upperTreeItem.getChildren().size() > 0) {

				// 하위 트리도 존재하고 내 자신에게도 MO가 있을 경우, 임의의 트리명을 지정한 경우에 한하여 그 트리에 추가해주고
				// 그외는 무시한다.
				UiUserTreeVo treeVo = (UiUserTreeVo) upperTreeItem.getValue().getSource();
				String etcMoTreeName = treeVo.getEtcMoTreeName();
				if (etcMoTreeName == null || etcMoTreeName.length() == 0) {
					return;
				}

				TreeItemVo treeItemVo = new TreeItemVo(etcMoTreeName);

				// 하위 tree이 있으면 구분하기 위해서 하나의 트리를 만들어 그 밑에 MO를 넣는다.
				TreeItem<TreeItemVo> treeItem = new TreeItem<>(treeItemVo,
						new ImageView(ImagePointer.getAlarmGoodGroupImage()));

				upperTreeItem.getChildren().add(treeItem);
				treeItemVo.getTreeItemList().add(treeItem);

				upperTreeItem = treeItem;
			}

			for (Map<String, Object> e : moList) {
				TreeItem<TreeItemVo> treeItem = addItem(upperTreeItem, e, Mo.class);
				addItems(treeItem, e);
			}
		}
	}

	private void collapseAll(TreeItem<TreeItemVo> treeItem) {

		if (treeItem.equals(rootNode)) {
			treeItem.setExpanded(true);
		} else {
			treeItem.setExpanded(false);
		}

		for (TreeItem<TreeItemVo> child : treeItem.getChildren()) {
			if (child.isLeaf() == false) {
				collapseAll(child);
			}
		}
	}

	private ContextMenu createMenu() {

		contextMenu = new ContextMenu();

		MenuItem item = new MenuItem(Lang.getText(Type.menu, "Refresh"));
		item.setOnAction((ActionEvent e) -> {
			refreshRoot();
		});

		miMoTree = new FxDataMenuItem(FxTreeBase.this, OP_NAME.MoTreeShow, null) {
			@Override
			protected void onAction(Map<String, Object> data) {
				long upperMoNo = getLong(data, "upperMoNo", 0);
				if (upperMoNo > 0) {
					data.put("moNo", upperMoNo);
				}

				super.onAction(data);
			}
		};

		contextMenu.getItems().add(item);
		contextMenu.getItems().add(new SeparatorMenuItem());
		contextMenu.getItems().add(miMoTree);

		return contextMenu;
	}

	private void expand(TreeItem<TreeItemVo> treeItem) {
		if (treeItem.getParent() != null) {
			treeItem.getParent().setExpanded(true);
			expand(treeItem.getParent());
		}
	}

	private void findTreeItem(String findValue) {

		foundList.clear();
		foundIndex = 0;

		if (findValue == null || findValue.length() == 0) {

			collapseAll(rootNode);

		} else {

			for (TreeItem<TreeItemVo> child : rootNode.getChildren()) {
				findTreeItem(child, findValue, foundList);
			}

			collapseAll(rootNode);

			if (foundList.size() > 0) {
				moveFocusNext();
			} else {
				btnFind.setText("Not Found");
			}
		}
	}

	private void findTreeItem(TreeItem<TreeItemVo> parent, String findValue, List<TreeItem<TreeItemVo>> list) {
		Object value;
		TreeMoVo mo;

		for (TreeItem<TreeItemVo> child : parent.getChildren()) {
			value = child.getValue();
			if (value instanceof LocationVo) {
				if (((LocationVo) value).getInloFname().contains(findValue)) {
					list.add(child);
				}
			} else if (value instanceof TreeMoVo) {
				mo = (TreeMoVo) value;
				if (mo.getMo().getMoName().contains(findValue)) {
					list.add(child);
				}
			}

			findTreeItem(child, findValue, list);
		}
	}

	private Node makeTop() {

		tfFind = new TextField();
		tfFind.setPromptText("찾은 문자열을 입력하세요");
		tfFind.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				btnFind.setText("Search");
				foundIndex = -1;
			}
		});

		btnFind = new Button("Search");
		btnFind.setDefaultButton(true);
		btnFind.setPrefWidth(72);
		btnFind.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (foundIndex == -1) {
					findTreeItem(tfFind.getText());
				} else {
					moveFocusNext();
				}
			}

		});
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(2, 1, 2, 1));
		pane.setCenter(tfFind);
		pane.setRight(btnFind);

		return pane;
	}

	private void makeTree4Root(List<UiUserTreeVo> list) {

		moMap.clear();
		rootNode.getChildren().clear();
		rootNode.setExpanded(true);

		for (UiUserTreeVo tree : list) {

			if (moClass == null || moClass.equals(tree.getTargetMoClass())) {

				if (tree.getUpperTreeNo() <= 0) {
					TreeItemVo treeItemVo = new TreeItemVo(tree);

					TreeItem<TreeItemVo> treeItem = new TreeItem<>(treeItemVo,
							new ImageView(ImagePointer.getAlarmGoodGroupImage()));
					rootNode.getChildren().add(treeItem);

					treeItemVo.getTreeItemList().add(treeItem);

					reloadTree(treeItem);

				}
			}
		}

	}

	private void moveFocusNext() {

		if (foundList.size() > 0 && foundIndex < foundList.size()) {

			TreeItem<TreeItemVo> treeItem = foundList.get(foundIndex);

			// 부모를 먼저 펼친 후 선택해야 함.
			expand(treeItem);

			treeView.getSelectionModel().select(treeItem);
			if (treeView.getSelectionModel().getSelectedIndex() >= 0) {
				// 아래 넣으면 가끔 오류가 발생하네 ㅠㅠ
				// treeView.scrollTo(treeView.getSelectionModel().getSelectedIndex());
			}
			foundIndex++;

			if (foundList.size() == 1) {
				btnFind.setText("1 found");
			} else {
				btnFind.setText("Next " + foundIndex + "/" + foundList.size());
			}
		} else {
			btnFind.setText("No More");
		}

	}

	@SuppressWarnings("unchecked")
	private void refreshRoot() {

		Map<String, Object> map;
		try {

			rootNode.getChildren().clear();

			map = DxAsyncSelector.getSelector().callMethod(this, CodeMap.getMap().getOpCode(OP_NAME.TreeGetList), null);

			List<UiUserTreeVo> folderList = DxAsyncSelector.convert(UiUserTreeVo.class,
					(List<Map<String, Object>>) map.get("tree-list"));

			folderList.sort(new Comparator<UiUserTreeVo>() {

				@Override
				public int compare(UiUserTreeVo a1, UiUserTreeVo a2) {
					if (a1.getOrderBy() == a2.getOrderBy()) {
						return a1.getTreeName().compareTo(a2.getTreeName());
					}
					return a1.getOrderBy() - a2.getOrderBy();
				}
			});

			makeTree4Root(folderList);

		} catch (Exception e1) {
			Logger.logger.error(e1);
			return;
		}
	}

	/**
	 * 상위 트리를 재 구성한다.
	 * 
	 * @param folderTreeItem
	 */
	private void reloadTree(final TreeItem<TreeItemVo> folderTreeItem) {

		UiUserTreeVo treeVo = (UiUserTreeVo) folderTreeItem.getValue().getSource();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("treeNo", treeVo.getTreeNo());

		DxAsyncSelector.getSelector().callMethod(CodeMap.getMap().getOpCode(OP_NAME.TreeGetItems), parameters,
				new FxCallback<Map<String, Object>>() {
					@SuppressWarnings("unchecked")
					@Override
					public void onCallback(Map<String, Object> data) {

						for (String treeNo : data.keySet()) {

							if (treeNo.equals(String.valueOf(treeVo.getTreeNo())) == false) {
								continue;
							}

							Platform.runLater(new Runnable() {
								public void run() {
									addItems(folderTreeItem, (Map<String, Object>) data.get(treeNo));
									removeNoChildren(folderTreeItem);
									if (folderTreeItem.getChildren().size() == 0) {
										folderTreeItem.getValue().setState("0");
									}
									onMakeCompleted();
								}
							});

						}
					}
				});

	}

	/**
	 * 하위가 없는 노드를 제거한다.
	 * 
	 * @param tree
	 */
	private void removeNoChildren(TreeItem<TreeItemVo> tree) {

		if (tree.getChildren() == null) {
			return;
		}

		TreeItem<TreeItemVo> child;

		for (int i = tree.getChildren().size() - 1; i >= 0; i--) {
			child = tree.getChildren().get(i);
			if (child.getValue().getSource() instanceof Mo) {
			} else {
				if (child.getChildren().size() > 0) {
					removeNoChildren(child);
				}

				if (child.getChildren().size() == 0) {
					tree.getChildren().remove(i);
				} else {
					child.getValue().setState(child.getChildren().size());
				}
			}
		}

	}

	protected <T> TreeItem<TreeItemVo> addItem(TreeItem<TreeItemVo> upperTreeItem, Map<String, Object> e,
			Class<T> classOfT) {

		T source = DxAsyncSelector.convert(classOfT, e);
		TreeItemVo treeItemVo = null;

		if (source instanceof Mo) {
			Mo mo = (Mo) source;

			TreeMoVo treeMo = moMap.get(mo.getMoNo());
			if (treeMo == null) {
				treeMo = new TreeMoVo(mo);
				moMap.put(mo.getMoNo(), treeMo);
			}

			treeItemVo = treeMo;
		} else {
			treeItemVo = new TreeItemVo(source);
		}

		TreeItem<TreeItemVo> treeItem = new TreeItem<>(treeItemVo,
				new ImageView(ImagePointer.getAlarmImage(source, -1)));

		upperTreeItem.getChildren().add(treeItem);
		treeItemVo.getTreeItemList().add(treeItem);

		return treeItem;
	}

	protected abstract void onMakeCompleted();

	protected abstract void onSelected(TreeItemVo treeItemVo);

}

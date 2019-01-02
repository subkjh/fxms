package com.fxms.ui.biz.pane;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.mo.ContainerMo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.LocationTreeVo;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.biz.action.FxContextMenu;
import com.fxms.ui.biz.action.MoMngYnMenuItem;
import com.fxms.ui.biz.action.PsViewMenuItem;
import com.fxms.ui.biz.action.mo.MoShowDetailMenuItem;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class MoTreePane extends BorderPane {

	private final FxContextMenu<Map<String, Object>> moContextMenu = new FxContextMenu<>();

	private final class TextFieldTreeCellImpl extends TreeCell<Object> {

		private TextField textField;
		private final ContextMenu locationContextMenu = new ContextMenu();

		public TextFieldTreeCellImpl() {

			// System.out.println(getTreeItem());

			MenuItem addMenuItem = new MenuItem("Add MO");
			locationContextMenu.getItems().add(addMenuItem);

			addMenuItem.setOnAction((ActionEvent t) -> {
				TreeItem<Object> newEmployee = new TreeItem<>("New MO");
				getTreeItem().getChildren().add(newEmployee);
			});

			FxCallback<Map<String, Object>> callback = new FxCallback<Map<String, Object>>() {

				@Override
				public void onCallback(Map<String, Object> data) {
					Mo mo = new Mo();
					ObjectUtil.toObject(data, mo);
					setItem(mo);
					setGraphic(new ImageView(mo.isMngYn() ? ImagePointer.managedIcon : ImagePointer.unmanagedIcon));
				}

			};

			MoMngYnMenuItem ynMenu = new MoMngYnMenuItem(MoTreePane.this, callback);

			moContextMenu.getItems().add(new MoShowDetailMenuItem(MoTreePane.this));
			moContextMenu.getItems().add(new SeparatorMenuItem());
			moContextMenu.getItems().add(new PsViewMenuItem<Map<String, Object>>(MoTreePane.this));
			moContextMenu.getItems().add(new SeparatorMenuItem());
			moContextMenu.getItems().add(ynMenu);
		}

		// @Override
		// public void cancelEdit() {
		// super.cancelEdit();
		//
		// setText(getString());
		// setGraphic(getTreeItem().getGraphic());
		// }
		//
		// @Override
		// public void startEdit() {
		// super.startEdit();
		//
		// if (textField == null) {
		// createTextField();
		// }
		// setText(null);
		// setGraphic(textField);
		// textField.selectAll();
		// }
		//
		@Override
		public void updateItem(Object item, boolean empty) {

			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(getTreeItem().getGraphic());
					if (item instanceof LocationTreeVo) {
						setContextMenu(locationContextMenu);
					} else if (item instanceof Mo) {
						setContextMenu(moContextMenu);
					}
				}
			}
		}

		//
		// private void createTextField() {
		// textField = new TextField(getString());
		// textField.setOnKeyReleased((KeyEvent t) -> {
		// if (t.getCode() == KeyCode.ENTER) {
		// commitEdit(textField.getText());
		// } else if (t.getCode() == KeyCode.ESCAPE) {
		// cancelEdit();
		// }
		// });
		//
		// }
		//
		private String getString() {
			Object item = getItem();
			if (item == null) {
				return "";
			}

			if (item instanceof LocationVo) {
				return ((LocationVo) item).getInloName();
			} else if (item instanceof Mo) {
				return ((Mo) item).getMoName();
			}

			return item.toString();
		}
	}

	private TreeItem<Object> rootNode = new TreeItem<>("Location Tree");
	private TreeItem<Object> tagToSelect = new TreeItem<>("");
	private TreeItem<Object> tagLoading = new TreeItem<>("loading ...");

	public MoTreePane() {

		setPrefSize(1000, 600);

		rootNode.setExpanded(true);

		loadInLo();

		TreeView<Object> treeView = new TreeView<>(rootNode);
		treeView.setEditable(false);
		treeView.setCellFactory((TreeView<Object> p) -> new TextFieldTreeCellImpl());
		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Object>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Object>> observable, TreeItem<Object> old_val,
					TreeItem<Object> new_val) {
				if (new_val.getValue() instanceof Mo) {
					moContextMenu.onSelectedData(ObjectUtil.toMap(new_val.getValue()));
				}
			}
		});

		setCenter(treeView);
	}

	public MoTreePane(Mo mo) {

		setPrefSize(500, 600);

		rootNode = new TreeItem<>(mo);
		rootNode.setExpanded(true);

		TreeView<Object> treeView = new TreeView<>(rootNode);
		treeView.setEditable(false);
		treeView.setCellFactory((TreeView<Object> p) -> new TextFieldTreeCellImpl());

		setCenter(treeView);

		loadMo4Upper(rootNode);

	}

	private void addTreeItem4Mo(TreeItem<Object> upperItem, Mo mo) {

		TreeItem<Object> item = new TreeItem<>(mo,
				new ImageView(mo.isMngYn() ? ImagePointer.managedIcon : ImagePointer.unmanagedIcon));

		item.addEventHandler(TreeItem.treeNotificationEvent(),
				new EventHandler<TreeItem.TreeModificationEvent<Object>>() {
					@Override
					public void handle(TreeItem.TreeModificationEvent<Object> event) {
						if (event.getEventType() == TreeItem.branchExpandedEvent()) {
							if (event.getSource().getChildren().contains(tagToSelect)) {
								event.getSource().getChildren().remove(tagToSelect);
								loadMo4Upper(event.getSource());
							}
						}
					}
				});

		upperItem.getChildren().add(item);

		if (Mo.isLeaf(mo) == false) {
			item.getChildren().add(tagToSelect);
		}
	}

	private void loadInLo() {

		List<LocationTreeVo> list;
		try {
			list = DxAsyncSelector.getSelector().getLocationList();
			makeTree(rootNode, list);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
	}

	private void loadMo(TreeItem<Object> upperItem) {

		upperItem.getChildren().add(tagLoading);

		Object obj = upperItem.getValue();
		if (obj instanceof LocationVo) {
			try {
				DxAsyncSelector.getSelector().getContainerMoList(((LocationVo) obj).getInloNo(),
						new FxCallback<List<ContainerMo>>() {
							@Override
							public void onCallback(List<ContainerMo> data) {

								upperItem.getChildren().remove(tagLoading);

								for (Mo mo : data) {
									addTreeItem4Mo(upperItem, mo);
								}

							}

						});

			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}
		}
	}

	private void loadMo4Upper(TreeItem<Object> upperItem) {

		upperItem.getChildren().add(tagLoading);

		Object obj = upperItem.getValue();
		if (obj instanceof Mo) {
			DxAsyncSelector.getSelector().getMoList(((Mo) obj).getMoNo(), null, new FxCallback<List<Mo>>() {
				@Override
				public void onCallback(List<Mo> data) {
					upperItem.getChildren().remove(tagLoading);
					for (Mo mo : data) {
						addTreeItem4Mo(upperItem, mo);
					}
				}

			});
		}
	}

	private void makeTree(TreeItem<Object> upperItem, List<LocationTreeVo> list) {

		for (LocationTreeVo location : list) {

			TreeItem<Object> item = new TreeItem<>(location);
			item.addEventHandler(TreeItem.treeNotificationEvent(),
					new EventHandler<TreeItem.TreeModificationEvent<Object>>() {
						@Override
						public void handle(TreeItem.TreeModificationEvent<Object> event) {
							if (event.getEventType() == TreeItem.branchExpandedEvent()
									&& event.getSource().getChildren().contains(tagToSelect)) {
								event.getSource().getChildren().remove(tagToSelect);
								loadMo(event.getSource());
							}
						}
					});

			upperItem.getChildren().add(item);
			makeTree(item, location.getChildren());
			item.getChildren().add(tagToSelect);
		}
	}
}

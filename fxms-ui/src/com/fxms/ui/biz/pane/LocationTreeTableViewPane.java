package com.fxms.ui.biz.pane;

import java.util.List;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.LocationTreeVo;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.biz.action.FxDataMenuItem;
import com.fxms.ui.biz.action.FxContextMenu;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.ImageView;

public class LocationTreeTableViewPane extends ScrollPane {

	class DxContextMenu extends FxContextMenu<Map<String, Object>> {

		public DxContextMenu() {

			FxDataMenuItem miNewLoc = new FxDataMenuItem(treeTableView, OP_NAME.LocationAdd, new DxCallback() {

				@Override
				public void onCallback(Map<String, Object> data) {
					LocationVo newLocation = new LocationVo();
					ObjectUtil.toObject(data, newLocation);
					if (selectedItem == null) {
						root.getChildren().add(new TreeItem<LocationVo>(newLocation,
								new ImageView(ImagePointer.getLocationTypeImage(newLocation))));
					} else {
						selectedItem.getChildren().add(new TreeItem<LocationVo>(newLocation,
								new ImageView(ImagePointer.getLocationTypeImage(newLocation))));
					}
				}
			}

			) {
				protected void onAction(Map<String, Object> data) {
					if (selectedItem == null) {
						getParameters().put("upperInloNo", 0);
					} else {
						getParameters().put("upperInloNo", selectedItem.getValue().getInloNo());
					}
					super.onAction();
				}

			};

			FxDataMenuItem miUpdateLoc = new FxDataMenuItem(treeTableView, OP_NAME.LocationUpdate, new DxCallback() {
				@Override
				public void onCallback(Map<String, Object> data) {
					LocationVo newLocation = new LocationVo();
					ObjectUtil.toObject(data, newLocation);
					selectedItem.setValue(newLocation);
				}
			});

			FxDataMenuItem miDelLoc = new FxDataMenuItem(treeTableView, OP_NAME.LocationDelete, new DxCallback() {
				@Override
				public void onCallback(Map<String, Object> data) {
					selectedItem.getParent().getChildren().remove(selectedItem);

				}
			});

			getItems().add(miNewLoc);
			getItems().add(miUpdateLoc);
			getItems().add(miDelLoc);

		}

	}

	private final TreeItem<LocationVo> root = new TreeItem<>(new LocationVo("설치위치"));
	private TreeTableView<LocationVo> treeTableView;
	private TreeItem<LocationVo> selectedItem;

	@SuppressWarnings("unchecked")
	public LocationTreeTableViewPane() {

		root.setExpanded(true);

		List<LocationTreeVo> list;
		try {
			list = DxAsyncSelector.getSelector().getLocationList();
			makeTree(root, list);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		TreeTableColumn<LocationVo, String> nameColumn = new TreeTableColumn<>("Name");
		nameColumn.setPrefWidth(200);
		nameColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<LocationVo, String> param) -> new ReadOnlyStringWrapper(
						param.getValue().getValue().getInloName()));

		TreeTableColumn<LocationVo, String> typeColumn = new TreeTableColumn<>("Type");
		typeColumn.setPrefWidth(100);
		typeColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<LocationVo, String> param) -> new ReadOnlyStringWrapper(
						param.getValue().getValue().getInloType()));

		TreeTableColumn<LocationVo, String> fnameColumn = new TreeTableColumn<>("FullName");
		fnameColumn.setPrefWidth(200);
		fnameColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<LocationVo, String> param) -> new ReadOnlyStringWrapper(
						param.getValue().getValue().getInloFname()));

		treeTableView = new TreeTableView<>(root);
		treeTableView.getColumns().setAll(nameColumn, typeColumn, fnameColumn);
		treeTableView.setTableMenuButtonVisible(true);
		treeTableView.setShowRoot(false);

		this.setContent(treeTableView);
		this.setFitToHeight(true);
		this.setFitToWidth(true);
		// getChildren().add(treeTableView);

		DxContextMenu menu = new DxContextMenu();
		treeTableView.setContextMenu(menu);

		treeTableView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TreeItem<LocationVo>>() {
					@Override
					public void changed(ObservableValue<? extends TreeItem<LocationVo>> observable,
							TreeItem<LocationVo> oldValue, TreeItem<LocationVo> newValue) {

						selectedItem = newValue;
						Map<String, Object> newMap = ObjectUtil.toMap(newValue.getValue());
						menu.onSelectedData(newMap);

					}
				});
	}

	private void makeTree(TreeItem<LocationVo> upperItem, List<LocationTreeVo> list) {

		for (LocationTreeVo location : list) {
			TreeItem<LocationVo> item = new TreeItem<>(location.getMe(),
					new ImageView(ImagePointer.getLocationTypeImage(location.getMe())));
			upperItem.getChildren().add(item);
			makeTree(item, location.getChildren());
		}
	}
}

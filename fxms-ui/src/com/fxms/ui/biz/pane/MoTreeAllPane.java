package com.fxms.ui.biz.pane;

import java.util.List;

import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.MoLocatable;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

public class MoTreeAllPane extends LocationTreeBasePanel {

	private final class TextFieldTreeCellImpl extends TreeCell<Object> {

		public TextFieldTreeCellImpl() {

		}

		@Override
		public void updateItem(Object item, boolean empty) {

			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				setText(getString());
				setGraphic(getTreeItem().getGraphic());
			}
		}

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

	private String moClass;
	private TreeItem<Object> tagToSelect = new TreeItem<>("");
	private TreeItem<Object> tagLoading = new TreeItem<>("loading ...");

	public MoTreeAllPane(String moClass, FxCallback<Object> callback) {

		super(moClass, callback);

		setPrefSize(350, 600);

		this.moClass = moClass;

		loadMo();

		getTreeView().setCellFactory((TreeView<Object> p) -> new TextFieldTreeCellImpl());

	}

	public Mo getMo(long moNo) {

		TreeItem<Object> item = findMo(getRootNode().getChildren(), moNo);

		if (item != null) {
			getTreeView().getSelectionModel().select(item);
			return (Mo) item.getValue();
		}

		return null;
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

	private TreeItem<Object> findMo(List<TreeItem<Object>> list, long moNo) {

		if (list == null) {
			return null;
		}

		TreeItem<Object> ret;
		Object value;
		Mo mo;

		for (TreeItem<Object> item : list) {
			value = item.getValue();
			if (value instanceof Mo) {
				mo = (Mo) value;
				if (mo.getMoNo() == moNo) {
					return item;
				}
			}

			ret = findMo(item.getChildren(), moNo);
			if (ret != null) {
				return ret;
			}
		}

		return null;
	}

	private void loadMo() {

		DxAsyncSelector.getSelector().selectMoList(moClass, new FxCallback<List<? extends Mo>>() {
			@Override
			public void onCallback(List<? extends Mo> data) {

				TreeItem<Object> upperItem;

				for (Mo mo : data) {

					upperItem = null;

					if (mo instanceof MoLocatable) {
						upperItem = getLocationTreeItem(((MoLocatable) mo).getInloNo());
					}

					if (upperItem == null) {
						upperItem = getRootNode();
					}

					upperItem.setExpanded(true);

					addTreeItem4Mo(upperItem, mo);
				}
			}

		});
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

}

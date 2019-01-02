package com.fxms.ui.node.tree;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.tree.vo.TreeItemVo;

import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

public abstract class FxTreeAllPane extends FxTreePane {

	private final TreeItem<TreeItemVo> tagToSelect = new TreeItem<>(new TreeItemVo(""));

	/**
	 * 
	 * @param moClass
	 *            보일 MO_CLASS
	 */
	public FxTreeAllPane(String moClass) {
		super(moClass);
	}

	@Override
	protected <T> TreeItem<TreeItemVo> addItem(TreeItem<TreeItemVo> upperTreeItem, Map<String, Object> e,
			Class<T> classOfT) {

		TreeItem<TreeItemVo> treeItem = super.addItem(upperTreeItem, e, classOfT);

		treeItem.addEventHandler(TreeItem.treeNotificationEvent(),
				new EventHandler<TreeItem.TreeModificationEvent<TreeItemVo>>() {
					@Override
					public void handle(TreeItem.TreeModificationEvent<TreeItemVo> event) {
						if (event.getEventType() == TreeItem.branchExpandedEvent()) {
							if (event.getSource().getChildren().contains(tagToSelect)) {
								event.getSource().getChildren().remove(tagToSelect);
								loadMo4Upper(event.getSource());
							}
						}
					}
				});

		if (treeItem.getValue().getSource() instanceof Mo) {
			Mo mo = (Mo) treeItem.getValue().getSource();
			if (Mo.isLeaf(mo)) {
				// leaf mo이면 하위가 없으면 무시
			} else {
				// MO이면 하위 MO를 조회하기 위해 보인다.
				treeItem.getChildren().add(tagToSelect);
			}
		}

		return treeItem;
	}

	private void loadMo4Upper(TreeItem<TreeItemVo> upperItem) {

		Object obj = upperItem.getValue().getSource();
		if ((obj instanceof Mo) == false) {
			return;
		}

		upperItem.getValue().setState("loading...");

		DxAsyncSelector.getSelector().getMoList(((Mo) obj).getMoNo(), null, new FxCallback<List<Mo>>() {
			@Override
			public void onCallback(List<Mo> data) {
				upperItem.getValue().setState(data.size());
				for (Mo mo : data) {
					addTreeItem4Mo(upperItem, mo);
				}
				treeView.refresh();
			}

		});

	}

	private void addTreeItem4Mo(TreeItem<TreeItemVo> upperItem, Mo mo) {

		TreeItem<TreeItemVo> item = new TreeItem<>(new TreeItemVo(mo),
				new ImageView(mo.isMngYn() ? ImagePointer.managedIcon : ImagePointer.unmanagedIcon));

		item.addEventHandler(TreeItem.treeNotificationEvent(),
				new EventHandler<TreeItem.TreeModificationEvent<TreeItemVo>>() {
					@Override
					public void handle(TreeItem.TreeModificationEvent<TreeItemVo> event) {
						if (event.getEventType() == TreeItem.branchExpandedEvent()) {
							if (event.getSource().getChildren().contains(tagToSelect)) {
								event.getSource().getChildren().remove(tagToSelect);
								loadMo4Upper(event.getSource());
							}
						}
					}
				});

		upperItem.getChildren().add(item);
		if (Mo.isLeaf(mo)) {
			// no children
		} else {
			item.getChildren().add(tagToSelect);
		}
	}
}

package com.fxms.ui.biz.pane;

import java.util.List;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.vo.LocationTreeVo;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.FxCallback;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * 설치 위치를 트리로 나타낸 클래스
 * 
 * @author SUBKJH-DEV
 *
 */
public class LocationTreeBasePanel extends BorderPane {

	private TreeItem<Object> rootNode;
	private TreeView<Object> treeView;

	public LocationTreeBasePanel(String rootText, FxCallback<Object> callback) {

		rootNode = new TreeItem<>(rootText);

		treeView = new TreeView<Object>(rootNode);
		treeView.setEditable(false);

		// ScrollPane sp = new ScrollPane();
		// sp.setFitToHeight(true);
		// sp.setFitToWidth(true);
		// sp.setContent(treeView);

		setCenter(treeView);

		makeLocationTree();

		rootNode.setExpanded(true);

		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Object>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Object>> observable, TreeItem<Object> old_val,
					TreeItem<Object> new_val) {
				if (callback != null) {
					callback.onCallback(new_val.getValue());
				}
			}
		});

	}

	public LocationVo getLocation(int locationNo) {

		TreeItem<Object> item = find(rootNode.getChildren(), locationNo);

		if (item != null) {
			treeView.getSelectionModel().select(item);
			return (LocationVo) item.getValue();
		}

		return null;
	}

	public TreeItem<Object> getLocationTreeItem(int locationNo) {
		return find(rootNode.getChildren(), locationNo);
	}

	public TreeItem<Object> getRootNode() {
		return rootNode;
	}

	public TreeView<Object> getTreeView() {
		return treeView;
	}

	private TreeItem<Object> find(List<TreeItem<Object>> list, int locationNo) {

		if (list == null) {
			return null;
		}

		TreeItem<Object> ret;
		Object value;
		LocationVo location;

		for (TreeItem<Object> item : list) {
			value = item.getValue();
			if (value instanceof LocationVo) {
				location = (LocationVo) value;
				if (location.getInloNo() == locationNo) {
					return item;
				}
			}

			ret = find(item.getChildren(), locationNo);
			if (ret != null) {
				return ret;
			}
		}

		return null;
	}

	private void makeLocationTree() {

		List<LocationTreeVo> list;
		try {
			list = CodeMap.getMap().getLocationList();
			makeTree(rootNode, list);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
	}

	private void makeTree(TreeItem<Object> upperItem, List<LocationTreeVo> list) {

		for (LocationTreeVo location : list) {
			TreeItem<Object> item = new TreeItem<>(location.getMe(),
					new ImageView(ImagePointer.getLocationTypeImage(location.getMe())));
			upperItem.getChildren().add(item);
			makeTree(item, location.getChildren());
		}
	}
}

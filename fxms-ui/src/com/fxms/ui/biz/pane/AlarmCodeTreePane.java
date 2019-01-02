package com.fxms.ui.biz.pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiAlarmCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.dx.FxCallback;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

public class AlarmCodeTreePane extends BorderPane {

	private TreeItem<Object> rootNode;
	private TreeView<Object> treeView;

	public AlarmCodeTreePane(FxCallback<UiAlarmCodeVo> callback) {

		rootNode = new TreeItem<>("");

		treeView = new TreeView<>(rootNode);
		treeView.setEditable(false);
		treeView.setShowRoot(false);

		// ScrollPane sp = new ScrollPane();
		// sp.setFitToHeight(true);
		// sp.setFitToWidth(true);
		// sp.setContent(treeView);

		setCenter(treeView);

		makeTree();

		rootNode.setExpanded(true);

		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Object>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Object>> observable, TreeItem<Object> old_val,
					TreeItem<Object> new_val) {
				if (new_val.getValue() instanceof UiAlarmCodeVo) {

					System.out.println(ObjectUtil.toMap(new_val.getValue()));

					if (callback != null) {
						callback.onCallback((UiAlarmCodeVo) new_val.getValue());
					}
				}
			}
		});

		setPrefSize(325, 750);
	}

	private void makeTree() {

		List<UiAlarmCodeVo> list;
		try {
			Map<String, TreeItem<Object>> upperMap = new HashMap<String, TreeItem<Object>>();

			list = CodeMap.getMap().getAlarmCodeList();
			TreeItem<Object> upper;
			for (UiAlarmCodeVo alarmCode : list) {

				upper = upperMap.get(alarmCode.getTargetMoClass());

				if (upper == null) {
					upper = new TreeItem<>(alarmCode.getTargetMoClass());
					rootNode.getChildren().add(upper);
					upperMap.put(alarmCode.getTargetMoClass(), upper);
				}

				upper.getChildren().add(new TreeItem<>(alarmCode));
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
	}

}

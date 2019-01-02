package com.fxms.ui.bas.pane;

import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeAttrVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.renderer.FxRenderer;
import com.fxms.ui.bas.utils.ObjectUtil;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ShowDetailTabPane extends TabPane {

	private UiOpCodeVo opCode;

	public ShowDetailTabPane(UiOpCodeVo opCode) {
		this.opCode = opCode;

		for (String group : opCode.getAttrGroupList()) {
			showGroup(group);
		}

	}

	public void showData(Object data) {
		Map<String, Object> map = ObjectUtil.toMap(data);

		if (map != null) {

			UiOpCodeAttrVo attr;
			Object value;
			for (Tab tab : getTabs()) {
				for (Node node : ((Pane) (tab.getContent())).getChildren()) {
					if (node.getUserData() instanceof UiOpCodeAttrVo) {
						attr = (UiOpCodeAttrVo) node.getUserData();
						if (node instanceof FxRenderer) {
							value = data == null ? null : map.get(attr.getAttrName());
							((FxRenderer) node).setValue(value, attr.getAttrValueList());
						}
					}
				}
			}
		}
	}

	private void addRenderer(GridPane gridPane, int col, int row, UiOpCodeAttrVo attr) {

		final Label nameLabel = new Label(attr.getAttrDisp());
		final Node renderer = attr.makeRenderer();

		nameLabel.getStyleClass().add("show-detail-item");
		renderer.setUserData(attr);

		GridPane.setConstraints(nameLabel, col, row);
		gridPane.getChildren().add(nameLabel);

		GridPane.setConstraints(renderer, col + 1, row);
		gridPane.getChildren().add(renderer);

	}

	private void showGroup(String groupName) {

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(12, 12, 12, 12));
		gridPane.setVgap(5);
		gridPane.setHgap(10);

		Tab tab = new Tab();
		tab.setClosable(false);
		tab.setText(groupName);
		tab.setContent(makePane(groupName, false));
		getTabs().add(tab);

	}

	public GridPane makePane(String groupName, boolean showTitle) {

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(12, 12, 12, 12));
		gridPane.setVgap(5);
		gridPane.setHgap(10);

		if (showTitle) {
			final Label groupLabel = new Label(groupName == null ? "..." : groupName);
			groupLabel.getStyleClass().add("editor-group");
			GridPane.setConstraints(groupLabel, 0, 0, 2, 1);
			gridPane.getChildren().add(groupLabel);
		}

		int col = 0;
		int row = 0;
		int seqBy = -1;

		for (UiOpCodeAttrVo attr : opCode.getChildren(groupName)) {

			if (attr.isEditable() == false) {
				continue;
			}

			if (seqBy / 10 == attr.getSeqBy() / 10) {
				col += 2;
			} else {
				col = 0;
				row++;
			}

			addRenderer(gridPane, col, row, attr);

			seqBy = attr.getSeqBy();

		}

		return gridPane;

	}

}

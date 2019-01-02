package com.fxms.ui.bas.pane;

import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeAttrVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.renderer.FxRenderer;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ShowDetailPane extends BorderPane implements FxUi {

	private GridPane grid;
	private int index = 0;
	private int maxCol = 3;
	private UiOpCodeVo opCode;
	private int row = 0;
	private ScrollPane scroll;

	public ShowDetailPane() {
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public void init(UiOpCodeVo opCode) {

		this.opCode = opCode;

		scroll = new ScrollPane();
		setCenter(scroll);

		this.opCode = opCode;

		if (opCode == null) {
			scroll.setContent(new Label("Not define op-code"));
			return;
		}

		grid = new GridPane();
		grid.setPadding(new Insets(20, 20, 20, 20));
		grid.setVgap(10);
		grid.setHgap(10);

		scroll.setContent(grid);

		for (String group : opCode.getAttrGroupList()) {
			showGroup(group);
		}
	}

	@Override
	public void initData(Map<String, Object> data) {

		if (opCode.isCall()) {
			data = DxAsyncSelector.getSelector().callMethod(this, opCode, data);
		}

		if (data != null) {

			UiOpCodeAttrVo attr;
			Object value;
			for (Node node : grid.getChildren()) {
				if (node.getUserData() instanceof UiOpCodeAttrVo) {
					attr = (UiOpCodeAttrVo) node.getUserData();
					if (node instanceof FxRenderer) {
						value = data.get(attr.getAttrName());
						((FxRenderer) node).setValue(value, attr.getAttrValueList());
					}
				}
			}
		}

	}

	public void setMaxCol(int maxCol) {
		this.maxCol = maxCol;
	}

	private void addGroup(int col, int row, String text) {
		final Label groupLabel = new Label(text);
		groupLabel.getStyleClass().add("show-detail-title");
		GridPane.setConstraints(groupLabel, col, row, 2, 1);
		grid.getChildren().add(groupLabel);

	}

	private void addRenderer(int col, int row, UiOpCodeAttrVo attr) {

		final Label nameLabel = new Label(attr.getAttrDisp());
		final Node renderer = attr.makeRenderer();

		nameLabel.getStyleClass().add("show-detail-item");
		renderer.setUserData(attr);

		GridPane.setConstraints(nameLabel, col, row);
		grid.getChildren().add(nameLabel);

		GridPane.setConstraints(renderer, col + 1, row);
		grid.getChildren().add(renderer);

	}

	private void showGroup(String groupName) {

		int col = index * 2;
		int row = this.row;

		addGroup(col, row, groupName);

		for (UiOpCodeAttrVo attr : opCode.getChildren(groupName)) {
			addRenderer(col, ++row, attr);
		}

		index++;

		if (index == maxCol) {
			index = 0;
			this.row = row + 1;
		}
	}
	
	protected void hide()
	{
		try {
			getScene().getWindow().hide();
		} catch (Exception e) {
		}
	}

}

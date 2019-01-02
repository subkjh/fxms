package com.fxms.ui.node.dxeditor;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.event.FxBounds;

import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class DxEditItem extends Label {

	private DiagNodeBounds diagData = new DiagNodeBounds(true, true);
	private UiOpCodeVo opCode;
	private Map<String, Object> properties;

	public Map<String, Object> getDxItemProperties() {

		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;

	}

	public void setText() {
		setText(opCode.getOpTitle() + ", " + getDxItemProperties() + ", " + getBounds());
	}

	public DxEditItem(UiOpCodeVo opcode, double x, double y, double width, double height) {

		super(opcode.getOpTitle());

		if (x < 8) {
			x = 8;
		}

		if (y < 8) {
			y = 8;
		}

		this.opCode = opcode;

		setBorder(new Border(
				new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

		setLayoutX(x);
		setLayoutY(y);
		setPrefWidth(width);
		setPrefHeight(height);
		setWrapText(true);
	}

	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	public FxBounds getBounds() {
		return new FxBounds(getLayoutX(), getLayoutY(), getPrefWidth(), getPrefHeight());
	}

	public DiagNodeBounds getDiagNodeBounds() {
		return diagData;
	}

	public void resize(FxBounds bounds) {

		if (bounds == null) {
			return;
		}

		if (bounds.getX() > 0) {
			setLayoutX(bounds.getX());
		}
		if (bounds.getY() > 0) {
			setLayoutY(bounds.getY());
		}
		if (bounds.getWidth() > 0) {
			setPrefWidth(bounds.getWidth());
		}
		if (bounds.getHeight() > 0) {
			setPrefHeight(bounds.getHeight());
		}

		setText();
	}
}

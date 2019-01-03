package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.event.FxBounds;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeType;

public class DiagNodeRing extends BorderPane implements DiagNode {

	private final Group group = new Group();
	private DiagNodeBounds diagData = new DiagNodeBounds();
	private DiagNodeVo vo;

	private final double radius = 250;
	private final double center = 300;

	public DiagNodeRing() {
		setCenter(group);
	}

	@Override
	public FxBounds getBounds() {
		return new FxBounds(getLayoutX(), getLayoutY(), getPrefWidth(), getPrefHeight());
	}

	@Override
	public DiagNodeBounds getDiagNodeBounds() {
		return diagData;
	}

	@Override
	public DiagNodeVo getDiagNodeVo() {
		return vo;
	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramNetworkEdit;
	}

	@Override
	public void setDiagNodeLocation(FxBounds box) {

		if (box == null) {
			return;
		}

		if (box.getX() > 0) {
			vo.setDiagNodeX(box.getX());
			setLayoutX(box.getX());
		}
		if (box.getY() > 0) {
			vo.setDiagNodeY(box.getY());
			setLayoutY(box.getY());
		}

		if (box.getHeight() > 0) {
			vo.setDiagNodeHeight(box.getHeight());
			setPrefHeight(box.getHeight());
		}

		if (box.getWidth() > 0) {
			vo.setDiagNodeWidth(box.getWidth());
			setPrefWidth(box.getWidth());
		}

	}

	public void setDiagNodeVo(DiagNodeVo vo) {
		this.vo = vo;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		// TODO Auto-generated method stub

	}

	void makeArc(int size, int index) {

		double angle = 360f / size;
		double startAngle = angle * index;
		double d = 2 * Math.PI / size;

		Arc arc = new Arc();
		arc.setCenterX(center);
		arc.setCenterY(center);
		arc.setRadiusX(radius);
		arc.setRadiusY(radius);
		arc.setStartAngle(startAngle);
		arc.setLength(angle - 2f); // 간격을 두기 위해서.
		arc.setType(ArcType.OPEN);
		arc.setFill(Color.AZURE);
		arc.setStrokeWidth(2);
		arc.setStrokeType(StrokeType.INSIDE);
		arc.setStroke(Color.FORESTGREEN);

		group.getChildren().add(arc);

		double x = center + Math.cos(d * index) * radius;
		double y = center - Math.sin(d * index) * radius;

		Label text = new Label(index + "");
		text.setLayoutX(x - 5);
		text.setLayoutY(y - 5);
		group.getChildren().add(text);

	}

}

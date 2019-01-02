package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.event.FxBounds;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DiagNodeBox extends Rectangle implements DiagNode {

	private DiagNodeBounds diagData = new DiagNodeBounds();
	private DiagNodeVo vo;

	// double sceneX;
	// double sceneY;
	// double nodeX;
	// double nodeY;
	// double nodeWidth;
	// double nodeHeight;
	//
	// EventType<MouseEvent> mouseEvent;

	public void setDiagNodeVo(DiagNodeVo vo) {

		this.vo = vo;

		setX(50);
		setY(50);
		setWidth(vo.getDiagNodeWidth());
		setHeight(vo.getDiagNodeHeight());

		setLayoutX(vo.getDiagNodeX());
		setLayoutY(vo.getDiagNodeY());
		setId(vo.getDiagNodeNo() + "");

		setStrokeWidth(-2);

		setProperties(vo.getProperties());

	}

	@Override
	public void setProperties(Map<String, Object> properties) {

		Object value;

		value = properties.get("arcHeight");
		if (value != null) {
			try {
				setArcHeight(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("arcWidth");
		if (value != null) {
			try {
				setArcWidth(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("opacity");
		if (value != null) {
			try {
				setOpacity(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("stroke");
		if (value != null) {
			try {
				setStroke(Color.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("fill");
		if (value != null) {
			try {
				setFill(Color.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		if (box.getWidth() > 0) {
			vo.setDiagNodeWidth(box.getWidth());
			setWidth(box.getWidth());
		}
		if (box.getHeight() > 0) {
			vo.setDiagNodeHeight(box.getHeight());
			setHeight(box.getHeight());
		}
	}

	@Override
	public DiagNodeBounds getDiagNodeBounds() {
		return diagData;
	}

	@Override
	public DiagNodeVo getDiagNodeVo() {
		vo.getProperties().put("arcHeight", getArcHeight());
		vo.getProperties().put("arcWidth", getArcWidth());
		vo.getProperties().put("fill", ((Color) getFill()).toString());
		vo.getProperties().put("opacity", getOpacity());
		return vo;
	}

	@Override
	public String toString() {
		return "box";
	}

	@Override
	public FxBounds getBounds() {
		return new FxBounds(getLayoutX(), getLayoutY(), getWidth(), getHeight());
	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramBoxEdit;
	}
}

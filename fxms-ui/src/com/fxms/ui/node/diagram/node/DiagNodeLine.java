package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.node.diagram.vo.DiagLineVo;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class DiagNodeLine extends Line {

	private DiagLineVo vo;
	private DiagNode startNode;
	private DiagNode endNode;

	public DiagNodeLine(DiagLineVo vo, DiagNode startNode, DiagNode endNode) {

		this.vo = vo;
		this.startNode = startNode;
		this.endNode = endNode;

		setProperties(vo.getProperties());

		redraw();
	}

	public boolean containsNode(DiagNode node) {

		if (startNode != null && startNode.equals(node)) {
			return true;
		}
		if (endNode != null && endNode.equals(node)) {
			return true;
		}

		return false;
	}

	public void setProperties(Map<String, Object> properties) {
		Object value;

		value = properties.get("strokeWidth");
		if (value != null) {
			try {
				setStrokeWidth(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			setStrokeWidth(5);
		}

		value = properties.get("stroke");
		if (value != null) {
			try {
				setStroke(Color.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			setStroke(Color.DEEPSKYBLUE);
		}
	}

	public DiagLineVo getDiagLineVo() {

		vo.getProperties().put("strokeWidth", this.getStrokeWidth());
		vo.getProperties().put("stroke",  ((Color) getStroke()).toString());

		return vo;
	}

	public void redraw() {

		if (startNode == null || endNode == null) {
			return;
		}

		DiagNodeVo node1 = startNode.getDiagNodeVo();
		DiagNodeVo node2 = endNode.getDiagNodeVo();

		setStartX(node1.getDiagNodeX() + (node1.getDiagNodeWidth() / 2d));
		setStartY(node1.getDiagNodeY() + (node1.getDiagNodeHeight() / 2d));

		setEndX(node2.getDiagNodeX() + (node2.getDiagNodeWidth() / 2d));
		setEndY(node2.getDiagNodeY() + (node2.getDiagNodeHeight() / 2d));
	}
	
	public Node getSelectedDisplayNode()
	{
		Line line = new Line(getStartX(), getStartY(), getEndX(), getEndY());
		line.setStrokeWidth(getStrokeWidth() + 4);
		line.setStroke(Color.GOLD);
		return line;
	}
}

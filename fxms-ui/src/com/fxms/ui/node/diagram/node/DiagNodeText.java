package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.event.FxBounds;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.scene.text.Text;

public class DiagNodeText extends Text implements DiagNode {

	private DiagNodeBounds diagData = new DiagNodeBounds();
	private DiagNodeVo vo;
	
	@Override
	public FxBounds getBounds() {
		double width = getLayoutBounds().getWidth();
		double height = getLayoutBounds().getHeight();
		return new FxBounds(getLayoutX(), getLayoutY() + getLayoutBounds().getMinY(), width, height);
	}

	@Override
	public DiagNodeBounds getDiagNodeBounds() {
		return diagData;
	}

	@Override
	public DiagNodeVo getDiagNodeVo() {

		DiagNode.setNode2Attributes(this, vo.getProperties());
		vo.getProperties().put("text", getText());

		return vo;
	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramTextEdit;
	}

	@Override
	public void setDiagNodeLocation(FxBounds bounds) {

		if (bounds == null) {
			return;
		}

		if (bounds.getX() > 0) {
			vo.setDiagNodeX(bounds.getX());
			setLayoutX(bounds.getX());
		}

		if (bounds.getY() > 0) {
			vo.setDiagNodeY(bounds.getY());
			setLayoutY(bounds.getY() + getFont().getSize());
		}
		// if (height > 0)
		// setHeight(height);
		// if (width > 0)
		// setWidth(width);
	}

	public void setDiagNodeVo(DiagNodeVo vo) {

		this.vo = vo;

		setId(vo.getDiagNodeNo() + "");
		setLayoutX(vo.getDiagNodeX());
		setLayoutY(vo.getDiagNodeY());
		setProperties(vo.getProperties());
	}

	@Override
	public void setProperties(Map<String, Object> properties) {

		Object value;

		DiagNode.setAttributes2Node(properties, this);

		value = properties.get("text");
		if (value != null) {
			setText(value.toString());
		} else {
			setText("Empty Text");
		}

	}

	@Override
	public String toString() {
		return "text:" + getText();
	}
}

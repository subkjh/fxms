package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.event.FxBounds;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.scene.image.ImageView;

public class DiagNodeImage extends ImageView implements DiagNode {

	private DiagNodeBounds diagData = new DiagNodeBounds();
	private DiagNodeVo vo;
	private String imageName;
	
	@Override
	public FxBounds getBounds() {
		return new FxBounds(getLayoutX(), getLayoutY(), getFitWidth(), getFitHeight());
	}

	@Override
	public DiagNodeBounds getDiagNodeBounds() {
		return diagData;
	}

	@Override
	public DiagNodeVo getDiagNodeVo() {

		vo.getProperties().put("image", imageName);
		vo.getProperties().put("opacity", getOpacity());

		return vo;
	}

	@Override
	public OP_NAME getOpName() {
		return OP_NAME.DiagramImageEdit;
	}

	@Override
	public void setDiagNodeLocation(FxBounds bounds) {

		if (bounds == null) {
			return;
		}

		vo.setBounds(bounds);

		if (bounds.getX() > 0) {
			setLayoutX(bounds.getX());
		}
		if (bounds.getY() > 0) {
			setLayoutY(bounds.getY());
		}

		if (bounds.getHeight() > 0) {
			setFitHeight(bounds.getHeight());
		}

		if (bounds.getWidth() > 0) {
			setFitWidth(bounds.getWidth());
		}
	}

	public void setDiagNodeVo(DiagNodeVo vo) {

		this.vo = vo;

		setId(vo.getDiagNodeNo() + "");
		setFitHeight(vo.getDiagNodeHeight());
		setFitWidth(vo.getDiagNodeWidth());

		relocate(vo.getDiagNodeX(), vo.getDiagNodeY());

		setProperties(vo.getProperties());

	}

	@Override
	public void setProperties(Map<String, Object> properties) {

		Object value;

		value = properties.get("image");
		if (value != null) {
			imageName = value.toString();
		} else {
			imageName = "fxms.png";
		}

		try {
			setImage(ImagePointer.getImage(imageName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		value = properties.get("opacity");
		if (value != null) {
			try {
				setOpacity(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

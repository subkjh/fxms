package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.event.FxBounds;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public abstract class DiagNodeBase extends Label implements DiagNode {

	private DiagNodeBounds diagData = new DiagNodeBounds();
	private DiagNodeVo vo;
	private ImageView imageView;

	public void setDiagNodeVo(DiagNodeVo vo) {

		this.vo = vo;

		setPrefSize(vo.getDiagNodeWidth(), vo.getDiagNodeHeight());
		setLayoutX(vo.getDiagNodeX());
		setLayoutY(vo.getDiagNodeY());
		setId(vo.getDiagNodeNo() + "");

		if (vo.isRemoved()) {
			getStyleClass().add("diagram-node-base-removed");
		} else {
			getStyleClass().add("diagram-node-base");
		}
		setMinSize(DiagNodeBounds.GRID_UNIT, DiagNodeBounds.GRID_UNIT);

		setBorder(new Border(
				new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

		setProperties(vo.getProperties());

	}

	@Override
	public DiagNodeBounds getDiagNodeBounds() {
		return diagData;
	}

	@Override
	public DiagNodeVo getDiagNodeVo() {

		vo.getProperties().put("opacity", getOpacity());
		vo.getProperties().put("style", getStyle());
		if (getGraphic() instanceof ImageView) {
			vo.getProperties().put("graphic", ((ImageView) getGraphic()).getUserData());
		}
		return vo;
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

	@Override
	public void setProperties(Map<String, Object> properties) {

		Object value;

		value = properties.get("opacity");
		if (value != null) {
			try {
				setOpacity(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("style");
		if (value != null) {
			try {
				setStyle(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		value = properties.get("graphic");
		if (value != null) {
			try {
				imageView = new ImageView(ImagePointer.getImage(value.toString()));
				imageView.setUserData(value.toString());
				imageView.fitWidthProperty().bind(widthProperty());
				imageView.fitHeightProperty().bind(heightProperty());
				setGraphic(imageView);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public FxBounds getBounds() {
		return new FxBounds(getLayoutX(), getLayoutY(), getPrefWidth(), getPrefHeight());
	}

}

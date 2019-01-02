package com.fxms.ui.node.diagram.event;

import com.fxms.ui.bas.vo.ui.UiBasicVo;

public class FxBounds {

	private double x = -1;
	private double y = -1;
	private double width;
	private double height;

	public FxBounds() {

	}

	public void makeBound(UiBasicVo node) {

		x = (x == -1 ? node.getUiX() : x > node.getUiX() ? node.getUiX() : x);
		y = (y == -1 ? node.getUiY() : y > node.getUiY() ? node.getUiY() : y);

		if (width < node.getUiX() + node.getUiWidth()) {
			width = node.getUiX() + node.getUiWidth();
		}

		if (height < node.getUiY() + node.getUiHeight()) {
			height = node.getUiY() + node.getUiHeight();
		}

	}

	public FxBounds(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("(%d,%d)(%d,%d)", (int) x, (int) y, (int) width, (int) height);
	}

}

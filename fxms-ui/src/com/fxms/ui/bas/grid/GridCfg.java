package com.fxms.ui.bas.grid;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;

public class GridCfg {

	public static GridCfg getGridCfg(Rectangle2D screen) {
		GridCfg cfg = new GridCfg(16, 100, 100);
		cfg.hgap = 3;
		cfg.vgap = 3;

		double size;

		if (cfg.dir == GridNode.Direction.Left2Right) {
			size = cfg.getBaseHeight(screen.getHeight());
			cfg.nodeWidth = cfg.nodeWidth * (size / cfg.nodeHeight);
			cfg.nodeHeight = size;
		} else {
			size = cfg.getBaseWidth(screen.getWidth());
			cfg.nodeHeight = cfg.nodeHeight * (size / cfg.nodeWidth);
			cfg.nodeWidth = size;
		}

		return cfg;
		// Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		//
		// // set Stage boundaries to visible bounds of the main screen
		// primaryStage.setX(primaryScreenBounds.getMinX());
		// primaryStage.setY(primaryScreenBounds.getMinY());
		// primaryStage.setWidth(primaryScreenBounds.getWidth());
		// primaryStage.setHeight(primaryScreenBounds.getHeight());
	}

	public static boolean showTitle = true;

	public int max = 4;
	public double nodeHeight = 48;
	public double nodeWidth = 48;
	public int hgap = 5;
	public int vgap = 5;
	public GridNode.Direction dir = GridNode.Direction.Left2Right;
	public Insets padding = new Insets(10, 10, 10, 10);
	public boolean gridLineVisible = false;

	public GridCfg() {

	}

	public GridCfg(GridNode.Direction dir, int max, int hgap, int vgap, double nodeWidth, double nodeHeight) {
		this.dir = dir;
		this.max = max;
		this.hgap = hgap;
		this.vgap = vgap;
		this.nodeHeight = nodeHeight;
		this.nodeWidth = nodeWidth;
	}

	public GridCfg(int max, double nodeWidth, double nodeHeight) {
		this.max = max;
		this.nodeHeight = nodeHeight;
		this.nodeWidth = nodeWidth;
	}

	public GridCfg(int max) {
		this.max = max;
	}

	public void remakeMax(int width, int height) {
		if (dir == GridNode.Direction.Left2Right) {
			max = (int) ((height - (padding.getTop() + padding.getBottom())) / (nodeHeight + hgap));
		} else if (dir == GridNode.Direction.Top2Bottom) {
			max = (int) ((width - (padding.getLeft() + padding.getRight())) / (nodeWidth + vgap));
		}
	}

	public double getBaseHeight(double height) {
		double ret = (height - (padding.getTop() + padding.getBottom()) - 30) / max;
		return ret - hgap;
	}

	public double getBaseWidth(double width) {
		double ret = (width - (padding.getLeft() + padding.getRight())) / max;
		return ret - vgap;
	}

	public double getNodeWidth(int colSize) {
		return nodeWidth + ((nodeWidth + hgap) * (colSize - 1));
	}

	public double getNodeHeight(int rowSize) {
		return nodeHeight + ((nodeHeight + vgap) * (rowSize - 1));
	}
}

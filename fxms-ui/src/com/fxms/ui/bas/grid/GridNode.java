package com.fxms.ui.bas.grid;

public class GridNode {
	
	public enum Direction {
		Left2Right, Top2Bottom;
	}


	public static final class XY {

		public int x;
		public int y;

		public XY() {

		}

		public XY(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static final class Size {

		private int colSize = 1;
		private int rowSize = 1;

		public Size(int colSize, int rowSize) {
			this.colSize = colSize;
			this.rowSize = rowSize;
		}

		public int getColSize() {
			return colSize;
		}

		public void setColSize(int colSize) {
			this.colSize = colSize;
		}

		public int getRowSize() {
			return rowSize;
		}

		public void setRowSize(int rowSize) {
			this.rowSize = rowSize;
		}

	}

	public GridNode.XY xy;

	public GridNode.Size size;

	public GridNode() {

	}

	public GridNode(GridNode.XY xy, GridNode.Size size) {
		this.xy = xy;
		this.size = size;
	}
	
	public GridNode(GridNode.XY xy, int colSize, int rowSize) {
		this.xy = xy;
		this.size = new GridNode.Size(colSize, rowSize);
	}

	public GridNode(int x, int y, int colSize, int rowSize) {
		this.xy = new GridNode.XY(x, y);
		this.size = new GridNode.Size(colSize, rowSize);
	}
}

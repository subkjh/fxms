package com.fxms.ui.bas.grid;

import java.util.Arrays;

public class GridXyTop2Bottom extends GridXy {

	private int lastRow;

	protected GridXyTop2Bottom(int max) {
		super(max);
	}

	@Override
	public GridNode.XY find(int colSize, int rowSize) {

		GridNode.XY xy = new GridNode.XY();
		char data[];
		int matchCount = 0;
		xy.x = 0;
		xy.y = 0;

		NEXT: for (int row = (fromToLast ? lastRow : 0); row < dataList.size(); row++) {
			data = dataList.get(row);

			for (int col = xy.x; col < max; col++) {

				if (data[col] == 'x' && (col + colSize) <= max) {

					if (isEmpty(data, col, colSize)) {

						if (matchCount == 0) {
							xy.x = col;
							xy.y = row;
						}

						matchCount++;

						if (matchCount == rowSize) {
							setData(xy, colSize, rowSize);
							lastRow = xy.y;
							return xy;
						}

						continue NEXT;
					}
				} else if (matchCount != 0) {
					matchCount = 0;
				}
			}
			xy.y = 0;
			matchCount = 0;
		}

		addNewCol();
		return find(colSize, rowSize);
	}

	private void setData(GridNode.XY xy, int colSize, int rowSize) {
		char data[];

		for (int row = 0; row < rowSize; row++) {
			data = dataList.get(row + xy.y);
			for (int col = 0; col < colSize; col++) {
				data[col + xy.x] = 'o';
			}
		}
	}

	@Override
	protected void printData() {
		for (char data[] : dataList) {
			System.out.println(Arrays.toString(data));
		}
	}
}

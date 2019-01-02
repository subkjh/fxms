package com.fxms.ui.bas.grid;

public class GridXyLeft2Right extends GridXy {

	private int lastCol = 0;

	protected GridXyLeft2Right(int max) {
		super(max);
	}

	@Override
	public GridNode.XY find(int colSize, int rowSize) {

		GridNode.XY xy = new GridNode.XY();
		char data[];
		int matchCount = 0;
		xy.x = 0;
		xy.y = 0;

		NEXT: for (int col = (fromToLast ? lastCol : 0); col < dataList.size(); col++) {
			data = dataList.get(col);

			for (int row = xy.y; row < max; row++) {

				if (data[row] == 'x' && (row + rowSize) <= max) {

					if (isEmpty(data, row, rowSize)) {

						if (matchCount == 0) {
							xy.x = col;
							xy.y = row;
						}

						matchCount++;

						if (matchCount == colSize) {
							setData(xy, colSize, rowSize);
							lastCol = xy.x;
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

		for (int col = 0; col < colSize; col++) {
			data = dataList.get(col + xy.x);
			for (int row = 0; row < rowSize; row++) {
				data[row + xy.y] = 'o';
			}
		}
	}

	@Override
	protected void printData() {
		StringBuffer line[] = new StringBuffer[max];
		for (int i = 0; i < line.length; i++) {
			line[i] = new StringBuffer();
		}

		for (char data[] : dataList) {
			for (int i = 0; i < data.length; i++) {
				line[i].append(data[i]);
				line[i].append(" ");
			}
		}

		for (StringBuffer s : line) {
			System.out.println(s);
		}
	}
}

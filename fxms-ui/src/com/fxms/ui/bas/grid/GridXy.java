package com.fxms.ui.bas.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GridXy {

	/** 다음 위치를 최근 위치와 같더나 더 아래에 배치여부 */
	protected boolean fromToLast = false;
	protected int max = 4;
	protected List<char[]> dataList;

	public static GridXy getNewGridXy(GridCfg cfg) {
		if (cfg.dir == GridNode.Direction.Left2Right) {
			return new GridXyLeft2Right(cfg.max);
		} else if (cfg.dir == GridNode.Direction.Top2Bottom) {
			return new GridXyTop2Bottom(cfg.max);
		} else {
			return new GridXyLeft2Right(cfg.max);
		}
	}

	public abstract GridNode.XY find(int colSize, int rowSize);

	protected abstract void printData();

	protected GridXy(int max) {
		dataList = new ArrayList<char[]>();
		this.max = max;
	}

	protected void addNewCol() {
		char data[] = new char[max];
		Arrays.fill(data, 'x');
		dataList.add(data);
	}

	protected boolean isEmpty(char data[], int index, int offset) {
		for (int i = index; i < (index + offset); i++) {
			if (data[i] != 'x') {
				return false;
			}
		}
		return true;
	}
}

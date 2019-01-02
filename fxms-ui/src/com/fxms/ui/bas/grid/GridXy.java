package com.fxms.ui.bas.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

	public static void main(String[] args) {
		GridXy grid = getNewGridXy(new GridCfg(4));
		// grid.find(1, 1);
		// grid.printData();
		grid.fromToLast = true;
		grid.test();
	}

	protected void test() {

		Scanner sc = new Scanner(System.in);
		String command;
		int col, row;
		while (true) {

			System.out.print("$ ");
			command = sc.nextLine();

			if (command == null || command.length() == 0) {
				continue;
			}

			String ss[] = command.split(",");
			try {
				col = Integer.parseInt(ss[0]);
				row = Integer.parseInt(ss[1]);

				find(col, row);
				printData();
			} catch (Exception e) {
				e.printStackTrace();
			}

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

package subkjh.module.excel;

public class TestExcel {

	public static void main(String[] args) {

		TestExcel c = new TestExcel();
		c.test2();
	}

	void test1() {
		long ptime = System.currentTimeMillis();

		MakeExcel excel = new MakeExcel();
		excel.addCell(0, 0, "Test", MakeExcel.CELL_STYLE_TITLE, 0, 4);
		excel.setRowHeight(0, 100);
		excel.addCell(1, 0, "r1", MakeExcel.CELL_STYLE_COLUMN, 1, 0);
		excel.addCell(1, 1, "r2", MakeExcel.CELL_STYLE_COLUMN, 0, 1);
		excel.addCell(2, 1, "c2", MakeExcel.CELL_STYLE_COLUMN, 0, 0);
		excel.addCell(2, 2, "c2", MakeExcel.CELL_STYLE_COLUMN, 0, 0);
		excel.addCell(1, 3, "r3", MakeExcel.CELL_STYLE_COLUMN, 1, 0);
		excel.addCell(1, 4, "r4", MakeExcel.CELL_STYLE_COLUMN, 1, 0);
		excel.setColWidths(100, 20, 50, 100, 500);

		for (int i = 3; i < 1000000; i++) {
			excel.addCells(i, 0, 1, 2, 3, 4, 5);
			if (i % 10000 == 0) {
				System.out.println(i);
			}
		}

		System.out.println(System.currentTimeMillis() - ptime);
		ptime = System.currentTimeMillis();

		excel.export("a.xlsx");

		System.out.println(System.currentTimeMillis() - ptime);
	}

	void test2() {
		long ptime = System.currentTimeMillis();

		MakeExcel excel = new MakeExcel();
		excel.addCell(0, 0, "Test", MakeExcel.CELL_STYLE_TITLE, 0, 5);
		excel.setRowHeight(0, 36);

		excel.setColumns(new ExcelColumn(1, 0, "c0-0", 2, 1));
		excel.setColumns(new ExcelColumn(1, 1, "c1-2", 1, 2));
		excel.setColumns(new ExcelColumn(2, 1, "c1-2r0", 1, 1));
		excel.setColumns(new ExcelColumn(2, 2, "c1-2r1", 1, 1));
		excel.setColumns(new ExcelColumn(1, 3, "c3", 2, 1));
		excel.setColumns(new ExcelColumn(1, 4, "c4", 2, 1));
		excel.setColumns(new ExcelColumn(1, 5, "c5", 2, 1));

		excel.setColWidths(100, 20, 50, 100, 500);

		for (int i = 3; i < 1000; i++) {
			excel.addCells(i, 0, "v0", "v1", "v2", "v3", "v4", "v5");
			if (i % 10000 == 0) {
				System.out.println(i);
			}
		}

		System.out.println(System.currentTimeMillis() - ptime);
		ptime = System.currentTimeMillis();

		excel.export("tmp/a.xlsx");

		System.out.println(System.currentTimeMillis() - ptime);
	}
}

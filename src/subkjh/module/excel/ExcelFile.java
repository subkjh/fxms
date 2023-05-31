package subkjh.module.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelFile {

	public static void main(String[] args) throws Exception {

		// List<Object[]> list = ExcelReader.getDatas(new
		// File("C:\\WORK\\2011\\MobileKBS\\01. doc\\03. 설계\\코드관리.xlsx"),
		// 4);
		ExcelFile excelFile = new ExcelFile("C:\\Documents and Settings\\Administrator\\바탕 화면\\코드(Code).xlsx", 0, 1, 2);
		excelFile.printInsertQuery("df_cd_code", true);
	}

	private List<Object[]> cellList;

	private Map<Integer, String> col2Map;

	private Map<String, Integer> colMap;

	private int sizeColMax;

	public ExcelFile(File file, String sheetName, int rowNoKey, int rowNoStart) throws Exception {
		Workbook wb = WorkbookFactory.create(new FileInputStream(file));
		Sheet sheet = wb.getSheet(sheetName);
		init(wb, sheet, rowNoKey, rowNoStart);
	}

	public ExcelFile(String filename, int sheetNo, int rowNoKey, int rowNoStart) throws Exception {

		Workbook wb = WorkbookFactory.create(new FileInputStream(filename));
		Sheet sheet = wb.getSheetAt(sheetNo);
		init(wb, sheet, rowNoKey, rowNoStart);

	}

	public Map<String, Integer> getColMap() {
		return colMap;
	}

	public int getSize() {
		return cellList.size();
	}

	public boolean getValue(int index, String colName, boolean defaultVal) {
		Integer col = colMap.get(colName);
		if (col == null)
			return defaultVal;

		Object value = cellList.get(index)[col];
		if (value == null)
			return defaultVal;

		return "Y".equalsIgnoreCase(value.toString());
	}

	public int getValue(int index, String colName, int defaultVal) {
		Integer col = colMap.get(colName);
		if (col == null)
			return defaultVal;

		Object value = cellList.get(index)[col];
		if (value == null)
			return defaultVal;
		try {
			if (value instanceof Number)
				return ((Number) value).intValue();
			return Float.valueOf(value.toString()).intValue();
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public long getValue(int index, String colName, long defaultVal) {
		Integer col = colMap.get(colName);
		if (col == null)
			return defaultVal;

		Object value = cellList.get(index)[col];
		if (value == null)
			return defaultVal;
		try {
			if (value instanceof Number)
				return ((Number) value).longValue();
			return Float.valueOf(value.toString()).longValue();
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public String getValue(int index, String colName, String defaultVal) {
		Integer col = colMap.get(colName);
		if (col == null)
			return defaultVal;

		Object value = cellList.get(index)[col];
		if (value == null)
			return defaultVal;

		return value.toString();
	}

	public void print() {
		String col;
		for (int i = 0; i < sizeColMax; i++) {
			col = col2Map.get(i);
			System.out.print(col + "|");
		}
		System.out.println();
		System.out.println("---------------------------------------------------------------------");

		Object entry[];
		for (int i = 0, size = cellList.size(); i < size; i++) {
			entry = cellList.get(i);
			for (int n = 0; n < entry.length; n++) {
				System.out.print(entry[n] + "|");
			}
			System.out.println();
		}
	}

	public void printInsertQuery(String tablename, boolean isIntOnly) {
		Object entry[];
		for (int row = 0, size = cellList.size(); row < size; row++) {

			entry = cellList.get(row);

			StringBuffer sb = new StringBuffer();

			sb.append("insert into ");
			sb.append(tablename.toUpperCase());
			sb.append(" ( ");
			sb.append(col2Map.get(0));
			for (int i = 1; i < sizeColMax; i++) {
				sb.append(", " + col2Map.get(i));
			}
			sb.append(" ) values ( ");

			sb.append(getColValue(entry[0], isIntOnly));

			for (int n = 1; n < entry.length; n++) {
				sb.append(", " + getColValue(entry[n], isIntOnly));
			}

			sb.append(");");

			System.out.println(sb.toString());
		}
	}

	private Object getCellValue(Cell cell) {
		CellType cellType;

		if (cell == null)
			return null;

		cellType = cell.getCellType();

		if (cellType == CellType.STRING)
			return cell.getStringCellValue();
		else if (cellType == CellType.NUMERIC)
			return cell.getNumericCellValue();
		else if (cellType == CellType.BLANK)
			return null;
		else if (cellType == CellType.FORMULA) {
			// TODO
			try {
				return cell.getCellFormula();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			return cell.toString();

		return null;
	}

	private String getColValue(Object val, boolean isIntOnly) {
		if (val == null)
			return "null";
		String s;
		if (val instanceof Number) {
			if (isIntOnly) {
				s = String.valueOf(((Number) val).longValue());
			} else {
				s = String.valueOf(((Number) val).floatValue());
			}
		} else {
			s = val.toString().replaceAll("'", "''");
		}

		return "'" + s + "'";
	}

	/**
	 * 가장 많은 컬럼의 갯수를 가져옵니다.
	 * 
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	private int getSizeColMax(Sheet sheet) throws Exception {
		Row row;
		int sizeColMax = 0;
		for (int rowNo = 0, size = sheet.getPhysicalNumberOfRows(); rowNo < size; rowNo++) {
			row = sheet.getRow(rowNo);
			if (row == null)
				continue;
			sizeColMax = Math.max(sizeColMax, row.getLastCellNum());
		}
		return sizeColMax;
	}

	private void init(Workbook wb, Sheet sheet, int rowNoKey, int rowNoStart) throws Exception {

		colMap = new HashMap<String, Integer>();
		col2Map = new HashMap<Integer, String>();
		cellList = new ArrayList<Object[]>();

		Map<Integer, String> keyColMap = new HashMap<Integer, String>();
		Object[] entry;
		Row row;
		Cell cell;
		sizeColMax = getSizeColMax(sheet);
		Object cellValue;

		// 1. key 컬럼
		row = sheet.getRow(rowNoKey);
		for (int colNo = 0; colNo < sizeColMax; colNo++) {
			cell = row.getCell(colNo);
			cellValue = getCellValue(cell);
			if (cellValue != null) {
				keyColMap.put(colNo, cellValue.toString());
				colMap.put(cellValue.toString(), colNo);
				col2Map.put(colNo, cellValue.toString());
			}
		}

		// 데이터
		for (int rowNo = rowNoStart, size = sheet.getPhysicalNumberOfRows(); rowNo < size; rowNo++) {
			row = sheet.getRow(rowNo);
			if (row == null)
				continue;
			entry = new Object[sizeColMax];

			cellList.add(entry);

			for (int colNo = 0; colNo < sizeColMax; colNo++) {
				cell = row.getCell(colNo);
				entry[colNo] = getCellValue(cell);
			}
		}
	}

}

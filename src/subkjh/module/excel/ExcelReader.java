package subkjh.module.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
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

import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.util.DaoUtil;

public class ExcelReader {

	/**
	 * 
	 * @param filename 확장자 포함된 화일명
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> getDatas(File filename, int sheetNo) throws Exception {
		return getDatas(new FileInputStream(filename), sheetNo);
	}

	public static List<List<Object[]>> getDatas(File filename) throws Exception {
		int sheetNo = 0;
		List<List<Object[]>> ret = new ArrayList<List<Object[]>>();
		List<Object[]> entry;
		while (true) {
			try {
				entry = getDatas(new FileInputStream(filename), sheetNo);
			} catch (IllegalArgumentException e) {
				break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

			sheetNo++;

			ret.add(entry);
		}

		return ret;
	}

	public static List<Object[]> getDatas(InputStream inp, int sheetNo) throws Exception {
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(sheetNo);

		List<Object[]> list = new ArrayList<Object[]>();
		Row row;
		Cell cell;
		Object entry[];
		CellType cellType;
		int sizeColMax = getSizeColMax(sheet);

		for (int rowNo = 0, size = sheet.getLastRowNum() + 1; rowNo < size; rowNo++) {
			row = sheet.getRow(rowNo);
			entry = new Object[sizeColMax];

			if (row == null) {
				for (int colNo = 0; colNo < entry.length; colNo++) {
					entry[colNo] = null;
				}
			} else {
				// System.out.println(entry.length);
				// System.out.println(row.getLastCellNum());

				for (int colNo = 0; colNo < entry.length; colNo++) {
					cell = row.getCell(colNo);
					if (cell == null) {
						entry[colNo] = null;
						continue;
					}
					cellType = cell.getCellType();

					if (cellType == CellType.STRING)
						entry[colNo] = cell.getStringCellValue();
					else if (cellType == CellType.NUMERIC) {
						entry[colNo] = cell.getNumericCellValue();
					} else if (cellType == CellType.BLANK)
						entry[colNo] = null;
					else if (cellType == CellType.FORMULA) {
						// TODO
						try {
							entry[colNo] = cell.getCellFormula();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else
						entry[colNo] = cell.toString();
				}
			}

			list.add(entry);
		}

		return list;
	}

	public static List<Map<String, Object>> getMap(String filename, int sheetNo, int rowNoKey) throws Exception {
		return getMap(new FileInputStream(filename), sheetNo, rowNoKey);
	}

	public static List<Map<String, Object>> getMap(InputStream inp, int sheetNo, int rowNoKey) throws Exception {
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(sheetNo);
		Map<Integer, String> keyColMap = new HashMap<Integer, String>();
		Map<String, Object> entry;
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		Row row;
		Cell cell;
		int sizeColMax = getSizeColMax(sheet);
		Object cellValue;

		// 1. key 컬럼
		row = sheet.getRow(rowNoKey);
		for (int colNo = 0; colNo < sizeColMax; colNo++) {
			cell = row.getCell(colNo);
			cellValue = getCellValue(cell);
			if (cellValue != null) {
				keyColMap.put(colNo, cellValue.toString());
			}
		}

		// 데이터
		for (int rowNo = rowNoKey + 1, size = sheet.getPhysicalNumberOfRows(); rowNo < size; rowNo++) {
			row = sheet.getRow(rowNo);
			if (row == null)
				continue;
			entry = new HashMap<String, Object>();
			ret.add(entry);
			for (int colNo = 0; colNo < sizeColMax; colNo++) {
				cell = row.getCell(colNo);
				cellValue = getCellValue(cell);
				if (cellValue != null)
					entry.put(keyColMap.get(colNo), cellValue);
			}
		}

		return ret;
	}

	private static Object getCellValue(Cell cell) {
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

	/**
	 * 가장 많은 컬럼의 갯수를 가져옵니다.
	 * 
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	private static int getSizeColMax(Sheet sheet) throws Exception {
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

	/**
	 * Excel 화일로 부터 자바객체 목록으로 변환합니다.<br>
	 * 
	 * @param file     Excel 화일
	 * @param sheetNo  시트번호
	 * @param cls      변환할 자바 클래스
	 * @param rowField 필드열번호<br>
	 *                 이 열 다음부터 자료가 있다고 간주합니다.
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> getListFromExcel(File file, int sheetNo, Class<T> cls, int rowField) throws Exception {
		List<Object[]> cellList;

		try {
			cellList = ExcelReader.getDatas(file, sheetNo);
		} catch (Exception e) {
			throw e;
		}

		T obj;
		List<ExcelField> fieldList = makeFieldList(cellList.get(rowField), cls);
		List<T> list = new ArrayList();
		for (int i = rowField + 1, size = cellList.size(); i < size; i++) {
			obj = cls.newInstance();
			try {
				setObject(cellList.get(i), obj, fieldList);
				list.add(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public static void main(String[] args) throws Exception {

		String sql1 = "insert into ix_sys_bz ( biz_no, biz_name ) values ( NO, 'NAME');";
		String sql2 = "insert into ix_sys_bz_prefix ( biz_no, prefix_val ) values ( NO, 'PREFIX');";
		// List<List<Object[]>> sheetList = ExcelReader.getDatas(new File(
		// "D:\\02.Projects(B)\\2013\\05. [한국스마트카드]단말원격관리시스템\\05. 설계\\26. 데이터베이스
		// 설계서(쿼리생성용).xlsx"));
		List<List<Object[]>> sheetList = ExcelReader.getDatas(new File("datas/biz.xls"));
//		int index = 0;
		String s;
		String sp = null;
		for (List<Object[]> list : sheetList) {
//			index = 0;

			list.remove(0);
			list.remove(0);

			for (Object obj[] : list) {

				s = sql1;
				s = s.replaceAll("NO", ((int) (Double.parseDouble(obj[0].toString()))) + "");
				s = s.replaceAll("NAME", obj[1].toString());

				if (sp == null || sp.equals(s) == false) {
					System.out.println(s);
				}

				sp = s;

			}

			for (Object obj[] : list) {

				s = sql2;
				s = s.replaceAll("NO", ((int) (Double.parseDouble(obj[0].toString()))) + "");
				s = s.replaceAll("PREFIX", obj[2].toString());

				System.out.println(s);

			}
			// System.out.print(index + "\t");
			// for (Object o : obj) {
			// System.out.print(o + "\t");
			// }
			// System.out.println();
			// index++;
			// }
			//
			// System.out
			// .println("------------------------------------------------------------------------------------------------------");
		}
		// List<Map<String, Object>> list =
		// ExcelReader.getMap("C:\\WORK\\2012\\05. [DAIMS]
		// nPrism\\dfc_ems_doc\\설계\\코드(성능).xlsx",
		// 4, 0);
		// ExcelReader.printMap(list);
	}

	public static void print(List<Object[]> list) {
		Object entry[];
		for (int i = 0, size = list.size(); i < size; i++) {
			entry = list.get(i);
			for (int n = 0; n < entry.length; n++) {
				System.out.print(entry[n] + "\t");
			}
			System.out.println();
		}
	}

	public static void printMap(List<Map<String, Object>> list) {
		Map<String, Object> entry, entryKey;

		entryKey = list.get(0);
		for (String key : entryKey.keySet()) {
			System.out.print(key + "\t");
		}
		System.out.println();
		System.out.println("------------------------------------------------------------");
		for (int i = 0, size = list.size(); i < size; i++) {
			entry = list.get(i);
			for (String key : entryKey.keySet()) {
				System.out.print(entry.get(key) + "\t");
			}
			System.out.println();
		}
	}

	private static List<ExcelField> makeFieldList(Object cell[], Class<?> target) {
		List<ExcelField> fieldList = new ArrayList<ExcelField>();
		ExcelField field;
		String methodName;

		for (int i = 0; i < cell.length; i++) {
			if (cell[i] == null || cell[i].toString().trim().length() == 0)
				continue;

			methodName = DaoUtil.getSetter(cell[i].toString());
			field = new ExcelField();
			field.index = i;
			field.method = null;

			for (Method method : target.getMethods()) {
				if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
					field.method = method;
					break;
				}
			}

			fieldList.add(field);
		}

		return fieldList;
	}

	public static String getString(Object val, String defaultVal) {
		return val == null ? defaultVal : val.toString();
	}

	public static int getInt(Object val, int defaultVal) {
		if (val == null)
			return defaultVal;
		try {
			return new Double(val.toString()).intValue();
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static boolean getBoolean(Object val, boolean defaultVal) {
		if (val == null)
			return defaultVal;
		return "Y".equalsIgnoreCase(val.toString());
	}

	private static void setObject(Object cellArray[], Object obj, List<ExcelField> fieldList) throws Exception {
		ExcelField field;
		Object cell;

		for (int index = 0; index < fieldList.size(); index++) {
			field = fieldList.get(index);
			if (field.method == null)
				continue;

			if (cellArray.length <= field.index)
				continue;

			cell = cellArray[field.index];

			if (cell == null || cell.toString().trim().length() == 0)
				continue;

			ObjectUtil.setMethod(obj, field.method, cell.toString());
		}
	}
}

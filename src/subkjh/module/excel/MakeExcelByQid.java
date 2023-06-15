package subkjh.module.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import fxms.bas.impl.dao.TestQid;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;
import subkjh.dao.def.DaoListener;

public class MakeExcelByQid implements DaoListener {

	public static void main(String[] args) throws Exception {
		try {
			DBManager.getMgr().addDataBase(new File("deploy/conf/databases.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		DataBase database = DBManager.getMgr().getDataBase("ADAMS_2018_DEV_MIAPP");

		QidDao tran = database.createQidDao(TestQid.QUERY_XML_FILE);
		MakeExcelByQid c = new MakeExcelByQid(-1);
		c.addAlias("시도호", "시도\n호");
		c.makeExcel(tran, "QID_SELECT_EXCEL_LIST", null, "test", "excel-test-qid.xlsx");
	}

	private Map<String, String> columnAliasMap;
	/** 실제 Excel에서 사용하는 컬럼, 여기에는 Extra도 포함됩니다 . */
	private List<ExcelColumn> columns;
	/** DB외 컬럼 */
	private List<ExcelColumn> columnsExtra;
	private MakeExcelList excel;
	private int fileIndex = 1;
	private String filename;
	private int sizePerFile = 0;
	private String title;

	public MakeExcelByQid() {
	}

	/**
	 * 
	 * @param sizePerFile 화일당 페이지 수
	 */
	public MakeExcelByQid(int sizePerFile) {
		this.sizePerFile = sizePerFile;
	}

	/**
	 * 컬럼에 대한 별칭을 설정합니다.
	 * 
	 * @param colName 컬럼
	 * @param alias   별칭
	 */
	public void addAlias(String colName, String alias) {
		columnAliasMap = new HashMap<String, String>();
		columnAliasMap.put(colName, alias);
	}

	/**
	 * DB외 컬럼을 추가합니다.
	 * 
	 * @param column
	 */
	public void addColumnExtra(ExcelColumn column) {
		if (columnsExtra == null) {
			columnsExtra = new ArrayList<ExcelColumn>();
		}
		if (columns == null) {
			columns = new ArrayList<ExcelColumn>();
		}
		columnsExtra.add(column);
		columns.add(column);
	}

	/**
	 * Excel 컬럼을 설정합니다.
	 * 
	 * @param colNames 컬럼명<br>
	 *                 &lt;K|D|S&gt;&lt;S|I|P|F&gt;&lt;C|R|L&gt;&lt;숫자3자리&gt;_컬럼명
	 *                 형식을 취합니다.<br>
	 *                 G|K|D|S = G(그룹), K(키로 사용), D(단순 데이터), S(소계가 생김)<br>
	 *                 S|I|P|F = S(문자열), I(정수), F(실수), P(백분율)<br>
	 *                 C|L|R = C(중앙정렬), R(오른쪽정렬), L(왼쪽정렬)<br>
	 *                 숫자3자리 = 컬럼 넓이<br>
	 *                 컬럼명 = 컬럼내용<br>
	 * @throws Exception
	 */
	public void addColumns(List<String> colNames) throws Exception {
		if (columns == null) {
			columns = new ArrayList<ExcelColumn>();
		}

		ExcelColumn element;
		String column;
		for (int i = colNames.size() - 1; i >= 0; i--) {
			column = colNames.get(i);
			element = new ExcelColumn(i, column.substring(column.indexOf("_") + 1), //
					getDataFormat(column), //
					getWidth(column), //
					getTag(column), //
					getAlignment(column));

			columns.add(0, element);
		}
	}

	/**
	 * DB 데이터 외의 기타 값을 제공합니다.<br>
	 * 주로 계산식이 포함됩니다.
	 * 
	 * @return
	 */
	public Object[] getExtraValues() {
		if (columnsExtra == null)
			return null;
		Object ret[] = new Object[columnsExtra.size()];
		for (int i = 0; i < columnsExtra.size(); i++) {
			ret[i] = null; // columnsExtra.get(i).tag;
		}
		return ret;
	}

	/**
	 * Excel 화일을 만듭니다.
	 * 
	 * @param title    타이틀
	 * @param filename 화일명<br>
	 *                 확장자는 제외합니다.
	 * @return 처리결과
	 */
	public void makeExcel(QidDao tran, String qid, Object para, String title, String filename) throws Exception {

		this.title = title;
		this.filename = filename;

		excel = new MakeExcelList();

		tran.selectQid(qid, para, this);
	}

	public void onExecuted(Object data, Exception ex) throws Exception {

	}

	@Override
	public void onFinish(Exception ex) {

		excel.mergeGroup(null); // 마지막 데이터 병합을 위해서 호출함

		excel.close(filename);
	}

	@Override
	public void onSelected(int rowNo, Object obj) {
		Object[] data = (Object[]) obj;
		excel.addRow(data, getExtraValues());
	}

	@Override
	public void onStart(String colNames[]) throws Exception {

		List<String> columnNames = Arrays.asList(colNames);
		addColumns(columnNames);

		excel.open(title, getHeaders(), getWidths(), getAlignments(), getDataFormast(), getTags());
		// excel.setCellToSum(getSummarys());
	}

	private String getAlias(String column) {
		if (columnAliasMap == null)
			return column;
		String ret = columnAliasMap.get(column);
		return ret == null ? column : ret;
	}

	private HorizontalAlignment getAlignment(String column) {
		char ch = column.toUpperCase().charAt(2);

		if (ch == 'L')
			return HorizontalAlignment.LEFT;
		else if (ch == 'R')
			return HorizontalAlignment.RIGHT;
		else
			return HorizontalAlignment.CENTER;
	}

	// private HorizontalAlignment[] getAlignments() {
	// HorizontalAlignment ret[] = new HorizontalAlignment[columns.size()];
	// for (int i = 0; i < columns.size(); i++) {
	// ret[i] = columns.get(i).alignment;
	// }
	// return ret;
	// }

	private String[] getAlignments() {
		String ret[] = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			ret[i] = columns.get(i).alignment == HorizontalAlignment.LEFT ? "L"
					: columns.get(i).alignment == HorizontalAlignment.RIGHT ? "R" : "C";
		}
		return ret;
	}

	private String[] getDataFormast() {
		String ret[] = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			ret[i] = columns.get(i).dataFormat;
		}
		return ret;
	}

	private ExcelColumn.COLUM_TAG[] getTags() {
		ExcelColumn.COLUM_TAG ret[] = new ExcelColumn.COLUM_TAG[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			ret[i] = columns.get(i).tag;
		}
		return ret;
	}

	private String getDataFormat(String column) {
		char ch = column.toUpperCase().charAt(1);

		if (ch == 'S')
			return "text";
		else if (ch == 'I')
			return "#,##0";
		else if (ch == 'F')
			return "#,##0.0";
		else if (ch == 'P')
			return "##0.0%";
		else
			return "text";
	}

	private String[] getHeaders() {
		String ret[] = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			ret[i] = getAlias(columns.get(i).text);
		}
		return ret;
	}

	private ExcelColumn.COLUM_TAG getTag(String column) {
		char ch = column.toUpperCase().charAt(0);

		if (ch == 'K')
			return ExcelColumn.COLUM_TAG.Key;
		else if (ch == 'G')
			return ExcelColumn.COLUM_TAG.Group;
		else if (ch == 'S')
			return ExcelColumn.COLUM_TAG.Sum;
		else
			return ExcelColumn.COLUM_TAG.Normal;

	}

	// private String[] getSummarys() {
	// String ret[] = new String[columns.size()];
	// for (int i = 0; i < columns.size(); i++) {
	// ret[i] = columns.get(i).summary;
	// }
	// return ret;
	// }

	private int getWidth(String column) throws Exception {
		return Integer.parseInt(column.substring(3, 6));
	}

	private int[] getWidths() {
		int ret[] = new int[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			ret[i] = columns.get(i).width;
		}
		return ret;
	}
}

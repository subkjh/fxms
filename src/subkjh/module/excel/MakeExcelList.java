package subkjh.module.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class MakeExcelList extends MakeExcel {

	public static void main(String[] args) {
		MakeExcelList c = new MakeExcelList();

		String columns[] = { "고객번호", "고객명", "가번", "TID", "일자", "시도호", "완료호", "완료율" };
		int widths[] = { 200, 200, 200, 200, 80, 100, 100, 100 };
		String alignments[] = { "L", "L", "L", "L", "C", "R", "R", "R" };
		String datafmts[] = { null, null, null, null, null, null, null, "##0.0%" };

		c.open("목록 자동 생성", columns, widths, alignments, datafmts, null);
		// c.setCellToSum("key", "key2", "key", "key", "", "=sum", "=sum",
		// "=G%rowNo%/F%rowNo%");

		List<Object[]> list = new ArrayList<Object[]>();

		String custNo;
		String custName;
		String entSvcNo;
		String tid;
		String date;
		long att, ans, tmp;
		for (int index = 0; index < 15; index++) {
			custNo = "000" + ((int) (index / 3));
			custName = "고객 No." + custNo;
			entSvcNo = "0012" + (index % 3);
			tid = "aaa";
			for (int yyyymmdd = 20180101; yyyymmdd < 20181231; yyyymmdd++) {
				date = yyyymmdd + "";
				att = (long) (Math.random() * 1000);
				ans = (long) (Math.random() * 1000);
				if (att < ans) {
					tmp = att;
					att = ans;
					ans = tmp;
				}

				c.addRow(new Object[] { custNo, custName, entSvcNo, tid, date, att, ans, "=G%rowNo%/F%rowNo%" });
			}
			tid = "bbb";
			for (int yyyymmdd = 20180101; yyyymmdd < 20181231; yyyymmdd++) {
				date = yyyymmdd + "";
				att = (long) (Math.random() * 1000);
				ans = (long) (Math.random() * 1000);
				if (att < ans) {
					tmp = att;
					att = ans;
					ans = tmp;
				}

				c.addRow(new Object[] { custNo, custName, entSvcNo, tid, date, att, ans, "=G%rowNo%/F%rowNo%" });
			}

		}

		c.close("excel-test-list.xlsx");
	}

	/** 컬럼 수 */
	private int colLength = 0;
	/** 이전 데이터 */
	private Object entryPrev[];
	/** 현재 ROW 번호 */
	private int rowNo = 0;
	/** 컬럼 태그 */
	private ExcelColumn.COLUM_TAG tags[];
	/** 각 행의 병합 데이터의 시작 열번호를 가지는 맵 */
	private Map<String, Integer> firstRowNoMap = new HashMap<String, Integer>();
	/** 병합 컬럼이 존재하는지 여부 */
	private boolean hasGroupTag = false;

	/**
	 * 
	 * @param title
	 *            타이틀
	 */
	public MakeExcelList() {
	}

	/**
	 * 열을 추가한다.
	 * 
	 * @param entryCur
	 * @param dataExtra
	 * @return
	 */
	public int addRow(Object[] entryCur, Object... dataExtra) {

		rowNo++;

		addCells(rowNo, 0, entryCur);

		if (dataExtra != null && dataExtra.length > 0) {
			addCells(rowNo, entryCur.length, dataExtra);
		}

		mergeGroup(entryCur);

		return rowNo;
	}

	public void addRows(List<Object[]> list, Object... dataExtra) {
		Object entryCur[];
		for (int row = 0, size = list.size(); row < size; row++) {
			entryCur = list.get(row);
			addRow(entryCur, dataExtra);
		}

	}

	/**
	 * 파일명을 기록합니다.
	 * 
	 * @param filename
	 *            화일명
	 */
	public void close(String filename) {
		// rowNo = addSum(rowNo, entryPrev, entryPrev, true);
		// addSumTotal(rowNo);
		export(filename);
	}

	/**
	 * 엑셀 객체를 연다.
	 * 
	 * @param title
	 *            제목
	 * @param columns
	 *            컬럼명
	 * @param widths
	 *            컬럼넓이
	 * @param alignments
	 *            컬럼정렬
	 * @param dataFormats
	 *            컬럼데이터형식
	 * @param tags
	 *            컬럼태그
	 */
	public void open(String title, String columns[], int widths[], String alignments[], String dataFormats[],
			ExcelColumn.COLUM_TAG tags[]) {

		this.colLength = columns.length;
		this.tags = tags;
		rowNo = 0;
		setTitle(rowNo, 0, title, colLength);
		setRowHeight(rowNo, 40);

		setColWidths(widths);
		setColDataFormats(dataFormats);
		setColAlignments(alignments);

		rowNo = 1;
		setColTexts(rowNo, columns);
		setRowHeight(rowNo, 30);

		if (tags != null) {
			for (ExcelColumn.COLUM_TAG tag : tags) {
				if (tag == ExcelColumn.COLUM_TAG.Group) {
					hasGroupTag = true;
					break;
				}
			}
		}
	}

	/**
	 * 두 객체가 동일한지 여부를 판단한다.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	private boolean equalObject(Object o1, Object o2) {
		return o1 != null && o2 != null && o1.equals(o2);
	}

	@Override
	protected void initCellStyle() {
		super.initCellStyle();
		makeCellStyle("total", FONT_DEFAULT, null, BorderStyle.THIN, HorizontalAlignment.LEFT, null);
	}

	/**
	 * 병합 컬럼이 있으면 병합한다.
	 * 
	 * @param entryCur
	 */
	protected void mergeGroup(Object[] entryCur) {

		if (hasGroupTag == false) {
			return;
		}

		int rowNo = this.rowNo;

		if (entryCur == null) {
			rowNo++;
			entryCur = new Object[entryPrev.length];
			Arrays.fill(entryCur, null);
		}

		Integer firstRowNo;
		if (entryPrev != null) {
			ExcelColumn.COLUM_TAG tag;
			for (int i = 0; i < tags.length; i++) {
				tag = tags[i];
				if (tag == ExcelColumn.COLUM_TAG.Group) {
					if (equalObject(entryCur[i], entryPrev[i])) {
						firstRowNo = firstRowNoMap.get(i + "\t" + entryCur[i]);
						if (firstRowNo == null) {
							firstRowNoMap.put(i + "\t" + entryCur[i], rowNo - 1);
						}
					} else {
						firstRowNo = firstRowNoMap.remove(i + "\t" + entryPrev[i]);
						if (firstRowNo != null) {
							mergeCell(null, firstRowNo, i, rowNo - firstRowNo - 1, 0);
						}
						firstRowNoMap.put(i + "\t" + entryCur[i], rowNo);
					}
				}
			}
		}
		entryPrev = entryCur;
	}

}

// public void setCellToSum(String... summary) {
//
// this.isKeyTemps = new boolean[summary.length];
//
// this.summary = summary;
// sumValues = new double[summary.length][];
// for (int i = 0; i < sumValues.length; i++) {
// sumValues[i] = new double[summary.length];
// for (int j = 0; j < sumValues[i].length; j++) {
// sumValues[i][j] = 0;
// }
// }
//
// if (summary == null || summary.length == 0) {
// colNoFirstSum = -1;
// } else {
// for (int i = 0; i < summary.length; i++) {
// if (summary[i] != null && summary[i].equals("=sum")) {
// colNoFirstSum = i;
// break;
// }
// }
// }
// }

// private int addSum(int rowNo, int colNo, int colNoLastKey) {
// int colNo1 = colNoLastKey < 0 ? colNo + 1 : colNoLastKey;
// int colNo2 = colNoFirstSum - 1;
//
// addCells(rowNo, 0, getValueNull());
//
// addCell(rowNo, colNo1, "소계", getCellStyle("total"));
//
// for (int i = 0; i < summary.length; i++) {
// if (summary[i].startsWith("=")) {
// addCell(rowNo, i, summary[i].equals("=sum") ? sumValues[colNo1][i] :
// summary[i]);
// sumValues[colNo1][i] = 0;
// }
// }
//
// // System.out.println(rowNo + ", " + colNo1 + ", " + colNo2);
// //
// // if (colNo2 >= 0)
// // addCellRangeAddress(rowNo, rowNo, colNo1, colNo2);
//
// rowNo++;
// return rowNo;
// }
//
// private int addSum(int rowNo, Object[] entryPrev, Object[] entryCur, boolean
// isForce) {
// if (summary == null)
// return rowNo;
// if (entryPrev == null || entryCur == null)
// return rowNo;
//
// int conNoLastKey = -1;
//
// // 합산할 컬럼을 미리 구합니다.
// A: for (int i = 0; i < summary.length; i++) {
// isKeyTemps[i] = summary[i].equals("key") && (entryPrev[i].equals(entryCur[i])
// == false || isForce);
//
// // 상위가 합산되어야 한다면 하위는 무조건 합산되어야 합니다.
// if (isKeyTemps[i]) {
// for (; i < summary.length; i++)
// isKeyTemps[i] = summary[i].equals("key");
// break A;
// }
// }
//
// for (int colNo = summary.length - 1; colNo >= 0; colNo--) {
//
// if (isKeyTemps[colNo]) {
//
// rowNo = addSum(rowNo, colNo, conNoLastKey);
//
// // 다음 키까지 Cell도 각각 컬럼을 병합합니다.
// for (int i = colNo + 1; i < summary.length; i++) {
// if (summary[i].equals("key2")) {
// addCellRangeAddress(rowNoStart[colNo], rowNo - 1, i, i);
// } else {
// break;
// }
// }
//
// // 자신을 병합합니다.
// // addCellRangeAddress(rowNoStart[colNo], rowNo - 1, colNo, colNo);
// conNoLastKey = colNo;
// }
// }
//
// for (int i = 0; i < summary.length; i++) {
// if (isKeyTemps[i])
// rowNoStart[i] = rowNo;
// }
//
// return rowNo;
// }
//
// /**
// * 전체 소개 추가합니다.
// *
// * @param rowNo
// * 추가할 열 번호
// * @return 추가후 열 번호
// */
// private int addSumTotal(int rowNo) {
// if (isMakeSummaryTotal == false || summary == null)
// return rowNo;
//
// for (int colNo = 0; colNo < summary.length; colNo++) {
// if (summary[colNo].equals("key")) {
// rowNo = addSum(rowNo, colNo, colNo);
// rowNo++;
// break;
// }
// }
// return rowNo;
// }

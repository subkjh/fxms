package subkjh.module.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MakeExcel {

	public static final String CELL_STYLE_TITLE = "title";
	public static final String CELL_STYLE_COLUMN = "column";
	public static final String CELL_STYLE_INTEGER = "integer";
	public static final String CELL_STYLE_FLOAT = "float";
	public static final String CELL_STYLE_TEXT = "text";

	public static final String FONT_DEFAULT = "default";
	public static final String FONT_COLUMN = "column";
	public static final String FONT_TITLE = "title";

	/** 컬럼 길이 */
	protected int cellWidth2[];

	// private List<Sheet> sheets = new ArrayList<Sheet>();

	protected SXSSFWorkbook wb;
	// protected XSSFWorkbook wb;
	// protected HSSFWorkbook wb;

	private CreationHelper createHelper;

	private String fontNameDefault = "맑은 고딕";
	private final short HEIGHT_UNIT = 15;

	private Sheet sheet;

	private Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();

	private Map<String, Font> fontMap = new HashMap<String, Font>();

	private final short WIDTH_UNIT = 32;

	private XSSFDrawing xSSFDrawing = null;

	private int maxColNo = 0;

	public MakeExcel() {
		this("Excel");
	}

	public MakeExcel(String sheetName) {
		wb = new SXSSFWorkbook(10000);
		// wb = new XSSFWorkbook();
		createHelper = wb.getCreationHelper();
		if (sheetName != null)
			sheet = makeSheet(sheetName);

		initFont();
		initCellStyle();
	}

	public Cell addCell(int rowNo, int colNo, Object value) {
		CellStyle cellStyle = getCellStyle("col" + colNo);
		if (cellStyle == null)
			cellStyle = getCellStyleForValue(value);
		return addCell(rowNo, colNo, value, cellStyle);
	}

	public Cell addCell(int rowNo, int colNo, Object value, CellStyle cellStyle) {
		return addCell(sheet, rowNo, colNo, value, cellStyle);
	}

	/**
	 * 
	 * @param rowNo
	 * @param colNo
	 * @param value
	 * @param cellStyleName
	 * @param rowCount
	 *            추가 병합할 열 수
	 * @param colCount
	 *            추가 병합할 컬럼 수
	 * @return
	 */
	public Cell addCell(int rowNo, int colNo, Object value, String cellStyleName, int rowCount, int colCount) {

		CellStyle cellStyle = getCellStyle(cellStyleName);

		CellRangeAddress range = mergeCell(sheet, rowNo, colNo, rowCount, colCount);
		if (range != null && cellStyle != null) {
			RegionUtil.setBorderBottom(cellStyle.getBorderBottom(), range, sheet);
			RegionUtil.setBorderTop(cellStyle.getBorderTop(), range, sheet);
			RegionUtil.setBorderLeft(cellStyle.getBorderLeft(), range, sheet);
			RegionUtil.setBorderRight(cellStyle.getBorderRight(), range, sheet);
		}

		return addCell(rowNo, colNo, value, cellStyle);
	}

	public Cell addCell(Sheet excelSheet, int rowNo, int colNo, Object value, CellStyle cellStyle) {
		Sheet s = excelSheet != null ? excelSheet : sheet;

		Row row = s.getRow(rowNo);
		if (row == null)
			row = s.createRow(rowNo);

		Cell cell = row.createCell(colNo);
		cell.setCellStyle(cellStyle);
		setCellValue(cell, value);

		if (colNo > maxColNo) {
			maxColNo = colNo;
		}

		return cell;
	}

	public Cell addCell(Sheet excelSheet, int rowNo, int colNo, Object value, String cellStyleName, int rowCount,
			int colCount) {

		CellStyle cellStyle = getCellStyle(cellStyleName);

		if (rowCount > 0 || colCount > 0) {
			CellRangeAddress range = new CellRangeAddress(rowNo, // first row
																	// (0-based)
					rowNo + rowCount, // last row (0-based)
					colNo, // first column (0-based)
					colNo + colCount // last column (0-based)
			);

			excelSheet.addMergedRegion(range);

			// cellStyle.setBorderBottom(BorderStyle.THIN);
			// cellStyle.setBorderLeft(BorderStyle.THIN);
			// cellStyle.setBorderTop(BorderStyle.THIN);
			// cellStyle.setBorderRight(BorderStyle.THIN);
			if (cellStyle != null) {
				RegionUtil.setBorderBottom(cellStyle.getBorderBottom(), range, excelSheet);
				RegionUtil.setBorderTop(cellStyle.getBorderTop(), range, excelSheet);
				RegionUtil.setBorderLeft(cellStyle.getBorderLeft(), range, excelSheet);
				RegionUtil.setBorderRight(cellStyle.getBorderRight(), range, excelSheet);
				RegionUtil.setBottomBorderColor(cellStyle.getBottomBorderColor(), range, excelSheet);
				RegionUtil.setLeftBorderColor(cellStyle.getLeftBorderColor(), range, excelSheet);
				RegionUtil.setRightBorderColor(cellStyle.getRightBorderColor(), range, excelSheet);
				RegionUtil.setTopBorderColor(cellStyle.getTopBorderColor(), range, excelSheet);
			}

			// HSSFRegionUtil.setBorderBottom( (short) 1, range, sheet, wb );
		}

		return addCell(excelSheet, rowNo, colNo, value, cellStyle);
	}

	public CellRangeAddress mergeCell(Sheet excelSheet, int rowNo, int colNo, int rowCount, int colCount) {

		if (rowCount <= 0 && colCount <= 0) {
			return null;
		}

		Sheet s = excelSheet != null ? excelSheet : sheet;

		CellRangeAddress range = new CellRangeAddress(rowNo, // first row
																// (0-based)
				rowNo + rowCount, // last row (0-based)
				colNo, // first column (0-based)
				colNo + colCount // last column (0-based)
		);

		s.addMergedRegion(range);

		return range;
	}

	public void addCellImage(int rowNo, int colNo, File file, int rowCount, int colCount) throws Exception {

		xSSFDrawing = (XSSFDrawing) sheet.createDrawingPatriarch();

		byte[] bytes = new byte[(int) file.length()];
		FileInputStream is = new FileInputStream(file);
		is.read(bytes);
		is.close();
		int index;

		if (file.getName().toLowerCase().indexOf(".jpg") >= 0) {
			index = wb.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_JPEG);
		} else if (file.getName().toLowerCase().indexOf(".png") >= 0) {
			index = wb.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
		} else {
			throw new Exception("FILE(" + file.getPath() + ") NOT IMPLEMENT");
		}

		XSSFClientAnchor clientAnchor = new XSSFClientAnchor(0, 0, 0, 0, colNo, rowNo, colCount + colNo,
				rowNo + rowCount);
		clientAnchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
		xSSFDrawing.createPicture(clientAnchor, index);
	}

	/**
	 * EXCEL에 이미지를 추가합니다.
	 * 
	 * @param rowNo
	 *            시작 위치 (열)
	 * @param colNo
	 *            시작 위치 (행)
	 * @param rowCount
	 *            크기(열 수)
	 * @param colCount
	 *            크기(행 수)
	 * @param bytes
	 *            이미지
	 * @param pictureType
	 *            이미지 종류
	 * @throws Exception
	 */
	public void addCellImage(int rowNo, int colNo, int rowCount, int colCount, byte bytes[], int pictureType)
			throws Exception {

		xSSFDrawing = (XSSFDrawing) sheet.createDrawingPatriarch();

		int index;

		index = wb.addPicture(bytes, pictureType);

		XSSFClientAnchor clientAnchor = new XSSFClientAnchor(0, 0, 0, 0, colNo, rowNo, colCount + colNo,
				rowNo + rowCount);
		clientAnchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
		xSSFDrawing.createPicture(clientAnchor, index);
	}

	/**
	 * 
	 * EXCEL에 이미지를 추가합니다.
	 * 
	 * @param excelSheet
	 *            sheet
	 * @param rowNo
	 *            시작 위치 (열)
	 * @param colNo
	 *            시작 위치 (행)
	 * @param rowCount
	 *            크기(열 수)
	 * @param colCount
	 *            크기(행 수)
	 * @param bytes
	 *            이미지
	 * @param pictureType
	 *            이미지 종류
	 */
	public void addCellImage(Sheet excelSheet, int rowNo, int colNo, int rowCount, int colCount, byte bytes[],
			int pictureType) throws Exception {

		xSSFDrawing = (XSSFDrawing) excelSheet.createDrawingPatriarch();

		int index;

		index = wb.addPicture(bytes, pictureType);

		XSSFClientAnchor clientAnchor = new XSSFClientAnchor(0, 0, 0, 0, colNo, rowNo, colCount + colNo,
				rowNo + rowCount);
		clientAnchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
		xSSFDrawing.createPicture(clientAnchor, index);
	}

	/**
	 * 
	 * @param rowNoFirst
	 * @param rowNoLast
	 * @param colNoFirst
	 * @param colNoLast
	 */
	public void addCellRangeAddress(int rowNoFirst, int rowNoLast, int colNoFirst, int colNoLast) {
		CellRangeAddress range = new CellRangeAddress(rowNoFirst, // first row
																	// (0-based)
				rowNoLast, // last row (0-based)
				colNoFirst, // first column (0-based)
				colNoLast // last column (0-based)
		);

		sheet.addMergedRegion(range);
	}

	public void addCells(int rowNo, int colNoStart, Object... values) {
		Row row = sheet.getRow(rowNo);
		if (row == null)
			row = sheet.createRow(rowNo);

		Cell cell;
		CellStyle cellStyle;

		for (int i = 0; i < values.length; i++) {
			cell = row.createCell(colNoStart + i);
			cellStyle = getCellStyle("col" + (colNoStart + i));
			if (cellStyle == null)
				cellStyle = getCellStyleForValue(values[i]);
			cell.setCellStyle(cellStyle);
			setCellValue(cell, values[i]);
		}
	}

	public void addCellStyle(String name, short fontSize, boolean fontBold, HorizontalAlignment alignment,
			short colorBg, boolean border) {
		addCellStyle(name, fontSize, fontBold, alignment, colorBg, (short) 0, border);
	}

	public void addCellStyle(String name, short fontSize, boolean fontBold, HorizontalAlignment alignment,
			short colorBg, short borderBg, boolean border) {
		CellStyle cellStyle;

		Font font = wb.createFont();
		font.setFontHeightInPoints(fontSize);
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(fontBold);
		font.setFontName(fontNameDefault);

		cellStyle = wb.createCellStyle();
		if (border) {
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);

			if (borderBg > 0) {
				cellStyle.setBottomBorderColor(borderBg);
				cellStyle.setTopBorderColor(borderBg);
				cellStyle.setLeftBorderColor(borderBg);
				cellStyle.setRightBorderColor(borderBg);
			}
		}

		cellStyle.setFont(font);
		cellStyle.setAlignment(alignment);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		if (colorBg > 0) {
			cellStyle.setFillForegroundColor(colorBg);
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}

		styleMap.put(name, cellStyle);
	}

	public void export(String filename) {

		FileOutputStream out;
		try {
			out = new FileOutputStream(filename);
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CellStyle getCellStyle(String name) {
		CellStyle cellStyle = styleMap.get(name);
		return cellStyle;
	}

	public String getFontNameDefault() {
		return fontNameDefault;
	}

	public CellStyle makeCellStyle(String styleName, String fontId, String dataFmt, BorderStyle borderStyle,
			HorizontalAlignment ha, HSSFColor.HSSFColorPredefined fillColor) {

		CellStyle cellStyle = wb.createCellStyle();
		if (borderStyle != null) {
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);
		}

		if (fillColor != null) {
			// 텍스트 컬러는 폰트에서 지정한다.
			cellStyle.setFillForegroundColor(fillColor.getIndex());
			// cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle.setFillPattern(FillPatternType.DIAMONDS);
		}

		if (dataFmt != null) {
			DataFormat df = wb.createDataFormat();
			cellStyle.setDataFormat(df.getFormat(dataFmt));
		}

		cellStyle.setFont(getFont(fontId != null ? fontId : FONT_DEFAULT));

		cellStyle.setAlignment(ha != null ? ha : HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		styleMap.put(styleName, cellStyle);

		return cellStyle;
	}

	public void makeFont(String key, String fontName, short fontSize, boolean bold, IndexedColors color) {
		Font font = wb.createFont();
		font.setFontHeightInPoints(fontSize);
		font.setColor(color == null ? IndexedColors.BLACK.getIndex() : color.getIndex());
		font.setBold(bold);
		font.setFontName(fontName);
		fontMap.put(key, font);
	}

	public Sheet makeSheet(String sheetName) {

		Sheet sheet = wb.createSheet(sheetName);

		sheet.setMargin(HSSFSheet.RightMargin, 0.5d);

		return sheet;
	}

	public Sheet makeSheets(String sheetName) {
		return makeSheet(sheetName);
	}

	public void setAlignment(HorizontalAlignment... align) {
		CellStyle cellStyle;
		for (int i = 0; i < align.length; i++) {
			cellStyle = getCellStyleByColumn(i);
			cellStyle.setAlignment(align[i]);
		}
	}

	public void setCellWidth(int cellNo, int width) {
		sheet.setColumnWidth(cellNo, WIDTH_UNIT * width);
	}

	public void setColAlignments(String... alignments) {
		CellStyle cellStyle;
		String a;
		for (int i = 0; i < alignments.length; i++) {
			if (alignments[i] != null) {
				cellStyle = getCellStyleByColumn(i);
				a = alignments[i].toUpperCase();
				cellStyle.setAlignment(a.equals("L") ? HorizontalAlignment.LEFT
						: a.equals("R") ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER);
			}
		}
	}

	public void setColDataFormats(String... dataFormats) {
		CellStyle cellStyle;

		if (dataFormats != null) {
			DataFormat df = wb.createDataFormat();
			for (int i = 0; i < dataFormats.length; i++) {
				if (dataFormats[i] != null) {
					cellStyle = getCellStyleByColumn(i);
					cellStyle.setDataFormat(df.getFormat(dataFormats[i]));
				}
			}
		}
	}

	public void setTitle(int rowNo, int colNo, String title, int colLength) {
		addCell(rowNo, colNo, title, MakeExcel.CELL_STYLE_TITLE, 0, colLength - 1);

	}

	public void setColumns(ExcelColumn... columns) {
		for (ExcelColumn col : columns) {
			addCell(col.rowNo, col.colNo, col.text, MakeExcel.CELL_STYLE_COLUMN, col.sizeRow - 1, col.sizeCol - 1);
		}
	}

	public void setColTexts(int rowNo, String... columns) {
		for (int i = 0; i < columns.length; i++) {
			setColumns(new ExcelColumn(rowNo, i, columns[i], 1, 1));
		}
	}

	public void setColWidths(int... widths) {
		for (int i = 0; i < widths.length; i++)
			sheet.setColumnWidth(i, WIDTH_UNIT * widths[i]);
	}

	public void setColWidths(Sheet excelSheet, int... widths) {
		for (int i = 0; i < widths.length; i++)
			excelSheet.setColumnWidth(i, WIDTH_UNIT * widths[i]);
	}

	public void setFontNameDefault(String fontNameDefault) {
		this.fontNameDefault = fontNameDefault;
	}

	public void setRowHeight(int rowNo, int height) {
		Row row = sheet.getRow(rowNo);
		if (row == null)
			row = sheet.createRow(rowNo);
		row.setHeight((short) (height * HEIGHT_UNIT));
	}

	public void setRowHeight(Sheet excelSheet, int rowNo, int height) {
		Row row = excelSheet.getRow(rowNo);
		if (row == null)
			row = excelSheet.createRow(rowNo);
		row.setHeight((short) (height * HEIGHT_UNIT));
	}

	private CellStyle getCellStyleByColumn(int colNo) {
		CellStyle cellStyle = styleMap.get("col" + colNo);
		if (cellStyle == null) {
			return makeCellStyle("col" + colNo, FONT_DEFAULT, null, BorderStyle.THIN, HorizontalAlignment.LEFT, null);
		}
		return cellStyle;
	}

	private void setCellValue(Cell cell, Object value) {
		if (value instanceof Double || value instanceof Float) {
			cell.setCellValue((double) ((Number) value).doubleValue());
		} else if (value instanceof Number) {
			cell.setCellValue((long) ((Number) value).longValue());
		} else if (value == null) {

		} else if (value.toString().startsWith("=")) {
			int rowNo = cell.getRow().getRowNum() + 1;
			String s = value.toString().substring(1);
			s = s.replaceAll("%rowNo%", rowNo + "");
			cell.setCellFormula(s);
		} else {
			cell.setCellValue(createHelper.createRichTextString(value.toString()));
		}
	}

	protected CellStyle getCellStyleForValue(Object value) {
		if (value instanceof Double)
			return styleMap.get(CELL_STYLE_FLOAT);
		else if (value instanceof Float)
			return styleMap.get(CELL_STYLE_FLOAT);
		else if (value instanceof Number)
			return styleMap.get(CELL_STYLE_INTEGER);
		else
			return styleMap.get(CELL_STYLE_TEXT);
	}

	protected Font getFont(String fontId) {
		return fontMap.get(fontId);
	}

	protected void initCellStyle() {

		makeCellStyle(CELL_STYLE_TITLE, FONT_TITLE, null, BorderStyle.THIN, HorizontalAlignment.CENTER, null);

		makeCellStyle(CELL_STYLE_COLUMN, FONT_COLUMN, null, BorderStyle.THIN, HorizontalAlignment.CENTER,
				HSSFColor.HSSFColorPredefined.SKY_BLUE);

		makeCellStyle(CELL_STYLE_INTEGER, FONT_DEFAULT, "#,##0", BorderStyle.THIN, HorizontalAlignment.RIGHT, null);

		makeCellStyle(CELL_STYLE_FLOAT, FONT_DEFAULT, "#,##0.0", BorderStyle.THIN, HorizontalAlignment.RIGHT, null);

		makeCellStyle(CELL_STYLE_TEXT, FONT_DEFAULT, "text", BorderStyle.THIN, HorizontalAlignment.LEFT, null);

	}

	protected void initFont() {
		makeFont(FONT_DEFAULT, "맑은 고딕", (short) 9, false, IndexedColors.BLACK);
		makeFont(FONT_COLUMN, "맑은 고딕", (short) 10, true, IndexedColors.BLACK);
		makeFont(FONT_TITLE, "맑은 고딕", (short) 12, true, IndexedColors.BLACK);
	}

}

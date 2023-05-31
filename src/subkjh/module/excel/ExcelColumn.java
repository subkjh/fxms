package subkjh.module.excel;

import java.io.Serializable;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel의 컬럼을 정의합니다.
 * 
 * @author subkjh
 * 
 */
public class ExcelColumn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4424561118813277904L;

	public enum COLUM_TAG {
		Normal, Group, Key, Sum;
	}

	/** 컬럼명 */
	public String text;
	/** 자료형식 */
	public String dataFormat;
	/** 넓이 */
	public int width;
	/** 요약표시값 */
	public COLUM_TAG tag;
	/** 가로정렬 */
	public HorizontalAlignment alignment = HorizontalAlignment.CENTER;;
	public int colNo;
	public int rowNo;
	public int sizeCol = 1;
	public int sizeRow = 1;

	/**
	 * 
	 * @param header
	 *            컬럼명
	 * @param dataFormat
	 *            데이터형식
	 * @param width
	 *            넓이
	 * @param summary
	 *            요약표시값
	 * @param alignment
	 *            가로정렬
	 */
	ExcelColumn(int colNo, String text, String dataFormat, int width, COLUM_TAG tag, HorizontalAlignment alignment) {
		this.colNo = colNo;
		this.text = text;
		this.dataFormat = dataFormat;
		this.width = width;
		this.tag = tag;
		this.alignment = alignment;
	}

	ExcelColumn(int rowNo, int colNo, String text) {
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.text = text;
	}

	ExcelColumn(int rowNo, int colNo, String text, int sizeRow, int sizeCol) {
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.text = text;
		this.sizeRow = sizeRow;
		this.sizeCol = sizeCol;
	}
}

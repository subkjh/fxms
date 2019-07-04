package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CD_CODE", comment = "코드테이블")
@FxIndex(name = "FX_CD_CODE__PK", type = INDEX_TYPE.PK, columns = { "CD_TYPE", "CD_CODE" })
public class FX_CD_CODE {

	@FxColumn(name = "CD_TYPE", size = 30, comment = "코드분류")
	private String cdType;

	@FxColumn(name = "CD_CODE", size = 30, comment = "코드")
	private String cdCode;

	@FxColumn(name = "CD_NAME", size = 50, nullable = true, comment = "코드명")
	private String cdName;

	@FxColumn(name = "CD_DESCR", size = 200, nullable = true, comment = "설명")
	private String cdDescr;

	@FxColumn(name = "CD_EDIT_YN", size = 1, nullable = true, comment = "코드편집여부")
	private boolean cdEditYn;

	@FxColumn(name = "VAL1", size = 1000, nullable = true, comment = "값1")
	private String val1;

	@FxColumn(name = "VAL2", size = 1000, nullable = true, comment = "값2")
	private String val2;

	@FxColumn(name = "VAL3", size = 1000, nullable = true, comment = "값3")
	private String val3;

	@FxColumn(name = "VAL4", size = 1000, nullable = true, comment = "값4")
	private String val4;

	@FxColumn(name = "VAL5", size = 1000, nullable = true, comment = "값5")
	private String val5;

	@FxColumn(name = "VAL6", size = 1000, nullable = true, comment = "값6")
	private String val6;

	@FxColumn(name = "SEQ_BY", size = 5, nullable = true, comment = "정렬순서")
	private int seqBy;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부")
	private boolean useYn;

	@FxColumn(name = "CHK_QRY", size = 1000, nullable = true, comment = "사용중여부 확인 쿼리")
	private String chkQry;

	@FxColumn(name = "FILL_QRY", size = 1000, nullable = true, comment = "채울쿼리")
	private String fillQry;

	public FX_CD_CODE() {
	}

	/**
	 * 코드
	 * 
	 * @return 코드
	 */
	public String getCdCode() {
		return cdCode;
	}

	/**
	 * 설명
	 * 
	 * @return 설명
	 */
	public String getCdDescr() {
		return cdDescr;
	}

	/**
	 * 코드명
	 * 
	 * @return 코드명
	 */
	public String getCdName() {
		return cdName;
	}

	/**
	 * 코드분류
	 * 
	 * @return 코드분류
	 */
	public String getCdType() {
		return cdType;
	}

	/**
	 * 사용중여부 확인 쿼리
	 * 
	 * @return 사용중여부 확인 쿼리
	 */
	public String getChkQry() {
		return chkQry;
	}

	public String getFillQry() {
		return fillQry;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public int getSeqBy() {
		return seqBy;
	}

	/**
	 * 값1
	 * 
	 * @return 값1
	 */
	public String getVal1() {
		return val1;
	}

	/**
	 * 값2
	 * 
	 * @return 값2
	 */
	public String getVal2() {
		return val2;
	}

	/**
	 * 값3
	 * 
	 * @return 값3
	 */
	public String getVal3() {
		return val3;
	}

	/**
	 * 값4
	 * 
	 * @return 값4
	 */
	public String getVal4() {
		return val4;
	}

	/**
	 * 값5
	 * 
	 * @return 값5
	 */
	public String getVal5() {
		return val5;
	}

	/**
	 * 값6
	 * 
	 * @return 값6
	 */
	public String getVal6() {
		return val6;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public boolean isUseYn() {
		return useYn;
	}

	/**
	 * 코드
	 * 
	 * @param cdCode
	 *            코드
	 */
	public void setCdCode(String cdCode) {
		this.cdCode = cdCode;
	}

	/**
	 * 설명
	 * 
	 * @param cdDescr
	 *            설명
	 */
	public void setCdDescr(String cdDescr) {
		this.cdDescr = cdDescr;
	}

	public boolean isCdEditYn() {
		return cdEditYn;
	}

	public void setCdEditYn(boolean cdEditYn) {
		this.cdEditYn = cdEditYn;
	}

	/**
	 * 코드명
	 * 
	 * @param cdName
	 *            코드명
	 */
	public void setCdName(String cdName) {
		this.cdName = cdName;
	}

	/**
	 * 코드분류
	 * 
	 * @param cdType
	 *            코드분류
	 */
	public void setCdType(String cdType) {
		this.cdType = cdType;
	}

	/**
	 * 사용중여부 확인 쿼리
	 * 
	 * @param chkQry
	 *            사용중여부 확인 쿼리
	 */
	public void setChkQry(String chkQry) {
		this.chkQry = chkQry;
	}

	public void setFillQry(String fillQry) {
		this.fillQry = fillQry;
	}

	/**
	 * 정렬순서
	 * 
	 * @param seqBy
	 *            정렬순서
	 */
	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn
	 *            사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	/**
	 * 값1
	 * 
	 * @param val1
	 *            값1
	 */
	public void setVal1(String val1) {
		this.val1 = val1;
	}

	/**
	 * 값2
	 * 
	 * @param val2
	 *            값2
	 */
	public void setVal2(String val2) {
		this.val2 = val2;
	}

	/**
	 * 값3
	 * 
	 * @param val3
	 *            값3
	 */
	public void setVal3(String val3) {
		this.val3 = val3;
	}

	/**
	 * 값4
	 * 
	 * @param val4
	 *            값4
	 */
	public void setVal4(String val4) {
		this.val4 = val4;
	}

	/**
	 * 값5
	 * 
	 * @param val5
	 *            값5
	 */
	public void setVal5(String val5) {
		this.val5 = val5;
	}

	/**
	 * 값6
	 * 
	 * @param val6
	 *            값6
	 */
	public void setVal6(String val6) {
		this.val6 = val6;
	}
}

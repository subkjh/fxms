package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.06.03 14:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MX_WORK_HST", comment = "MO작업이력테이블")
@FxIndex(name = "FX_MX_WORK_HST__PK", type = INDEX_TYPE.PK, columns = { "WORK_HST_NO" })
@FxIndex(name = "FX_MX_WORK_HST__KEY", type = INDEX_TYPE.KEY, columns = { "RST_NO" })
public class FX_MX_WORK_HST {

	public FX_MX_WORK_HST() {
	}

	public static final String FX_SEQ_WORKHSTNO = "FX_SEQ_WORKHSTNO";
	@FxColumn(name = "WORK_HST_NO", size = 19, comment = "작업이력번호", sequence = "FX_SEQ_WORKHSTNO")
	private long workHstNo;

	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
	private long moNo;

	@FxColumn(name = "MO_NAME", size = 200, comment = "MO명")
	private String moName;

	@FxColumn(name = "MO_WORK_TYPE_CD", size = 1, comment = "MO작업유형코드")
	private String moWorkTypeCd;

	@FxColumn(name = "MO_WORK_TYPE_NAME", size = 50, nullable = true, comment = "MO작업유형명")
	private String moWorkTypeName;

	@FxColumn(name = "ATTR_NAME", size = 50, nullable = true, comment = "속성명")
	private String attrName;

	@FxColumn(name = "ATTR_BF_VAL", size = 100, nullable = true, comment = "이전속성값")
	private String attrBfVal;

	@FxColumn(name = "ATTR_AF_VAL", size = 100, nullable = true, comment = "이후속성값")
	private String attrAfVal;

	@FxColumn(name = "RST_NO", size = 9, comment = "결과번호", defValue = "0")
	private int rstNo = 0;

	@FxColumn(name = "RST_CONT", size = 1000, nullable = true, comment = "결과내용")
	private String rstCont;

	@FxColumn(name = "STRT_DTM", size = 14, nullable = true, comment = "시작일시")
	private long strtDtm;

	@FxColumn(name = "END_DTM", size = 14, nullable = true, comment = "종료일시")
	private long endDtm;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 작업이력번호
	 * 
	 * @return 작업이력번호
	 */
	public long getWorkHstNo() {
		return workHstNo;
	}

	/**
	 * 작업이력번호
	 * 
	 * @param workHstNo 작업이력번호
	 */
	public void setWorkHstNo(long workHstNo) {
		this.workHstNo = workHstNo;
	}

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo MO번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName() {
		return moName;
	}

	/**
	 * MO명
	 * 
	 * @param moName MO명
	 */
	public void setMoName(String moName) {
		this.moName = moName;
	}

	/**
	 * MO작업유형코드
	 * 
	 * @return MO작업유형코드
	 */
	public String getMoWorkTypeCd() {
		return moWorkTypeCd;
	}

	/**
	 * MO작업유형코드
	 * 
	 * @param moWorkTypeCd MO작업유형코드
	 */
	public void setMoWorkTypeCd(String moWorkTypeCd) {
		this.moWorkTypeCd = moWorkTypeCd;
	}

	/**
	 * MO작업유형명
	 * 
	 * @return MO작업유형명
	 */
	public String getMoWorkTypeName() {
		return moWorkTypeName;
	}

	/**
	 * MO작업유형명
	 * 
	 * @param moWorkTypeName MO작업유형명
	 */
	public void setMoWorkTypeName(String moWorkTypeName) {
		this.moWorkTypeName = moWorkTypeName;
	}


	/**
	 * 속성명
	 * 
	 * @return 속성명
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * 속성명
	 * 
	 * @param attrName 속성명
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	/**
	 * 이전속성값
	 * 
	 * @return 이전속성값
	 */
	public String getAttrBfVal() {
		return attrBfVal;
	}

	/**
	 * 이전속성값
	 * 
	 * @param attrBfVal 이전속성값
	 */
	public void setAttrBfVal(String attrBfVal) {
		this.attrBfVal = attrBfVal;
	}

	/**
	 * 이후속성값
	 * 
	 * @return 이후속성값
	 */
	public String getAttrAfVal() {
		return attrAfVal;
	}

	/**
	 * 이후속성값
	 * 
	 * @param attrAfVal 이후속성값
	 */
	public void setAttrAfVal(String attrAfVal) {
		this.attrAfVal = attrAfVal;
	}

	/**
	 * 결과번호
	 * 
	 * @return 결과번호
	 */
	public int getRstNo() {
		return rstNo;
	}

	/**
	 * 결과번호
	 * 
	 * @param rstNo 결과번호
	 */
	public void setRstNo(int rstNo) {
		this.rstNo = rstNo;
	}

	/**
	 * 결과내용
	 * 
	 * @return 결과내용
	 */
	public String getRstCont() {
		return rstCont;
	}

	/**
	 * 결과내용
	 * 
	 * @param rstCont 결과내용
	 */
	public void setRstCont(String rstCont) {
		this.rstCont = rstCont;
	}

	/**
	 * 시작일시
	 * 
	 * @return 시작일시
	 */
	public long getStrtDtm() {
		return strtDtm;
	}

	/**
	 * 시작일시
	 * 
	 * @param strtDtm 시작일시
	 */
	public void setStrtDtm(long strtDtm) {
		this.strtDtm = strtDtm;
	}

	/**
	 * 종료일시
	 * 
	 * @return 종료일시
	 */
	public long getEndDtm() {
		return endDtm;
	}

	/**
	 * 종료일시
	 * 
	 * @param endDtm 종료일시
	 */
	public void setEndDtm(long endDtm) {
		this.endDtm = endDtm;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}
}

package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CF_INLO", comment = "설치위치테이블")
@FxIndex(name = "FX_CF_INLO__PK", type = INDEX_TYPE.PK, columns = { "INLO_NO" })
@FxIndex(name = "FX_CF_INLO_NAME__UK", type = INDEX_TYPE.UK, columns = { "UPPER_INLO_NO", "INLO_NAME" })
public class FX_CF_INLO implements FX_COMMON {

	public FX_CF_INLO() {
	}

	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호")
	private int inloNo;

	@FxColumn(name = "UPPER_INLO_NO", size = 9, comment = "상위설치위치번호", defValue = "0")
	private int upperInloNo = 0;

	@FxColumn(name = "INLO_NAME", size = 100, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "INLO_FNAME", size = 200, comment = "설치위치전체명")
	private String inloFname;

	@FxColumn(name = "INLO_TYPE", size = 30, comment = "설치위치종류(코드집)")
	private String inloType;

	@FxColumn(name = "UGRP_NO", size = 9, comment = "운용자그룹번호", defValue = "0")
	private int ugrpNo = 0;

	@FxColumn(name = "INLO_FLAG", size = 10, nullable = true, comment = "설치위치플래그")
	private String inloFlag;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0", operator = COLUMN_OP.insert)
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시", operator = COLUMN_OP.insert)
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	/**
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo
	 *            설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * 상위설치위치번호
	 * 
	 * @return 상위설치위치번호
	 */
	public int getUpperInloNo() {
		return upperInloNo;
	}

	/**
	 * 상위설치위치번호
	 * 
	 * @param upperInloNo
	 *            상위설치위치번호
	 */
	public void setUpperInloNo(int upperInloNo) {
		this.upperInloNo = upperInloNo;
	}

	/**
	 * 설치위치명
	 * 
	 * @return 설치위치명
	 */
	public String getInloName() {
		return inloName;
	}

	/**
	 * 설치위치명
	 * 
	 * @param inloName
	 *            설치위치명
	 */
	public void setInloName(String inloName) {
		this.inloName = inloName;
	}

	/**
	 * 설치위치전체명
	 * 
	 * @return 설치위치전체명
	 */
	public String getInloFname() {
		return inloFname;
	}

	/**
	 * 설치위치전체명
	 * 
	 * @param inloFname
	 *            설치위치전체명
	 */
	public void setInloFname(String inloFname) {
		this.inloFname = inloFname;
	}

	/**
	 * 설치위치종류(코드집)
	 * 
	 * @return 설치위치종류(코드집)
	 */
	public String getInloType() {
		return inloType;
	}

	/**
	 * 설치위치종류(코드집)
	 * 
	 * @param inloType
	 *            설치위치종류(코드집)
	 */
	public void setInloType(String inloType) {
		this.inloType = inloType;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	/**
	 * 설치위치플래그
	 * 
	 * @return 설치위치플래그
	 */
	public String getInloFlag() {
		return inloFlag;
	}

	/**
	 * 설치위치플래그
	 * 
	 * @param inloFlag
	 *            설치위치플래그
	 */
	public void setInloFlag(String inloFlag) {
		this.inloFlag = inloFlag;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}
}

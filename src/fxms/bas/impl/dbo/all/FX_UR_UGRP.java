package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.18 17:17
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_UGRP", comment = "사용자그룹테이블")
@FxIndex(name = "FX_UR_UGRP__PK", type = INDEX_TYPE.PK, columns = { "UGRP_NO" })
@FxIndex(name = "FX_UR_UGRP__UK_NAME", type = INDEX_TYPE.UK, columns = { "UGRP_NAME" })
public class FX_UR_UGRP  {

	public FX_UR_UGRP() {
	}

	public static final String FX_SEQ_UGRPNO = "FX_SEQ_UGRPNO";
	@FxColumn(name = "UGRP_NO", size = 9, comment = "사용자그룹번호", sequence = "FX_SEQ_UGRPNO")
	private int ugrpNo = -1;

	@FxColumn(name = "UGRP_NAME", size = 100, comment = "사용자그룹명")
	private String ugrpName;

	@FxColumn(name = "UGRP_DESC", size = 200, nullable = true, comment = "사용자그룹설명")
	private String ugrpDesc;

	@FxColumn(name = "DELBL_YN", size = 1, comment = "삭제가능여부", defValue = "N")
	private boolean delblYn = false;

	@FxColumn(name = "UI_DISP_YN", size = 1, comment = "화면표시여부", defValue = "Y")
	private boolean uiDispYn = true;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 사용자그룹번호
	 * 
	 * @return 사용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	/**
	 * 사용자그룹번호
	 * 
	 * @param ugrpNo 사용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	/**
	 * 사용자그룹명
	 * 
	 * @return 사용자그룹명
	 */
	public String getUgrpName() {
		return ugrpName;
	}

	/**
	 * 사용자그룹명
	 * 
	 * @param ugrpName 사용자그룹명
	 */
	public void setUgrpName(String ugrpName) {
		this.ugrpName = ugrpName;
	}

	/**
	 * 사용자그룹설명
	 * 
	 * @return 사용자그룹설명
	 */
	public String getUgrpDesc() {
		return ugrpDesc;
	}

	/**
	 * 사용자그룹설명
	 * 
	 * @param ugrpDesc 사용자그룹설명
	 */
	public void setUgrpDesc(String ugrpDesc) {
		this.ugrpDesc = ugrpDesc;
	}

	/**
	 * 삭제가능여부
	 * 
	 * @return 삭제가능여부
	 */
	public boolean isDelblYn() {
		return delblYn;
	}

	/**
	 * 삭제가능여부
	 * 
	 * @param delblYn 삭제가능여부
	 */
	public void setDelblYn(boolean delblYn) {
		this.delblYn = delblYn;
	}

	/**
	 * 화면표시여부
	 * 
	 * @return 화면표시여부
	 */
	public boolean isUiDispYn() {
		return uiDispYn;
	}

	/**
	 * 화면표시여부
	 * 
	 * @param uiDispYn 화면표시여부
	 */
	public void setUiDispYn(boolean uiDispYn) {
		this.uiDispYn = uiDispYn;
	}

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
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
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

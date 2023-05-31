package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.20 09:51
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CO_OP", comment = "기능테이블")
@FxIndex(name = "FX_CO_OP__PK", type = INDEX_TYPE.PK, columns = { "OP_ID" })
@FxIndex(name = "FX_CO_OP__UK_NAME", type = INDEX_TYPE.UK, columns = { "OP_NAME" })
public class FX_CO_OP  {

	public FX_CO_OP() {
	}

	@FxColumn(name = "OP_ID", size = 30, comment = "기능ID")
	private String opId;

	@FxColumn(name = "UPPER_OP_ID", size = 30, nullable = true, comment = "상위기능ID")
	private String upperOpId;

	@FxColumn(name = "OP_NAME", size = 30, comment = "기능명")
	private String opName;

	@FxColumn(name = "OP_TITLE", size = 50, nullable = true, comment = "기능타이틀")
	private String opTitle;

	@FxColumn(name = "OP_DESC", size = 100, nullable = true, comment = "기능설명")
	private String opDesc;

	@FxColumn(name = "OP_HINT", size = 100, nullable = true, comment = "기능힌트")
	private String opHint;

	@FxColumn(name = "OP_TYPE_CD", size = 10, nullable = true, comment = "기능유형코드")
	private String opTypeCd;

	@FxColumn(name = "OP_TYPE_TEXT", size = 50, nullable = true, comment = "기능유형문구")
	private String opTypeText;

	@FxColumn(name = "OP_URI", size = 50, nullable = true, comment = "기능URI")
	private String opUri;

	@FxColumn(name = "OP_MTHD", size = 50, nullable = true, comment = "기능메소드")
	private String opMthd;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private boolean useYn = true;

	@FxColumn(name = "UGRP_NO", size = 9, nullable = true, comment = "사용자그룹번호")
	private int ugrpNo;

	@FxColumn(name = "MNG_DIV", size = 20, nullable = true, comment = "관리구분")
	private String mngDiv;

	@FxColumn(name = "UI_CMPT_NAME", size = 100, nullable = true, comment = "화면컴포넌트명")
	private String uiCmptName;

	@FxColumn(name = "UI_WDTH", size = 9, nullable = true, comment = "화면폭", defValue = "-1")
	private int uiWdth = -1;

	@FxColumn(name = "UI_HGHT", size = 9, nullable = true, comment = "화면높이", defValue = "-1")
	private int uiHght = -1;

	@FxColumn(name = "SORT_SEQ", size = 9, nullable = true, comment = "정렬순서", defValue = "0")
	private int sortSeq = 0;

	@FxColumn(name = "DATA_TYPE", size = 20, nullable = true, comment = "데이터유형")
	private String dataType;

	@FxColumn(name = "CNFM_MSG", size = 200, nullable = true, comment = "확인메시지")
	private String cnfmMsg;

	@FxColumn(name = "OK_MSG", size = 200, nullable = true, comment = "성공메시지")
	private String okMsg;

	@FxColumn(name = "ERR_MSG", size = 200, nullable = true, comment = "오류메시지")
	private String errMsg;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 기능ID
	 * 
	 * @return 기능ID
	 */
	public String getOpId() {
		return opId;
	}

	/**
	 * 기능ID
	 * 
	 * @param opId 기능ID
	 */
	public void setOpId(String opId) {
		this.opId = opId;
	}

	/**
	 * 상위기능ID
	 * 
	 * @return 상위기능ID
	 */
	public String getUpperOpId() {
		return upperOpId;
	}

	/**
	 * 상위기능ID
	 * 
	 * @param upperOpId 상위기능ID
	 */
	public void setUpperOpId(String upperOpId) {
		this.upperOpId = upperOpId;
	}

	/**
	 * 기능명
	 * 
	 * @return 기능명
	 */
	public String getOpName() {
		return opName;
	}

	/**
	 * 기능명
	 * 
	 * @param opName 기능명
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * 기능타이틀
	 * 
	 * @return 기능타이틀
	 */
	public String getOpTitle() {
		return opTitle;
	}

	/**
	 * 기능타이틀
	 * 
	 * @param opTitle 기능타이틀
	 */
	public void setOpTitle(String opTitle) {
		this.opTitle = opTitle;
	}

	/**
	 * 기능설명
	 * 
	 * @return 기능설명
	 */
	public String getOpDesc() {
		return opDesc;
	}

	/**
	 * 기능설명
	 * 
	 * @param opDesc 기능설명
	 */
	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
	}

	/**
	 * 기능힌트
	 * 
	 * @return 기능힌트
	 */
	public String getOpHint() {
		return opHint;
	}

	/**
	 * 기능힌트
	 * 
	 * @param opHint 기능힌트
	 */
	public void setOpHint(String opHint) {
		this.opHint = opHint;
	}

	/**
	 * 기능유형코드
	 * 
	 * @return 기능유형코드
	 */
	public String getOpTypeCd() {
		return opTypeCd;
	}

	/**
	 * 기능유형코드
	 * 
	 * @param opTypeCd 기능유형코드
	 */
	public void setOpTypeCd(String opTypeCd) {
		this.opTypeCd = opTypeCd;
	}

	/**
	 * 기능유형문구
	 * 
	 * @return 기능유형문구
	 */
	public String getOpTypeText() {
		return opTypeText;
	}

	/**
	 * 기능유형문구
	 * 
	 * @param opTypeText 기능유형문구
	 */
	public void setOpTypeText(String opTypeText) {
		this.opTypeText = opTypeText;
	}

	/**
	 * 기능URI
	 * 
	 * @return 기능URI
	 */
	public String getOpUri() {
		return opUri;
	}

	/**
	 * 기능URI
	 * 
	 * @param opUri 기능URI
	 */
	public void setOpUri(String opUri) {
		this.opUri = opUri;
	}

	/**
	 * 기능메소드
	 * 
	 * @return 기능메소드
	 */
	public String getOpMthd() {
		return opMthd;
	}

	/**
	 * 기능메소드
	 * 
	 * @param opMthd 기능메소드
	 */
	public void setOpMthd(String opMthd) {
		this.opMthd = opMthd;
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
	 * 사용여부
	 * 
	 * @param useYn 사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

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
	 * 관리구분
	 * 
	 * @return 관리구분
	 */
	public String getMngDiv() {
		return mngDiv;
	}

	/**
	 * 관리구분
	 * 
	 * @param mngDiv 관리구분
	 */
	public void setMngDiv(String mngDiv) {
		this.mngDiv = mngDiv;
	}

	/**
	 * 화면컴포넌트명
	 * 
	 * @return 화면컴포넌트명
	 */
	public String getUiCmptName() {
		return uiCmptName;
	}

	/**
	 * 화면컴포넌트명
	 * 
	 * @param uiCmptName 화면컴포넌트명
	 */
	public void setUiCmptName(String uiCmptName) {
		this.uiCmptName = uiCmptName;
	}

	/**
	 * 화면폭
	 * 
	 * @return 화면폭
	 */
	public int getUiWdth() {
		return uiWdth;
	}

	/**
	 * 화면폭
	 * 
	 * @param uiWdth 화면폭
	 */
	public void setUiWdth(int uiWdth) {
		this.uiWdth = uiWdth;
	}

	/**
	 * 화면높이
	 * 
	 * @return 화면높이
	 */
	public int getUiHght() {
		return uiHght;
	}

	/**
	 * 화면높이
	 * 
	 * @param uiHght 화면높이
	 */
	public void setUiHght(int uiHght) {
		this.uiHght = uiHght;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public int getSortSeq() {
		return sortSeq;
	}

	/**
	 * 정렬순서
	 * 
	 * @param sortSeq 정렬순서
	 */
	public void setSortSeq(int sortSeq) {
		this.sortSeq = sortSeq;
	}

	/**
	 * 데이터유형
	 * 
	 * @return 데이터유형
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * 데이터유형
	 * 
	 * @param dataType 데이터유형
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 확인메시지
	 * 
	 * @return 확인메시지
	 */
	public String getCnfmMsg() {
		return cnfmMsg;
	}

	/**
	 * 확인메시지
	 * 
	 * @param cnfmMsg 확인메시지
	 */
	public void setCnfmMsg(String cnfmMsg) {
		this.cnfmMsg = cnfmMsg;
	}

	/**
	 * 성공메시지
	 * 
	 * @return 성공메시지
	 */
	public String getOkMsg() {
		return okMsg;
	}

	/**
	 * 성공메시지
	 * 
	 * @param okMsg 성공메시지
	 */
	public void setOkMsg(String okMsg) {
		this.okMsg = okMsg;
	}

	/**
	 * 오류메시지
	 * 
	 * @return 오류메시지
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * 오류메시지
	 * 
	 * @param errMsg 오류메시지
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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

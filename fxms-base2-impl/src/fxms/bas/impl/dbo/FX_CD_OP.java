package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.02.12 13:51
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CD_OP", comment = "기능테이블")
@FxIndex(name = "FX_CD_OP__PK", type = INDEX_TYPE.PK, columns = { "OP_NO" })
@FxIndex(name = "FX_CD_OP__UK_NM", type = INDEX_TYPE.UK, columns = { "OP_NAME" })
public class FX_CD_OP {

	@FxColumn(name = "OP_NO", size = 9, comment = "기능번호")
	private Integer opNo;

	@FxColumn(name = "OP_NAME", size = 30, comment = "기능명")
	private String opName;

	@FxColumn(name = "OP_TITLE", size = 100, comment = "기능타이틀")
	private String opTitle;

	@FxColumn(name = "UPPER_OP_NO", size = 9, nullable = true, comment = "상위기능번호")
	private Integer upperOpNo;

	@FxColumn(name = "OP_URI", size = 50, nullable = true, comment = "기능URI")
	private String opUri;

	@FxColumn(name = "OP_METHOD", size = 50, nullable = true, comment = "기능메소드")
	private String opMethod;

	@FxColumn(name = "OP_DESCR", size = 100, nullable = true, comment = "기능설명")
	private String opDescr;

	@FxColumn(name = "OP_HINT", size = 100, nullable = true, comment = "기능힌트")
	private String opHint;

	@FxColumn(name = "UI_JAVA_CLASS", size = 100, nullable = true, comment = "화면자바클래스")
	private String uiJavaClass;

	@FxColumn(name = "UI_WIDTH", size = 9, nullable = true, comment = "화면넓이")
	private String uiWidth;

	@FxColumn(name = "UI_HEIGHT", size = 9, nullable = true, comment = "화면높이")
	private String uiHeight;

	@FxColumn(name = "OP_TYPE_TEXT", size = 50, comment = "버튼텍스트")
	private String opTypeText;

	@FxColumn(name = "UGRP_NO", size = 9, nullable = true, comment = "운용자그룹번호")
	private Integer ugrpNo;

	@FxColumn(name = "MNG_DIV", size = 20, nullable = true, comment = "관리구분")
	private String mngDiv;

	@FxColumn(name = "OP_TYPE", size = 1, nullable = true, comment = "기능구분")
	private Integer opType;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "‘Y’")
	private String useYn = "Y";

	@FxColumn(name = "SEQ_BY", size = 9, nullable = true, comment = "정렬순서", defValue = "0")
	private Integer seqBy = 0;

	@FxColumn(name = "OK_MSG", size = 200, nullable = true, comment = "성공메시지")
	private String okMsg;

	@FxColumn(name = "CONFIRM_MSG", size = 200, nullable = true, comment = "확인메시지")
	private String confirmMsg;

	@FxColumn(name = "ERR_MSG", size = 200, nullable = true, comment = "오류메시지")
	private String errMsg;

	@FxColumn(name = "DATA_TYPE", size = 20, nullable = true, comment = "데이터유형")
	private String dataType;

	public FX_CD_OP() {
	}

	public String getDataType() {
		return dataType;
	}

	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * 관리구분
	 * 
	 * @return 관리구분
	 */
	public String getMngDiv() {
		return mngDiv;
	}

	public String getOkMsg() {
		return okMsg;
	}

	/**
	 * 기능설명
	 * 
	 * @return 기능설명
	 */
	public String getOpDescr() {
		return opDescr;
	}

	public String getOpHint() {
		return opHint;
	}

	/**
	 * 기능메소드
	 * 
	 * @return 기능메소드
	 */
	public String getOpMethod() {
		return opMethod;
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
	 * 기능번호
	 * 
	 * @return 기능번호
	 */
	public Integer getOpNo() {
		return opNo;
	}

	/**
	 * 기능구분
	 * 
	 * @return 기능구분
	 */
	public Integer getOpType() {
		return opType;
	}

	public String getOpTypeText() {
		return opTypeText;
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
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public Integer getSeqBy() {
		return seqBy;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public Integer getUgrpNo() {
		return ugrpNo;
	}

	public String getUiHeight() {
		return uiHeight;
	}

	public String getUiJavaClass() {
		return uiJavaClass;
	}

	public String getUiWidth() {
		return uiWidth;
	}

	/**
	 * 상위기능번호
	 * 
	 * @return 상위기능번호
	 */
	public Integer getUpperOpNo() {
		return upperOpNo;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public String isUseYn() {
		return useYn;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * 관리구분
	 * 
	 * @param mngDiv
	 *            관리구분
	 */
	public void setMngDiv(String mngDiv) {
		this.mngDiv = mngDiv;
	}

	public void setOkMsg(String okMsg) {
		this.okMsg = okMsg;
	}

	/**
	 * 기능설명
	 * 
	 * @param opDescr
	 *            기능설명
	 */
	public void setOpDescr(String opDescr) {
		this.opDescr = opDescr;
	}

	public void setOpHint(String opHint) {
		this.opHint = opHint;
	}

	/**
	 * 기능메소드
	 * 
	 * @param opMethod
	 *            기능메소드
	 */
	public void setOpMethod(String opMethod) {
		this.opMethod = opMethod;
	}

	/**
	 * 기능명
	 * 
	 * @param opName
	 *            기능명
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * 기능번호
	 * 
	 * @param opNo
	 *            기능번호
	 */
	public void setOpNo(Integer opNo) {
		this.opNo = opNo;
	}

	/**
	 * 기능구분
	 * 
	 * @param opType
	 *            기능구분
	 */
	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	public void setOpTypeText(String opTypeText) {
		this.opTypeText = opTypeText;
	}

	/**
	 * 기능URI
	 * 
	 * @param opUri
	 *            기능URI
	 */
	public void setOpUri(String opUri) {
		this.opUri = opUri;
	}

	/**
	 * 정렬순서
	 * 
	 * @param seqBy
	 *            정렬순서
	 */
	public void setSeqBy(Integer seqBy) {
		this.seqBy = seqBy;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(Integer ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	public void setUiHeight(String uiHeight) {
		this.uiHeight = uiHeight;
	}

	public void setUiJavaClass(String uiJavaClass) {
		this.uiJavaClass = uiJavaClass;
	}

	public void setUiWidth(String uiWidth) {
		this.uiWidth = uiWidth;
	}

	/**
	 * 상위기능번호
	 * 
	 * @param upperOpNo
	 *            상위기능번호
	 */
	public void setUpperOpNo(Integer upperOpNo) {
		this.upperOpNo = upperOpNo;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn
	 *            사용여부
	 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	protected String getOpTitle() {
		return opTitle;
	}

	protected void setOpTitle(String opTitle) {
		this.opTitle = opTitle;
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}

	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}

}

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

@FxTable(name = "FX_CD_OP_ATTR", comment = "기능속성테이블")
@FxIndex(name = "FX_CD_OP_ATTR__PK", type = INDEX_TYPE.PK, columns = { "OP_NO", "ATTR_NAME" })
@FxIndex(name = "FX_CD_OP_ATTR__FK", type = INDEX_TYPE.FK, columns = {
		"OP_NO" }, fkTable = "FX_CD_OP", fkColumn = "OP_NO")
public class FX_CD_OP_ATTR {

	@FxColumn(name = "OP_NO", size = 9, comment = "기능번호")
	private Integer opNo;

	@FxColumn(name = "ATTR_GROUP", size = 50, comment = "속성그룹명")
	private String attrGroup;

	@FxColumn(name = "ATTR_DISP", size = 50, comment = "속성화면명")
	private String attrDisp;

	@FxColumn(name = "ATTR_NAME", size = 50, comment = "속성명")
	private String attrName;

	@FxColumn(name = "ATTR_TYPE", size = 10, comment = "속성종류")
	private String attrType;

	@FxColumn(name = "ATTR_DEFAULT_VALUE", size = 50, nullable = true, comment = "속성기본값")
	private String attrDefaultValue;

	@FxColumn(name = "ATTR_VALUE_LIST", size = 240, nullable = true, comment = "속성값목록")
	private String attrValueList;

	@FxColumn(name = "PROMPT_TEXT", size = 100, nullable = true, comment = "프롬프트텍스트")
	private String promptText;

	@FxColumn(name = "READ_ONLY_YN", size = 1, nullable = true, comment = "읽기전용여부", defValue = "'N'")
	private String readOnlyYn = "Y";

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "‘Y’")
	private String useYn = "Y";

	@FxColumn(name = "NULLABLE_YN", size = 1, nullable = true, comment = "NULL가능여부", defValue = "‘Y’")
	private String nullableYn = "Y";

	@FxColumn(name = "SEQ_BY", size = 9, nullable = true, comment = "정렬순서", defValue = "0")
	private Integer seqBy = 0;
	
	@FxColumn(name = "ATTR_WIDTH", size = 4, nullable = true, comment = "속성넓이", defValue = "0")
	private int attrWidth = 0;

	public int getAttrWidth() {
		return attrWidth;
	}

	public void setAttrWidth(int attrWidth) {
		this.attrWidth = attrWidth;
	}

	public FX_CD_OP_ATTR() {
	}

	/**
	 * 속성기본값
	 * 
	 * @return 속성기본값
	 */
	public String getAttrDefaultValue() {
		return attrDefaultValue;
	}

	/**
	 * 속성화면명
	 * 
	 * @return 속성화면명
	 */
	public String getAttrDisp() {
		return attrDisp;
	}

	/**
	 * 속성그룹명
	 * 
	 * @return 속성그룹명
	 */
	public String getAttrGroup() {
		return attrGroup;
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
	 * 속성종류
	 * 
	 * @return 속성종류
	 */
	public String getAttrType() {
		return attrType;
	}

	/**
	 * 속성값목록
	 * 
	 * @return 속성값목록
	 */
	public String getAttrValueList() {
		return attrValueList;
	}

	/**
	 * 기능번호
	 * 
	 * @return 기능번호
	 */
	public Integer getOpNo() {
		return opNo;
	}

	public String getPromptText() {
		return promptText;
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
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public String isUseYn() {
		return useYn;
	}

	/**
	 * 속성기본값
	 * 
	 * @param attrDefaultValue
	 *            속성기본값
	 */
	public void setAttrDefaultValue(String attrDefaultValue) {
		this.attrDefaultValue = attrDefaultValue;
	}

	/**
	 * 속성화면명
	 * 
	 * @param attrDisp
	 *            속성화면명
	 */
	public void setAttrDisp(String attrDisp) {
		this.attrDisp = attrDisp;
	}

	/**
	 * 속성그룹명
	 * 
	 * @param attrGroup
	 *            속성그룹명
	 */
	public void setAttrGroup(String attrGroup) {
		this.attrGroup = attrGroup;
	}

	/**
	 * 속성명
	 * 
	 * @param attrName
	 *            속성명
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	/**
	 * 속성종류
	 * 
	 * @param attrType
	 *            속성종류
	 */
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	/**
	 * 속성값목록
	 * 
	 * @param attrValueList
	 *            속성값목록
	 */
	public void setAttrValueList(String attrValueList) {
		this.attrValueList = attrValueList;
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

	public void setPromptText(String promptText) {
		this.promptText = promptText;
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

	public String getReadOnlyYn() {
		return readOnlyYn;
	}

	public void setReadOnlyYn(String readOnlyYn) {
		this.readOnlyYn = readOnlyYn;
	}

	public String getNullableYn() {
		return nullableYn;
	}

	public void setNullableYn(String nullableYn) {
		this.nullableYn = nullableYn;
	}

	public String getUseYn() {
		return useYn;
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
}

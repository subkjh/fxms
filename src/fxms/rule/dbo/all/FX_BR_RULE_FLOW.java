package fxms.rule.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.01.31 15:01
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_BR_RULE_FLOW", comment = "룰흐름테이블")
@FxIndex(name = "FX_BR_RULE_FLOW__PK", type = INDEX_TYPE.PK, columns = { "BR_RULE_NO", "FLOW_NO" })
@FxIndex(name = "FX_BR_RULE_FLOW__FK1", type = INDEX_TYPE.FK, columns = {
		"BR_RULE_NO" }, fkTable = "FX_BR_RULE", fkColumn = "BR_RULE_NO")
@FxIndex(name = "FX_BR_RULE_FLOW__FK2", type = INDEX_TYPE.FK, columns = {
		"BR_COND_NAME" }, fkTable = "FX_BR_ITEM_DEF", fkColumn = "BR_ITEM_NAME")
@FxIndex(name = "FX_BR_RULE_FLOW__FK3", type = INDEX_TYPE.FK, columns = {
		"BR_ACT_NAME" }, fkTable = "FX_BR_ITEM_DEF", fkColumn = "BR_ITEM_NAME")
public class FX_BR_RULE_FLOW {

	public FX_BR_RULE_FLOW() {
	}

	public String toString() {
		return brRuleNo + "-" + flowNo + ":" + brActName;
	}

	@FxColumn(name = "BR_RULE_NO", size = 9, comment = "비즈니스룰번호", defValue = "-1")
	private Integer brRuleNo = -1;

	@FxColumn(name = "FLOW_NO", size = 9, comment = "흐름번호", defValue = "-1")
	private Integer flowNo = -1;

	@FxColumn(name = "BR_COND_NAME", size = 200, nullable = true, comment = "비즈니스룰조건명")
	private String brCondName;

	@FxColumn(name = "BR_COND_JSON", size = 1000, nullable = true, comment = "비즈니스룰조건JSON")
	private String brCondJson;

	@FxColumn(name = "BR_COND_RET_VAL", size = 200, nullable = true, comment = "비즈니스룰조건결과값", defValue = "true")
	private String brCondRetVal = "true";

	@FxColumn(name = "BR_ACT_NAME", size = 200, comment = "비즈니스룰행위명")
	private String brActName;

	@FxColumn(name = "BR_ACT_JSON", size = 1000, nullable = true, comment = "비즈니스룰행위JSON")
	private String brActJson;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;

	/**
	 * 비즈니스룰번호
	 * 
	 * @return 비즈니스룰번호
	 */
	public Integer getBrRuleNo() {
		return brRuleNo;
	}

	/**
	 * 비즈니스룰번호
	 * 
	 * @param brRuleNo 비즈니스룰번호
	 */
	public void setBrRuleNo(Integer brRuleNo) {
		this.brRuleNo = brRuleNo;
	}

	/**
	 * 흐름번호
	 * 
	 * @return 흐름번호
	 */
	public Integer getFlowNo() {
		return flowNo;
	}

	/**
	 * 흐름번호
	 * 
	 * @param flowNo 흐름번호
	 */
	public void setFlowNo(Integer flowNo) {
		this.flowNo = flowNo;
	}

	/**
	 * 비즈니스룰조건명
	 * 
	 * @return 비즈니스룰조건명
	 */
	public String getBrCondName() {
		return brCondName;
	}

	/**
	 * 비즈니스룰조건명
	 * 
	 * @param brCondName 비즈니스룰조건명
	 */
	public void setBrCondName(String brCondName) {
		this.brCondName = brCondName;
	}

	/**
	 * 비즈니스룰조건JSON
	 * 
	 * @return 비즈니스룰조건JSON
	 */
	public String getBrCondJson() {
		return brCondJson;
	}

	/**
	 * 비즈니스룰조건JSON
	 * 
	 * @param brCondJson 비즈니스룰조건JSON
	 */
	public void setBrCondJson(String brCondJson) {
		this.brCondJson = brCondJson;
	}

	/**
	 * 비즈니스룰조건결과값
	 * 
	 * @return 비즈니스룰조건결과값
	 */
	public String getBrCondRetVal() {
		return brCondRetVal;
	}

	/**
	 * 비즈니스룰조건결과값
	 * 
	 * @param brCondRetVal 비즈니스룰조건결과값
	 */
	public void setBrCondRetVal(String brCondRetVal) {
		this.brCondRetVal = brCondRetVal;
	}

	/**
	 * 비즈니스룰행위명
	 * 
	 * @return 비즈니스룰행위명
	 */
	public String getBrActName() {
		return brActName;
	}

	/**
	 * 비즈니스룰행위명
	 * 
	 * @param brActName 비즈니스룰행위명
	 */
	public void setBrActName(String brActName) {
		this.brActName = brActName;
	}

	/**
	 * 비즈니스룰행위JSON
	 * 
	 * @return 비즈니스룰행위JSON
	 */
	public String getBrActJson() {
		return brActJson;
	}

	/**
	 * 비즈니스룰행위JSON
	 * 
	 * @param brActJson 비즈니스룰행위JSON
	 */
	public void setBrActJson(String brActJson) {
		this.brActJson = brActJson;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public Integer getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(Integer regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(Long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public Integer getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(Integer chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public Long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(Long chgDtm) {
		this.chgDtm = chgDtm;
	}
}

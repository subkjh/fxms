package fxms.rule.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.01.26 16:58
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_BR_RULE", comment = "룰기본테이블")
@FxIndex(name = "FX_BR_RULE__PK", type = INDEX_TYPE.PK, columns = { "BR_RULE_NO" })
@FxIndex(name = "FX_BR_RULE__FK1", type = INDEX_TYPE.FK, columns = {
		"TRIGGER_NAME" }, fkTable = "FX_BR_ITEM_DEF", fkColumn = "BR_ITEM_NAME")
public class FX_BR_RULE {

	public FX_BR_RULE() {
	}

	public static final String FX_SEQ_BRRULENO = "FX_SEQ_BRRULENO";
	@FxColumn(name = "BR_RULE_NO", size = 9, comment = "비즈니스룰번호", defValue = "-1", sequence = "FX_SEQ_BRRULENO")
	private int brRuleNo = -1;

	@FxColumn(name = "RULE_NAME", size = 100, comment = "룰명")
	private String ruleName;

	@FxColumn(name = "RULE_DESCR", size = 240, comment = "룰설명")
	private String ruleDescr;

	@FxColumn(name = "TRIGGER_NAME", size = 200, comment = "TRIGGER명")
	private String triggerName;

	@FxColumn(name = "TRIGGER_JSON", size = 1000, nullable = true, comment = "TRIGGER_JSON")
	private String triggerJson;

	@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
	private String useYn = "Y";

	@FxColumn(name = "ALWAYS_RUN_YN", size = 1, comment = "항상실행여부", defValue = "N")
	private String alwaysRunYn = "N";

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 비즈니스룰번호
	 * 
	 * @return 비즈니스룰번호
	 */
	public int getBrRuleNo() {
		return brRuleNo;
	}

	/**
	 * 비즈니스룰번호
	 * 
	 * @param brRuleNo 비즈니스룰번호
	 */
	public void setBrRuleNo(int brRuleNo) {
		this.brRuleNo = brRuleNo;
	}

	/**
	 * 룰명
	 * 
	 * @return 룰명
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * 룰명
	 * 
	 * @param ruleName 룰명
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * 룰설명
	 * 
	 * @return 룰설명
	 */
	public String getRuleDescr() {
		return ruleDescr;
	}

	/**
	 * 룰설명
	 * 
	 * @param ruleDescr 룰설명
	 */
	public void setRuleDescr(String ruleDescr) {
		this.ruleDescr = ruleDescr;
	}

	/**
	 * TRIGGER명
	 * 
	 * @return TRIGGER명
	 */
	public String getTriggerName() {
		return triggerName;
	}

	/**
	 * TRIGGER명
	 * 
	 * @param triggerName TRIGGER명
	 */
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	/**
	 * TRIGGER_JSON
	 * 
	 * @return TRIGGER_JSON
	 */
	public String getTriggerJson() {
		return triggerJson;
	}

	/**
	 * TRIGGER_JSON
	 * 
	 * @param triggerJson TRIGGER_JSON
	 */
	public void setTriggerJson(String triggerJson) {
		this.triggerJson = triggerJson;
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
	 * 사용여부
	 * 
	 * @param useYn 사용여부
	 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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

	/**
	 * 항상실행여부
	 * 
	 * @return 항상실행여부
	 */
	public String isAlwaysRunYn() {
		return alwaysRunYn;
	}

	/**
	 * 항상실행여부
	 * 
	 * @param alwaysRunYn 항상실행여부
	 */
	public void setAlwaysRunYn(String alwaysRunYn) {
		this.alwaysRunYn = alwaysRunYn;
	}
}

package fxms.rule.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * 룰 조회 조건
 * 
 * @author subkjh
 *
 */
@FxTable(name = "FX_BR_RULE", comment = "룰기본테이블")
@FxIndex(name = "FX_BR_RULE__PK", type = INDEX_TYPE.PK, columns = { "BR_RULE_NO" })
public class SelectRulePara {

	public SelectRulePara() {
	}

	@FxColumn(name = "BR_RULE_NO", size = 9, comment = "비즈니스룰번호")
	private Integer brRuleNo = -1;

	public Integer getBrRuleNo() {
		return brRuleNo;
	}

}
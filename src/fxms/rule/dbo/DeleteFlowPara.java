package fxms.rule.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * FX_BR_RULE_FLOW에서 해당 룰 삭제용
 * 
 * @author subkjh
 *
 */

@FxTable(name = "FX_BR_RULE_FLOW", comment = "룰흐름테이블")
@FxIndex(name = "FX_BR_RULE_FLOW__PK", type = INDEX_TYPE.PK, columns = { "BR_RULE_NO" })
public class DeleteFlowPara {

	public DeleteFlowPara() {
	}

	@FxColumn(name = "BR_RULE_NO", size = 9, comment = "비즈니스룰번호")
	private Integer brRuleNo = -1;

	public Integer getBrRuleNo() {
		return brRuleNo;
	}

	public void setBrRuleNo(Integer brRuleNo) {
		this.brRuleNo = brRuleNo;
	}

}
package fxms.rule.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * 룰 사용 중지
 * 
 * @author subkjh
 * @since 2023.02
 */
@FxTable(name = "FX_BR_RULE", comment = "룰기본테이블")
@FxIndex(name = "FX_BR_RULE__PK", type = INDEX_TYPE.PK, columns = { "BR_RULE_NO" })
public class DeleteRulePara {

	public DeleteRulePara() {
	}

	@FxColumn(name = "BR_RULE_NO", size = 9, comment = "비즈니스룰번호")
	private Integer brRuleNo = -1;

	@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
	private String useYn = "N";

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;

	public Integer getBrRuleNo() {
		return brRuleNo;
	}

	public void setBrRuleNo(Integer brRuleNo) {
		this.brRuleNo = brRuleNo;
	}

}
package fxms.rule.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.rule.dbo.all.FX_BR_RULE;
import fxms.rule.dbo.all.FX_BR_RULE_FLOW;

public class RuleVo {

	private FX_BR_RULE rule;

	private List<FX_BR_RULE_FLOW> flows;

	public RuleVo() {
		flows = new ArrayList<FX_BR_RULE_FLOW>();
	}

	public FX_BR_RULE getRule() {
		return rule;
	}

	public void setRule(FX_BR_RULE rule) {
		this.rule = rule;
	}

	public List<FX_BR_RULE_FLOW> getFlows() {
		return flows;
	}

	public void setFlows(List<FX_BR_RULE_FLOW> flows) {
		this.flows = flows;
	}

}

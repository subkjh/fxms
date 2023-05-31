package fxms.rule.test;

import java.util.Map;

import fxms.bas.api.ValueApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.rule.FxBusinessRuleEngine;
import fxms.rule.FxRuleDefault;
import fxms.rule.FxRuleFact;
import fxms.rule.RuleApi;
import fxms.rule.action.PrintAction;
import fxms.rule.action.alarm.CheckAlarmPsValueAction;
import fxms.rule.action.mo.SelectMoListAction;
import fxms.rule.action.ps.MakePsValueJscodeAction;
import fxms.rule.action.ps.SavePsValueAction;
import fxms.rule.action.ps.UpdatePsValueAction;
import fxms.rule.triger.RunTrigger;

public class RuleMaker {

	public static void main(String[] args) {
		RuleMaker maker = new RuleMaker();
		try {
			maker.makeSensorValue2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void makeSensorValue() throws Exception {
		ValueApi.api = new ValueApiDfo();

		String jscode = "var psId = 'TEMP';\n";
		jscode += "var moNo = [ 100005, 100006, 100007, 100008];\n";
		jscode += "moNo;";
//		jscode += "var moNo = 12345;";
//		jscode += "moNo;";

		FxBusinessRuleEngine re = new FxBusinessRuleEngine(new FxRuleFact());
		re.addRule(new FxRuleDefault(
				new MakePsValueJscodeAction(RuleApi.makePara("jscode", jscode, "min", 0, "max", 100))));
		re.addRule(new FxRuleDefault(new SavePsValueAction(null)));
		re.addRule(new FxRuleDefault(new CheckAlarmPsValueAction(null)));
		re.addRule(new FxRuleDefault(new UpdatePsValueAction(null)));
//		re.addRule(new FxRuleDefault(new PeekPsValueAction(null)));
		re.addRule(new FxRuleDefault(new PrintAction(null)));
//		PollingTrigger c = new PollingTrigger(RuleApi.makePara("cycle", 5));
		RunTrigger c = new RunTrigger(null);
		c.setRuleEngine(re);
		c.trigger(null);

//		FX_BR_RULE rule = RuleApi.getApi().makeRule("주기작업테스트", "주기작업테스트", c);
//		List<FX_BR_RULE_FLOW> list = RuleApi.getApi().makeRuleFlow(c);
//		RuleApi.getApi().setRuleFlow(0, rule, list);

	}

	void makeSensorValue2() throws Exception {
		ValueApi.api = new ValueApiDfo();

		Map<String, Object> para = RuleApi.makePara("moClass", "SENSOR", "moType", "TEST");
		String paraJson = FxmsUtil.toJson(para);
	

		FxBusinessRuleEngine re = new FxBusinessRuleEngine(new FxRuleFact());
		re.addRule(new FxRuleDefault(
				new SelectMoListAction(RuleApi.makePara("paraJson", paraJson, "var", "moList"))));
		re.addRule(new FxRuleDefault(new PrintAction(null)));
//		PollingTrigger c = new PollingTrigger(RuleApi.makePara("cycle", 5));
		RunTrigger c = new RunTrigger(null);
		c.setRuleEngine(re);
		c.trigger(null);

//		FX_BR_RULE rule = RuleApi.getApi().makeRule("주기작업테스트", "주기작업테스트", c);
//		List<FX_BR_RULE_FLOW> list = RuleApi.getApi().makeRuleFlow(c);
//		RuleApi.getApi().setRuleFlow(0, rule, list);

	}
}

package fxms.rule.test;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.ValueApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.rule.FxBusinessRuleEngine;
import fxms.rule.FxRuleDefault;
import fxms.rule.FxRuleFact;
import fxms.rule.action.JavaScriptAction;
import fxms.rule.action.PrintAction;
import fxms.rule.action.ps.GetPsValueAction;
import fxms.rule.triger.RunTrigger;

public class RunTest {
	public static void main(String[] args) throws Exception {

		ValueApi.api = new ValueApiDfo();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("moNo", 100003);
		map.put("psId", "TEMP");
		map.put("var", "psvalue");
		map.put("psDataName", "MIN5");
		map.put("funcs", "AVG");

		FxBusinessRuleEngine re = new FxBusinessRuleEngine(new FxRuleFact());

		re.addRule(new FxRuleDefault(new GetPsValueAction(map)));

		String jscode = "payload.psvalue[0].mo.sensrName;";

		map.clear();
		map.put("jscode", jscode);
		map.put("var", "teeeeeeeeeeeeeeeeeeeeeeest");
		re.addRule(new FxRuleDefault(new JavaScriptAction(map)));
		re.addRule(new FxRuleDefault(new PrintAction(map)));

		RunTrigger c = new RunTrigger(null);
		c.setRuleEngine(re);
		c.trigger(null);
		System.out.println(FxmsUtil.toJson(c.getRuleEngine().getFact()));

	}
}

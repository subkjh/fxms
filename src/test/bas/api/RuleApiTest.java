package test.bas.api;

import fxms.bas.fxo.FxmsUtil;
import fxms.rule.RuleApi;
import fxms.rule.RuleApiDB;

public class RuleApiTest {

	public static void main(String[] args) {
		RuleApi api = new RuleApiDB() {
		};
		RuleApiTest c = new RuleApiTest();

		c.getToRun();

	}

	void getToRun() {
		try {
			System.out.println(FxmsUtil.toJson(RuleApi.getApi().getRuleToRunAlways()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void runToStop() {
		try {
			long brRuleNo = RuleApi.getApi().runRule(6);
			RuleApi.getApi().setInterrupt(brRuleNo, 20);
//			FxRuleTrigger trigger = api.makeTrigger(api.makeSampleJson());
//			trigger.trigger();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package fxms.rule.triger;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.rule.FxBusinessRuleEngine;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleDefault;
import fxms.rule.FxRuleFact;
import fxms.rule.FxRuleSwtich;
import fxms.rule.RuleApi;
import fxms.rule.action.AddDataAction;
import fxms.rule.action.JavaScriptAction;
import fxms.rule.action.PrintAction;
import fxms.rule.action.TestAction;
import fxms.rule.action.mo.SelectMoAction;
import fxms.rule.action.ps.MakeRamPsValueAction;
import fxms.rule.condition.FxConditionIf;
import fxms.rule.condition.FxConditionRange;
import fxms.rule.condition.FxRuleSwitchJscode;

/**
 * 
 * @author subkjh
 * @since 2023.02
 *
 */
@FxRuleActionInfo(name = "실행", descr = "한 번 룰엔진의 내용을 실행한다.")
public class RunTrigger extends FxRuleTriggerImpl {

	public static void main(String[] args) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("moNo", 1001);
		map.put("psId", "TEMP");

		String jscode = "var result = 'off';\n ";
		jscode += " if ( payload.test <= 5 ) result = 'on';\n";
		jscode += " result;\n";

		map.put("jscode", jscode);
		map.put("var", "jsResult");

		System.out.println(FxmsUtil.toJson(map));

		FxBusinessRuleEngine re = new FxBusinessRuleEngine(new FxRuleFact());
		re.addRule(new FxRuleDefault(new MakeRamPsValueAction(map)));
//		re.addRule(new FxRuleDefault(new SavePsValueNode(map)));
//		re.addRule(new FxRuleDefault(new CheckAlarmPsValueNode(map)));
//		re.addRule(new FxRuleDefault(new UpdatePsValueNode(map)));
//		re.addRule(new FxRuleDefault(new PeekPsValueNode(map)));
//		re.addRule(new FxRuleDefault(new BroadcastNode(map)));
		re.addRule(new FxRuleDefault(new TestAction(RuleApi.makePara("var", "test", "min", 1, "max", 10))));
		re.addRule(new FxRuleDefault(new FxConditionIf(RuleApi.makePara("name", "aaa", "condition", "<=", "value", 8)),
				new AddDataAction(RuleApi.makePara("var", "test111", "value", "test는 8보다 작거나 같습니다."))));
		re.addRule(new FxRuleDefault(new FxConditionRange(RuleApi.makePara("name", "test", "min", 4, "max", 5)),
				new AddDataAction(RuleApi.makePara("var", "message", "value", "test는 4와 8사이 값입니다."))));
		re.addRule(new FxRuleDefault(new JavaScriptAction(map)));
		FxRuleSwtich rule = new FxRuleSwtich(
				new FxRuleSwitchJscode(RuleApi.makePara("jscode", jscode, "var", "result")));
		rule.addAction("on", new AddDataAction(RuleApi.makePara("var", "jsexecute", "value", "JS가 on으로 처리되었네요")));
		rule.addAction("off", new AddDataAction(RuleApi.makePara("var", "jsexecute", "value", "JS가 off으로 처리되었네요")));
		rule.addAction(null, new AddDataAction(RuleApi.makePara("var", "jsexecute", "value", "JS가 null으로 처리되었네요")));
		re.addRule(rule);

//		map.clear();
//		map.put("var", "molist");
//		map.put("paraJson", FxmsUtil.toJson(FxmsUtil.makePara("moClass", "SENSOR")));
//		re.addRule(new FxRuleDefault(new SelectMoListAction(map)));

		map.clear();
		map.put("moNo", 101127);
		map.put("var", "mo");
		re.addRule(new FxRuleDefault(new SelectMoAction(map)));

		re.addRule(new FxRuleDefault(new PrintAction(map)));

		RunTrigger c = new RunTrigger(null);
		c.setRuleEngine(re);
		c.trigger(null);
		System.out.println(FxmsUtil.toJson(c.getRuleEngine().getFact()));

	}

	public RunTrigger(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void trigger(TriggerListener listener) throws Exception {
		this.getRuleEngine().run();
		if (listener != null)
			listener.onFinish();
	}

	@Override
	public void stopTrigger() {
	}

}

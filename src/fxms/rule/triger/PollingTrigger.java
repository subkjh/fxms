package fxms.rule.triger;

import java.util.List;
import java.util.Map;

import fxms.bas.api.ValueApi;
import fxms.bas.cron.Cron;
import fxms.bas.fxo.FxAttr;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.rule.FxBusinessRuleEngine;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleDefault;
import fxms.rule.FxRuleFact;
import fxms.rule.RuleApi;
import fxms.rule.action.AddDataAction;
import fxms.rule.action.PrintAction;
import fxms.rule.action.TestAction;
import fxms.rule.action.ps.MakeRamPsValueAction;
import fxms.rule.condition.FxConditionIf;
import fxms.rule.condition.FxConditionRange;
import fxms.rule.dbo.all.FX_BR_RULE;
import fxms.rule.dbo.all.FX_BR_RULE_FLOW;

/**
 * @author subkjh
 *
 */
@FxRuleActionInfo(name = "스케줄 실행", descr = "unix cron 형태 또는 일정주기 마다 실행하는 트리거이다.")
public class PollingTrigger extends FxRuleTriggerImpl {

	public static void main(String[] args) throws Exception {

		ValueApi.api = new ValueApiDfo();

		FxBusinessRuleEngine re = new FxBusinessRuleEngine(new FxRuleFact());
		try {
			re.addRule(new FxRuleDefault(
					new MakeRamPsValueAction(RuleApi.makePara("moNo", 1001, "psId", "TEMP", "min", 0, "max", 100))));
//			re.addRule(new FxRuleDefault(new SavePsValueNode(map)));
//			re.addRule(new FxRuleDefault(new CheckAlarmPsValueNode(map)));
//			re.addRule(new FxRuleDefault(new UpdatePsValueNode(map)));
//			re.addRule(new FxRuleDefault(new PeekPsValueNode(map)));
//			re.addRule(new FxRuleDefault(new BroadcastNode(map)));
			re.addRule(new FxRuleDefault(new TestAction(RuleApi.makePara("var", "test", "min", 1, "max", 10))));
			re.addRule(
					new FxRuleDefault(new FxConditionIf(RuleApi.makePara("name", "aaa", "condition", "<=", "value", 8)),
							new AddDataAction(RuleApi.makePara("var", "test111", "value", "test는 8보다 작거나 같습니다."))));
			re.addRule(new FxRuleDefault(new FxConditionRange(RuleApi.makePara("name", "test", "min", 4, "max", 5)),
					new AddDataAction(RuleApi.makePara("var", "message", "value", "test는 4와 8사이 값입니다."))));
			re.addRule(new FxRuleDefault(new PrintAction(null)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		PollingTrigger c = new PollingTrigger(RuleApi.makePara("cycle", 3));
		c.setRuleEngine(re);
//		c.trigger();

		FX_BR_RULE rule = RuleApi.getApi().makeRule("주기작업테스트", "주기작업테스트", c);
		List<FX_BR_RULE_FLOW> list = RuleApi.getApi().makeRuleFlow(c);
		RuleApi.getApi().setRuleFlow(0, rule, list);
	}

	@FxAttr(text = "실행주기(초)", description = "초단위의 실행 주기는 입력한다.", required = false)
	private int cycle;

	@FxAttr(text = "스케쥴", description = "unix cron형식의 실행 일정을 입력한다.", required = false)
	private String schedule;

	private boolean isContinue = true;

	public PollingTrigger(Map<String, Object> map) throws Exception {
		super(map);
		if (cycle == 0) {
			cycle = 10;
		}
	}

	@Override
	public void trigger(TriggerListener listener) throws Exception {

		Cron cron = new Cron();
		try {
			if (schedule != null && schedule.length() > 5) {
				cron.setSchedule(schedule);
			} else {
				cron.setSchedule("period " + cycle);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (this.isContinue) {
			if (cron.isOnTime(System.currentTimeMillis())) {
				try {
					this.getRuleEngine().setFact(new FxRuleFact());
					this.getRuleEngine().run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (listener != null)
			listener.onFinish();
	}

	@Override
	public void stopTrigger() {
		this.isContinue = false;
	}

}

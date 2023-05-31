package fxms.rule.action;

import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * @author subkjh
 * @since 2023.02
 * 
 */

@FxRuleActionInfo(name = "실행중지", descr = "일정 시간동안 실행을 중지한다.")
public class SleepAction extends FxRuleActionImpl {

	@FxAttr(text = "중지시간(초)", description = "실행을 입력한 시간 동안 중지한다.")
	private int seconds;

	public SleepAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
		}

	}
}

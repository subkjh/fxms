package fxms.rule;

import java.util.HashMap;
import java.util.Map;

import fxms.rule.condition.FxRuleSwitch;

/**
 * switch문 형식의 룰
 *
 * @author subkjh
 * @since 2023.02
 */
public class FxRuleSwtich implements FxRule {

	private final FxRuleSwitch condition;
	private final Map<Object, FxRuleAction> actMap;

	/**
	 *
	 * @param condition 조건
	 */
	public FxRuleSwtich(final FxRuleSwitch condition) {
		this.actMap = new HashMap<>();
		this.condition = condition;
	}

	/**
	 * 조건과 행위를 추가한다.<br>
	 * 조건의 값이 맞으면 행위를 한다.
	 *
	 * @param result
	 * @param action
	 */
	public void addAction(final Object result, final FxRuleAction action) {
		this.actMap.put(result, action);
	}

	@Override
	public FxRuleAction perform(FxRuleFact fact) {

		Object ret = condition.evaluate(fact);
		FxRuleAction action = actMap.get(ret);
		if (action != null) {
			try {
				action.execute(fact);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return action;
		}

		return null;
	}

	/**
	 * 조건의 행위 목록을 조회한다.
	 * 
	 * @return
	 */
	public Map<Object, FxRuleAction> getActMap() {
		return actMap;
	}

	@Override
	public FxRuleSwitch getCondition() {
		return condition;
	}
}
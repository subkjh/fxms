package fxms.rule;

import fxms.rule.condition.FxRuleCondition;
import subkjh.bas.co.log.Logger;

/**
 * 기본 룰
 * 
 * @author subkjh
 *
 */
public class FxRuleDefault implements FxRule {

	private final FxRuleCondition<Boolean> condition;
	private final FxRuleAction action;

	public FxRuleDefault(final FxRuleAction action) {
		this.condition = null;
		this.action = action;
	}

	public FxRuleDefault(final FxRuleCondition<Boolean> condition, final FxRuleAction action) {
		this.condition = condition;
		this.action = action;
	}

	@Override
	public FxRuleAction perform(FxRuleFact fact) {

		// 조건에 맞으면 행위를 한다.
		if (condition == null || condition.evaluate(fact)) {
			try {
				action.execute(fact);
				return action;
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return null;
	}

	/**
	 * 
	 * @return
	 */
	public FxRuleAction getAction() {
		return action;
	}

	@Override
	public FxRuleCondition<Boolean> getCondition() {
		return condition;
	}

}

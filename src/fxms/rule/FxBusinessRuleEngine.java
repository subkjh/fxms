package fxms.rule;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;

/**
 * 등록된 액션을 순차적으로 실행하는 클래스
 * 
 * @author subkjh
 * @since 2023.02
 */
public class FxBusinessRuleEngine {

	/** 조건과 액션 목록 */
	private final List<FxRule> ruleList;
	/** 팩트 */
	private FxRuleFact fact;

	/**
	 * 
	 * @param fact 팩트
	 */
	public FxBusinessRuleEngine(final FxRuleFact fact) {
		this.ruleList = new ArrayList<>();
		this.fact = fact;
	}

	public FxRuleFact getFact() {
		return fact;
	}

	/**
	 * 새로운 팩트를 지정한다.
	 * 
	 * @param fact
	 */
	public void setFact(FxRuleFact fact) {
		this.fact = fact;
	}

	/**
	 * 룰을 추가한다.
	 * 
	 * @param rule 룰
	 */
	public void addRule(final FxRule rule) {
		this.ruleList.add(rule);
	}

	/**
	 * 룰 목록을 순차적으로 실행한다.
	 */
	public void run() {

		this.fact.addHeader("run.startTime", System.currentTimeMillis());

		for (FxRule rule : ruleList) {

			try {

				FxRuleAction action = rule.perform(this.fact);

				this.fact.addRuleResult(rule.getCondition(), action);

			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		}

		this.fact.addHeader("run.endTime", System.currentTimeMillis());

//		System.out.println(this.fact.getRunRuleCnt() + "/" + this.fact.getTotRuleCnt());

	}

	/**
	 * 
	 * @return 룰 목록
	 */
	public List<FxRule> getRuleList() {
		return ruleList;
	}

}

package fxms.rule;

import fxms.rule.condition.FxRuleCondition;

/**
 * 비즈니스 규칙 엔진의 요구사항은 크게 4가지로 나뉜다.<br>
 * <br>
 * 팩트 : 규칙이 확인할 수 있는 정보<br>
 * 액션 : 수행하려는 동작<br>
 * 조건 : 액션을 언제 발생시킬지 지정<br>
 * 규칙 : 실행하려는 비즈니스 규칙을 지정<br>
 * 보통 팩트/액션/조건을 한 그룹으로 묶어서 규칙으로 만든다.<br>
 * 
 * 규칙 클래스<br>
 * 
 * @author subkjh
 * @since 2023.02
 */
public interface FxRule {

	/**
	 * 
	 * @return 조건
	 */
	public FxRuleCondition<?> getCondition();

	/**
	 * 팩트를 이용하여 조건이 맞은 규칙을 수행한다.
	 * 
	 * @param fact
	 * @return 처리한 행위
	 */
	public FxRuleAction perform(FxRuleFact fact);

}

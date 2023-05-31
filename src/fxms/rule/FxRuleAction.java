package fxms.rule;

import java.util.Map;

/**
 * 룰 행위 선언
 * 
 * @author subkjh
 * @since 2023.02
 */
public interface FxRuleAction {

	/**
	 * 팩트를 이용하여 행위를 한다.
	 * 
	 * @param fact 팩트
	 * @throws Exception
	 */
	public void execute(FxRuleFact fact) throws Exception;

	/**
	 * 
	 * @return 파라메터목록
	 */
	public Map<String, Object> getParaMap();

}

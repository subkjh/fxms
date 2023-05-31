package fxms.rule.condition;

import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import fxms.rule.RuleApi;
import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "자바스크립트 결과 조건", descr = "자바스크립트를 작성하여 결과가 입력된 값과 같은지 확인한다.")
public class FxConditionJscode extends FxRuleConditionImpl {

	@FxAttr(text = "자바스크립트", description = "payload.변수명을 이용하여 원하는 코드를 작성한다.")
	private String jscode;

	@FxAttr(text = "비교값", description = "비교할 값을 입력한다.")
	private Object value;

	public FxConditionJscode(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public Boolean evaluate(FxRuleFact fact) {

		try {
			Object result = RuleApi.getApi().runScript(fact, jscode, null);
			return RuleApi.equals(this.value, result);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return false;
	}
}
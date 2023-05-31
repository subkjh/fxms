package fxms.rule.condition;

import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxAttrApi;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import fxms.rule.RuleApi;
import subkjh.bas.co.log.Logger;

/**
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "자바스크립트 실행 결과 조건", descr = "자바스크립트를 코딩하여 결과를 이용하여 어떤 액션을 처리할지 지정한다.")
public class FxRuleSwitchJscode implements FxRuleSwitch {

	@FxAttr(text = "자바스크립트", description = "payload.변수명을 이용하여 원하는 코드를 작성한다.")
	private String jscode;

	@FxAttr(text = "결과변수명", description = "결과를 넣을 변수를 지정한다. 지정하지 않으면 최종 처리 내용을 사용한다.")
	private String var;

	private final Map<String, Object> paraMap;

	public FxRuleSwitchJscode(Map<String, Object> datas) throws Exception {
		paraMap = FxAttrApi.toObject(datas, this);
	}

	@Override
	public boolean equals(Object obj) {

		// 동일한 switch문인지 확인용

		if (obj instanceof FxRuleSwitchJscode) {
			FxRuleSwitchJscode c = (FxRuleSwitchJscode) obj;
			return c.jscode.equals(this.jscode);
		}
		return super.equals(obj);
	}

	@Override
	public Object evaluate(FxRuleFact fact) {
		try {
			return RuleApi.getApi().runScript(fact, jscode, var);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
		return null;
	}

	public Map<String, Object> getParaMap() {
		return paraMap;
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
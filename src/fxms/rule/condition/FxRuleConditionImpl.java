package fxms.rule.condition;

import java.util.Map;

import fxms.bas.fxo.FxAttrApi;

/**
 * 
 * @author subkjh
 * @since 2023.02
 */
public abstract class FxRuleConditionImpl implements FxRuleCondition<Boolean> {

	private final Map<String, Object> paraMap;

	public FxRuleConditionImpl(Map<String, Object> datas) throws Exception {
		paraMap = FxAttrApi.toObject(datas, this);
	}

	public Map<String, Object> getParaMap() {
		return paraMap;
	}
}

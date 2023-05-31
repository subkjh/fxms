package fxms.rule;

import java.util.Map;

import fxms.bas.fxo.FxAttrApi;

/**
 * Rule 기반으로 사용할 기본 노드
 * 
 * @author subkjh
 * @since 2023.02
 */
public abstract class FxRuleActionImpl implements FxRuleAction {

	private final Map<String, Object> paraMap;

	public FxRuleActionImpl(Map<String, Object> datas) throws Exception {
		paraMap = FxAttrApi.toObject(datas, this);
	}

	@Override
	public Map<String, Object> getParaMap() {
		return paraMap;
	}

}

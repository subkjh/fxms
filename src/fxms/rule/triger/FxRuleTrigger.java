package fxms.rule.triger;

import java.util.Map;

import fxms.rule.FxBusinessRuleEngine;

/**
 * 룰 트리거
 * 
 * @author subkjh
 * @since 2023.02
 */
public interface FxRuleTrigger {

	/**
	 * 룰엔진
	 * 
	 * @return
	 */
	public FxBusinessRuleEngine getRuleEngine();

	/**
	 * 
	 * @param bre
	 */
	public void setRuleEngine(FxBusinessRuleEngine bre);

	/**
	 * 트리거 발생
	 * 
	 * @param listener 종료 확인 리슨나
	 * @throws Exception
	 */
	public void trigger(TriggerListener listener) throws Exception;

	/**
	 * 
	 * @return 트리거 파라메터
	 */
	public Map<String, Object> getParaMap();

	/**
	 * 트리거 중지 요청
	 */
	public abstract void stopTrigger();
}

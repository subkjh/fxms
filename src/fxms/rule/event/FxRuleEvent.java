package fxms.rule.event;

import fxms.bas.event.FxEventImpl;

/**
 * 룰 이벤트
 * 
 * @author subkjh
 * @since 2023.02
 */
public class FxRuleEvent extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 672051786778846756L;

	private Object object;

	public FxRuleEvent(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

}

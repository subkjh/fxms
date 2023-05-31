package fxms.rule.event;

import fxms.bas.event.FxEventImpl;

/**
 * 실행중인 룰에 대한 중지 요청 이벤트
 * 
 * @author subkjh
 *
 */
public class FxRuleStopEvent extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1761515164340530684L;
	private final long brRunNo;

	public FxRuleStopEvent(long brRunNo) {
		this.brRunNo = brRunNo;
	}

	public long getBrRunNo() {
		return brRunNo;
	}

}

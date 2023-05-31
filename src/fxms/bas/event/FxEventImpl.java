package fxms.bas.event;

import java.io.Serializable;

import fxms.bas.fxo.FxObject;

/**
 * FxMS에서 사용되는 이벤트
 * 
 * @author subkjh
 *
 */
public class FxEventImpl implements FxObject, Serializable, FxEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6377602869223261093L;

	private STATUS status = STATUS.raw;
	private final String eventType;
	private long no;

	public FxEventImpl(String eventType, STATUS status) {
		this.eventType = eventType;
		this.status = status;
	}

	public FxEventImpl() {
		this.eventType = getClass().getSimpleName();
	}

	public String getEventType() {
		return eventType;
	}

	public long getNo() {
		return no;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(no).append(")");
		sb.append(getClass().getSimpleName());
		sb.append("(");
		sb.append(status);
		sb.append(")");

		return sb.toString();
	}
}

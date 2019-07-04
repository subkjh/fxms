package fxms.bas.co.noti;

import java.io.Serializable;

import fxms.bas.fxo.FxObject;

public interface FxEvent extends FxObject, Serializable {

	public enum STATUS {

		acked

		, added

		, changed

		, deleted

		, notified

		, raw;

	}

	public String getEventType();

	public STATUS getStatus();

	public void setEventType(String eventType);

	public void setStatus(STATUS status);

}

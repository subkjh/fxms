package fxms.bas.vo;

import java.io.Serializable;

public abstract class AlarmEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3334010529722436799L;

	private long eventMstime;

	private boolean broadcast;

	public AlarmEvent() {
		this.eventMstime = System.currentTimeMillis();
		this.broadcast = true;
	}

	public long getEventMstime() {
		return eventMstime;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public void setEventMstime(long eventMstime) {
		this.eventMstime = eventMstime;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

}

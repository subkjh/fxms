package com.fxms.ui.bas.vo;

public class PsValue {

	private long time;

	private Number value;

	public PsValue(Number time, Number value) {
		this.time = time.longValue();
		this.value = value;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

}

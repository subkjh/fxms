package fxms.bas.vo;

import java.io.Serializable;

public class PsValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -901485719827721674L;

	/** 성능값 */
	private final Number value;

	/** 데이터 수집 시간 */
	private final long date;

	public PsValue(long date, Number value) {
		this.date = date;
		this.value = value;
	}

	public Number getValue() {
		return value;
	}

	public long getPsDtm() {
		return date;
	}

	public long getDate() {
		return date;
	}

}

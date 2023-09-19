package fxms.bas.vo;

import java.io.Serializable;

public class PsValueComp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2737291211793694904L;

	private final long moNo;
	private final String psId;

	private final Number curValue;
	private final long curDate;
	private final Number prevValue;
	private final long prevDate;

	public PsValueComp(long moNo, String psId, Number prevDate, Number prevValue, Number curDate, Number curValue) {
		this.moNo = moNo;
		this.psId = psId;
		this.prevDate = prevDate != null ? prevDate.longValue() : 0;
		this.prevValue = prevValue;
		this.curDate = curDate != null ? curDate.longValue() : 0;
		this.curValue = curValue;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("curDate=").append(curDate);
		sb.append(",curValue=").append(curValue);
		return sb.toString();
	}

	public Number getCurValue() {
		return curValue;
	}

	public long getCurDate() {
		return curDate;
	}

	public Number getPrevValue() {
		return prevValue;
	}

	public long getPrevDate() {
		return prevDate;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsId() {
		return psId;
	}

}

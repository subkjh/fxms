package fxms.bas.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;

public class TimeSeriesVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -630068444311155323L;

	private long moNo;

	private String moInstance;

	private String psCode;

	private List<Long> timeList;

	private List<Number> valueList;

	public int size() {
		return timeList.size();
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();
		sb.append(Logger.fill('.', 20, String.valueOf(moNo)));
		sb.append("\n");
		sb.append(Logger.fill('.', 20, moInstance));
		sb.append("\n");
		sb.append(Logger.fill('.', 20, psCode));
		sb.append("\n");

		for (int i = 0; i < size(); i++) {
			sb.append(timeList.get(i) + " " + valueList.get(i));
			sb.append("\n");
		}

		return sb.toString();
	}

	public TimeSeriesVo() {
		timeList = new ArrayList<Long>();
		valueList = new ArrayList<Number>();
	}

	public void add(long time, Number value) {
		timeList.add(time);
		valueList.add(value);
	}

	public List<Long> getTimeList() {
		return timeList;
	}

	public List<Number> getValueList() {
		return valueList;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsCode() {
		return psCode;
	}

	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

}

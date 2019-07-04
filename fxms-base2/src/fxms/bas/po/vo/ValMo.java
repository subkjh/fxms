package fxms.bas.po.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValMo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5673101548366711495L;

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	/** MO관리번호 */
	private long moNo;

	private String moInstance;

	/** 값 */
	private Number value;

	/** 데이터 수집 시간 */
	private long mstime;

	private String psCode;

	public ValMo() {

	}

	public ValMo(long moNo, String moInstance, Number value) {
		this.moNo = moNo;
		this.moInstance = moInstance;
		this.value = value;
	}

	public ValMo(String key, Number value) {
		String ss[] = key.split("\t");
		this.moNo = Long.parseLong(ss[0]);
		if (ss.length >= 2)
			this.moInstance = ss[1];
		this.value = value;
	}

	public String getKey() {
		if (moInstance != null && moInstance.length() > 0)
			return moNo + "\t" + moInstance;
		return moNo + "";
	}

	public String getMoInstance() {
		return moInstance;
	}

	public long getMoNo() {
		return moNo;
	}

	public long getMstime() {
		return mstime;
	}

	public String getPsCode() {
		return psCode;
	}

	public Number getValue() {
		return value;
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

	public void setValue(Number value) {
		this.value = value;
	}

	public void setValue(Number value, long mstime) {
		this.value = value;
		this.mstime = mstime;
	}

	@Override
	public String toString() {
		return getKey() + "(" + YYYYMMDDHHMMSS.format(new Date(mstime)) + ")=" + getValue();
	}
}

package com.fxms.ws.biz.ps;

public class RecvPsVo {

	/** 관리대상 */
	private long moNo;
	/** 인스턴스 */
	private String moInstance;
	/** 성능번호 */
	private String psCode;
	/** 성능값 */
	private Number value;

	private long mstime;

	public long getMoNo() {
		return moNo;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	public String getPsCode() {
		return psCode;
	}

	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public long getMstime() {
		return mstime;
	}

	public void setMstime(long mstime) {
		this.mstime = mstime;
	}

}

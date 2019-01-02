package com.daims.dfc.filter.config.intigate.bean;

public class QosRulePolicyBean {

	/** QOS MAXINBOUND */
	private int inboundMax;
	/** QOS MININBOUND */
	private int inboundMin;
	private long moNo;
	/** QOS MAXOUTBOUND */
	private int outboundMax;
	/** QOS MINOUTBOUND */
	private int outboundMin;
	/** QOS Id정보 */
	private int ruleId;
	/** QOS 명 */
	private String ruleName;

	public int getInboundMax() {
		return inboundMax;
	}

	public int getInboundMin() {
		return inboundMin;
	}

	public long getMoNo() {
		return moNo;
	}

	public int getOutboundMax() {
		return outboundMax;
	}

	public int getOutboundMin() {
		return outboundMin;
	}

	public int getRuleId() {
		return ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setInboundMax(int inboundMax) {
		this.inboundMax = inboundMax;
	}

	public void setInboundMin(int inboundMin) {
		this.inboundMin = inboundMin;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setOutboundMax(int outboundMax) {
		this.outboundMax = outboundMax;
	}

	public void setOutboundMin(int outboundMin) {
		this.outboundMin = outboundMin;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
}

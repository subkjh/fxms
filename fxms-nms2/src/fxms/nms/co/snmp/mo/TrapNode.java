package fxms.nms.co.snmp.mo;

import fxms.bas.mo.Mo;

/**
 * 트랩을 보내는 노드
 * 
 * @author subkjh
 * 
 */
public class TrapNode extends Mo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9137680625442472598L;

	/** dhcp 장비 여부 */
	private boolean dhcp;
	/** IP주소 */
	private String ipAddress;
	/** 모델명 */
	private String modelName;
	/** 모델번호 */
	private int modelNo;
	/** 서비스 코드 */
	private String serviceCode;
	/** 트랩 수신 여부 */
	private boolean trapRecv;

	public TrapNode() {
		trapRecv = true;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getModelName() {
		return modelName;
	}

	public int getModelNo() {
		return modelNo;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public boolean isDhcp() {
		return dhcp;
	}

	public boolean isTrapRecv() {
		return trapRecv;
	}

	public void setDhcp(boolean dhcp) {
		this.dhcp = dhcp;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setTrapRecv(boolean trapRecv) {
		this.trapRecv = trapRecv;
	}

}

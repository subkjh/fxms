package fxms.nms.co.syslog.mo;

import fxms.bas.mo.Mo;

/**
 * Syslog 노드
 * 
 * @author subkjh
 * 
 */
public class SyslogNode extends Mo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3663199848622755961L;

	/** IP 주소 */
	private String ipAddress;
	/** 모델명 */
	private String modelName;
	/** 모델번호 */
	private int modelNo;
	/** 장비의 hostname으로 검색하기 위해 필요함. */
	private String sysName;

	public String getIpAddress() {
		return ipAddress;
	}

	public String getModelName() {
		return modelName;
	}

	public int getModelNo() {
		return modelNo;
	}

	public String getSysName() {
		return sysName;
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

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

}

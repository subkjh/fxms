package fxms.bas.co.vo;

import fxms.bas.fxo.FxCfg;

public class FxServiceVo {

	/** 서비스명 */
	private String serviceName;

	/** 서비스자바클래스 */
	private String serviceJavaClass;

	/** 관리서버주소 */
	private String msIpaddr;

	public FxServiceVo() {

	}

	public String getKey() {
		return msIpaddr + "/" + serviceName;
	}

	public FxServiceVo(String msIpaddr, String serviceName, String serviceJavaClass) {
		this.msIpaddr = msIpaddr;
		this.serviceJavaClass = serviceJavaClass;
		this.serviceName = serviceName;
	}

	/**
	 * 관리서버주소
	 * 
	 * @return 관리서버주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
	}

	public String getServiceJavaClass() {
		return serviceJavaClass;
	}

	/**
	 * 서비스명
	 * 
	 * @return 서비스명
	 */
	public String getServiceName() {
		return serviceName;
	}

	public String getUrl() {
		int portRmi = FxCfg.getCfg().getRmiPort();
		if (portRmi > 0) {
			return "rmi://" + getMsIpaddr() + ":" + portRmi + "/" + getServiceName();
		} else {
			return null;
		}
	}

	/**
	 * 관리서버주소
	 * 
	 * @param msIpaddr 관리서버주소
	 */
	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	public void setServiceJavaClass(String serviceJavaClass) {
		this.serviceJavaClass = serviceJavaClass;
	}

	/**
	 * 서비스명
	 * 
	 * @param serviceName 서비스명
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}

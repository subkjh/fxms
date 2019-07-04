package fxms.bas.mo;

import java.io.Serializable;

import fxms.bas.fxo.FxCfg;

public class ServiceMo extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5408583450598547234L;

	public static final String MO_CLASS = "FXSERVICE";

	public static String getMoName(String msIpaddr, String serviceName) {
		return msIpaddr + "/" + serviceName;
	}

	public static void set(ServiceMo mo, String msIpaddr, String serviceName) {
		mo.setMngYn(true);
		mo.setServiceName(serviceName);
		mo.setMoName(getMoName(msIpaddr, serviceName));
		mo.setMsIpaddr(msIpaddr);

	}

	/** 서비스명 */
	private String serviceName;

	/** 서비스설명 */
	private String serviceDescr;

	/** 서비스자바클래스 */
	private String serviceJavaClass;

	/** 관리서버주소 */
	private String msIpaddr;

	/** 서비스상태 */
	private String serviceStatus;

	/** 서비스상태변경일시 */
	private Number statusChgDate;

	/** 시작일시 */
	private long startDate;

	public ServiceMo() {
		setMoClass(MO_CLASS);
	}

	/**
	 * 관리서버주소
	 * 
	 * @return 관리서버주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
	}

	/**
	 * 서비스설명
	 * 
	 * @return 서비스설명
	 */
	public String getServiceDescr() {
		return serviceDescr;
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

	/**
	 * 서비스상태
	 * 
	 * @return 서비스상태
	 */
	public String getServiceStatus() {
		return serviceStatus;
	}

	public long getStartDate() {
		return startDate;
	}

	/**
	 * 서비스상태변경잀
	 * 
	 * @return 서비스상태변경잀
	 */
	public Number getStatusChgDate() {
		return statusChgDate;
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
	 * @param msIpaddr
	 *            관리서버주소
	 */
	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	/**
	 * 서비스설명
	 * 
	 * @param serviceDescr
	 *            서비스설명
	 */
	public void setServiceDescr(String serviceDescr) {
		this.serviceDescr = serviceDescr;
	}

	public void setServiceJavaClass(String serviceJavaClass) {
		this.serviceJavaClass = serviceJavaClass;
	}

	/**
	 * 서비스명
	 * 
	 * @param serviceName
	 *            서비스명
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 서비스상태
	 * 
	 * @param serviceStatus
	 *            서비스상태
	 */
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	/**
	 * 서비스상태변경잀
	 * 
	 * @param statusChgDate
	 *            서비스상태변경잀
	 */
	public void setStatusChgDate(Number statusChgDate) {
		this.statusChgDate = statusChgDate;
	}
}

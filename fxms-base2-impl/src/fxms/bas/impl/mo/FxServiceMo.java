package fxms.bas.impl.mo;

import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.HasAlarmCfg;

public class FxServiceMo extends Mo implements HasAlarmCfg {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5893350422822233588L;

	public static final String MO_CLASS = "FXSERVICE";

	/** MO표시명 */
	private String moAname;

	/** MO종류 */
	private String moType;

	/** MO메모 */
	private String moMemo;

	/** 경보조건번호 */
	private int alarmCfgNo = -1;

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

	public FxServiceMo() {
		setMoClass(MO_CLASS);
	}

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public String getMoAname() {
		return moAname == null ? getMoName() : moAname;
	}

	public String getMoMemo() {
		return moMemo;
	}

	public String getMoType() {
		return moType;
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

	public void set(String msIpaddr, String serviceName) {
		setMngYn(true);
		setServiceName(serviceName);
		setMoName(msIpaddr + "/" + serviceName);
		setMsIpaddr(msIpaddr);
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	public void setMoAname(String moAname) {
		this.moAname = moAname;
	}

	public void setMoMemo(String moMemo) {
		this.moMemo = moMemo;
	}

	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 관리서버주소
	 * 
	 * @param msIpaddr 관리서버주소
	 */
	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	/**
	 * 서비스설명
	 * 
	 * @param serviceDescr 서비스설명
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
	 * @param serviceName 서비스명
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 서비스상태
	 * 
	 * @param serviceStatus 서비스상태
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
	 * @param statusChgDate 서비스상태변경잀
	 */
	public void setStatusChgDate(Number statusChgDate) {
		this.statusChgDate = statusChgDate;
	}
}

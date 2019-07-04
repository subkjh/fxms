package fxms.bas.po.vo;


/**
 * 서비스성능항목
 * 
 * @author subkjh(김종훈)
 *
 */
public class FxServicePsVo {

	/** 서비스명 */
	private String serviceName;

	/** 관리서버주소 */
	private String msIpaddr;

	/** 액터자바클래스 */
	private String fxactorJavaClass;

	/** 상태값번호 */
	private String psCode;

	/** 최근일시 */
	private long lastDate;
	
	public FxServicePsVo(String serviceName, String msIpaddr, String fxactorJavaClass, String psCode, long lastDate)
	{
		this.serviceName = serviceName;
		this.msIpaddr = msIpaddr;
		this.fxactorJavaClass = fxactorJavaClass;
		this.psCode = psCode;
		this.lastDate = lastDate;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMsIpaddr() {
		return msIpaddr;
	}

	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	public String getFxactorJavaClass() {
		return fxactorJavaClass;
	}

	public void setFxactorJavaClass(String fxactorJavaClass) {
		this.fxactorJavaClass = fxactorJavaClass;
	}

	public String getPsCode() {
		return psCode;
	}

	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	public long getLastDate() {
		return lastDate;
	}

	public void setLastDate(long lastDate) {
		this.lastDate = lastDate;
	}

}

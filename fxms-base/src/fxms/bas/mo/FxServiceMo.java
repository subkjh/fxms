package fxms.bas.mo;

import java.io.Serializable;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;
import subkjh.bas.user.User;

@FxTable(name = "FX_MO_FXSERVICE", comment = "FX 서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_FXSERVICE__UK", type = INDEX_TYPE.UK, columns = { "MS_IPADDR", "SERVICE_NAME" })
@FxIndex(name = "FX_MO_FXSERVICE__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")

public class FxServiceMo extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5408583450598547234L;

	public static final String MO_CLASS = "FXSERVICE";

	public static String getMoName(String msIpaddr, String serviceName) {
		return msIpaddr + "/" + serviceName;
	}

	public static void set(FxServiceMo mo, String msIpaddr, String serviceName) {
		mo.setMngYn(true);
		mo.setServiceName(serviceName);
		mo.setMoAname(serviceName);
		mo.setMoName(getMoName(msIpaddr, serviceName));
		mo.setMsIpaddr(msIpaddr);
		mo.setChgDate(FxApi.getDate(0));
		mo.setChgUserNo(User.USER_NO_SYSTEM);
		mo.setRegDate(FxApi.getDate(0));
		mo.setRegUserNo(User.USER_NO_SYSTEM);
	}

	@FxColumn(name = "SERVICE_NAME", size = 50, comment = "서비스명")
	private String serviceName;

	@FxColumn(name = "SERVICE_DESCR", size = 39, nullable = true, comment = "서비스설명")
	private String serviceDescr;
	
	@FxColumn(name = "SERVICE_JAVA_CLASS", size = 200, comment = "서비스자바클래스")
	private String serviceJavaClass;
	
	@FxColumn(name = "MS_IPADDR", size = 39, comment = "관리서버주소")
	private String msIpaddr;

	@FxColumn(name = "SERVICE_STATUS", size = 10, nullable = true, comment = "서비스상태")
	private String serviceStatus;

	@FxColumn(name = "STATUS_CHG_DATE", size = 14, nullable = true, comment = "서비스상태변경일시")
	private Number statusChgDate;
	
	@FxColumn(name = "START_DATE", size = 14, nullable = true, comment = "시작일시")
	private long startDate;

	public FxServiceMo() {
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

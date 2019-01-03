package fxms.bas.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.01.17 11:32
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_FXSERVICE", comment = "FX 서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_FXSERVICE__UK", type = INDEX_TYPE.UK, columns = { "MS_IPADDR", "SERVICE_NAME" })
@FxIndex(name = "FX_MO_FXSERVICE__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FX_MO_FXSERVICE {

	public FX_MO_FXSERVICE() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "장치관리번호")
	private Number moNo;

	@FxColumn(name = "SERVICE_NAME", size = 50, comment = "서비스명")
	private String serviceName;

	@FxColumn(name = "SERVICE_DESCR", size = 39, nullable = true, comment = "서비스설명")
	private String serviceDescr;

	@FxColumn(name = "MS_IPADDR", size = 39, comment = "관리서버주소")
	private String msIpaddr;

	@FxColumn(name = "SERVICE_STATUS", size = 10, nullable = true, comment = "서비스상태")
	private String serviceStatus;

	@FxColumn(name = "STATUS_CHG_DATE", size = 14, nullable = true, comment = "서비스상태변경잀")
	private Number statusChgDate;

	/**
	 * 장치관리번호
	 * 
	 * @return 장치관리번호
	 */
	public Number getMoNo() {
		return moNo;
	}

	/**
	 * 장치관리번호
	 * 
	 * @param moNo
	 *            장치관리번호
	 */
	public void setMoNo(Number moNo) {
		this.moNo = moNo;
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
	 * 서비스명
	 * 
	 * @param serviceName
	 *            서비스명
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * 서비스설명
	 * 
	 * @return 서비스설명
	 */
	public String getServiceDescr() {
		return serviceDescr;
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

	/**
	 * 관리서버주소
	 * 
	 * @return 관리서버주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
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
	 * 서비스상태
	 * 
	 * @return 서비스상태
	 */
	public String getServiceStatus() {
		return serviceStatus;
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

	/**
	 * 서비스상태변경잀
	 * 
	 * @return 서비스상태변경잀
	 */
	public Number getStatusChgDate() {
		return statusChgDate;
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

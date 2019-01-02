package fxms.bas.dbo.mo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_MO_FXSERVICE", comment = "FX 서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "MS_IPADDR" })
public class FxServiceMoUpdateStatusAllDbo {

	@FxColumn(name = "MS_IPADDR", size = 39, comment = "관리서버주소")
	private String msIpaddr;

	@FxColumn(name = "SERVICE_STATUS", size = 10, nullable = true, comment = "서비스상태")
	private String serviceStatus;

	@FxColumn(name = "STATUS_CHG_DATE", size = 14, nullable = true, comment = "서비스상태변경일시")
	private Number statusChgDate;

	public FxServiceMoUpdateStatusAllDbo() {
	}

	public FxServiceMoUpdateStatusAllDbo(String msIpaddr, String serviceStatus, Number statusChgDate) {
		this.msIpaddr = msIpaddr;
		this.serviceStatus = serviceStatus;
		this.statusChgDate = statusChgDate;
	}

	public String getMsIpaddr() {
		return msIpaddr;
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
	 * 서비스상태변경잀
	 * 
	 * @return 서비스상태변경잀
	 */
	public Number getStatusChgDate() {
		return statusChgDate;
	}

	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
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
	 * @param statusChgDate
	 *            서비스상태변경잀
	 */
	public void setStatusChgDate(Number statusChgDate) {
		this.statusChgDate = statusChgDate;
	}
}
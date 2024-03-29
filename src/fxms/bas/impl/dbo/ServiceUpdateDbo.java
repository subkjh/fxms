package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_CF_SERVICE", comment = "수집서비스테이블")
@FxIndex(name = "FX_CF_SERVICE__PK", type = INDEX_TYPE.PK, columns = { "MS_IPADDR", "SERVICE_NAME" })
public class ServiceUpdateDbo {

	@FxColumn(name = "SERVICE_NAME", size = 50, comment = "서비스명")
	private String serviceName;

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

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public long getStatusChgDate() {
		return statusChgDate;
	}

	public void setStatusChgDate(long statusChgDate) {
		this.statusChgDate = statusChgDate;
	}

	@FxColumn(name = "MS_IPADDR", size = 39, comment = "관리서버주소")
	private String msIpaddr;

	@FxColumn(name = "SERVICE_STATUS", size = 10, nullable = true, comment = "서비스상태")
	private String serviceStatus;

	@FxColumn(name = "STATUS_CHG_DATE", size = 14, nullable = true, comment = "서비스상태변경잀")
	private long statusChgDate;

}

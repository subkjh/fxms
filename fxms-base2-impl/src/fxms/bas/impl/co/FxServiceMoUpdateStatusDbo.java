package fxms.bas.impl.co;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_MO_FXSERVICE", comment = "FX 서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
public class FxServiceMoUpdateStatusDbo {

	public FxServiceMoUpdateStatusDbo() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "관리번호")
	private Number moNo;

	@FxColumn(name = "SERVICE_STATUS", size = 10, nullable = true, comment = "서비스상태")
	private String serviceStatus;

	@FxColumn(name = "STATUS_CHG_DATE", size = 14, nullable = true, comment = "서비스상태변경일시")
	private Number statusChgDate;

	@FxColumn(name = "START_DATE", size = 14, nullable = true, comment = "시작일시")
	private long startDate;

	public FxServiceMoUpdateStatusDbo(Number moNo, long startDate, String serviceStatus, Number statusChgDate) {
		this.moNo = moNo;
		this.startDate = startDate;
		this.serviceStatus = serviceStatus;
		this.statusChgDate = statusChgDate;
	}

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
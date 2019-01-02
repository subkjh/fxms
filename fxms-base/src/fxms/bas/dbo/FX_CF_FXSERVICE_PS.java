package fxms.bas.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.05.24 16:26
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CF_FXSERVICE_PS", comment = "서비스성능항목테이블")
@FxIndex(name = "FX_MO_FXSERVICE_PS__PK", type = INDEX_TYPE.PK, columns = { "SERVICE_NAME", "MS_IPADDR",
		"FXACTOR_JAVA_CLASS", "PS_CODE" })
public class FX_CF_FXSERVICE_PS {

	public FX_CF_FXSERVICE_PS() {
	}

	@FxColumn(name = "SERVICE_NAME", size = 50, comment = "서비스명")
	private String serviceName;

	@FxColumn(name = "MS_IPADDR", size = 39, comment = "관리서버주소")
	private String msIpaddr;

	@FxColumn(name = "FXACTOR_JAVA_CLASS", size = 200, comment = "액터자바클래스")
	private String fxactorJavaClass;

	@FxColumn(name = "PS_CODE", size = 20, comment = "상태값번호")
	private String psCode;

	@FxColumn(name = "LAST_DATE", size = 14, nullable = true, comment = "최근일시")
	private long lastDate;

	@Override
	public String toString() {
		return fxactorJavaClass + "-" + psCode + "=" + lastDate;
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
	 * 액터자바클래스
	 * 
	 * @return 액터자바클래스
	 */
	public String getFxactorJavaClass() {
		return fxactorJavaClass;
	}

	/**
	 * 액터자바클래스
	 * 
	 * @param fxactorJavaClass
	 *            액터자바클래스
	 */
	public void setFxactorJavaClass(String fxactorJavaClass) {
		this.fxactorJavaClass = fxactorJavaClass;
	}

	/**
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCode() {
		return psCode;
	}

	/**
	 * 상태값번호
	 * 
	 * @param psCode
	 *            상태값번호
	 */
	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	/**
	 * 최근일시
	 * 
	 * @return 최근일시
	 */
	public long getLastDate() {
		return lastDate;
	}

	/**
	 * 최근일시
	 * 
	 * @param lastDate
	 *            최근일시
	 */
	public void setLastDate(long lastDate) {
		this.lastDate = lastDate;
	}
}

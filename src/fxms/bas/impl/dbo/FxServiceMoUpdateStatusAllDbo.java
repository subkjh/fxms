package fxms.bas.impl.dbo;

import fxms.bas.co.CoCode.FXSVC_ST_CD;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_MO_FXSERVICE", comment = "FX 서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "FXSVR_IP_ADDR" })
public class FxServiceMoUpdateStatusAllDbo {

	@FxColumn(name = "FXSVR_IP_ADDR", size = 39, comment = "FX서버IP주소")
	private String fxsvrIpAddr;

	@FxColumn(name = "FXSVC_ST_CD", size = 10, nullable = true, comment = "FX서비스상태코드")
	private String fxsvcStCd;

	@FxColumn(name = "FXSVC_ST_CHG_DTM", size = 14, nullable = true, comment = "FX서비스상태변경일시")
	private long fxsvcStChgDtm;

	public FxServiceMoUpdateStatusAllDbo() {
	}

	public FxServiceMoUpdateStatusAllDbo(String fxsvrIpAddr, FXSVC_ST_CD serviceStatus, long fxsvcStChgDtm) {
		this.fxsvrIpAddr = fxsvrIpAddr;
		this.fxsvcStCd = serviceStatus.name();
		this.fxsvcStChgDtm = fxsvcStChgDtm;
	}

	public String getFxsvrIpAddr() {
		return fxsvrIpAddr;
	}

	public void setFxsvrIpAddr(String fxsvrIpAddr) {
		this.fxsvrIpAddr = fxsvrIpAddr;
	}

	public String getFxsvcStCd() {
		return fxsvcStCd;
	}

	public void setFxsvcStCd(String fxsvcStCd) {
		this.fxsvcStCd = fxsvcStCd;
	}

	public long getFxsvcStChgDtm() {
		return fxsvcStChgDtm;
	}

	public void setFxsvcStChgDtm(long fxsvcStChgDtm) {
		this.fxsvcStChgDtm = fxsvcStChgDtm;
	}

}
package fxms.bas.impl.dbo;

import fxms.bas.co.CoCode.FXSVC_ST_CD;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_MO_FXSERVICE", comment = "FX 서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
public class FxServiceMoUpdateStatusDbo {

	public FxServiceMoUpdateStatusDbo() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "관리번호")
	private Number moNo;

	@FxColumn(name = "STRT_DTM", size = 14, nullable = true, comment = "시작일시")
	private long strtDtm;

	@FxColumn(name = "RMI_PORT", size = 5, nullable = true, comment = "RMI포트", defValue = "-1")
	private Integer rmiPort = -1;

	@FxColumn(name = "FXSVC_PORT", size = 5, nullable = true, comment = "FX서비스포트", defValue = "-1")
	private Integer fxsvcPort = -1;

	@FxColumn(name = "FXSVC_ST_CD", size = 10, nullable = true, comment = "FX서비스상태코드")
	private String fxsvcStCd;

	@FxColumn(name = "FXSVC_ST_CHG_DTM", size = 14, nullable = true, comment = "FX서비스상태변경일시")
	private long fxsvcStChgDtm;

	public FxServiceMoUpdateStatusDbo(Number moNo, long strtDtm, FXSVC_ST_CD fxsvcStCd, long fxsvcStChgDtm, int rmiPort,
			int fxsvcPort) {
		this.moNo = moNo;
		this.strtDtm = strtDtm;
		this.fxsvcStCd = fxsvcStCd.name();
		this.fxsvcStChgDtm = fxsvcStChgDtm;
		this.rmiPort = rmiPort;
		this.fxsvcPort = fxsvcPort;
	}

	public Number getMoNo() {
		return moNo;
	}

	public void setMoNo(Number moNo) {
		this.moNo = moNo;
	}

	public long getStrtDtm() {
		return strtDtm;
	}

	public void setStrtDtm(long strtDtm) {
		this.strtDtm = strtDtm;
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

	public Integer getRmiPort() {
		return rmiPort;
	}

	public void setRmiPort(Integer rmiPort) {
		this.rmiPort = rmiPort;
	}

	public Integer getFxsvcPort() {
		return fxsvcPort;
	}

	public void setFxsvcPort(Integer fxsvcPort) {
		this.fxsvcPort = fxsvcPort;
	}

}
package fxms.bas.mo;

import fxms.bas.fxo.FxCfg;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_MO_FXSERVICE", comment = "서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_FXSERVICE__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FxServiceMo extends FxMo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5893350422822233588L;

	public static final String MO_CLASS = "FXSERVICE";

	@FxColumn(name = "FXSVC_NAME", size = 50, comment = "FX서비스명")
	private String fxsvcName;

	@FxColumn(name = "FXSVC_JAVA_CLASS", size = 200, comment = "FX서비스자바클래스")
	private String fxsvcJavaClass;

	@FxColumn(name = "FXSVR_IP_ADDR", size = 39, comment = "FX서버IP주소")
	private String fxsvrIpAddr;

	@FxColumn(name = "FXSVC_DESC", size = 100, nullable = true, comment = "FX서비스설명")
	private String fxsvcDesc;

	@FxColumn(name = "MNG_DIV", size = 20, nullable = true, comment = "관리구분")
	private String mngDiv;

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
	
	@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
	private String useYn = "Y";

	public FxServiceMo() {
		setMoClass(MO_CLASS);
	}

	public String getUrl() {
		int portRmi = FxCfg.getCfg().getRmiPort();
		if (portRmi > 0) {
			return "rmi://" + getFxsvrIpAddr() + ":" + portRmi + "/" + getFxsvcName();
		} else {
			return null;
		}
	}

	public void set(String fxsvrIpAddr, String fxsvcName) {
		setMngYn(true);
		this.setFxsvcName(fxsvcName);
		this.setFxsvrIpAddr(fxsvrIpAddr);
		this.setMoName(fxsvrIpAddr + "/" + fxsvcName);
	}

	public String getFxsvcName() {
		return fxsvcName;
	}

	public void setFxsvcName(String fxsvcName) {
		this.fxsvcName = fxsvcName;
	}

	public String getFxsvcJavaClass() {
		return fxsvcJavaClass;
	}

	public void setFxsvcJavaClass(String fxsvcJavaClass) {
		this.fxsvcJavaClass = fxsvcJavaClass;
	}

	public String getFxsvrIpAddr() {
		return fxsvrIpAddr;
	}

	public void setFxsvrIpAddr(String fxsvrIpAddr) {
		this.fxsvrIpAddr = fxsvrIpAddr;
	}

	public String getFxsvcDesc() {
		return fxsvcDesc;
	}

	public void setFxsvcDesc(String fxsvcDesc) {
		this.fxsvcDesc = fxsvcDesc;
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

	/**
	* 사용여부
	* @return 사용여부
	*/
	public String isUseYn() {
	return useYn;
	}
	/**
	* 사용여부
	*@param useYn 사용여부
	*/
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}

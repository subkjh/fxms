package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.02 18:01
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_FXSERVICE", comment = "서비스테이블")
@FxIndex(name = "FX_MO_FXSERVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_FXSERVICE__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FX_MO_FXSERVICE extends FX_MO implements Serializable {

	public FX_MO_FXSERVICE() {
	}

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

	@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
	private String useYn = "Y";

	@FxColumn(name = "RMI_PORT", size = 5, nullable = true, comment = "RMI포트", defValue = "-1")
	private Integer rmiPort = -1;

	@FxColumn(name = "FXSVC_PORT", size = 5, nullable = true, comment = "FX서비스포트", defValue = "-1")
	private Integer fxsvcPort = -1;

	@FxColumn(name = "STRT_DTM", size = 14, nullable = true, comment = "시작일시")
	private long strtDtm;

	@FxColumn(name = "FXSVC_ST_CD", size = 10, nullable = true, comment = "FX서비스상태코드")
	private String fxsvcStCd;

	@FxColumn(name = "FXSVC_ST_CHG_DTM", size = 14, nullable = true, comment = "FX서비스상태변경일시")
	private long fxsvcStChgDtm;

	/**
	 * FX서비스명
	 * 
	 * @return FX서비스명
	 */
	public String getFxsvcName() {
		return fxsvcName;
	}

	/**
	 * FX서비스명
	 * 
	 * @param fxsvcName FX서비스명
	 */
	public void setFxsvcName(String fxsvcName) {
		this.fxsvcName = fxsvcName;
	}

	/**
	 * FX서비스자바클래스
	 * 
	 * @return FX서비스자바클래스
	 */
	public String getFxsvcJavaClass() {
		return fxsvcJavaClass;
	}

	/**
	 * FX서비스자바클래스
	 * 
	 * @param fxsvcJavaClass FX서비스자바클래스
	 */
	public void setFxsvcJavaClass(String fxsvcJavaClass) {
		this.fxsvcJavaClass = fxsvcJavaClass;
	}

	/**
	 * FX서버IP주소
	 * 
	 * @return FX서버IP주소
	 */
	public String getFxsvrIpAddr() {
		return fxsvrIpAddr;
	}

	/**
	 * FX서버IP주소
	 * 
	 * @param fxsvrIpAddr FX서버IP주소
	 */
	public void setFxsvrIpAddr(String fxsvrIpAddr) {
		this.fxsvrIpAddr = fxsvrIpAddr;
	}

	/**
	 * FX서비스설명
	 * 
	 * @return FX서비스설명
	 */
	public String getFxsvcDesc() {
		return fxsvcDesc;
	}

	/**
	 * FX서비스설명
	 * 
	 * @param fxsvcDesc FX서비스설명
	 */
	public void setFxsvcDesc(String fxsvcDesc) {
		this.fxsvcDesc = fxsvcDesc;
	}

	/**
	 * 시작일시
	 * 
	 * @return 시작일시
	 */
	public long getStrtDtm() {
		return strtDtm;
	}

	/**
	 * 시작일시
	 * 
	 * @param strtDtm 시작일시
	 */
	public void setStrtDtm(long strtDtm) {
		this.strtDtm = strtDtm;
	}

	/**
	 * FX서비스상태코드
	 * 
	 * @return FX서비스상태코드
	 */
	public String getFxsvcStCd() {
		return fxsvcStCd;
	}

	/**
	 * FX서비스상태코드
	 * 
	 * @param fxsvcStCd FX서비스상태코드
	 */
	public void setFxsvcStCd(String fxsvcStCd) {
		this.fxsvcStCd = fxsvcStCd;
	}

	/**
	 * FX서비스상태변경일시
	 * 
	 * @return FX서비스상태변경일시
	 */
	public long getFxsvcStChgDtm() {
		return fxsvcStChgDtm;
	}

	/**
	 * FX서비스상태변경일시
	 * 
	 * @param fxsvcStChgDtm FX서비스상태변경일시
	 */
	public void setFxsvcStChgDtm(long fxsvcStChgDtm) {
		this.fxsvcStChgDtm = fxsvcStChgDtm;
	}

	public String getMngDiv() {
		return mngDiv;
	}

	public void setMngDiv(String mngDiv) {
		this.mngDiv = mngDiv;
	}

	/**
	 * RMI포트
	 * 
	 * @return RMI포트
	 */
	public Integer getRmiPort() {
		return rmiPort;
	}

	/**
	 * RMI포트
	 * 
	 * @param rmiPort RMI포트
	 */
	public void setRmiPort(Integer rmiPort) {
		this.rmiPort = rmiPort;
	}

	/**
	 * FX서비스포트
	 * 
	 * @return FX서비스포트
	 */
	public Integer getFxsvcPort() {
		return fxsvcPort;
	}

	/**
	 * FX서비스포트
	 * 
	 * @param fxsvcPort FX서비스포트
	 */
	public void setFxsvcPort(Integer fxsvcPort) {
		this.fxsvcPort = fxsvcPort;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public String isUseYn() {
		return useYn;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn 사용여부
	 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}

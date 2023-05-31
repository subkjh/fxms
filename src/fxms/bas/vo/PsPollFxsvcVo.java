package fxms.bas.vo;

/**
 * 서비스성능항목
 * 
 * @author subkjh(김종훈)
 *
 */
public class PsPollFxsvcVo {

	/** 서비스명 */
	private String fxsvcName;

	/** 관리서버주소 */
	private String fxsvrIpAddr;

	/** 액터자바클래스 */
	private String adptName;

	/** 상태값번호 */
	private String psId;

	/** 최근일시 */
	private long lastDtm;

	public PsPollFxsvcVo(String fxsvcName, String fxsvrIpAddr, String adptName, String psId, long lastDtm) {
		this.fxsvcName = fxsvcName;
		this.fxsvrIpAddr = fxsvrIpAddr;
		this.adptName = adptName;
		this.psId = psId;
		this.lastDtm = lastDtm;
	}

	public String getFxsvcName() {
		return fxsvcName;
	}

	public void setFxsvcName(String fxsvcName) {
		this.fxsvcName = fxsvcName;
	}

	public String getFxsvrIpAddr() {
		return fxsvrIpAddr;
	}

	public void setFxsvrIpAddr(String fxsvrIpAddr) {
		this.fxsvrIpAddr = fxsvrIpAddr;
	}

	public String getAdptName() {
		return adptName;
	}

	public void setAdptName(String adptName) {
		this.adptName = adptName;
	}

	public String getPsId() {
		return psId;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public long getLastDtm() {
		return lastDtm;
	}

	public void setLastDtm(long lastDtm) {
		this.lastDtm = lastDtm;
	}

	
}

package fxms.bas.vo;

/**
 * 관제점에 대한 정보를 갖는다.
 * 
 * @author subkjh
 * @since 2023.07.25
 */
public class PsPoint {

	private Integer alcdNo; // 경보코드번호

	private String facTid; // 설비TID

	private long moNo; // 관리대상번호
	
	private String moName; // 관리대상명

	private String moTid; // MO대상ID

	private String pointDescr; // 관제점설명

	private String pointId; // 관제점ID

	private String pointNm; // 관제점명

	private String pointTid; // 관제점TID

	private String pointUnit; // 관제점단위

	private String psId; // 성능항목ID

	public Integer getAlcdNo() {
		return alcdNo;
	}

	public String getFacTid() {
		return facTid;
	}

	public String getMoName() {
		return moName;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getMoTid() {
		return moTid;
	}

	public String getPointDescr() {
		return pointDescr;
	}

	public String getPointId() {
		return pointId;
	}

	public String getPointNm() {
		return pointNm;
	}

	public String getPointTid() {
		return pointTid;
	}

	public String getPointUnit() {
		return pointUnit;
	}

	public String getPsId() {
		return psId;
	}

	public void setAlcdNo(Integer alcdNo) {
		this.alcdNo = alcdNo;
	}

	public void setFacTid(String facTid) {
		this.facTid = facTid;
	}

	public void setMoName(String moName) {
		this.moName = moName;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setMoTid(String moTid) {
		this.moTid = moTid;
	}

	public void setPointDescr(String pointDescr) {
		this.pointDescr = pointDescr;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public void setPointNm(String pointNm) {
		this.pointNm = pointNm;
	}

	public void setPointTid(String pointTid) {
		this.pointTid = pointTid;
	}

	public void setPointUnit(String pointUnit) {
		this.pointUnit = pointUnit;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}
	
	

}

package fxms.bas.vo;

/**
 * 발생 알람
 * 
 * @author subkjh
 *
 */
public class OccurAlarm extends Alarm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5011730973179600794L;

	private String alcdName;

	private String alarmClCd;

	private String psId;

	private Double psVal;

	private Double cmprVal;

	private Long psDtm;

	private String psName;

	private String moDispName;

	private String upperMoDispName;

	private String inloName;

	private int modelNo;

	private String modelName;

	private Long ackDtm;

	private Integer ackUserNo;

	private Long rsnRegDtm;

	private Integer rsnRegUserNo;

	private Integer rsnNo = 0;

	private String rsnName;

	private String rsnMemo;

	private String ipAddr;

	private long chgDtm;

	private String fpactCd;

	private String fpactName;

	private long regDtm;

	public OccurAlarm() {

	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 경보코드명
	 * 
	 * @return 경보코드명
	 */
	public String getAlcdName() {
		return alcdName;
	}

	/**
	 * 경보코드명
	 * 
	 * @param alcdName 경보코드명
	 */
	public void setAlcdName(String alcdName) {
		this.alcdName = alcdName;
	}

	/**
	 * 알람구분코드
	 * 
	 * @return 알람구분코드
	 */
	public String getAlarmClCd() {
		return alarmClCd;
	}

	/**
	 * 알람구분코드
	 * 
	 * @param alarmClCd 알람구분코드
	 */
	public void setAlarmClCd(String alarmClCd) {
		this.alarmClCd = alarmClCd;
	}

	/**
	 * 성능ID
	 * 
	 * @return 성능ID
	 */
	public String getPsId() {
		return psId;
	}

	/**
	 * 성능ID
	 * 
	 * @param psId 성능ID
	 */
	public void setPsId(String psId) {
		this.psId = psId;
	}

	/**
	 * 성능값
	 * 
	 * @return 성능값
	 */
	public Double getPsVal() {
		return psVal;
	}

	/**
	 * 성능값
	 * 
	 * @param psVal 성능값
	 */
	public void setPsVal(Double psVal) {
		this.psVal = psVal;
	}

	/**
	 * 비교값
	 * 
	 * @return 비교값
	 */
	public Double getCmprVal() {
		return cmprVal;
	}

	/**
	 * 비교값
	 * 
	 * @param cmprVal 비교값
	 */
	public void setCmprVal(Double cmprVal) {
		this.cmprVal = cmprVal;
	}

	/**
	 * 상태수집일시
	 * 
	 * @return 상태수집일시
	 */
	public Long getPsDtm() {
		return psDtm;
	}

	/**
	 * 상태수집일시
	 * 
	 * @param psDtm 상태수집일시
	 */
	public void setPsDtm(Long psDtm) {
		this.psDtm = psDtm;
	}

	/**
	 * 상태값명
	 * 
	 * @return 상태값명
	 */
	public String getPsName() {
		return psName;
	}

	/**
	 * 상태값명
	 * 
	 * @param psName 상태값명
	 */
	public void setPsName(String psName) {
		this.psName = psName;
	}

	/**
	 * MO표시명
	 * 
	 * @return MO표시명
	 */
	public String getMoDispName() {
		return moDispName;
	}

	/**
	 * MO표시명
	 * 
	 * @param moDispName MO표시명
	 */
	public void setMoDispName(String moDispName) {
		this.moDispName = moDispName;
	}

	/**
	 * 상위MO표시명
	 * 
	 * @return 상위MO표시명
	 */
	public String getUpperMoDispName() {
		return upperMoDispName;
	}

	/**
	 * 상위MO표시명
	 * 
	 * @param upperMoDispName 상위MO표시명
	 */
	public void setUpperMoDispName(String upperMoDispName) {
		this.upperMoDispName = upperMoDispName;
	}

	/**
	 * 설치위치명
	 * 
	 * @return 설치위치명
	 */
	public String getInloName() {
		return inloName;
	}

	/**
	 * 설치위치명
	 * 
	 * @param inloName 설치위치명
	 */
	public void setInloName(String inloName) {
		this.inloName = inloName;
	}

	/**
	 * 모델번호
	 * 
	 * @return 모델번호
	 */
	public int getModelNo() {
		return modelNo;
	}

	/**
	 * 모델번호
	 * 
	 * @param modelNo 모델번호
	 */
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * 모델명
	 * 
	 * @return 모델명
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * 모델명
	 * 
	 * @param modelName 모델명
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * 확인일시
	 * 
	 * @return 확인일시
	 */
	public Long getAckDtm() {
		return ackDtm;
	}

	/**
	 * 확인일시
	 * 
	 * @param ackDtm 확인일시
	 */
	public void setAckDtm(Long ackDtm) {
		this.ackDtm = ackDtm;
	}

	/**
	 * 확인사용자번호
	 * 
	 * @return 확인사용자번호
	 */
	public Integer getAckUserNo() {
		return ackUserNo;
	}

	/**
	 * 확인사용자번호
	 * 
	 * @param ackUserNo 확인사용자번호
	 */
	public void setAckUserNo(Integer ackUserNo) {
		this.ackUserNo = ackUserNo;
	}

	/**
	 * 원인등록일시
	 * 
	 * @return 원인등록일시
	 */
	public Long getRsnRegDtm() {
		return rsnRegDtm;
	}

	/**
	 * 원인등록일시
	 * 
	 * @param rsnRegDtm 원인등록일시
	 */
	public void setRsnRegDtm(Long rsnRegDtm) {
		this.rsnRegDtm = rsnRegDtm;
	}

	/**
	 * 원인등록사용자번호
	 * 
	 * @return 원인등록사용자번호
	 */
	public Integer getRsnRegUserNo() {
		return rsnRegUserNo;
	}

	/**
	 * 원인등록사용자번호
	 * 
	 * @param rsnRegUserNo 원인등록사용자번호
	 */
	public void setRsnRegUserNo(Integer rsnRegUserNo) {
		this.rsnRegUserNo = rsnRegUserNo;
	}

	/**
	 * 원인번호
	 * 
	 * @return 원인번호
	 */
	public Integer getRsnNo() {
		return rsnNo;
	}

	/**
	 * 원인번호
	 * 
	 * @param rsnNo 원인번호
	 */
	public void setRsnNo(Integer rsnNo) {
		this.rsnNo = rsnNo;
	}

	/**
	 * 원인명
	 * 
	 * @return 원인명
	 */
	public String getRsnName() {
		return rsnName;
	}

	/**
	 * 원인명
	 * 
	 * @param rsnName 원인명
	 */
	public void setRsnName(String rsnName) {
		this.rsnName = rsnName;
	}

	/**
	 * 원인메모
	 * 
	 * @return 원인메모
	 */
	public String getRsnMemo() {
		return rsnMemo;
	}

	/**
	 * 원인메모
	 * 
	 * @param rsnMemo 원인메모
	 */
	public void setRsnMemo(String rsnMemo) {
		this.rsnMemo = rsnMemo;
	}

	/**
	 * IP주소
	 * 
	 * @return IP주소
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * IP주소
	 * 
	 * @param ipAddr IP주소
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * 변경일시
	 * 
	 * @return 변경일시
	 */
	public long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 변경일시
	 * 
	 * @param chgDtm 변경일시
	 */
	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}

	/**
	 * 후속조치코드
	 * 
	 * @return 후속조치코드
	 */
	public String getFpactCd() {
		return fpactCd;
	}

	/**
	 * 후속조치코드
	 * 
	 * @param fpactCd 후속조치코드
	 */
	public void setFpactCd(String fpactCd) {
		this.fpactCd = fpactCd;
	}

	/**
	 * 후속조치명
	 * 
	 * @return 후속조치명
	 */
	public String getFpactName() {
		return fpactName;
	}

	/**
	 * 후속조치명
	 * 
	 * @param fpactName 후속조치명
	 */
	public void setFpactName(String fpactName) {
		this.fpactName = fpactName;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();

		sb.append("ALARM(NO(" + getAlarmNo() + ")");
		sb.append("KEY(" + getAlarmKey() + ")");
		sb.append(")");

		return sb.toString();
	}

}

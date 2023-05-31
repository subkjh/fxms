package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.01.17 17:11
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AL_ALARM_HST", comment = "경보알람이력테이블")
@FxIndex(name = "FX_AL_HST__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
@FxIndex(name = "FX_AL_HST__KEY_MONO", type = INDEX_TYPE.KEY, columns = { "MO_NO" })
@FxIndex(name = "FX_AL_HST__KEY_ALCD", type = INDEX_TYPE.KEY, columns = { "ALCD_NO" })
@FxIndex(name = "FX_AL_HST__KEY_OCCUR", type = INDEX_TYPE.KEY, columns = { "OCCUR_DTM" })
@FxIndex(name = "FX_AL_HST__KEY_RLSE", type = INDEX_TYPE.KEY, columns = { "RLSE_DTM" })
@FxIndex(name = "FX_AL_HST__KEY_MOCLASS", type = INDEX_TYPE.KEY, columns = { "MO_CLASS" })
@FxIndex(name = "FX_AL_HST__FK1", type = INDEX_TYPE.FK, columns = {
		"ALCD_NO" }, fkTable = "FX_AL_CD", fkColumn = "ALCD_NO")
public class FX_AL_ALARM_HST implements Serializable {

	public FX_AL_ALARM_HST() {
	}

	@FxColumn(name = "ALARM_NO", size = 19, comment = "알람번호")
	private Long alarmNo;

	@FxColumn(name = "ALARM_KEY", size = 100, nullable = true, comment = "알람키")
	private String alarmKey;

	@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "알람조건번호")
	private Integer alarmCfgNo;

	@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드번호")
	private Integer alcdNo;

	@FxColumn(name = "ALCD_NAME", size = 200, nullable = true, comment = "경보코드명")
	private String alcdName;

	@FxColumn(name = "ALARM_CL_CD", size = 10, nullable = true, comment = "알람구분코드")
	private String alarmClCd;

	@FxColumn(name = "ALARM_LEVEL", size = 1, nullable = true, comment = "경보등급")
	private Integer alarmLevel;

	@FxColumn(name = "ALARM_MSG", size = 1000, nullable = true, comment = "알람메세지")
	private String alarmMsg;

	@FxColumn(name = "PS_ID", size = 50, nullable = true, comment = "성능ID")
	private String psId;

	@FxColumn(name = "PS_VAL", size = 19, nullable = true, comment = "성능값")
	private Double psVal;

	@FxColumn(name = "CMPR_VAL", size = 19, nullable = true, comment = "비교값")
	private Double cmprVal;

	@FxColumn(name = "PS_DTM", size = 14, nullable = true, comment = "상태수집일시")
	private Long psDtm;

	@FxColumn(name = "PS_NAME", size = 100, nullable = true, comment = "상태값명")
	private String psName;

	@FxColumn(name = "MO_NO", size = 19, nullable = true, comment = "MO번호")
	private Long moNo;

	@FxColumn(name = "MO_NAME", size = 200, nullable = true, comment = "MO명")
	private String moName;

	@FxColumn(name = "MO_DISP_NAME", size = 200, nullable = true, comment = "MO표시명")
	private String moDispName;

	@FxColumn(name = "MO_INSTANCE", size = 50, nullable = true, comment = "MO인스턴스")
	private String moInstance;

	@FxColumn(name = "MO_CLASS", size = 20, comment = "MO클래스 ")
	private String moClass;

	@FxColumn(name = "UPPER_MO_NO", size = 19, comment = "상위MO번호", defValue = "-1")
	private Long upperMoNo = -1L;

	@FxColumn(name = "UPPER_MO_NAME", size = 200, nullable = true, comment = "상위MO명")
	private String upperMoName;

	@FxColumn(name = "UPPER_MO_DISP_NAME", size = 200, nullable = true, comment = "상위MO표시명")
	private String upperMoDispName;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호")
	private Integer inloNo;

	@FxColumn(name = "INLO_NAME", size = 100, nullable = true, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "MODEL_NO", size = 9, nullable = true, comment = "모델번호")
	private Integer modelNo;

	@FxColumn(name = "MODEL_NAME", size = 100, nullable = true, comment = "모델명")
	private String modelName;

	@FxColumn(name = "FAC_NO", size = 14, nullable = true, comment = "설비번호")
	private Long facNo;

	@FxColumn(name = "FAC_NAME", size = 100, nullable = true, comment = "설비명")
	private String facName;

	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
	private Long regDtm;

	@FxColumn(name = "OCCUR_DTM", size = 14, nullable = true, comment = "발생일시")
	private Long occurDtm;

	@FxColumn(name = "OCCUR_CNT", size = 9, nullable = true, comment = "발생횟수", defValue = "0")
	private Integer occurCnt = 1;

	@FxColumn(name = "ACK_DTM", size = 14, nullable = true, comment = "확인일시")
	private Long ackDtm;

	@FxColumn(name = "ACK_USER_NO", size = 9, nullable = true, comment = "확인사용자번호")
	private Integer ackUserNo;

	@FxColumn(name = "RSN_REG_DTM", size = 14, nullable = true, comment = "원인등록일시")
	private Long rsnRegDtm;

	@FxColumn(name = "RSN_REG_USER_NO", size = 9, nullable = true, comment = "원인등록사용자번호")
	private Integer rsnRegUserNo;

	@FxColumn(name = "RSN_NO", size = 9, nullable = true, comment = "원인번호")
	private Integer rsnNo;

	@FxColumn(name = "RSN_NAME", size = 100, nullable = true, comment = "원인명")
	private String rsnName;

	@FxColumn(name = "RSN_MEMO", size = 200, nullable = true, comment = "원인메모")
	private String rsnMemo;

	@FxColumn(name = "IP_ADDR", size = 39, nullable = true, comment = "IP주소")
	private String ipAddr;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "변경일시")
	private Long chgDtm;

	@FxColumn(name = "FPACT_CD", size = 10, nullable = true, comment = "후속조치코드")
	private String fpactCd;

	@FxColumn(name = "FPACT_NAME", size = 100, nullable = true, comment = "후속조치명")
	private String fpactName;

	@FxColumn(name = "RLSE_YN", size = 1, comment = "해제여부", defValue = "N")
	private String rlseYn = "N";

	@FxColumn(name = "RLSE_DTM", size = 14, nullable = true, comment = "해제일시")
	private Long rlseDtm;

	@FxColumn(name = "RLSE_USER_NO", size = 9, nullable = true, comment = "해제사용자번호")
	private Integer rlseUserNo;

	@FxColumn(name = "ALARM_RLSE_RSN_CD", size = 10, nullable = true, comment = "알람해제원인코드")
	private String alarmRlseRsnCd;

	@FxColumn(name = "ALARM_RLSE_RSN_NAME", size = 100, nullable = true, comment = "알람해제원인명")
	private String alarmRlseRsnName;

	@FxColumn(name = "RLSE_MEMO", size = 200, nullable = true, comment = "해제메모")
	private String rlseMemo;

	/**
	 * 알람번호
	 * 
	 * @return 알람번호
	 */
	public Long getAlarmNo() {
		return alarmNo;
	}

	/**
	 * 알람번호
	 * 
	 * @param alarmNo 알람번호
	 */
	public void setAlarmNo(Long alarmNo) {
		this.alarmNo = alarmNo;
	}

	/**
	 * 알람키
	 * 
	 * @return 알람키
	 */
	public String getAlarmKey() {
		return alarmKey;
	}

	/**
	 * 알람키
	 * 
	 * @param alarmKey 알람키
	 */
	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	/**
	 * 알람조건번호
	 * 
	 * @return 알람조건번호
	 */
	public Integer getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 알람조건번호
	 * 
	 * @param alarmCfgNo 알람조건번호
	 */
	public void setAlarmCfgNo(Integer alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 경보코드번호
	 * 
	 * @return 경보코드번호
	 */
	public Integer getAlcdNo() {
		return alcdNo;
	}

	/**
	 * 경보코드번호
	 * 
	 * @param alcdNo 경보코드번호
	 */
	public void setAlcdNo(Integer alcdNo) {
		this.alcdNo = alcdNo;
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
	 * 경보등급
	 * 
	 * @return 경보등급
	 */
	public Integer getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * 경보등급
	 * 
	 * @param alarmLevel 경보등급
	 */
	public void setAlarmLevel(Integer alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 알람메세지
	 * 
	 * @return 알람메세지
	 */
	public String getAlarmMsg() {
		return alarmMsg;
	}

	/**
	 * 알람메세지
	 * 
	 * @param alarmMsg 알람메세지
	 */
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
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
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public Long getMoNo() {
		return moNo;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo MO번호
	 */
	public void setMoNo(Long moNo) {
		this.moNo = moNo;
	}

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName() {
		return moName;
	}

	/**
	 * MO명
	 * 
	 * @param moName MO명
	 */
	public void setMoName(String moName) {
		this.moName = moName;
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
	 * MO인스턴스
	 * 
	 * @return MO인스턴스
	 */
	public String getMoInstance() {
		return moInstance;
	}

	/**
	 * MO인스턴스
	 * 
	 * @param moInstance MO인스턴스
	 */
	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	/**
	 * MO클래스
	 * 
	 * @return MO클래스
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * MO클래스
	 * 
	 * @param moClass MO클래스
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	/**
	 * 상위MO번호
	 * 
	 * @return 상위MO번호
	 */
	public Long getUpperMoNo() {
		return upperMoNo;
	}

	/**
	 * 상위MO번호
	 * 
	 * @param upperMoNo 상위MO번호
	 */
	public void setUpperMoNo(Long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	/**
	 * 상위MO명
	 * 
	 * @return 상위MO명
	 */
	public String getUpperMoName() {
		return upperMoName;
	}

	/**
	 * 상위MO명
	 * 
	 * @param upperMoName 상위MO명
	 */
	public void setUpperMoName(String upperMoName) {
		this.upperMoName = upperMoName;
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
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public Integer getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(Integer inloNo) {
		this.inloNo = inloNo;
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
	public Integer getModelNo() {
		return modelNo;
	}

	/**
	 * 모델번호
	 * 
	 * @param modelNo 모델번호
	 */
	public void setModelNo(Integer modelNo) {
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
	 * 설비번호
	 * 
	 * @return 설비번호
	 */
	public Long getFacNo() {
		return facNo;
	}

	/**
	 * 설비번호
	 * 
	 * @param facNo 설비번호
	 */
	public void setFacNo(Long facNo) {
		this.facNo = facNo;
	}

	/**
	 * 설비명
	 * 
	 * @return 설비명
	 */
	public String getFacName() {
		return facName;
	}

	/**
	 * 설비명
	 * 
	 * @param facName 설비명
	 */
	public void setFacName(String facName) {
		this.facName = facName;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(Long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 발생일시
	 * 
	 * @return 발생일시
	 */
	public Long getOccurDtm() {
		return occurDtm;
	}

	/**
	 * 발생일시
	 * 
	 * @param occurDtm 발생일시
	 */
	public void setOccurDtm(Long occurDtm) {
		this.occurDtm = occurDtm;
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
	public Long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 변경일시
	 * 
	 * @param chgDtm 변경일시
	 */
	public void setChgDtm(Long chgDtm) {
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

	/**
	 * 해제여부
	 * 
	 * @return 해제여부
	 */
	public String isRlseYn() {
		return rlseYn;
	}

	/**
	 * 해제여부
	 * 
	 * @param rlseYn 해제여부
	 */
	public void setRlseYn(String rlseYn) {
		this.rlseYn = rlseYn;
	}

	/**
	 * 해제일시
	 * 
	 * @return 해제일시
	 */
	public Long getRlseDtm() {
		return rlseDtm;
	}

	/**
	 * 해제일시
	 * 
	 * @param rlseDtm 해제일시
	 */
	public void setRlseDtm(Long rlseDtm) {
		this.rlseDtm = rlseDtm;
	}

	/**
	 * 해제사용자번호
	 * 
	 * @return 해제사용자번호
	 */
	public Integer getRlseUserNo() {
		return rlseUserNo;
	}

	/**
	 * 해제사용자번호
	 * 
	 * @param rlseUserNo 해제사용자번호
	 */
	public void setRlseUserNo(Integer rlseUserNo) {
		this.rlseUserNo = rlseUserNo;
	}

	/**
	 * 알람해제원인코드
	 * 
	 * @return 알람해제원인코드
	 */
	public String getAlarmRlseRsnCd() {
		return alarmRlseRsnCd;
	}

	/**
	 * 알람해제원인코드
	 * 
	 * @param alarmRlseRsnCd 알람해제원인코드
	 */
	public void setAlarmRlseRsnCd(String alarmRlseRsnCd) {
		this.alarmRlseRsnCd = alarmRlseRsnCd;
	}

	/**
	 * 알람해제원인명
	 * 
	 * @return 알람해제원인명
	 */
	public String getAlarmRlseRsnName() {
		return alarmRlseRsnName;
	}

	/**
	 * 알람해제원인명
	 * 
	 * @param alarmRlseRsnName 알람해제원인명
	 */
	public void setAlarmRlseRsnName(String alarmRlseRsnName) {
		this.alarmRlseRsnName = alarmRlseRsnName;
	}

	/**
	 * 해제메모
	 * 
	 * @return 해제메모
	 */
	public String getRlseMemo() {
		return rlseMemo;
	}

	/**
	 * 해제메모
	 * 
	 * @param rlseMemo 해제메모
	 */
	public void setRlseMemo(String rlseMemo) {
		this.rlseMemo = rlseMemo;
	}

	/**
	 * 발생횟수
	 * 
	 * @return 발생횟수
	 */
	public Integer getOccurCnt() {
		return occurCnt;
	}

	/**
	 * 발생횟수
	 * 
	 * @param occurCnt 발생횟수
	 */
	public void setOccurCnt(Integer occurCnt) {
		this.occurCnt = occurCnt;
	}
}

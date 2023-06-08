package fxms.bas.impl.dbo.all;


import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.06.08 13:31
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_AL_ALARM_CUR", comment = "경보알람현재테이블")
@FxIndex(name = "FX_AL_ALARM_CUR__PK", type = INDEX_TYPE.PK, columns = {"ALARM_NO"})
@FxIndex(name = "FX_AL_ALARM_CUR__UK_KEY", type = INDEX_TYPE.UK, columns = {"ALARM_KEY"})
@FxIndex(name = "FX_AL_ALARM_CUR__KEY_MO", type = INDEX_TYPE.KEY, columns = {"MO_NO"})
@FxIndex(name = "FX_AL_ALARM_CUR__FK1", type = INDEX_TYPE.FK, columns = {"MO_NO"}, fkTable = "FX_MO", fkColumn = "MO_NO")
@FxIndex(name = "FX_AL_ALARM_CUR__FK2", type = INDEX_TYPE.FK, columns = {"ALARM_NO"}, fkTable = "FX_AL_ALARM_HST", fkColumn = "ALARM_NO")
public class FX_AL_ALARM_CUR  {

public FX_AL_ALARM_CUR() {
 }

public static final String FX_SEQ_ALARMNO  = "FX_SEQ_ALARMNO"; 
    @FxColumn(name = "ALARM_NO", size = 19, comment = "알람번호", sequence = "FX_SEQ_ALARMNO")
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

    @FxColumn(name = "ALARM_LEVEL", size = 1, nullable = true, comment = "알람등급")
    private Integer alarmLevel;

    @FxColumn(name = "ALARM_MSG", size = 1000, nullable = true, comment = "알람메세지")
    private String alarmMsg;

    @FxColumn(name = "ALARM_MEMO", size = 1000, nullable = true, comment = "알람메모")
    private String alarmMemo;

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
    private Integer occurCnt = 0;

    @FxColumn(name = "ACK_DTM", size = 14, nullable = true, comment = "확인일시")
    private Long ackDtm;

    @FxColumn(name = "ACK_USER_NO", size = 9, nullable = true, comment = "확인사용자번호")
    private Integer ackUserNo;

    @FxColumn(name = "RSN_REG_DTM", size = 14, nullable = true, comment = "원인등록일시")
    private Long rsnRegDtm;

    @FxColumn(name = "RSN_REG_USER_NO", size = 9, nullable = true, comment = "원인등록사용자번호")
    private Integer rsnRegUserNo;

    @FxColumn(name = "RSN_NO", size = 9, nullable = true, comment = "원인번호", defValue = "0")
    private Integer rsnNo = 0;

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

/**
 * @return 알람번호
*/
public Long getAlarmNo() { 
    return alarmNo;
}
/**
 * @param alarmNo 알람번호
*/
public void setAlarmNo(Long alarmNo) { 
    this.alarmNo = alarmNo;
}
/**
 * @return 알람키
*/
public String getAlarmKey() { 
    return alarmKey;
}
/**
 * @param alarmKey 알람키
*/
public void setAlarmKey(String alarmKey) { 
    this.alarmKey = alarmKey;
}
/**
 * @return 알람조건번호
*/
public Integer getAlarmCfgNo() { 
    return alarmCfgNo;
}
/**
 * @param alarmCfgNo 알람조건번호
*/
public void setAlarmCfgNo(Integer alarmCfgNo) { 
    this.alarmCfgNo = alarmCfgNo;
}
/**
 * @return 경보코드번호
*/
public Integer getAlcdNo() { 
    return alcdNo;
}
/**
 * @param alcdNo 경보코드번호
*/
public void setAlcdNo(Integer alcdNo) { 
    this.alcdNo = alcdNo;
}
/**
 * @return 경보코드명
*/
public String getAlcdName() { 
    return alcdName;
}
/**
 * @param alcdName 경보코드명
*/
public void setAlcdName(String alcdName) { 
    this.alcdName = alcdName;
}
/**
 * @return 알람구분코드
*/
public String getAlarmClCd() { 
    return alarmClCd;
}
/**
 * @param alarmClCd 알람구분코드
*/
public void setAlarmClCd(String alarmClCd) { 
    this.alarmClCd = alarmClCd;
}
/**
 * @return 알람등급
*/
public Integer getAlarmLevel() { 
    return alarmLevel;
}
/**
 * @param alarmLevel 알람등급
*/
public void setAlarmLevel(Integer alarmLevel) { 
    this.alarmLevel = alarmLevel;
}
/**
 * @return 알람메세지
*/
public String getAlarmMsg() { 
    return alarmMsg;
}
/**
 * @param alarmMsg 알람메세지
*/
public void setAlarmMsg(String alarmMsg) { 
    this.alarmMsg = alarmMsg;
}
/**
 * @return 알람메모
*/
public String getAlarmMemo() { 
    return alarmMemo;
}
/**
 * @param alarmMemo 알람메모
*/
public void setAlarmMemo(String alarmMemo) { 
    this.alarmMemo = alarmMemo;
}
/**
 * @return 성능ID
*/
public String getPsId() { 
    return psId;
}
/**
 * @param psId 성능ID
*/
public void setPsId(String psId) { 
    this.psId = psId;
}
/**
 * @return 성능값
*/
public Double getPsVal() { 
    return psVal;
}
/**
 * @param psVal 성능값
*/
public void setPsVal(Double psVal) { 
    this.psVal = psVal;
}
/**
 * @return 비교값
*/
public Double getCmprVal() { 
    return cmprVal;
}
/**
 * @param cmprVal 비교값
*/
public void setCmprVal(Double cmprVal) { 
    this.cmprVal = cmprVal;
}
/**
 * @return 상태수집일시
*/
public Long getPsDtm() { 
    return psDtm;
}
/**
 * @param psDtm 상태수집일시
*/
public void setPsDtm(Long psDtm) { 
    this.psDtm = psDtm;
}
/**
 * @return 상태값명
*/
public String getPsName() { 
    return psName;
}
/**
 * @param psName 상태값명
*/
public void setPsName(String psName) { 
    this.psName = psName;
}
/**
 * @return MO번호
*/
public Long getMoNo() { 
    return moNo;
}
/**
 * @param moNo MO번호
*/
public void setMoNo(Long moNo) { 
    this.moNo = moNo;
}
/**
 * @return MO명
*/
public String getMoName() { 
    return moName;
}
/**
 * @param moName MO명
*/
public void setMoName(String moName) { 
    this.moName = moName;
}
/**
 * @return MO표시명
*/
public String getMoDispName() { 
    return moDispName;
}
/**
 * @param moDispName MO표시명
*/
public void setMoDispName(String moDispName) { 
    this.moDispName = moDispName;
}
/**
 * @return MO인스턴스
*/
public String getMoInstance() { 
    return moInstance;
}
/**
 * @param moInstance MO인스턴스
*/
public void setMoInstance(String moInstance) { 
    this.moInstance = moInstance;
}
/**
 * @return MO클래스 
*/
public String getMoClass() { 
    return moClass;
}
/**
 * @param moClass MO클래스 
*/
public void setMoClass(String moClass) { 
    this.moClass = moClass;
}
/**
 * @return 상위MO번호
*/
public Long getUpperMoNo() { 
    return upperMoNo;
}
/**
 * @param upperMoNo 상위MO번호
*/
public void setUpperMoNo(Long upperMoNo) { 
    this.upperMoNo = upperMoNo;
}
/**
 * @return 상위MO명
*/
public String getUpperMoName() { 
    return upperMoName;
}
/**
 * @param upperMoName 상위MO명
*/
public void setUpperMoName(String upperMoName) { 
    this.upperMoName = upperMoName;
}
/**
 * @return 상위MO표시명
*/
public String getUpperMoDispName() { 
    return upperMoDispName;
}
/**
 * @param upperMoDispName 상위MO표시명
*/
public void setUpperMoDispName(String upperMoDispName) { 
    this.upperMoDispName = upperMoDispName;
}
/**
 * @return 설치위치번호
*/
public Integer getInloNo() { 
    return inloNo;
}
/**
 * @param inloNo 설치위치번호
*/
public void setInloNo(Integer inloNo) { 
    this.inloNo = inloNo;
}
/**
 * @return 설치위치명
*/
public String getInloName() { 
    return inloName;
}
/**
 * @param inloName 설치위치명
*/
public void setInloName(String inloName) { 
    this.inloName = inloName;
}
/**
 * @return 모델번호
*/
public Integer getModelNo() { 
    return modelNo;
}
/**
 * @param modelNo 모델번호
*/
public void setModelNo(Integer modelNo) { 
    this.modelNo = modelNo;
}
/**
 * @return 모델명
*/
public String getModelName() { 
    return modelName;
}
/**
 * @param modelName 모델명
*/
public void setModelName(String modelName) { 
    this.modelName = modelName;
}
/**
 * @return 설비번호
*/
public Long getFacNo() { 
    return facNo;
}
/**
 * @param facNo 설비번호
*/
public void setFacNo(Long facNo) { 
    this.facNo = facNo;
}
/**
 * @return 설비명
*/
public String getFacName() { 
    return facName;
}
/**
 * @param facName 설비명
*/
public void setFacName(String facName) { 
    this.facName = facName;
}
/**
 * @return 등록일시
*/
public Long getRegDtm() { 
    return regDtm;
}
/**
 * @param regDtm 등록일시
*/
public void setRegDtm(Long regDtm) { 
    this.regDtm = regDtm;
}
/**
 * @return 발생일시
*/
public Long getOccurDtm() { 
    return occurDtm;
}
/**
 * @param occurDtm 발생일시
*/
public void setOccurDtm(Long occurDtm) { 
    this.occurDtm = occurDtm;
}
/**
 * @return 발생횟수
*/
public Integer getOccurCnt() { 
    return occurCnt;
}
/**
 * @param occurCnt 발생횟수
*/
public void setOccurCnt(Integer occurCnt) { 
    this.occurCnt = occurCnt;
}
/**
 * @return 확인일시
*/
public Long getAckDtm() { 
    return ackDtm;
}
/**
 * @param ackDtm 확인일시
*/
public void setAckDtm(Long ackDtm) { 
    this.ackDtm = ackDtm;
}
/**
 * @return 확인사용자번호
*/
public Integer getAckUserNo() { 
    return ackUserNo;
}
/**
 * @param ackUserNo 확인사용자번호
*/
public void setAckUserNo(Integer ackUserNo) { 
    this.ackUserNo = ackUserNo;
}
/**
 * @return 원인등록일시
*/
public Long getRsnRegDtm() { 
    return rsnRegDtm;
}
/**
 * @param rsnRegDtm 원인등록일시
*/
public void setRsnRegDtm(Long rsnRegDtm) { 
    this.rsnRegDtm = rsnRegDtm;
}
/**
 * @return 원인등록사용자번호
*/
public Integer getRsnRegUserNo() { 
    return rsnRegUserNo;
}
/**
 * @param rsnRegUserNo 원인등록사용자번호
*/
public void setRsnRegUserNo(Integer rsnRegUserNo) { 
    this.rsnRegUserNo = rsnRegUserNo;
}
/**
 * @return 원인번호
*/
public Integer getRsnNo() { 
    return rsnNo;
}
/**
 * @param rsnNo 원인번호
*/
public void setRsnNo(Integer rsnNo) { 
    this.rsnNo = rsnNo;
}
/**
 * @return 원인명
*/
public String getRsnName() { 
    return rsnName;
}
/**
 * @param rsnName 원인명
*/
public void setRsnName(String rsnName) { 
    this.rsnName = rsnName;
}
/**
 * @return 원인메모
*/
public String getRsnMemo() { 
    return rsnMemo;
}
/**
 * @param rsnMemo 원인메모
*/
public void setRsnMemo(String rsnMemo) { 
    this.rsnMemo = rsnMemo;
}
/**
 * @return IP주소
*/
public String getIpAddr() { 
    return ipAddr;
}
/**
 * @param ipAddr IP주소
*/
public void setIpAddr(String ipAddr) { 
    this.ipAddr = ipAddr;
}
/**
 * @return 변경일시
*/
public Long getChgDtm() { 
    return chgDtm;
}
/**
 * @param chgDtm 변경일시
*/
public void setChgDtm(Long chgDtm) { 
    this.chgDtm = chgDtm;
}
/**
 * @return 후속조치코드
*/
public String getFpactCd() { 
    return fpactCd;
}
/**
 * @param fpactCd 후속조치코드
*/
public void setFpactCd(String fpactCd) { 
    this.fpactCd = fpactCd;
}
/**
 * @return 후속조치명
*/
public String getFpactName() { 
    return fpactName;
}
/**
 * @param fpactName 후속조치명
*/
public void setFpactName(String fpactName) { 
    this.fpactName = fpactName;
}
}

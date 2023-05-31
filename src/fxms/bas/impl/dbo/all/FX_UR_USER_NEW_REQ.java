package fxms.bas.impl.dbo.all;


import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.05.09 13:54
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_UR_USER_NEW_REQ", comment = "사용자신규요청테이블")
@FxIndex(name = "FX_UR_USER_NEW_REQ__PK", type = INDEX_TYPE.PK, columns = {"APPLY_DTM","APPLY_USER_ID"})
@FxIndex(name = "FX_UR_USER_NEW_REQ__KEY2", type = INDEX_TYPE.KEY, columns = {"APPLY_USER_ID"})
public class FX_UR_USER_NEW_REQ  {

public FX_UR_USER_NEW_REQ() {
 }

    @FxColumn(name = "APPLY_DTM", size = 14, comment = "신청일시")
    private Long applyDtm;

    @FxColumn(name = "APPLY_USER_ID", size = 100, comment = "신청사용자ID")
    private String applyUserId;

    @FxColumn(name = "APPLY_USER_NAME", size = 50, comment = "신청사용자명")
    private String applyUserName;

    @FxColumn(name = "APPLY_TEL_NO", size = 50, comment = "신청전화번호")
    private String applyTelNo;

    @FxColumn(name = "APPLY_INLO_NAME", size = 100, comment = "신청설치위치명")
    private String applyInloName;

    @FxColumn(name = "APPLY_INLO_NO", size = 9, nullable = true, comment = "신청설치위치번호", defValue = "0")
    private Integer applyInloNo = 0;

    @FxColumn(name = "APPLY_MEMO", size = 100, nullable = true, comment = "신청메모")
    private String applyMemo;

    @FxColumn(name = "USE_STRT_DATE", size = 8, nullable = true, comment = "사용시작일자", defValue = "20000101")
    private Integer useStrtDate = 20000101;

    @FxColumn(name = "USE_END_DATE", size = 8, nullable = true, comment = "사용종료일자", defValue = "39991231")
    private Integer useEndDate = 39991231;

    @FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접근NETWORK")
    private String accsNetwork;

    @FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접근NETMASK")
    private String accsNetmask;

    @FxColumn(name = "PROC_USER_NO", size = 9, nullable = true, comment = "처리사용자번호")
    private Integer procUserNo;

    @FxColumn(name = "PROC_DTM", size = 14, nullable = true, comment = "처리일시")
    private Long procDtm;

    @FxColumn(name = "PROC_RSN", size = 200, nullable = true, comment = "처리사유")
    private String procRsn;

    @FxColumn(name = "NEW_USER_REG_ST_CD", size = 1, nullable = true, comment = "신규사용자등록상태코드", defValue = "S")
    private String newUserRegStCd = "S";

    @FxColumn(name = "APPLY_USER_PWD", size = 255, comment = "신청사용자암호")
    private String applyUserPwd;

    @FxColumn(name = "APPLY_USER_MAIL", size = 200, comment = "신청사용자메일")
    private String applyUserMail;

    @FxColumn(name = "CERTF_NUM", size = 200, nullable = true, comment = "인증번호")
    private String certfNum;

    @FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
    private Integer regUserNo = 0;

    @FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
    private Long regDtm;

    @FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
    private Integer chgUserNo = 0;

    @FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
    private Long chgDtm;

/**
 * @return 신청일시
*/
public Long getApplyDtm() { 
    return applyDtm;
}
/**
 * @param applyDtm 신청일시
*/
public void setApplyDtm(Long applyDtm) { 
    this.applyDtm = applyDtm;
}
/**
 * @return 신청사용자ID
*/
public String getApplyUserId() { 
    return applyUserId;
}
/**
 * @param applyUserId 신청사용자ID
*/
public void setApplyUserId(String applyUserId) { 
    this.applyUserId = applyUserId;
}
/**
 * @return 신청사용자명
*/
public String getApplyUserName() { 
    return applyUserName;
}
/**
 * @param applyUserName 신청사용자명
*/
public void setApplyUserName(String applyUserName) { 
    this.applyUserName = applyUserName;
}
/**
 * @return 신청전화번호
*/
public String getApplyTelNo() { 
    return applyTelNo;
}
/**
 * @param applyTelNo 신청전화번호
*/
public void setApplyTelNo(String applyTelNo) { 
    this.applyTelNo = applyTelNo;
}
/**
 * @return 신청설치위치명
*/
public String getApplyInloName() { 
    return applyInloName;
}
/**
 * @param applyInloName 신청설치위치명
*/
public void setApplyInloName(String applyInloName) { 
    this.applyInloName = applyInloName;
}
/**
 * @return 신청설치위치번호
*/
public Integer getApplyInloNo() { 
    return applyInloNo;
}
/**
 * @param applyInloNo 신청설치위치번호
*/
public void setApplyInloNo(Integer applyInloNo) { 
    this.applyInloNo = applyInloNo;
}
/**
 * @return 신청메모
*/
public String getApplyMemo() { 
    return applyMemo;
}
/**
 * @param applyMemo 신청메모
*/
public void setApplyMemo(String applyMemo) { 
    this.applyMemo = applyMemo;
}
/**
 * @return 사용시작일자
*/
public Integer getUseStrtDate() { 
    return useStrtDate;
}
/**
 * @param useStrtDate 사용시작일자
*/
public void setUseStrtDate(Integer useStrtDate) { 
    this.useStrtDate = useStrtDate;
}
/**
 * @return 사용종료일자
*/
public Integer getUseEndDate() { 
    return useEndDate;
}
/**
 * @param useEndDate 사용종료일자
*/
public void setUseEndDate(Integer useEndDate) { 
    this.useEndDate = useEndDate;
}
/**
 * @return 접근NETWORK
*/
public String getAccsNetwork() { 
    return accsNetwork;
}
/**
 * @param accsNetwork 접근NETWORK
*/
public void setAccsNetwork(String accsNetwork) { 
    this.accsNetwork = accsNetwork;
}
/**
 * @return 접근NETMASK
*/
public String getAccsNetmask() { 
    return accsNetmask;
}
/**
 * @param accsNetmask 접근NETMASK
*/
public void setAccsNetmask(String accsNetmask) { 
    this.accsNetmask = accsNetmask;
}
/**
 * @return 처리사용자번호
*/
public Integer getProcUserNo() { 
    return procUserNo;
}
/**
 * @param procUserNo 처리사용자번호
*/
public void setProcUserNo(Integer procUserNo) { 
    this.procUserNo = procUserNo;
}
/**
 * @return 처리일시
*/
public Long getProcDtm() { 
    return procDtm;
}
/**
 * @param procDtm 처리일시
*/
public void setProcDtm(Long procDtm) { 
    this.procDtm = procDtm;
}
/**
 * @return 처리사유
*/
public String getProcRsn() { 
    return procRsn;
}
/**
 * @param procRsn 처리사유
*/
public void setProcRsn(String procRsn) { 
    this.procRsn = procRsn;
}
/**
 * @return 신규사용자등록상태코드
*/
public String getNewUserRegStCd() { 
    return newUserRegStCd;
}
/**
 * @param newUserRegStCd 신규사용자등록상태코드
*/
public void setNewUserRegStCd(String newUserRegStCd) { 
    this.newUserRegStCd = newUserRegStCd;
}
/**
 * @return 신청사용자암호
*/
public String getApplyUserPwd() { 
    return applyUserPwd;
}
/**
 * @param applyUserPwd 신청사용자암호
*/
public void setApplyUserPwd(String applyUserPwd) { 
    this.applyUserPwd = applyUserPwd;
}
/**
 * @return 신청사용자메일
*/
public String getApplyUserMail() { 
    return applyUserMail;
}
/**
 * @param applyUserMail 신청사용자메일
*/
public void setApplyUserMail(String applyUserMail) { 
    this.applyUserMail = applyUserMail;
}
/**
 * @return 인증번호
*/
public String getCertfNum() { 
    return certfNum;
}
/**
 * @param certfNum 인증번호
*/
public void setCertfNum(String certfNum) { 
    this.certfNum = certfNum;
}
/**
 * @return 등록사용자번호
*/
public Integer getRegUserNo() { 
    return regUserNo;
}
/**
 * @param regUserNo 등록사용자번호
*/
public void setRegUserNo(Integer regUserNo) { 
    this.regUserNo = regUserNo;
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
 * @return 수정사용자번호
*/
public Integer getChgUserNo() { 
    return chgUserNo;
}
/**
 * @param chgUserNo 수정사용자번호
*/
public void setChgUserNo(Integer chgUserNo) { 
    this.chgUserNo = chgUserNo;
}
/**
 * @return 수정일시
*/
public Long getChgDtm() { 
    return chgDtm;
}
/**
 * @param chgDtm 수정일시
*/
public void setChgDtm(Long chgDtm) { 
    this.chgDtm = chgDtm;
}
}

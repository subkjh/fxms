package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.02.20 16:00
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_EV_LOG", comment = "이벤트로그테이블")
@FxIndex(name = "FX_EV_LOG__PK", type = INDEX_TYPE.PK, columns = {"EVT_NO"})
@FxIndex(name = "FX_EV_LOG__KEY1", type = INDEX_TYPE.KEY, columns = {"RECV_DTM"})
public class FX_EV_LOG implements Serializable {

public FX_EV_LOG() {
 }

public static final String FX_SEQ_EVTNO  = "FX_SEQ_EVTNO"; 
@FxColumn(name = "EVT_NO", size = 19, comment = "이벤트번호", sequence = "FX_SEQ_EVTNO")
private Long evtNo;


@FxColumn(name = "FXSVC_NAME", size = 50, comment = "FX서비스명")
private String fxsvcName;


@FxColumn(name = "EVT_NAME", size = 50, comment = "이벤트명")
private String evtName;


@FxColumn(name = "RECV_DTM", size = 14, nullable = true, comment = "수신일시")
private Long recvDtm;


@FxColumn(name = "PROC_DTM", size = 14, nullable = true, comment = "처리일시")
private Long procDtm;


@FxColumn(name = "PROC_CNT", size = 9, nullable = true, comment = "처리건수")
private Integer procCnt;


@FxColumn(name = "OK_YN", size = 1, comment = "성공여부", defValue = "Y")
private String okYn = "Y";


@FxColumn(name = "ERR_JSON", size = 2000, nullable = true, comment = "오류JSON")
private String errJson;


/**
* 이벤트번호
* @return 이벤트번호
*/
public Long getEvtNo() {
return evtNo;
}
/**
* 이벤트번호
*@param evtNo 이벤트번호
*/
public void setEvtNo(Long evtNo) {
	this.evtNo = evtNo;
}
/**
* FX서비스명
* @return FX서비스명
*/
public String getFxsvcName() {
return fxsvcName;
}
/**
* FX서비스명
*@param fxsvcName FX서비스명
*/
public void setFxsvcName(String fxsvcName) {
	this.fxsvcName = fxsvcName;
}
/**
* 이벤트명
* @return 이벤트명
*/
public String getEvtName() {
return evtName;
}
/**
* 이벤트명
*@param evtName 이벤트명
*/
public void setEvtName(String evtName) {
	this.evtName = evtName;
}
/**
* 수신일시
* @return 수신일시
*/
public Long getRecvDtm() {
return recvDtm;
}
/**
* 수신일시
*@param recvDtm 수신일시
*/
public void setRecvDtm(Long recvDtm) {
	this.recvDtm = recvDtm;
}
/**
* 처리일시
* @return 처리일시
*/
public Long getProcDtm() {
return procDtm;
}
/**
* 처리일시
*@param procDtm 처리일시
*/
public void setProcDtm(Long procDtm) {
	this.procDtm = procDtm;
}
/**
* 처리건수
* @return 처리건수
*/
public Integer getProcCnt() {
return procCnt;
}
/**
* 처리건수
*@param procCnt 처리건수
*/
public void setProcCnt(Integer procCnt) {
	this.procCnt = procCnt;
}
/**
* 성공여부
* @return 성공여부
*/
public String isOkYn() {
return okYn;
}
/**
* 성공여부
*@param okYn 성공여부
*/
public void setOkYn(String okYn) {
	this.okYn = okYn;
}
/**
* 오류JSON
* @return 오류JSON
*/
public String getErrJson() {
return errJson;
}
/**
* 오류JSON
*@param errJson 오류JSON
*/
public void setErrJson(String errJson) {
	this.errJson = errJson;
}
}

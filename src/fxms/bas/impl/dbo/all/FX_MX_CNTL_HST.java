package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.02 18:01
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_MX_CNTL_HST", comment = "MO제어이력테이블")
@FxIndex(name = "FX_MX_CNTL_HST__PK", type = INDEX_TYPE.PK, columns = {"MO_NO", "CNTL_ID", "RUN_SEQ", "CNTL_DTM"})
public class FX_MX_CNTL_HST implements Serializable {

public FX_MX_CNTL_HST() {
 }

@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
private long moNo;


@FxColumn(name = "CNTL_ID", size = 20, comment = "제어ID")
private String cntlId;


@FxColumn(name = "RUN_SEQ", size = 5, comment = "실행순서")
private int runSeq;


@FxColumn(name = "CNTL_DTM", size = 14, comment = "제어일시")
private long cntlDtm;


@FxColumn(name = "RUN_CMD", size = 200, comment = "실행명령")
private String runCmd;


@FxColumn(name = "RES_TEXT", size = 200, nullable = true, comment = "응답문구")
private String resText;


@FxColumn(name = "OK_YN", size = 1, nullable = true, comment = "성공여부")
private boolean okYn;


/**
* MO번호
* @return MO번호
*/
public long getMoNo() {
return moNo;
}
/**
* MO번호
*@param moNo MO번호
*/
public void setMoNo(long moNo) {
	this.moNo = moNo;
}
/**
* 제어ID
* @return 제어ID
*/
public String getCntlId() {
return cntlId;
}
/**
* 제어ID
*@param cntlId 제어ID
*/
public void setCntlId(String cntlId) {
	this.cntlId = cntlId;
}
/**
* 실행순서
* @return 실행순서
*/
public int getRunSeq() {
return runSeq;
}
/**
* 실행순서
*@param runSeq 실행순서
*/
public void setRunSeq(int runSeq) {
	this.runSeq = runSeq;
}
/**
* 제어일시
* @return 제어일시
*/
public long getCntlDtm() {
return cntlDtm;
}
/**
* 제어일시
*@param cntlDtm 제어일시
*/
public void setCntlDtm(long cntlDtm) {
	this.cntlDtm = cntlDtm;
}
/**
* 실행명령
* @return 실행명령
*/
public String getRunCmd() {
return runCmd;
}
/**
* 실행명령
*@param runCmd 실행명령
*/
public void setRunCmd(String runCmd) {
	this.runCmd = runCmd;
}
/**
* 응답문구
* @return 응답문구
*/
public String getResText() {
return resText;
}
/**
* 응답문구
*@param resText 응답문구
*/
public void setResText(String resText) {
	this.resText = resText;
}
/**
* 성공여부
* @return 성공여부
*/
public boolean isOkYn() {
return okYn;
}
/**
* 성공여부
*@param okYn 성공여부
*/
public void setOkYn(boolean okYn) {
	this.okYn = okYn;
}
}

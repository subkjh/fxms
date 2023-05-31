package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
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


@FxTable(name = "FX_MX_CNTL_DEF_SEQ", comment = "MO제어정의순서테이블")
@FxIndex(name = "FX_MX_CNTL_DEF_SEQ__PK", type = INDEX_TYPE.PK, columns = {"CNTL_ID", "RUN_SEQ"})
public class FX_MX_CNTL_DEF_SEQ implements Serializable {

public FX_MX_CNTL_DEF_SEQ() {
 }

@FxColumn(name = "CNTL_ID", size = 20, comment = "제어ID")
private String cntlId;


@FxColumn(name = "RUN_SEQ", size = 5, comment = "실행순서")
private int runSeq;


@FxColumn(name = "RUN_CMD", size = 200, comment = "실행명령")
private String runCmd;


@FxColumn(name = "RUN_TEXT", size = 200, nullable = true, comment = "실행문구")
private String runText;


@FxColumn(name = "OK_RES_TEXT", size = 200, nullable = true, comment = "성공응답문구")
private String okResText;


@FxColumn(name = "ERR_RES_TEXT", size = 200, nullable = true, comment = "오류응답문구")
private String errResText;


@FxColumn(name = "OK_NEXT_RUN_SEQ", size = 5, comment = "성공다음실행순서")
private int okNextRunSeq;


@FxColumn(name = "ERR_NEXT_RUN_SEQ", size = 5, comment = "오류다음실행순서")
private int errNextRunSeq;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


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
* 실행문구
* @return 실행문구
*/
public String getRunText() {
return runText;
}
/**
* 실행문구
*@param runText 실행문구
*/
public void setRunText(String runText) {
	this.runText = runText;
}
/**
* 성공응답문구
* @return 성공응답문구
*/
public String getOkResText() {
return okResText;
}
/**
* 성공응답문구
*@param okResText 성공응답문구
*/
public void setOkResText(String okResText) {
	this.okResText = okResText;
}
/**
* 오류응답문구
* @return 오류응답문구
*/
public String getErrResText() {
return errResText;
}
/**
* 오류응답문구
*@param errResText 오류응답문구
*/
public void setErrResText(String errResText) {
	this.errResText = errResText;
}
/**
* 성공다음실행순서
* @return 성공다음실행순서
*/
public int getOkNextRunSeq() {
return okNextRunSeq;
}
/**
* 성공다음실행순서
*@param okNextRunSeq 성공다음실행순서
*/
public void setOkNextRunSeq(int okNextRunSeq) {
	this.okNextRunSeq = okNextRunSeq;
}
/**
* 오류다음실행순서
* @return 오류다음실행순서
*/
public int getErrNextRunSeq() {
return errNextRunSeq;
}
/**
* 오류다음실행순서
*@param errNextRunSeq 오류다음실행순서
*/
public void setErrNextRunSeq(int errNextRunSeq) {
	this.errNextRunSeq = errNextRunSeq;
}
/**
* 등록사용자번호
* @return 등록사용자번호
*/
public int getRegUserNo() {
return regUserNo;
}
/**
* 등록사용자번호
*@param regUserNo 등록사용자번호
*/
public void setRegUserNo(int regUserNo) {
	this.regUserNo = regUserNo;
}
/**
* 등록일시
* @return 등록일시
*/
public long getRegDtm() {
return regDtm;
}
/**
* 등록일시
*@param regDtm 등록일시
*/
public void setRegDtm(long regDtm) {
	this.regDtm = regDtm;
}
/**
* 수정사용자번호
* @return 수정사용자번호
*/
public int getChgUserNo() {
return chgUserNo;
}
/**
* 수정사용자번호
*@param chgUserNo 수정사용자번호
*/
public void setChgUserNo(int chgUserNo) {
	this.chgUserNo = chgUserNo;
}
/**
* 수정일시
* @return 수정일시
*/
public long getChgDtm() {
return chgDtm;
}
/**
* 수정일시
*@param chgDtm 수정일시
*/
public void setChgDtm(long chgDtm) {
	this.chgDtm = chgDtm;
}
}

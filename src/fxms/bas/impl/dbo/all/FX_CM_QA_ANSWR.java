package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.25 14:42
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_CM_QA_ANSWR", comment = "소통QA답변테이블")
@FxIndex(name = "FX_CM_QA_ANSWR__PK", type = INDEX_TYPE.PK, columns = {"QUEST_NO", "ANSWR_NO"})
public class FX_CM_QA_ANSWR implements Serializable {

public FX_CM_QA_ANSWR() {
 }

@FxColumn(name = "QUEST_NO", size = 9, comment = "질문번호")
private int questNo;


public static final String FX_SEQ_ANSWRNO  = "FX_SEQ_ANSWRNO"; 
@FxColumn(name = "ANSWR_NO", size = 9, comment = "답변번호", sequence = "FX_SEQ_ANSWRNO")
private int answrNo;


@FxColumn(name = "ANSWR_CNTS", size = 4000, comment = "답변내용")
private String answrCnts;


@FxColumn(name = "ANSWR_USER_NO", size = 9, nullable = true, comment = "답변사용자번호")
private int answrUserNo;


@FxColumn(name = "ANSWR_NAME", size = 50, comment = "답변자명")
private String answrName;


@FxColumn(name = "ANSWR_DATE", size = 8, comment = "답변일자")
private int answrDate;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 질문번호
* @return 질문번호
*/
public int getQuestNo() {
return questNo;
}
/**
* 질문번호
*@param questNo 질문번호
*/
public void setQuestNo(int questNo) {
	this.questNo = questNo;
}
/**
* 답변번호
* @return 답변번호
*/
public int getAnswrNo() {
return answrNo;
}
/**
* 답변번호
*@param answrNo 답변번호
*/
public void setAnswrNo(int answrNo) {
	this.answrNo = answrNo;
}
/**
* 답변내용
* @return 답변내용
*/
public String getAnswrCnts() {
return answrCnts;
}
/**
* 답변내용
*@param answrCnts 답변내용
*/
public void setAnswrCnts(String answrCnts) {
	this.answrCnts = answrCnts;
}
/**
* 답변사용자번호
* @return 답변사용자번호
*/
public int getAnswrUserNo() {
return answrUserNo;
}
/**
* 답변사용자번호
*@param answrUserNo 답변사용자번호
*/
public void setAnswrUserNo(int answrUserNo) {
	this.answrUserNo = answrUserNo;
}
/**
* 답변자명
* @return 답변자명
*/
public String getAnswrName() {
return answrName;
}
/**
* 답변자명
*@param answrName 답변자명
*/
public void setAnswrName(String answrName) {
	this.answrName = answrName;
}
/**
* 답변일자
* @return 답변일자
*/
public int getAnswrDate() {
return answrDate;
}
/**
* 답변일자
*@param answrDate 답변일자
*/
public void setAnswrDate(int answrDate) {
	this.answrDate = answrDate;
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

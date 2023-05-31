package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.24 15:47
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_CM_QA_FILE", comment = "소통QA파일테이블")
@FxIndex(name = "FX_CM_QUEST_FILE__PK", type = INDEX_TYPE.PK, columns = {"QUEST_NO", "ANSWR_NO", "FILE_NAME"})
public class FX_CM_QA_FILE implements Serializable {

public FX_CM_QA_FILE() {
 }

@FxColumn(name = "QUEST_NO", size = 9, comment = "질문번호")
private int questNo;


@FxColumn(name = "ANSWR_NO", size = 9, comment = "답변번호", defValue = "0")
private int answrNo = 0;


@FxColumn(name = "FILE_NAME", size = 100, comment = "파일명")
private String fileName;


@FxColumn(name = "FILE_PATH", size = 200, comment = "파일위치")
private String filePath;


@FxColumn(name = "FILE_SIZE", size = 10, nullable = true, comment = "파일크기")
private long fileSize;


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
* 파일명
* @return 파일명
*/
public String getFileName() {
return fileName;
}
/**
* 파일명
*@param fileName 파일명
*/
public void setFileName(String fileName) {
	this.fileName = fileName;
}
/**
* 파일위치
* @return 파일위치
*/
public String getFilePath() {
return filePath;
}
/**
* 파일위치
*@param filePath 파일위치
*/
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
/**
* 파일크기
* @return 파일크기
*/
public long getFileSize() {
return fileSize;
}
/**
* 파일크기
*@param fileSize 파일크기
*/
public void setFileSize(long fileSize) {
	this.fileSize = fileSize;
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

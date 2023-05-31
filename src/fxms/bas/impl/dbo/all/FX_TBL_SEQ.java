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


@FxTable(name = "FX_TBL_SEQ", comment = "테이블시퀀스테이블")
@FxIndex(name = "FX_TBL_SEQ__PK", type = INDEX_TYPE.PK, columns = {"SEQ_NAME"})
public class FX_TBL_SEQ implements Serializable {

public FX_TBL_SEQ() {
 }

@FxColumn(name = "SEQ_NAME", size = 20, comment = "시퀀스명")
private String seqName;


@FxColumn(name = "INC_VAL", size = 5, comment = "증가값")
private int incVal;


@FxColumn(name = "MIN_VAL", size = 19, comment = "최소값")
private long minVal;


@FxColumn(name = "MAX_VAL", size = 19, comment = "최대값")
private long maxVal;


@FxColumn(name = "NEXT_VAL", size = 19, comment = "다음값")
private long nextVal;


@FxColumn(name = "CYCLE_YN", size = 1, comment = "순환여부")
private boolean cycleYn;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 시퀀스명
* @return 시퀀스명
*/
public String getSeqName() {
return seqName;
}
/**
* 시퀀스명
*@param seqName 시퀀스명
*/
public void setSeqName(String seqName) {
	this.seqName = seqName;
}
/**
* 증가값
* @return 증가값
*/
public int getIncVal() {
return incVal;
}
/**
* 증가값
*@param incVal 증가값
*/
public void setIncVal(int incVal) {
	this.incVal = incVal;
}
/**
* 최소값
* @return 최소값
*/
public long getMinVal() {
return minVal;
}
/**
* 최소값
*@param minVal 최소값
*/
public void setMinVal(long minVal) {
	this.minVal = minVal;
}
/**
* 최대값
* @return 최대값
*/
public long getMaxVal() {
return maxVal;
}
/**
* 최대값
*@param maxVal 최대값
*/
public void setMaxVal(long maxVal) {
	this.maxVal = maxVal;
}
/**
* 다음값
* @return 다음값
*/
public long getNextVal() {
return nextVal;
}
/**
* 다음값
*@param nextVal 다음값
*/
public void setNextVal(long nextVal) {
	this.nextVal = nextVal;
}
/**
* 순환여부
* @return 순환여부
*/
public boolean isCycleYn() {
return cycleYn;
}
/**
* 순환여부
*@param cycleYn 순환여부
*/
public void setCycleYn(boolean cycleYn) {
	this.cycleYn = cycleYn;
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

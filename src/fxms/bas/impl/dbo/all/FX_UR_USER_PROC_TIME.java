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


@FxTable(name = "FX_UR_USER_PROC_TIME", comment = "사용자처리시간테이블")
@FxIndex(name = "FX_UR_USER_PROC_TIME__PK", type = INDEX_TYPE.PK, columns = {"USER_NO", "DATA_TYPE_VAL"})
public class FX_UR_USER_PROC_TIME implements Serializable {

public FX_UR_USER_PROC_TIME() {
 }

@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호", defValue = "0")
private int userNo = 0;


@FxColumn(name = "DATA_TYPE_VAL", size = 20, comment = "데이터유형값")
private String dataTypeVal;


@FxColumn(name = "FNSH_DTM", size = 14, comment = "종료일시")
private long fnshDtm;


@FxColumn(name = "PROC_DATA_CNT", size = 9, nullable = true, comment = "처리데이터건수")
private int procDataCnt;


/**
* 사용자번호
* @return 사용자번호
*/
public int getUserNo() {
return userNo;
}
/**
* 사용자번호
*@param userNo 사용자번호
*/
public void setUserNo(int userNo) {
	this.userNo = userNo;
}
/**
* 데이터유형값
* @return 데이터유형값
*/
public String getDataTypeVal() {
return dataTypeVal;
}
/**
* 데이터유형값
*@param dataTypeVal 데이터유형값
*/
public void setDataTypeVal(String dataTypeVal) {
	this.dataTypeVal = dataTypeVal;
}
/**
* 종료일시
* @return 종료일시
*/
public long getFnshDtm() {
return fnshDtm;
}
/**
* 종료일시
*@param fnshDtm 종료일시
*/
public void setFnshDtm(long fnshDtm) {
	this.fnshDtm = fnshDtm;
}
/**
* 처리데이터건수
* @return 처리데이터건수
*/
public int getProcDataCnt() {
return procDataCnt;
}
/**
* 처리데이터건수
*@param procDataCnt 처리데이터건수
*/
public void setProcDataCnt(int procDataCnt) {
	this.procDataCnt = procDataCnt;
}
}

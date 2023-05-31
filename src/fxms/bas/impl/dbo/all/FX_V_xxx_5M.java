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


@FxTable(name = "FX_V_xxx_5M", comment = "성능통계값테이블")
@FxIndex(name = "FX_V_xxx_5M__PK", type = INDEX_TYPE.PK, columns = {"PS_DTM", "MO_NO", "MO_INSTANCE"})
public class FX_V_xxx_5M implements Serializable {

public FX_V_xxx_5M() {
 }

@FxColumn(name = "PS_DTM", size = 14, comment = "수집일시")
private long psDtm;


@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
private long moNo;


@FxColumn(name = "MO_INSTANCE", size = 50, comment = "MO인스턴스")
private String moInstance;


@FxColumn(name = "DATA_CNT", size = 14, comment = "데이터건수")
private long dataCnt;


@FxColumn(name = "INS_DTM", size = 14, comment = "입력일시")
private long insDtm;


@FxColumn(name = "XXX_MIN_VAL", size = 19, nullable = true, comment = "최소값")
private double xxxMinVal;


@FxColumn(name = "XXX_MAX_VAL", size = 19, nullable = true, comment = "최대값")
private double xxxMaxVal;


@FxColumn(name = "XXX_AVG_VAL", size = 19, nullable = true, comment = "평균값")
private double xxxAvgVal;


@FxColumn(name = "XXX_SUM_VAL", size = 19, nullable = true, comment = "합산값")
private double xxxSumVal;


/**
* 수집일시
* @return 수집일시
*/
public long getPsDtm() {
return psDtm;
}
/**
* 수집일시
*@param psDtm 수집일시
*/
public void setPsDtm(long psDtm) {
	this.psDtm = psDtm;
}
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
* MO인스턴스
* @return MO인스턴스
*/
public String getMoInstance() {
return moInstance;
}
/**
* MO인스턴스
*@param moInstance MO인스턴스
*/
public void setMoInstance(String moInstance) {
	this.moInstance = moInstance;
}
/**
* 데이터건수
* @return 데이터건수
*/
public long getDataCnt() {
return dataCnt;
}
/**
* 데이터건수
*@param dataCnt 데이터건수
*/
public void setDataCnt(long dataCnt) {
	this.dataCnt = dataCnt;
}
/**
* 입력일시
* @return 입력일시
*/
public long getInsDtm() {
return insDtm;
}
/**
* 입력일시
*@param insDtm 입력일시
*/
public void setInsDtm(long insDtm) {
	this.insDtm = insDtm;
}
/**
* 최소값
* @return 최소값
*/
public double getXxxMinVal() {
return xxxMinVal;
}
/**
* 최소값
*@param xxxMinVal 최소값
*/
public void setXxxMinVal(double xxxMinVal) {
	this.xxxMinVal = xxxMinVal;
}
/**
* 최대값
* @return 최대값
*/
public double getXxxMaxVal() {
return xxxMaxVal;
}
/**
* 최대값
*@param xxxMaxVal 최대값
*/
public void setXxxMaxVal(double xxxMaxVal) {
	this.xxxMaxVal = xxxMaxVal;
}
/**
* 평균값
* @return 평균값
*/
public double getXxxAvgVal() {
return xxxAvgVal;
}
/**
* 평균값
*@param xxxAvgVal 평균값
*/
public void setXxxAvgVal(double xxxAvgVal) {
	this.xxxAvgVal = xxxAvgVal;
}
/**
* 합산값
* @return 합산값
*/
public double getXxxSumVal() {
return xxxSumVal;
}
/**
* 합산값
*@param xxxSumVal 합산값
*/
public void setXxxSumVal(double xxxSumVal) {
	this.xxxSumVal = xxxSumVal;
}
}

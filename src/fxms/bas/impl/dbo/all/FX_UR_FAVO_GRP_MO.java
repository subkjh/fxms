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


@FxTable(name = "FX_UR_FAVO_GRP_MO", comment = "관심그룹대상테이블")
@FxIndex(name = "FX_UR_FAVO_GRP_MO__PK", type = INDEX_TYPE.PK, columns = {"FAVO_GRP_NO", "MO_NO"})
public class FX_UR_FAVO_GRP_MO implements Serializable {

public FX_UR_FAVO_GRP_MO() {
 }

@FxColumn(name = "FAVO_GRP_NO", size = 9, comment = "관심그룹번호")
private int favoGrpNo;


@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
private long moNo;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일자")
private long chgDtm;


/**
* 관심그룹번호
* @return 관심그룹번호
*/
public int getFavoGrpNo() {
return favoGrpNo;
}
/**
* 관심그룹번호
*@param favoGrpNo 관심그룹번호
*/
public void setFavoGrpNo(int favoGrpNo) {
	this.favoGrpNo = favoGrpNo;
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
* 수정일자
* @return 수정일자
*/
public long getChgDtm() {
return chgDtm;
}
/**
* 수정일자
*@param chgDtm 수정일자
*/
public void setChgDtm(long chgDtm) {
	this.chgDtm = chgDtm;
}
}

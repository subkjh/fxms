package fxms.bas.impl.dbo.all;


import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.07.25 10:02
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_PS_POINT", comment = "성능관제점테이블")
@FxIndex(name = "FX_PS_POINT__PK", type = INDEX_TYPE.PK, columns = {"POINT_ID"})
@FxIndex(name = "FX_PS_POINT__KEY1", type = INDEX_TYPE.KEY, columns = {"FAC_TID"})
@FxIndex(name = "FX_PS_POINT__KEY2", type = INDEX_TYPE.KEY, columns = {"MO_TID"})
@FxIndex(name = "FX_PS_POINT__KEY3", type = INDEX_TYPE.KEY, columns = {"POINT_TID"})
public class FX_PS_POINT  {

public FX_PS_POINT() {
 }

    @FxColumn(name = "POINT_ID", size = 50, comment = "관제점ID")
    private String pointId;

    @FxColumn(name = "POINT_NM", size = 200, comment = "관제점명")
    private String pointNm;

    @FxColumn(name = "POINT_UNIT", size = 10, nullable = true, comment = "관제점단위")
    private String pointUnit;

    @FxColumn(name = "POINT_DESCR", size = 400, nullable = true, comment = "관제점설명")
    private String pointDescr;

    @FxColumn(name = "FAC_TID", size = 50, nullable = true, comment = "설비TID")
    private String facTid;

    @FxColumn(name = "MO_TID", size = 100, nullable = true, comment = "MO대상ID")
    private String moTid;

    @FxColumn(name = "POINT_TID", size = 50, comment = "관제점TID")
    private String pointTid;

    @FxColumn(name = "PS_ID", size = 50, nullable = true, comment = "성능항목ID")
    private String psId;

    @FxColumn(name = "ALCD_NO", size = 9, nullable = true, comment = "경보코드번호")
    private Integer alcdNo;

    @FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
    private Integer regUserNo = 0;

    @FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
    private Long regDtm;

    @FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
    private Integer chgUserNo = 0;

    @FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
    private Long chgDtm;

/**
 * @return 관제점ID
*/
public String getPointId() { 
    return pointId;
}
/**
 * @param pointId 관제점ID
*/
public void setPointId(String pointId) { 
    this.pointId = pointId;
}
/**
 * @return 관제점명
*/
public String getPointNm() { 
    return pointNm;
}
/**
 * @param pointNm 관제점명
*/
public void setPointNm(String pointNm) { 
    this.pointNm = pointNm;
}
/**
 * @return 관제점단위
*/
public String getPointUnit() { 
    return pointUnit;
}
/**
 * @param pointUnit 관제점단위
*/
public void setPointUnit(String pointUnit) { 
    this.pointUnit = pointUnit;
}
/**
 * @return 관제점설명
*/
public String getPointDescr() { 
    return pointDescr;
}
/**
 * @param pointDescr 관제점설명
*/
public void setPointDescr(String pointDescr) { 
    this.pointDescr = pointDescr;
}
/**
 * @return 설비TID
*/
public String getFacTid() { 
    return facTid;
}
/**
 * @param facTid 설비TID
*/
public void setFacTid(String facTid) { 
    this.facTid = facTid;
}
/**
 * @return MO대상ID
*/
public String getMoTid() { 
    return moTid;
}
/**
 * @param moTid MO대상ID
*/
public void setMoTid(String moTid) { 
    this.moTid = moTid;
}
/**
 * @return 관제점TID
*/
public String getPointTid() { 
    return pointTid;
}
/**
 * @param pointTid 관제점TID
*/
public void setPointTid(String pointTid) { 
    this.pointTid = pointTid;
}
/**
 * @return 성능항목ID
*/
public String getPsId() { 
    return psId;
}
/**
 * @param psId 성능항목ID
*/
public void setPsId(String psId) { 
    this.psId = psId;
}
/**
 * @return 경보코드번호
*/
public Integer getAlcdNo() { 
    return alcdNo;
}
/**
 * @param alcdNo 경보코드번호
*/
public void setAlcdNo(Integer alcdNo) { 
    this.alcdNo = alcdNo;
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

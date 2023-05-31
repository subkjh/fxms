package fxms.bas.impl.dbo.all;


import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.05.04 17:11
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_PS_STAT_KIND", comment = "성능통계종류테이블")
@FxIndex(name = "FX_PS_STAT_KIND__PK", type = INDEX_TYPE.PK, columns = {"PS_DATA_NAME"})
public class FX_PS_STAT_KIND  {

public FX_PS_STAT_KIND() {
 }

    @FxColumn(name = "PS_DATA_NAME", size = 50, comment = "성능데이터명")
    private String psDataName;

    @FxColumn(name = "PS_DATA_TAG", size = 3, comment = "성능데이터구분자")
    private String psDataTag;

    @FxColumn(name = "DATA_RANGE", size = 20, comment = "데이터범위")
    private String dataRange;

    @FxColumn(name = "TBL_PART_UNIT_CD", size = 10, comment = "테이블분리단위코드")
    private String tblPartUnitCd;

    @FxColumn(name = "TBL_PART_STORE_CNT", size = 9, comment = "테이블분리보관건수", defValue = "5")
    private Integer tblPartStoreCnt = 5;

    @FxColumn(name = "PS_DATA_DESCR", size = 400, nullable = true, comment = "성능데이터설명")
    private String psDataDescr;

    @FxColumn(name = "DATA_STORE_DAYS", size = 9, comment = "데이터보관일", defValue = "30")
    private Integer dataStoreDays = 30;

    @FxColumn(name = "PS_DATA_SRC", size = 50, comment = "성능데이터원천", defValue = "RAW")
    private String psDataSrc = "RAW";

    @FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
    private String useYn = "Y";

    @FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, comment = "등록사용자번호", defValue = "0")
    private Integer regUserNo = 0;

    @FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, comment = "등록일시")
    private Long regDtm;

    @FxColumn(name = "CHG_USER_NO", size = 9, comment = "수정사용자번호", defValue = "0")
    private Integer chgUserNo = 0;

    @FxColumn(name = "CHG_DTM", size = 14, comment = "수정일시")
    private Long chgDtm;

/**
 * @return 성능데이터명
*/
public String getPsDataName() { 
    return psDataName;
}
/**
 * @param psDataName 성능데이터명
*/
public void setPsDataName(String psDataName) { 
    this.psDataName = psDataName;
}
/**
 * @return 성능데이터구분자
*/
public String getPsDataTag() { 
    return psDataTag;
}
/**
 * @param psDataTag 성능데이터구분자
*/
public void setPsDataTag(String psDataTag) { 
    this.psDataTag = psDataTag;
}
/**
 * @return 데이터범위
*/
public String getDataRange() { 
    return dataRange;
}
/**
 * @param dataRange 데이터범위
*/
public void setDataRange(String dataRange) { 
    this.dataRange = dataRange;
}
/**
 * @return 테이블분리단위코드
*/
public String getTblPartUnitCd() { 
    return tblPartUnitCd;
}
/**
 * @param tblPartUnitCd 테이블분리단위코드
*/
public void setTblPartUnitCd(String tblPartUnitCd) { 
    this.tblPartUnitCd = tblPartUnitCd;
}
/**
 * @return 테이블분리보관건수
*/
public Integer getTblPartStoreCnt() { 
    return tblPartStoreCnt;
}
/**
 * @param tblPartStoreCnt 테이블분리보관건수
*/
public void setTblPartStoreCnt(Integer tblPartStoreCnt) { 
    this.tblPartStoreCnt = tblPartStoreCnt;
}
/**
 * @return 성능데이터설명
*/
public String getPsDataDescr() { 
    return psDataDescr;
}
/**
 * @param psDataDescr 성능데이터설명
*/
public void setPsDataDescr(String psDataDescr) { 
    this.psDataDescr = psDataDescr;
}
/**
 * @return 데이터보관일
*/
public Integer getDataStoreDays() { 
    return dataStoreDays;
}
/**
 * @param dataStoreDays 데이터보관일
*/
public void setDataStoreDays(Integer dataStoreDays) { 
    this.dataStoreDays = dataStoreDays;
}
/**
 * @return 성능데이터원천
*/
public String getPsDataSrc() { 
    return psDataSrc;
}
/**
 * @param psDataSrc 성능데이터원천
*/
public void setPsDataSrc(String psDataSrc) { 
    this.psDataSrc = psDataSrc;
}
/**
 * @return 사용여부
*/
public String isUseYn() { 
    return useYn;
}
/**
 * @param useYn 사용여부
*/
public void setUseYn(String useYn) { 
    this.useYn = useYn;
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

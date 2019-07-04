package fxms.bas.impl.dbo;


import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
* @since 2018.02.27 14:27
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_DI_PROPERTY", comment = "다이아그램속성테이블")
@FxIndex(name = "FY_DI_PROPERTY__PK", type = INDEX_TYPE.PK, columns = {"DIAG_NO", "DIAG_NODE_NO", "PROPERTY_NAME"})
public class FX_DI_PROPERTY implements Serializable {

public FX_DI_PROPERTY() {
 }

@FxColumn(name = "DIAG_NO", size = 9, comment = "챠트명")
private Integer diagNo;


@FxColumn(name = "DIAG_NODE_NO", size = 9, comment = "다이아그램노드번호")
private Integer diagNodeNo;


@FxColumn(name = "PROPERTY_NAME", size = 100, comment = "속성명")
private String propertyName;


@FxColumn(name = "PROPERTY_VALUE", size = 240, comment = "속성값")
private String propertyValue;


/**
* 챠트명
* @return 챠트명
*/
public Integer getDiagNo() {
return diagNo;
}
/**
* 챠트명
*@param diagNo 챠트명
*/
public void setDiagNo(Integer diagNo) {
	this.diagNo = diagNo;
}
/**
* 다이아그램노드번호
* @return 다이아그램노드번호
*/
public Integer getDiagNodeNo() {
return diagNodeNo;
}
/**
* 다이아그램노드번호
*@param diagNodeNo 다이아그램노드번호
*/
public void setDiagNodeNo(Integer diagNodeNo) {
	this.diagNodeNo = diagNodeNo;
}
/**
* 속성명
* @return 속성명
*/
public String getPropertyName() {
return propertyName;
}
/**
* 속성명
*@param propertyName 속성명
*/
public void setPropertyName(String propertyName) {
	this.propertyName = propertyName;
}
/**
* 속성값
* @return 속성값
*/
public String getPropertyValue() {
return propertyValue;
}
/**
* 속성값
*@param propertyValue 속성값
*/
public void setPropertyValue(String propertyValue) {
	this.propertyValue = propertyValue;
}
}

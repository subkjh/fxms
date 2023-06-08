package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/UiDashbHandler.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class UiDashbHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/UiDashbHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/UiDashbHandler.xml";

public UiDashbHandlerQid() { 
} 
/**
* para : <br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.*<br>		from    FX_UI_DASHB<br><br> <br>
*/
public final String select_dashb_grid_list = "select-dashb-grid-list";

}
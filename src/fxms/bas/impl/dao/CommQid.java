package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/comm.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class CommQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/comm.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/comm.xml";

public CommQid() { 
} 
/**
* para : $date, $date, $count<br>
* result : STRING=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	a.YMD<br>		from 	FX_CO_DATE a<br>		where	a.SPECL_DOW_CD = ( select DOW_CD from FX_CO_DATE a1 where a1.YMD = $date )<br>		and		a.YMD < $date<br>		order by a.YMD desc<br>		limit	0, $count<br><br> <br>
*/
public final String select_date_same_dow = "select-date-same-dow";

}
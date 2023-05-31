package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/test/test.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class TestQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/test/test.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/test/test.xml";

public TestQid() { 
} 
/**
* para : <br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select	a.EQUIP_ID				as GSC100장비ID<br>					, a.EQUIP_NM 			as GSL200장비명<br>					, a.equip_tid_val 		as GSL200장비TID<br>					, b.equip_port_id		as DSC120포트ID<br>					, b.port_desc			as DSL150포트설명					<br>					, c.equip_cons_itm_nm	as GSL200카드명<br>			from 	oiv10100 a<br>					, oiv10400 b<br>					, oiv10210 c<br>			where	a.equip_tid_val like '%CR_'<br>			and		a.equip_id = c.equip_id<br>			and		a.equip_id = b.equip_id<br>			and		c.equip_cons_itm_id = b.equip_cons_itm_id<br>			order by a.equip_id, c.equip_cons_itm_nm<br><br> <br>
*/
public final String QID_SELECT_EXCEL_LIST = "QID_SELECT_EXCEL_LIST";

}
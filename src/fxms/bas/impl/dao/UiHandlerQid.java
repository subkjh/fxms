package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/UiHandler.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class UiHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/UiHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/UiHandler.xml";

public UiHandlerQid() { 
} 
/**
* para : $userNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	B.OP_ID					as OP_ID<br>				, B.UPPER_OP_ID			as UPPER_OP_ID<br>				, B.OP_NAME				as OP_NAME<br>				, B1.OP_NAME			as UPPER_OP_NAME<br>				, B.OP_URI				as OP_URI<br>		from    FX_UR_UGRP_OP 			A<br>        		, FX_CO_OP 				B<br>        			left join FX_CO_OP 	B1	on B1.OP_ID	= B.UPPER_OP_ID<br>				, FX_UR_USER			C        			<br>		where	C.USER_NO    	= $userNo<br>		and		A.UGRP_NO		= C.UGRP_NO	<br>		and		B.OP_ID      	= A.OP_ID<br>		and		B.OP_TYPE_CD 	= 'MENU'<br>		order by <br>				B.SORT_SEQ<br><br> <br>
*/
public final String select_my_menu_list = "select-my-menu-list";

/**
* para : $userNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	B.OP_ID				as OP_ID<br>				, B.UPPER_OP_ID		as UPPER_OP_ID<br>				, B.OP_NAME			as OP_NAME<br>				, C.OP_NAME			as UPPER_OP_NAME<br>				, B.OP_URI			as OP_URI<br>				, B.OP_MTHD			as OP_MTHD<br>				, B.OP_TYPE_CD		as OP_TYPE_CD<br>		from    FX_UR_UGRP_OP 			A<br>        		, FX_CO_OP 				B<br>        			left join FX_CO_OP 	C	on C.OP_ID	= B.UPPER_OP_ID<br>				, FX_UR_USER			D        			<br>		where	D.USER_NO    	= $userNo<br>		and		A.UGRP_NO		= D.UGRP_NO	<br>		and		B.OP_ID      	= A.OP_ID<br><br> <br>
*/
public final String select_my_op_list = "select-my-op-list";

}
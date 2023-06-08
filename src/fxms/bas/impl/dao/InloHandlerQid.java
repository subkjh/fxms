package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/InloHandler.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class InloHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/InloHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/InloHandler.xml";

public InloHandlerQid() { 
} 
/**
* para : $inloNo, $moClass, $inloClCd<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>with DATAS as (<br>				select 	A.INLO_NO<br>						, max(ifnull(( select min(alarm_level) from FX_AL_ALARM_CUR where MO_NO = A.MO_NO ), 5))<br>												as ALARM_LEVEL<br>						, count(1)				as MO_COUNT<br>				from    FX_MO A<br>						, FX_CF_INLO_MEM B<br>				where	1 = 1<br>				and		B.INLO_NO 	= $inloNo<br>				and		A.INLO_NO 	= B.LOWER_INLO_NO<br>				and		A.MO_CLASS 	= $moClass<br>				group by <br>						A.INLO_NO<br>		)				<br>		select 	C.INLO_NO				as INLO_NO<br>				, C.INLO_NAME			as INLO_NAME<br>				, C.INLO_CL_CD			as INLO_CL_CD<br>				, C.UPPER_INLO_NO		as UPPER_INLO_NO<br>				, ( select max(CD_NAME) from FX_CO_CD where CD_CLASS = 'INLO_CL_CD' and CD_CODE = C.INLO_CL_CD )<br>										as INLO_CL_NAME<br>				, ifnull(min(B.ALARM_LEVEL), 5)	<br>										as ALARM_LEVEL<br>				, cast(sum(B.MO_COUNT) as integer)		<br>										as MO_COUNT										<br>		from	FX_CF_INLO_MEM		A    <br>				, DATAS 			B<br>				, FX_CF_INLO 		C				<br>		where	B.INLO_NO		= A.LOWER_INLO_NO<br>		and		C.INLO_NO		= A.INLO_NO<br>		and		C.INLO_CL_CD 	= $inloClCd<br>		group by<br>				C.INLO_NO<br>				, C.INLO_NAME<br>		order by<br>				C.INLO_NAME<br><br> <br>
*/
public final String select_inlo_alarm_state_list = "select-inlo-alarm-state-list";

/**
* para : $inloNo, $moClass, $inloClCd<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	C.INLO_NO<br>				, C.INLO_NAME<br>				, cast(sum(if(B.MO_ONLINE_ST_VAL = 1, 1, 0)) as integer) <br>											as ONLINE_COUNT<br>				, cast(sum(if(B.MO_ONLINE_ST_VAL = 1, 0, 1)) as integer) <br>											as OFFLINE_COUNT<br>		from	FX_CF_INLO_MEM 	A<br>				, FX_MO 		B<br>				, FX_CF_INLO	C<br>		where	1 = 1<br>		and		A.INLO_NO 		= $inloNo<br>		and		B.INLO_NO 		= A.LOWER_INLO_NO<br>		and		B.MO_CLASS 		= $moClass<br>		and		C.INLO_CL_CD	= $inloClCd<br>		and		C.INLO_NO		= B.INLO_NO<br>		group by<br>				C.INLO_NO<br>				, C.INLO_NAME<br>		order by<br>				C.INLO_NAME<br><br> <br>
*/
public final String select_inlo_online_state_list = "select-inlo-online-state-list";

}
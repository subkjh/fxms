package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/MoHandler.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class MoHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/MoHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/MoHandler.xml";

public MoHandlerQid() { 
} 
/**
* para : $userNo, $userNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select	MO_CLASS<br>				, COUNT(1)		as MO_COUNT<br>		from 	FX_MO<br>		where	1 = 1<br><br>and (  MO_NO in ( select MO_NO from FX_UR_MO where USER_NO = $userNo ) or UPPER_MO_NO in ( select MO_NO from FX_UR_MO where USER_NO = $userNo ) )<br><br>group by MO_CLASS<br><br> <br>
*/
public final String SELECT_MO_COUNT_LIST = "SELECT_MO_COUNT_LIST";

/**
* para : $userNo, $moClass, $inloNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.MO_NO						as MO_NO<br>				, A.MO_NAME					as MO_NAME<br>				, A.INLO_NO					as INLO_NO<br>				, A1.INLO_NAME				as INLO_NAME<br>				, ifnull( ( select min(alarm_level) from FX_AL_ALARM_CUR where MO_NO = A.MO_NO ), 0 )<br>											as MO_ALARM_ST_VAL<br>				, ( select count(1) from FX_AL_ALARM_CUR where MO_NO = A.MO_NO )<br>											as MO_ALARM_COUNT<br>		from    FX_MO 				A<br>					left join FX_CF_INLO	A1 on A1.INLO_NO = A.INLO_NO<br>				, FX_UR_USER_INLO 	B<br>				, FX_CF_INLO_MEM	C				<br>		where	1 = 1<br>		and		B.USER_NO 		= $userNo<br>		and		A.INLO_NO 		= B.INLO_NO<br>		and		A.MO_CLASS 		= $moClass<br>		and		C.INLO_NO		= $inloNo<br>		and		C.LOWER_INLO_NO = A.INLO_NO<br>		order by<br>				A.MO_NAME<br><br> <br>
*/
public final String select_mo_alarm_state_list = "select-mo-alarm-state-list";

/**
* para : $userNo$vendrName$inloClCd$modelClCd$moClass<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>with DATAS as (<br>				select 	<br>						C.MODEL_NO<br>						, C.INLO_NO				<br>						, C.MO_CLASS<br>						, sum(if(C.MO_ONLINE_ST_VAL = 1, 1, 0))		as MO_ONLINE_COUNT<br>						, sum(if(C.MO_ONLINE_ST_VAL = 1, 0, 1))		as MO_OFFLINE_COUNT<br>						, count(1)									as MO_COUNT<br>				from    FX_UR_USER			A<br>						, FX_CF_INLO_MEM	B<br>						, FX_MO				C<br>				where	A.USER_NO    	= $userNo<br>				and		B.INLO_NO		= A.INLO_NO<br>				and		C.INLO_NO		= B.LOWER_INLO_NO<br>				group by<br>						C.MODEL_NO<br>						, C.INLO_NO<br>						, C.MO_CLASS<br>		)<br>		select 	A.MODEL_NAME		as MODEL_NAME<br>				, A.MODEL_CL_CD		as MODEL_CL_CD<br>				, A.VENDR_NAME		as VENDR_NAME<br>				, ( select max(CD_NAME) from FX_CO_CD where CD_CLASS = 'MODEL_CL_CD' and CD_CODE = A.MODEL_CL_CD )<br>									as MODEL_CL_NAME<br>				, B.INLO_NAME		as INLO_NAME<br>				, B.INLO_CL_CD		as INLO_CL_CD<br>				, ( select max(CD_NAME) from FX_CO_CD where CD_CLASS = 'INLO_CL_CD' and CD_CODE = B.INLO_CL_CD )<br>									as INLO_CL_NAME<br>				, T.*<br>		from 	DATAS T<br>					left join FX_CF_MODEL 	A on A.MODEL_NO = T.MODEL_NO<br>					left join FX_CF_INLO	B on B.INLO_NO	= T.INLO_NO<br>		where 1 = 1<br><br>and A.VENDR_NAME = $vendrName<br><br>and B.INLO_CL_CD = $inloClCd<br><br>and A.MODEL_CL_CD = $modelClCd<br><br>and T.MO_CLASS = $moClass<br><br> <br>
*/
public final String select_mo_count_grid_list = "select-mo-count-grid-list";

/**
* para : $userNo, $moClass, $inloNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select A.MO_NO						as MO_NO<br>				, A.MO_NAME					as MO_NAME<br>				, A.INLO_NO					as INLO_NO<br>				, A1.INLO_NAME				as INLO_NAME<br>				, A.MO_ONLINE_ST_VAL		as MO_ONLINE_ST_VAL<br>		from    FX_MO 				A<br>					left join FX_CF_INLO	A1 on A1.INLO_NO = A.INLO_NO<br>				, FX_UR_USER_INLO 	B<br>				, FX_CF_INLO_MEM	C<br>		where	1 = 1<br>		and		B.USER_NO 		= $userNo<br>		and		A.INLO_NO 		= B.INLO_NO<br>		and		A.MO_CLASS 		= $moClass<br>		and		C.INLO_NO		= $inloNo<br>		and		C.LOWER_INLO_NO = A.INLO_NO<br>		order by<br>				A.MO_NAME<br><br> <br>
*/
public final String select_mo_online_state_list = "select-mo-online-state-list";

}
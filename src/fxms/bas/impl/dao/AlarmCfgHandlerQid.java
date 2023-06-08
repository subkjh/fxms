package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/AlarmCfgHandler.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class AlarmCfgHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/AlarmCfgHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/AlarmCfgHandler.xml";

public AlarmCfgHandlerQid() { 
} 
/**
* para : $userNo, $userNo$moClass<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select	A.ALARM_CFG_NO			as ALARM_CFG_NO<br>				, A.ALARM_CFG_NAME		as ALARM_CFG_NAME<br>				, A.ALARM_CFG_DESC		as ALARM_CFG_DESC<br>				, A.MO_CLASS			as MO_CLASS<br>				, ( select max(CD_NAME) from FX_CO_CD where CD_CLASS = 'MO_CLASS' and CD_CODE = A.MO_CLASS )<br>										as MO_CLASS_NAME<br>				, ( select 	count(1) <br>					from 	FX_MO 				T1<br>							, FX_UR_USER_MO 	T2<br>					where 	T1.ALARM_CFG_NO = A.ALARM_CFG_NO<br>					and		T2.MO_NO		= T1.MO_NO<br>					and		T2.USER_NO		= $userNo					<br>					 )<br>										as MO_COUNT<br>		from	FX_AL_CFG 			A<br>				, FX_UR_USER_INLO	B<br>		where	1 = 1<br>		and		B.USER_NO	= $userNo<br>		and		A.INLO_NO	= B.INLO_NO<br><br>and A.MO_CLASS = $moClass<br><br> <br>
*/
public final String select_alarm_cfg_grid_list = "select_alarm_cfg_grid_list";

/**
* para : $moNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	mo.MO_NO				as MO_NO<br>				, al.PS_ID 				as PS_ID<br>				, al.CMPR_CD 			as CMPR_CD<br>				, al.ALCD_NAME 			as ALCD_NAME<br>				, cfg.ALARM_CFG_NO		as ALARM_CFG_NO<br>				, cfg.AL_CRI_CMPR_VAL	as AL_CRI_CMPR_VAL<br>				, cfg.AL_MAJ_CMPR_VAL	as AL_MAJ_CMPR_VAL<br>				, cfg.AL_MIN_CMPR_VAL	as AL_MIN_CMPR_VAL<br>				, cfg.AL_WAR_CMPR_VAL	as AL_WAR_CMPR_VAL<br>		from 	FX_MO mo<br>				, FX_AL_CFG_MEM cfg<br>				, FX_AL_CD al<br>		where 	mo.mo_no 			= $moNo<br>		and 	mo.ALARM_CFG_NO 	= cfg.ALARM_CFG_NO <br>		and 	al.ALCD_NO			= cfg.ALCD_NO<br>		and		cfg.USE_YN 			= 'Y'<br>		order by <br>				al.PS_ID<br><br> <br>
*/
public final String select_alarm_cfg_mem_list_for_mono = "select_alarm_cfg_mem_list_for_mono";

/**
* para : $userNo$moClass<br>
* result : AlarmCfgVo=fxms.bas.impl.vo.AlarmCfgVo<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>with DATAS as ( <br>			select	C.ALARM_CFG_NO			as ALARM_CFG_NO<br>					, C.ALARM_CFG_NAME		as ALARM_CFG_NAME<br>					, C.MO_CLASS			as MO_CLASS<br>					, C.MO_TYPE<br>			from	FX_UR_USER			A<br>					, FX_CF_INLO_MEM	B<br>					, FX_AL_CFG 		C<br>			where	A.USER_NO		= $userNo<br>			and		B.INLO_NO		= A.INLO_NO<br>			and		C.INLO_NO		= B.LOWER_INLO_NO<br>		<br>			union <br>		<br>			select	C.ALARM_CFG_NO			as ALARM_CFG_NO<br>					, C.ALARM_CFG_NAME		as ALARM_CFG_NAME<br>					, C.MO_CLASS			as MO_CLASS<br>					, C.MO_TYPE<br>			from	FX_AL_CFG 		C<br>			where	C.INLO_NO		= 0<br>		)<br>select *<br>		from DATAS A<br>where A.MO_CLASS = $moClass<br>order by<br>		A.ALARM_CFG_NAME<br> <br>
*/
public final String select_alarm_cfg_simple_list = "select_alarm_cfg_simple_list";

}
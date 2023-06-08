package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/cron/CheckACron.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class CheckACronQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/cron/CheckACron.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/cron/CheckACron.xml";

public CheckACronQid() { 
} 
/**
* para : $moStatusPsId<br>
* result : RESULT_CollectInfoVo=fxms.bas.fxo.cron.vo.CollectInfoVo<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	a.MO_NO<br>				, a.MO_NAME<br>				, a.POLL_CYCLE<br>				, a.ALARM_CFG_NO <br>				, d.ALCD_NO<br>				, a.INLO_NO				<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_CRI_CMPR_VAL  second , '%Y%m%d%H%i%s') as CRI_DTM<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_MAJ_CMPR_VAL  second , '%Y%m%d%H%i%s') as MAJ_DTM<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_MIN_CMPR_VAL  second , '%Y%m%d%H%i%s') as MIN_DTM<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_WAR_CMPR_VAL  second , '%Y%m%d%H%i%s') as WAR_DTM		<br>				, ( select 	max(CUR_COLL_DTM) <br>					from 	FX_V_ACUR 	t<br>							, FX_MO 	t2 <br>					where 	t.MO_NO 	= t2.MO_NO<br>					and		t2.INLO_NO	= a.INLO_NO<br>					and 	t.PS_ID != $moStatusPsId<br>					) 				as COLL_DTM<br>		from 	FX_MO			a<br>				, FX_AL_CFG 	b<br>				, FX_AL_CFG_MEM c<br>				, FX_AL_CD		d		<br>				, FX_MO_NODE	e<br>		where 	a.MNG_YN 		= 'Y'<br>		and 	a.ALARM_CFG_NO 	= b.ALARM_CFG_NO<br>		and		a.MO_NO			= e.MO_NO <br>		and 	b.ALARM_CFG_NO 	= c.ALARM_CFG_NO <br>		and 	c.ALCD_NO 		= 11003		' NO_DATAS_PS_COLLECT '<br>		and		d.ALCD_NO		= c.ALCD_NO<br><br> <br>
*/
public final String select_not_collected_node_list = "select_not_collected_node_list";

/**
* para : $moStatusPsId<br>
* result : RESULT_CollectInfoVo=fxms.bas.fxo.cron.vo.CollectInfoVo<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	a.MO_NO<br>				, a.MO_NAME<br>				, a.POLL_CYCLE<br>				, a.ALARM_CFG_NO <br>				, d.ALCD_NO				<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_CRI_CMPR_VAL  second , '%Y%m%d%H%i%s') as CRI_DTM<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_MAJ_CMPR_VAL  second , '%Y%m%d%H%i%s') as MAJ_DTM<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_MIN_CMPR_VAL  second , '%Y%m%d%H%i%s') as MIN_DTM<br>				, DATE_FORMAT(now() + interval -1 * a.POLL_CYCLE * c.AL_WAR_CMPR_VAL  second , '%Y%m%d%H%i%s') as WAR_DTM		<br>				, ( select 	max(CUR_COLL_DTM) <br>					from 	FX_V_ACUR t <br>					where 	t.MO_NO = a.MO_NO <br>					and 	t.PS_ID != $moStatusPsId <br>					)  			as COLL_DTM					<br>		from 	FX_MO 			a<br>				, FE_MO_SENSOR	a1<br>				, FX_AL_CFG 	b<br>				, FX_AL_CFG_MEM c<br>				, FX_AL_CD		d		<br>		where 	a.MNG_YN 		= 'Y'<br>		and 	a.poll_cycle 	> 0<br>		and		a1.MO_NO		= a.MO_NO<br>		and 	a.ALARM_CFG_NO 	= b.ALARM_CFG_NO <br>		and 	b.ALARM_CFG_NO 	= c.ALARM_CFG_NO <br>		and 	c.ALCD_NO 		= 11003		' NO_DATAS_PS_COLLECT '<br>		and		d.ALCD_NO		= c.ALCD_NO<br><br> <br>
*/
public final String select_not_collected_sensor_list = "select_not_collected_sensor_list";

}
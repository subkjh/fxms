package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/cron/StatMakeDailyCron.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class StatMakeDailyCronQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/cron/StatMakeDailyCron.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/cron/StatMakeDailyCron.xml";

public StatMakeDailyCronQid() { 
} 
/**
* para : $stDate, $stDate, $stDate, $stDate, $regUserNo, $regDtm, $chgUserNo, $chgDtm, $chgUserNo, $chgDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_AL_STAT_MO (<br>				ST_DATE<br>				, MO_NO<br>				, ALARM_OCCUR_CNT<br>				, ALARM_RLSE_CNT<br>				, REG_USER_NO<br>				, REG_DTM<br>				, CHG_USER_NO<br>				, CHG_DTM<br>		)<br>		with datas as (<br>		<br>			select	$stDate			as ST_DATE<br>					, MO_NO			as MO_NO<br>					, count(1) 		as ALARM_OCCUR_CNT<br>					, 0				as ALARM_RLSE_CNT<br>			from 	FX_AL_ALARM_HST<br>			where	cast(OCCUR_DTM / 1000000 as UNSIGNED) 		= $stDate<br>			group by MO_NO<br>					<br><br>			union all<br>			<br>			select	$stDate			as ST_DATE<br>					, MO_NO			as MO_NO<br>					, 0				as ALARM_OCCUR_CNT<br>					, count(1) 		as ALARM_RLSE_CNT<br>			from 	FX_AL_ALARM_HST<br>			where	cast(RLSE_DTM / 1000000 as UNSIGNED) 		= $stDate<br>			group by MO_NO<br>		)<br>		, tmp as (<br>			select	a.ST_DATE<br>					, a.MO_NO<br>					, sum(a.ALARM_OCCUR_CNT)	as ALARM_OCCUR_CNT<br>					, sum(a.ALARM_RLSE_CNT) 	as ALARM_RLSE_CNT<br>			from 	datas a<br>			group by a.ST_DATE<br>					, a.MO_NO<br>		)<br>		select	tmp.*<br>				, $regUserNo		as REG_USER_NO<br>				, $regDtm			as REG_DTM<br>				, $chgUserNo		as CHG_USER_NO<br>				, $chgDtm			as CHG_DTM<br>		from 	tmp<br>								<br>		on duplicate key<br>		update 	ALARM_OCCUR_CNT 	= tmp.ALARM_OCCUR_CNT			<br>				, ALARM_RLSE_CNT 	= tmp.ALARM_RLSE_CNT<br>				, CHG_USER_NO		= $chgUserNo<br>				, CHG_DTM			= $chgDtm<br><br> <br>
*/
public final String MAKE_ALARM_STAT_MO = "MAKE_ALARM_STAT_MO";

}
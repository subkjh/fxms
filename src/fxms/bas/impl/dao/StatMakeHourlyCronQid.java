package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/cron/StatMakeHourlyCron.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class StatMakeHourlyCronQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/cron/StatMakeHourlyCron.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/cron/StatMakeHourlyCron.xml";

public StatMakeHourlyCronQid() { 
} 
/**
* para : $stDate, $stDate, $stDate, $stDate, $regUserNo, $regDtm, $chgUserNo, $chgDtm, $chgUserNo, $chgDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_AL_STAT_HH_ALCD (<br>				ST_DATE<br>				, HH<br>				, ALCD_NO<br>				, ALARM_OCCUR_CNT<br>				, ALARM_RLSE_CNT<br>				, REG_USER_NO<br>				, REG_DTM<br>				, CHG_USER_NO<br>				, CHG_DTM<br>		)<br>		with datas as (<br>		<br>			select	$stDate			as ST_DATE<br>					, TRUNCATE((OCCUR_DTM % 1000000) / 10000, 0) <br>									as HH<br>					, ALCD_NO		as ALCD_NO<br>					, count(1) 		as ALARM_OCCUR_CNT<br>					, 0				as ALARM_RLSE_CNT<br>			from 	FX_AL_ALARM_HST<br>			where	cast(OCCUR_DTM / 1000000 as UNSIGNED) 		= $stDate<br>			group by TRUNCATE((OCCUR_DTM % 1000000) / 10000, 0)<br>					, ALCD_NO<br>					<br><br>			union all<br>			<br>			select	$stDate			as ST_DATE<br>					, TRUNCATE((RLSE_DTM % 1000000) / 10000, 0) <br>									as HH<br>					, ALCD_NO		as ALCD_NO<br>					, 0				as ALARM_OCCUR_CNT<br>					, count(1) 		as ALARM_RLSE_CNT<br>			from 	FX_AL_ALARM_HST<br>			where	cast(RLSE_DTM / 1000000 as UNSIGNED) 		= $stDate<br>			group by TRUNCATE((RLSE_DTM % 1000000) / 10000, 0)<br>					, ALCD_NO<br>		)<br>		, tmp as (<br>			select	a.ST_DATE<br>					, a.HH<br>					, a.ALCD_NO<br>					, sum(a.ALARM_OCCUR_CNT)	as ALARM_OCCUR_CNT<br>					, sum(a.ALARM_RLSE_CNT) 	as ALARM_RLSE_CNT<br>			from 	datas a<br>			group by a.ST_DATE<br>					, a.HH<br>					, a.ALCD_NO<br>		)<br>		select	tmp.*<br>				, $regUserNo		as REG_USER_NO<br>				, $regDtm			as REG_DTM<br>				, $chgUserNo		as CHG_USER_NO<br>				, $chgDtm			as CHG_DTM<br>		from 	tmp<br>								<br>		on duplicate key<br>		update 	ALARM_OCCUR_CNT 	= tmp.ALARM_OCCUR_CNT			<br>				, ALARM_RLSE_CNT 	= tmp.ALARM_RLSE_CNT<br>				, CHG_USER_NO		= $chgUserNo				<br>				, CHG_DTM			= $chgDtm<br><br> <br>
*/
public final String MAKE_ALARM_STAT_HH_ALCD = "MAKE_ALARM_STAT_HH_ALCD";

/**
* para : $stDate, $stDate, $stDate, $stDate, $regUserNo, $regDtm, $chgUserNo, $chgDtm, $chgUserNo, $chgDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_AL_STAT_HH_INLO (<br>				ST_DATE<br>				, HH<br>				, INLO_NO<br>				, ALARM_OCCUR_CNT<br>				, ALARM_RLSE_CNT<br>				, REG_USER_NO<br>				, REG_DTM<br>				, CHG_USER_NO<br>				, CHG_DTM<br>		)<br>		with datas as (<br>		<br>			select	$stDate			as ST_DATE<br>					, TRUNCATE((OCCUR_DTM % 1000000) / 10000, 0) <br>									as HH<br>					, INLO_NO		as INLO_NO<br>					, count(1) 		as ALARM_OCCUR_CNT<br>					, 0				as ALARM_RLSE_CNT<br>			from 	FX_AL_ALARM_HST<br>			where	cast(OCCUR_DTM / 1000000 as UNSIGNED) 		= $stDate<br>			group by TRUNCATE((OCCUR_DTM % 1000000) / 10000, 0)<br>					, INLO_NO<br>					<br><br>			union all<br>			<br>			select	$stDate			as ST_DATE<br>					, TRUNCATE((RLSE_DTM % 1000000) / 10000, 0) <br>									as HH<br>					, INLO_NO		as INLO_NO<br>					, 0				as ALARM_OCCUR_CNT<br>					, count(1) 		as ALARM_RLSE_CNT<br>			from 	FX_AL_ALARM_HST<br>			where	cast(RLSE_DTM / 1000000 as UNSIGNED) 		= $stDate<br>			group by TRUNCATE((RLSE_DTM % 1000000) / 10000, 0)<br>					, INLO_NO<br>		)<br>		, tmp as (<br>			select	a.ST_DATE<br>					, a.HH<br>					, a.INLO_NO<br>					, sum(a.ALARM_OCCUR_CNT)	as ALARM_OCCUR_CNT<br>					, sum(a.ALARM_RLSE_CNT) 	as ALARM_RLSE_CNT<br>			from 	datas a<br>			group by a.ST_DATE<br>					, a.HH<br>					, a.INLO_NO<br>		)<br>		select	tmp.*<br>				, $regUserNo		as REG_USER_NO<br>				, $regDtm			as REG_DTM<br>				, $chgUserNo		as CHG_USER_NO<br>				, $chgDtm			as CHG_DTM<br>		from 	tmp<br>								<br>		on duplicate key<br>		update 	ALARM_OCCUR_CNT 	= tmp.ALARM_OCCUR_CNT			<br>				, ALARM_RLSE_CNT 	= tmp.ALARM_RLSE_CNT<br>				, CHG_USER_NO		= $chgUserNo<br>				, CHG_DTM			= $chgDtm<br><br> <br>
*/
public final String MAKE_ALARM_STAT_HH_INLO = "MAKE_ALARM_STAT_HH_INLO";

}
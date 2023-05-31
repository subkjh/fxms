package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/PsHandler.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class PsHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/PsHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/PsHandler.xml";

public PsHandlerQid() { 
} 
/**
* para : #psItem, #psItem, #psItem, #tableName, $moNo<br>
* result : RESULT_PS_VALUE=fxms.bas.vo.PsMinMaxAvgValue<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select	<br>				concat(substring(CONVERT(PS_DATE, CHAR), 1, 11), '000')<br>											as PS_DATE<br>				, min(#psItem)				as MIN_VALUE<br>				, max(#psItem)				as MAX_VALUE<br>				, avg(#psItem)				as AVG_VALUE<br>		from	#tableName<br>		where	1 = 1<br>		and		MO_NO 		= 	$moNo<br>		group by <br>				substring(CONVERT(PS_DATE, CHAR), 1, 11)<br><br> <br>
*/
public final String select_ps_value_min_max_avg_list = "select-ps-value-min-max-avg-list";

/**
* para : #psItem, #psItem, #psItem, #tableName, $moNo<br>
* result : RESULT_PS_VALUE=fxms.bas.vo.PsMinMaxAvgValue<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>with fx_co_time as (<br>			select 	CD_CODE			as HH<br>			from 	FX_CO_CD	<br>			where	CD_CLASS = 'PS_TIME_HOUR_CL_CD'<br>			order by	<br>					CD_CODE <br>		)<br>		, ps_value as (<br>				select	<br>						substring(CONVERT(PS_DATE, CHAR), 9, 2)		as HH<br>						, min(#psItem)								as MIN_VALUE<br>						, max(#psItem)								as MAX_VALUE<br>						, avg(#psItem)								as AVG_VALUE<br>				from	#tableName<br>				where	1 = 1<br>				and		MO_NO 		= 	$moNo<br>				group by <br>						substring(CONVERT(PS_DATE, CHAR), 9, 2)				<br>		)<br>		select	a.HH					as PS_DATE<br>				, b.MIN_VALUE			as MIN_VALUE<br>				, b.MAX_VALUE			as MAX_VALUE<br>				, b.AVG_VALUE			as AVG_VALUE<br>		from	fx_co_time a<br>				left join ps_value b on a.HH = b.HH<br><br> <br>
*/
public final String select_ps_value_min_max_hourly_list = "select-ps-value-min-max-hourly-list";

}
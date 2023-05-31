package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/service/AppService.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class AppServiceQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/service/AppService.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/service/AppService.xml";

public AppServiceQid() { 
} 
/**
* para : $loginDtmStart, $loginDtmEnd<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>update FX_UR_USER_ACCS_HST set<br>			ACCS_ST_CD 		= '2'<br>			, LOGOUT_DTM 	= DATE_FORMAT(now(), '%Y%m%d%H%i%s')<br>			, CHG_USER_NO 	= 0<br>			, CHG_DTM 		= DATE_FORMAT(now(), '%Y%m%d%H%i%s')<br>		where SESSION_ID IN (<br>			with DATAS as (<br>				select 	a.*<br>						, ( select 	max(strt_dtm) from FX_UR_USER_WORK_HST t where t.session_id = a.session_id ) as LAST_OP_DTM<br>				from 	FX_UR_USER_ACCS_HST a<br>				where 	a.LOGIN_DTM >= $loginDtmStart<br>				and		a.LOGIN_DTM <= $loginDtmEnd<br>				and 	a.ACCS_ST_CD = '0'<br>			)<br>			select 	a.SESSION_ID<br>			from 	DATAS a<br>			where 	LAST_OP_DTM is NULL <br>			or  	a.AUTO_LOGOUT_TIME_OUT < now() - STR_TO_DATE(LAST_OP_DTM, '%Y%m%d%H%i%s')<br>		)<br><br> <br>
*/
public final String update_auto_logout = "update_auto_logout";

}
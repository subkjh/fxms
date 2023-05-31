package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/UserHandler.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class UserHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/UserHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/UserHandler.xml";

public UserHandlerQid() { 
} 
/**
* para : $userNo$startLoginDtm$endLoginDtm<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select A.LOGIN_DTM<br>				, A.LOGOUT_DTM<br>				, A.ACCS_IP_ADDR<br>				, A.ACCS_MEDIA<br>				, A.ACCS_ST_CD<br>				, ( select max(CD_NAME) from FX_CO_CD where CD_CLASS = 'ACCS_ST_CD' and CD_CODE = A.ACCS_ST_CD )<br>								 	as ACCS_ST_NAME <br>		from 	FX_UR_USER_INLO 		U<br>				, FX_UR_USER_ACCS_HST 	A<br>				, FX_UR_USER 			B<br>		where	U.USER_NO 		= $userNo<br>		and		B.INLO_NO		= U.INLO_NO<br>		and		A.USER_NO		= B.USER_NO<br><br>and A.LOGIN_DTM >= $startLoginDtm<br><br>and A.LOGIN_DTM <= $endLoginDtm<br><br>order by<br>				A.LOGIN_DTM desc<br><br> <br>
*/
public final String select_user_access_hst_grid_list = "select-user-access-hst-grid-list";

/**
* para : $userNo$userId$userName$ugrpNo$inloNo, $inloNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.USER_NO<br>				, A.USER_ID<br>				, A.USER_NAME<br>				, A.USER_TEL_NO<br>				, A.USER_MAIL<br>				, A.USE_STRT_DATE<br>				, A.USE_END_DATE<br>				, A.USE_YN<br>				<br>				, A.UGRP_NO			<br>				, B.UGRP_NAME<br>				<br>				, A.INLO_NO<br>				, C.INLO_NAME<br><br>		from    FX_UR_USER_INLO U<br>				, FX_UR_USER  	A<br>				, FX_UR_UGRP 	B<br>				, FX_CF_INLO	C<br>		where	1 = 1<br>		and		U.USER_NO 	= $userNo<br>		and		A.INLO_NO 	= U.INLO_NO<br>		and		B.UGRP_NO	= A.UGRP_NO<br>		and		C.INLO_NO	= A.INLO_NO<br><br>and A.USER_ID = $userId<br><br>and A.USER_NAME = $userName<br><br>and A.UGRP_NO = $ugrpNo<br><br>and ( A.INLO_NO in ( select lower_inlo_no from<br>			FX_CF_INLO_MEM where INLO_NO = $inloNo ) or A.INLO_NO = $inloNo )<br><br> <br>
*/
public final String select_user_grid_list = "select-user-grid-list";

/**
* para : $userNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.UGRP_NO<br>				, A.UGRP_NAME<br>				, A.UGRP_DESC<br>				, A.INLO_NO<br>				, A1.INLO_NAME<br>				, A.REG_DTM<br>				, ( select count(1) from FX_UR_USER where UGRP_NO = A.UGRP_NO )<br>									as USER_COUNT<br>		from    FX_UR_USER_INLO U<br>				, FX_UR_UGRP  A<br>					left join FX_CF_INLO A1 on A1.INLO_NO = A.INLO_NO<br>		where	U.USER_NO 		= $userNo<br>		and		A.INLO_NO 		= U.INLO_NO<br>		and		A.UI_DISP_YN	= 'Y'<br>		order by<br>				A.UGRP_NAME<br><br> <br>
*/
public final String select_user_group_grid_list = "select-user-group-grid-list";

/**
* para : $ugrpNo<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.UPPER_OP_ID		as UPPER_OP_ID<br>				, A1.OP_NAME		as UPPER_OP_NAME<br>				, A.OP_ID			as OP_ID<br>				, A.OP_NAME			as OP_NAME<br>				, A.OP_DESC			as OP_DESC<br>				, A.OP_TYPE_CD		as OP_TYPE_CD<br>				, if ( A.UGRP_NO = 0, 'Y', if( is null(A2.OP_ID), 'N', 'Y') ) <br>									as OP_ENABLE<br>		from	FX_CO_OP	A<br>					left join FX_CO_OP 		A1 on A1.OP_ID = A.UPPER_OP_ID<br>					left join FX_UR_UGRP_OP A2 on A2.UGRP_NO = $ugrpNo and A2.OP_ID = A.OP_ID <br>		where 	1 = 1 <br>		order	by<br>				A1.OP_NAME<br>				, A.OP_NAME<br><br> <br>
*/
public final String select_user_group_op_grid_list = "select-user-group-op-grid-list";

/**
* para : $userNo$startStrtDtm$endStrtDtm<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.USER_NO<br>				, A.USER_NAME<br>				, A.STRT_DTM<br>				, A.OP_ID<br>				, A.OP_NAME<br>				, A.STRT_DTM<br>				, A.END_DTM<br>				, if(A.RST_NO = 0, 'SUCCESS', 'FAIL')<br>								as RST_ST_VAL<br>		from 	FX_UR_USER_INLO 		U<br>				, FX_UR_USER_WORK_HST 	A<br>				, FX_UR_USER 			B<br>		where	U.USER_NO 		= $userNo<br>		and		B.INLO_NO		= U.INLO_NO<br>		and		A.USER_NO		= B.USER_NO<br><br>and A.STRT_DTM >= $startStrtDtm<br><br>and A.STRT_DTM <= $endStrtDtm<br><br>order by<br>				A.STRT_DTM desc<br><br> <br>
*/
public final String select_user_work_hst_grid_list = "select-user-work-hst-grid-list";

/**
* para : $startApplyDate, $endApplyDate$applyUserId$applyUserName$newUserRegStCd$inloName<br>
* result : RESULT_MAP=java.util.HashMap<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.APPLY_DTM<br>				, A.APPLY_USER_ID<br>				, A.APPLY_USER_NAME<br>				, A.APPLY_USER_MAIL<br>				, A.APPLY_TEL_NO<br>				, A.APPLY_INLO_NO<br>				, a1.INLO_NAME<br>				, A.PROC_DTM	<br>				, A.PROC_RSN<br>				, A.NEW_USER_REG_ST_CD		as NEW_USER_REG_ST_CD 	<br>				, ( select CD_NAME from FX_CO_CD where CD_CLASS = 'NEW_USER_REG_ST_CD' and CD_CODE = A.NEW_USER_REG_ST_CD )<br>											as NEW_USER_REG_ST_NAME				<br>		from    FX_UR_USER_NEW_REQ  	A<br>					left join FX_CF_INLO	a1 on a1.INLO_NO = A.APPLY_INLO_NO<br>		where	1 = 1<br>		and		A.APPLY_DTM			>= concat($startApplyDate, '000000')<br>		and		A.APPLY_DTM			<= concat($endApplyDate, '235959')<br><br>and A.APPLY_USER_ID = $applyUserId<br><br>and A.APPLY_USER_NAME = $applyUserName<br><br>and A.NEW_USER_REG_ST_CD = $newUserRegStCd<br><br>and a1.INLO_NAME like concat('%', $inloName, '%')<br><br> <br>
*/
public final String select_user_new_apply_grid_list = "select_user_new_apply_grid_list";

}
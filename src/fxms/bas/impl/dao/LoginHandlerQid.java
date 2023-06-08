package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/handler/LoginHandler.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class LoginHandlerQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/handler/LoginHandler.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/handler/LoginHandler.xml";

public LoginHandlerQid() { 
} 
/**
* para : $userNo, $regDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>delete <br>		from 	FX_UR_USER_INLO<br>		where	USER_NO		= $userNo<br>		and		REG_DTM 	< $regDtm <br>		and		AUTO_REG_YN	= 'Y'<br><br> <br>
*/
public final String MAKE_USER_OBJECT__DELETE_INLO__EXPIRED = "MAKE_USER_OBJECT__DELETE_INLO__EXPIRED";

/**
* para : $userNo, $regDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>delete <br>		from 	FX_UR_USER_MO<br>		where	USER_NO		= $userNo<br>		and		REG_DTM 	< $regDtm <br>		and		AUTO_REG_YN	= 'Y'<br><br> <br>
*/
public final String MAKE_USER_OBJECT__DELETE_MO__EXPIRED = "MAKE_USER_OBJECT__DELETE_MO__EXPIRED";

/**
* para : $userNo, $regDtm, $regDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_UR_USER_INLO (<br>				USER_NO<br>				, INLO_NO<br>				, AUTO_REG_YN<br>				, REG_MEMO<br>				, REG_USER_NO<br>				, REG_DTM<br>		)<br>		select 	$userNo					as USER_NO<br>				, a.INLO_NO				as INLO_NO<br>				, 'Y'					as AUTO_REG_YN<br>				, 'BASIC'				as REG_MEMO<br>				, 0						as REG_USER_NO<br>				, $regDtm				as REG_DTM<br>		from 	FX_CF_INLO a<br>		where	a.INLO_NO = 0<br>		<br>		on duplicate key<br>		update REG_DTM = $regDtm<br><br> <br>
*/
public final String MAKE_USER_OBJECT__INLO_BASIC = "MAKE_USER_OBJECT__INLO_BASIC";

/**
* para : $regDtm, $userNo, $regDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_UR_USER_INLO (<br>				USER_NO<br>				, INLO_NO<br>				, AUTO_REG_YN<br>				, REG_MEMO<br>				, REG_USER_NO<br>				, REG_DTM<br>		)<br>		select 	A.USER_NO				as USER_NO<br>				, A.INLO_NO				as INLO_NO<br>				, 'Y'					as AUTO_REG_YN<br>				, 'USER.INLO_NO'		as REG_MEMO<br>				, 0						as REG_USER_NO<br>				, $regDtm				as REG_DTM<br>		from 	FX_UR_USER 			A<br>				, FX_CF_INLO		B<br>		where 	A.USER_NO 		= $userNo<br>		and		A.INLO_NO		= B.INLO_NO<br>		<br>		on duplicate key<br>		update REG_DTM = $regDtm<br><br> <br>
*/
public final String MAKE_USER_OBJECT__MERGE_INLO = "MAKE_USER_OBJECT__MERGE_INLO";

/**
* para : $regDtm, $userNo, $regDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_UR_USER_INLO (<br>				USER_NO<br>				, INLO_NO<br>				, AUTO_REG_YN<br>				, REG_MEMO				<br>				, REG_USER_NO<br>				, REG_DTM<br>		)<br>		select 	A.USER_NO				as USER_NO<br>				, B.LOWER_INLO_NO		as INLO_NO<br>				, 'Y'					as AUTO_REG_YN<br>				, 'INLO.LOWER_INLO_NO'	as REG_MEMO<br>				, 0						as REG_USER_NO<br>				, $regDtm				as REG_DTM<br>		from 	FX_UR_USER 			A<br>				, FX_CF_INLO_MEM 	B<br>				, FX_CF_INLO		C<br>		where 	A.USER_NO 			= $userNo<br>		and		A.INLO_NO 			= B.INLO_NO<br>		and		B.INLO_NO			= C.INLO_NO<br><br>		on duplicate key<br>		update REG_DTM = $regDtm<br><br> <br>
*/
public final String MAKE_USER_OBJECT__MERGE_INLO_LOWER = "MAKE_USER_OBJECT__MERGE_INLO_LOWER";

/**
* para : $userNo, $regDtm, $userNo, $regDtm<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_UR_USER_MO (<br>				USER_NO<br>				, MO_NO<br>				, AUTO_REG_YN<br>				, REG_MEMO <br>				, REG_USER_NO<br>				, REG_DTM<br>		)<br>		select 	$userNo			as USER_NO<br>				, B.MO_NO		as MO_NO<br>				, 'Y'			as AUTO_REG_YN<br>				, C.INLO_NAME	as REG_MEMO<br>				, 0				as REG_USER_NO<br>				, $regDtm		as REG_DTM<br>		from 	FX_UR_USER_INLO		A<br>				, FX_MO				B<br>				, FX_CF_INLO		C<br>		where 	A.USER_NO		= $userNo<br>		and		B.INLO_NO		= A.INLO_NO<br>		and		B.UPPER_MO_NO	<= 0<br>		and		C.INLO_NO		= B.INLO_NO<br>		<br>		on duplicate key<br>		update REG_DTM = $regDtm<br><br> <br>
*/
public final String MAKE_USER_OBJECT__MERGE_MO = "MAKE_USER_OBJECT__MERGE_MO";

}
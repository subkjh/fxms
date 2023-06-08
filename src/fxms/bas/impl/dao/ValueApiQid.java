package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/api/ValueApi.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class ValueApiQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/api/ValueApi.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/api/ValueApi.xml";

public ValueApiQid() { 
} 
/**
* para : $getMoNo(), $getMoInstance(), $getPsId(), $getPreCollVal(), $getCurCollVal(), $getPreCollDtm(), $getCurCollDtm(), $getCurCollVal(), $getCurCollDtm()<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>insert into FX_V_ACUR                 ' 성능수집최신값 '<br>		(<br>			  MO_NO                     ' MO번호 ' <br>			, MO_INSTANCE               ' MO인스턴스 ' <br>			, PS_ID                     ' 성능ID ' <br>			, PRE_COLL_VAL              ' 이전수집값 ' <br>			, CUR_COLL_VAL              ' 최근수집값 ' <br>			, PRE_COLL_DTM              ' 이전수집일시 ' <br>			, CUR_COLL_DTM              ' 최근수집일시 ' <br>		) values ( <br>			 $getMoNo()<br>			, $getMoInstance()<br>			, $getPsId()<br>			, $getPreCollVal()<br>			, $getCurCollVal()<br>			, $getPreCollDtm()<br>			, $getCurCollDtm()<br>		)<br>		ON DUPLICATE KEY UPDATE<br>			PRE_COLL_VAL 	= CUR_COLL_VAL<br>			, PRE_COLL_DTM 	= CUR_COLL_DTM<br>			, CUR_COLL_VAL 	= $getCurCollVal()<br>			, CUR_COLL_DTM 	= $getCurCollDtm()<br><br> <br>
*/
public final String INSERT_FX_V_ACUR = "INSERT_FX_V_ACUR";

}
package fxms.bas.impl.dao;

/**
* File : deploy/conf/sql/fxms/bas/api/RuleApi.xml<br>
* @since 20230607165339
* @author subkjh 
*
*/


public class RuleApiQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/api/RuleApi.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/api/RuleApi.xml";

public RuleApiQid() { 
} 
/**
* para : <br>
* result : FX_BR_RULE=fxms.rule.dbo.all.FX_BR_RULE<br>
* ---------------------------------------------------------------------------------- <br>
* database : null<br>
* sql <br><br>
 * <br>select 	A.*<br>		from    FX_BR_RULE A<br>		where	exists ( select 1 from FX_BR_RULE_RUN_HST A1 where A1.RUN_FNSH_DTM is null and A1.BR_RULE_NO = A.BR_RULE_NO)<br><br> <br>
*/
public final String select_rule_to_run = "select-rule-to-run";

}
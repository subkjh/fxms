package subkjh.dao.queries;

/**
* File : deploy/conf/sql/fxms/bas/def/queries.xml<br>
* @since 20230523152416
* @author subkjh 
*
*/


public class QueriesQid { 

/** 쿼리 모임 화일명. deploy/conf/sql/fxms/bas/def/queries.xml*/
public static final String QUERY_XML_FILE = "deploy/conf/sql/fxms/bas/def/queries.xml";

public QueriesQid() { 
} 
/**
* para : $seqName, $valueMin, $valueMax, $valueNext, $incBy, $isCycle<br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>insert into FX_TBL_SEQ (<br>			  	SEQ_NAME<br>			  	, VALUE_MIN<br>			  	, VALUE_MAX<br>			  	, VALUE_NEXT<br>			  	, INC_BY<br>			  	, IS_CYCLE<br>		) values (<br>				$seqName<br>				, $valueMin<br>				, $valueMax<br>				, $valueNext<br>				, $incBy<br>				, $isCycle<br>		)<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>insert into FX_TBL_SEQ (<br>			  	SEQ_NAME<br>			  	, VALUE_MIN<br>			  	, VALUE_MAX<br>			  	, VALUE_NEXT<br>			  	, INC_BY<br>			  	, IS_CYCLE<br>		) values (<br>				$seqName<br>				, $valueMin<br>				, $valueMax<br>				, $valueNext<br>				, $incBy<br>				, $isCycle<br>		)<br><br> <br>
*/
public final String CREATE_SEQUENCE = "CREATE_SEQUENCE";

/**
* para : <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>delete	<br>		from 	FX_TBL_SEQ<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>delete	<br>		from 	FX_TBL_SEQ<br><br> <br>
*/
public final String DELETE_SEQUENCE_ALL = "DELETE_SEQUENCE_ALL";

/**
* para : <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>grant all on vup.* to 'vup'@'%' identified by 'vup';<br>		flush privileges;<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>grant all on vup.* to 'vup'@'%' identified by 'vup';<br>		flush privileges;<br><br> <br>
*/
public final String GRANT_1 = "GRANT_1";

/**
* para : <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>create table if not exists FX_TBL_SEQ ( <br>			  SEQ_NAME                       varchar(20) binary  not null  COMMENT '시퀀스명'<br>			, INC_VAL                        int not null  COMMENT '증가값'<br>			, MIN_VAL                        bigint not null  COMMENT '최소값'<br>			, MAX_VAL                        bigint not null  COMMENT '최대값'<br>			, NEXT_VAL                       bigint not null  COMMENT '다음값'<br>			, CYCLE_YN                       char(1) not null  COMMENT '순환여부'<br>			, REG_USER_NO                    int default 0 COMMENT '등록사용자번호'<br>			, REG_DTM                        bigint COMMENT '등록일시'<br>			, CHG_USER_NO                    int default 0 COMMENT '수정사용자번호'<br>			, CHG_DTM                        bigint COMMENT '수정일시'<br>		<br>			, constraint FX_TBL_SEQ__PK                   primary key ( SEQ_NAME )<br>		 ) ENGINE=InnoDB COMMENT '테이블시퀀스테이블'<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>create table if not exists FX_TBL_SEQ ( <br>			  SEQ_NAME                       varchar(20) binary  not null  COMMENT '시퀀스명'<br>			, INC_VAL                        int not null  COMMENT '증가값'<br>			, MIN_VAL                        bigint not null  COMMENT '최소값'<br>			, MAX_VAL                        bigint not null  COMMENT '최대값'<br>			, NEXT_VAL                       bigint not null  COMMENT '다음값'<br>			, CYCLE_YN                       char(1) not null  COMMENT '순환여부'<br>			, REG_USER_NO                    int default 0 COMMENT '등록사용자번호'<br>			, REG_DTM                        bigint COMMENT '등록일시'<br>			, CHG_USER_NO                    int default 0 COMMENT '수정사용자번호'<br>			, CHG_DTM                        bigint COMMENT '수정일시'<br>		<br>			, constraint FX_TBL_SEQ__PK                   primary key ( SEQ_NAME )<br>		 ) ENGINE=InnoDB COMMENT '테이블시퀀스테이블'<br><br> <br>
*/
public final String INIT_SEQUENCE__1 = "INIT_SEQUENCE__1";

/**
* para : <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>drop function if exists nextval<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>drop function if exists nextval<br><br> <br>
*/
public final String INIT_SEQUENCE__2 = "INIT_SEQUENCE__2";

/**
* para : <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>create function nextval (name varchar(100))<br>		<br>			RETURNS bigint(20) READS SQL DATA<br>		<br>		BEGIN<br>    			DECLARE cur_val bigint(20);<br>				<br>				select<br>						NEXT_VAL into 	cur_val<br>				from<br>						FX_TBL_SEQ<br>				where<br>						SEQ_NAME = name<br>    			;<br> <br>    			IF cur_val IS NOT NULL THEN<br>        			UPDATE<br>						FX_TBL_SEQ<br>					SET<br>						NEXT_VAL = IF (<br>											(NEXT_VAL + INC_VAL) > MAX_VAL<br>											, IF ( CYCLE_YN = 'Y', MIN_VAL, NULL )<br>                							, NEXT_VAL + INC_VAL<br>            							)<br>        			WHERE<br>            			SEQ_NAME = name<br>        			;<br>    			END IF;<br>		    RETURN cur_val;<br>		END<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>create function nextval (name varchar(100))<br>		<br>			RETURNS bigint(20) READS SQL DATA<br>		<br>		BEGIN<br>    			DECLARE cur_val bigint(20);<br>				<br>				select<br>						NEXT_VAL into 	cur_val<br>				from<br>						FX_TBL_SEQ<br>				where<br>						SEQ_NAME = name<br>    			;<br> <br>    			IF cur_val IS NOT NULL THEN<br>        			UPDATE<br>						FX_TBL_SEQ<br>					SET<br>						NEXT_VAL = IF (<br>											(NEXT_VAL + INC_VAL) > MAX_VAL<br>											, IF ( CYCLE_YN = 'Y', MIN_VAL, NULL )<br>                							, NEXT_VAL + INC_VAL<br>            							)<br>        			WHERE<br>            			SEQ_NAME = name<br>        			;<br>    			END IF;<br>		    RETURN cur_val;<br>		END<br><br> <br>
*/
public final String INIT_SEQUENCE__3 = "INIT_SEQUENCE__3";

/**
* para : $tableName, $user<br>
* result : RID_TABLE_COMMENT=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	T.TABLE_NAME		COMMENTS<br>		from 	SYSTEM_.SYS_TABLES_ T<br>				, SYSTEM_.SYS_USERS_ U<br>		where 	T.TABLE_NAME = $tableName<br>		and		T.USER_ID = U.USER_ID<br>		and		U.USER_NAME = $user<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select	TABLE_COMMENT<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select	TABLE_COMMENT<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>SELECT <br>				COMMENTS<br>		FROM <br>				USER_TAB_COMMENTS<br>		WHERE <br>				TABLE_NAME = $tableName<br><br> <br>
*/
public final String QID_SELECT_TABLE_COMMENT = "QID_SELECT_TABLE_COMMENT";

/**
* para : $tableName, $user, $tableName, $user<br>
* result : RID_TABLE_INDEX=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select	INDEX_NAME<br>				, IDX_TYPE<br>				, COLUMN_NAME<br>		from ( <br>				select 	IDX.INDEX_NAME<br>						, decode(CONSTRAINT_NAME, null, decode(IS_UNIQUE, 'T', 'UK', 'KEY'), 'PK') IDX_TYPE<br>						,  C.COLUMN_NAME<br>						, IDXC.INDEX_COL_ORDER ORDERBY<br>				from 	SYSTEM_.SYS_INDICES_ IDX<br>							left join SYSTEM_.SYS_CONSTRAINTS_ SC on SC.CONSTRAINT_NAME = IDX.INDEX_NAME and SC.TABLE_ID = IDX.TABLE_ID<br>						, SYSTEM_.SYS_INDEX_COLUMNS_ IDXC<br>						, SYSTEM_.SYS_COLUMNS_ C<br>				where 	IDX.TABLE_ID in (	select 	T.TABLE_ID <br>											from 	SYSTEM_.SYS_TABLES_ T<br>													, SYSTEM_.SYS_USERS_ U<br>											where 	T.TABLE_NAME = $tableName<br>											and		T.USER_ID = U.USER_ID<br>											and		U.USER_NAME = $user				<br>										)<br>				and 	IDX.INDEX_ID = IDXC.INDEX_ID<br>				and 	C.COLUMN_ID = IDXC.COLUMN_ID					<br>				<br>				union all<br>		<br>				select 	IDX.CONSTRAINT_NAME INDEX_NAME<br>						, 'FK' IDX_TYPE<br>						,  CC2.COLUMN_NAME || ':' || C.COLUMN_NAME || '@' || T.TABLE_NAME<br>						, IDXC.INDEX_COL_ORDER ORDERBY<br>				from 	SYSTEM_.SYS_CONSTRAINTS_ IDX<br>						, SYSTEM_.SYS_CONSTRAINT_COLUMNS_ CC<br>						, SYSTEM_.SYS_INDEX_COLUMNS_ IDXC<br>						, SYSTEM_.SYS_COLUMNS_ C<br>						, SYSTEM_.SYS_TABLES_ T<br>						, SYSTEM_.SYS_COLUMNS_ CC2<br>				where 	<br>					IDX.TABLE_ID in (	select 	T.TABLE_ID <br>										from 	SYSTEM_.SYS_TABLES_ T<br>												, SYSTEM_.SYS_USERS_ U<br>										where 	T.TABLE_NAME = $tableName<br>										and		T.USER_ID = U.USER_ID<br>										and		U.USER_NAME = $user					<br>									)<br>				and 	CONSTRAINT_TYPE = 0<br>				and 	IDX.REFERENCED_INDEX_ID = IDXC.INDEX_ID<br>				and 	C.COLUMN_ID = IDXC.COLUMN_ID		<br>				and 	T.TABLE_ID = IDX.REFERENCED_TABLE_ID<br>				and 	CC.CONSTRAINT_ID = IDX.CONSTRAINT_ID<br>				and 	CC2.COLUMN_ID = CC.COLUMN_ID<br>			)<br>			<br>		order by <br>				INDEX_NAME<br>				, ORDERBY<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : informix<br>
* sql <br><br>
 * <br>SELECT 	IDXNAME						INDEX_NAME,<br>				'KEY'						IDX_TYPE,<br>       			''							COLUMN_NAME<br>		FROM 	<br>				INFORMIX.SYSINDEXES I,<br>				INFORMIX.SYSTABLES T<br>		WHERE 	<br>				T.TABNAME = '테이블 지정 안함 왜냐구 인덱스 구하기 싫으니깐..'<br>		AND		I.TABID = T.TABID<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select	kcu.table_name					as TABLE_NAME 	<br>				, kcu.CONSTRAINT_NAME 			as INDEX_NAME<br>   				, tc.CONSTRAINT_TYPE 			as INDEX_TYPE<br>   				, kcu.COLUMN_NAME				as COLUMN_NAME<br>   				, kcu.REFERENCED_TABLE_NAME 	as FK_TABLE_NAME<br>   				, kcu.REFERENCED_COLUMN_NAME 	as FK_COLUMN_NAME<br>		from 	information_schema.KEY_COLUMN_USAGE kcu<br>				, information_schema.TABLE_CONSTRAINTS tc <br>		where 	kcu.table_name = $tableName<br>		and		tc.table_name = $tableName<br>		and		tc.TABLE_SCHEMA = $dbName<br>		and		kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select	kcu.table_name					as TABLE_NAME 	<br>				, kcu.CONSTRAINT_NAME 			as INDEX_NAME<br>   				, tc.CONSTRAINT_TYPE 			as INDEX_TYPE<br>   				, kcu.COLUMN_NAME				as COLUMN_NAME<br>   				, kcu.REFERENCED_TABLE_NAME 	as FK_TABLE_NAME<br>   				, kcu.REFERENCED_COLUMN_NAME 	as FK_COLUMN_NAME<br>		from 	information_schema.KEY_COLUMN_USAGE kcu<br>				, information_schema.TABLE_CONSTRAINTS tc <br>		where 	kcu.table_name = $tableName<br>		and		tc.table_name = $tableName<br>		and		tc.TABLE_SCHEMA = $dbName<br>		and		kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select 	INDEX_NAME<br>				, IDX_TYPE<br>				, COLUMN_NAME<br>		from ( <br>				select 	IDX.INDEX_NAME<br>						, DECODE(C.CONSTRAINT_TYPE, NULL, decode(IDX.UNIQUENESS, 'UNIQUE', 'UK', 'KEY'), 'P', 'PK', 'R', 'FK', 'KEY' ) IDX_TYPE    <br>		       			, COL.COLUMN_NAME<br>		       			, COL.COLUMN_POSITION<br>				from 	<br>						USER_INDEXES IDX<br>							left join USER_CONSTRAINTS C on C.CONSTRAINT_NAME = IDX.INDEX_NAME<br>						, USER_IND_COLUMNS COL<br>				where   IDX.INDEX_NAME = COL.INDEX_NAME<br>		    	and 	IDX.TABLE_NAME = $tableName<br>				<br>						<br>				union all<br>				<br>				select 	C.CONSTRAINT_NAME										INDEX_NAME<br>						, 'FK' 													IDX_TYPE<br>						, COL.COLUMN_NAME || '@' || IDX.TABLE_NAME				COLUMN_NAME	<br>						, COL.COLUMN_POSITION<br>				from 	USER_CONSTRAINTS C<br>						, USER_INDEXES IDX<br>						, USER_IND_COLUMNS COL<br>				where 	C.TABLE_NAME = $tableName<br>				and 	C.CONSTRAINT_TYPE = 'R'<br>				and 	IDX.INDEX_NAME = C.R_CONSTRAINT_NAME<br>				and 	COL.INDEX_NAME = IDX.INDEX_NAME		<br>			)<br>		order by INDEX_NAME, COLUMN_POSITION<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : sqlserver<br>
* sql <br><br>
 * <br>SELECT 	CONSTRAINT_NAME				INDEX_NAME,<br>				'KEY'							IDX_TYPE,<br>       			COLUMN_NAME					COLUMN_NAME<br>		FROM 	<br>				information_schema.key_column_usage<br>		WHERE 	<br>				TABLE_NAME = $tableName<br><br> <br>
*/
public final String QID_SELECT_TABLE_INDEX = "QID_SELECT_TABLE_INDEX";

/**
* para : $tableName, $user<br>
* result : RESULT_TABLE_INFO=subkjh.dao.def.Column<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	COLUMN_ORDER				COLUMN_NO<br>				, COLUMN_NAME<br>    			, 'N'						IS_PK<br>    			, DECODE(IS_NULLABLE, 'T', 'Y', 'N')						<br>    										IS_NULLABLE<br>    			, '' 						COMMENTS<br>    			, decode(DATA_TYPE<br>    					, 2, 'NUMERIC'<br>    					, 1, 'CHAR'<br>    					, 'VARCHAR')		DATA_TYPE<br>    			, NVL(PRECISION, 0)			DATA_LENGTH<br>    			, NVL(SCALE, 0)				DATA_SCALE<br>    			, DEFAULT_VAL			 	DATA_DEFAULT    <br>		from <br>				SYSTEM_.SYS_COLUMNS_<br>		where <br>				TABLE_ID in ( 	select 	T.TABLE_ID <br>								from 	SYSTEM_.SYS_TABLES_ T<br>										, SYSTEM_.SYS_USERS_ U<br>								where 	T.TABLE_NAME = $tableName<br>								and		T.USER_ID = U.USER_ID<br>								and		U.USER_NAME = $user								<br>								)<br>		order by <br>				COLUMN_ORDER<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : informix<br>
* sql <br><br>
 * <br>SELECT 	<br>				COLNO						COLUMN_NO,<br>    			COLNAME						COLUMN_NAME,<br>    			'N'							IS_PK,<br>    			'Y'							IS_NULLABLE,<br>    			'' 							COMMENTS,<br>    			COLTYPE						DATA_TYPE,<br>    			COLLENGTH        			DATA_LENGTH,<br>    			0							DATA_SCALE,<br>    			''			 				DATA_DEFAULT    <br>		FROM <br>				INFORMIX.SYSCOLUMNS COL,<br>				INFORMIX.SYSTABLES TBL<br>		WHERE <br>				TBL.TABNAME = $tableName<br>		AND		COL.TABID = TBL.TABID<br>		ORDER BY <br>				COLNO<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select 	ORDINAL_POSITION			COLUMN_NO<br>				, upper(COLUMN_NAME)		COLUMN_NAME<br>				, if(COLUMN_KEY = 'PRI', 'Y', 'N') <br>    										IS_PK<br>				, IS_NULLABLE<br>				, COLUMN_COMMENT			COMMENTS<br>				, DATA_TYPE<br>				, ifnull(CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION)             <br>    										DATA_LENGTH<br>    			, ifnull(NUMERIC_SCALE, -1)	DATA_SCALE<br>    			, COLUMN_DEFAULT 				DATA_DEFAULT    <br>		from	INFORMATION_SCHEMA.COLUMNS<br>		where	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br>		order by <br>				ORDINAL_POSITION<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select 	ORDINAL_POSITION			COLUMN_NO<br>				, upper(COLUMN_NAME)		COLUMN_NAME<br>				, if(COLUMN_KEY = 'PRI', 'Y', 'N') <br>    										IS_PK<br>				, IS_NULLABLE<br>				, COLUMN_COMMENT			COMMENTS<br>				, DATA_TYPE<br>				, ifnull(CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION)             <br>    										DATA_LENGTH<br>    			, ifnull(NUMERIC_SCALE, -1)	DATA_SCALE<br>    			, COLUMN_DEFAULT 				DATA_DEFAULT    <br>		from	INFORMATION_SCHEMA.COLUMNS<br>		where	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br>		order by <br>				ORDINAL_POSITION<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>SELECT <br>				a.COLUMN_ID				COLUMN_NO,<br>				a.COLUMN_NAME,<br>				decode(b.CONSTRAINT_TYPE,null,'N','Y') IS_PK, <br>				a.NULLABLE IS_NULLABLE,<br>				c.COMMENTS,<br>				a.DATA_TYPE,<br>				NVL(a.DATA_PRECISION, a.DATA_LENGTH ) DATA_LENGTH,<br>				NVL(a.DATA_SCALE, -1) DATA_SCALE,<br>				a.DATA_DEFAULT<br>		FROM <br>				USER_TAB_COLUMNS a, <br>				(	SELECT <br>							a.TABLE_NAME,<br>							a.COLUMN_NAME,<br>							b.CONSTRAINT_TYPE<br>					FROM <br>							USER_CONS_COLUMNS a, <br>							USER_CONSTRAINTS b<br>					WHERE <br>							a.TABLE_NAME = b.TABLE_NAME<br>					AND <br>							a.CONSTRAINT_NAME = b.CONSTRAINT_NAME<br>					AND <br>							b.CONSTRAINT_TYPE='P'<br>				) b, USER_COL_COMMENTS c<br>		WHERE <br>				a.TABLE_NAME = b.TABLE_NAME (+)<br>		AND <br>				a.COLUMN_NAME = b.COLUMN_NAME (+)<br>		AND <br>				a.TABLE_NAME = c.TABLE_NAME (+)<br>		AND <br>				a.COLUMN_NAME = c.COLUMN_NAME (+)<br>		AND <br>				a.TABLE_NAME = $tableName<br>		ORDER BY <br>				a.COLUMN_ID<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : sqlserver<br>
* sql <br><br>
 * <br>SELECT 	<br>				ORDINAL_POSITION				COLUMN_NO,<br>    			COLUMN_NAME						COLUMN_NAME,<br>    			'N'								IS_PK,<br>    			IS_NULLABLE						IS_NULLABLE,<br>    			'' 								COMMENTS,<br>    			DATA_TYPE						DATA_TYPE,<br>    			ISNULL(NUMERIC_PRECISION, 0)	DATA_LENGTH,<br>    			ISNULL(NUMERIC_SCALE, 0)		DATA_SCALE,<br>    			''			 					DATA_DEFAULT    <br>		FROM <br>				information_schema.COLUMNS <br>		WHERE <br>				TABLE_NAME = $tableName<br>		ORDER BY <br>				ORDINAL_POSITION<br><br> <br>
*/
public final String QID_SELECT_TABLE_INFO = "QID_SELECT_TABLE_INFO";

/**
* para : #tableName<br>
* result : RID_TABLE_SCHEMA=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>show create table #tableName<br><br> <br>
*/
public final String QID_SELECT_TABLE_SCHEMA = "QID_SELECT_TABLE_SCHEMA";

/**
* para : $tableName, $user<br>
* result : RESULT_TABLE_NAME=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	COLUMN_NAME<br>		from 	SYSTEM_.SYS_COLUMNS_<br>		where	TABLE_ID in ( 	select 	T.TABLE_ID <br>								from 	SYSTEM_.SYS_TABLES_ T<br>										, SYSTEM_.SYS_USERS_ U<br>								where 	upper(T.TABLE_NAME) = $tableName<br>								and		T.USER_ID = U.USER_ID<br>								and		U.USER_NAME = $user								<br>								)<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select 	upper(COLUMN_NAME)	as COLUMN_NAME<br>		from	INFORMATION_SCHEMA.COLUMNS<br>		where	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select 	upper(COLUMN_NAME)	as COLUMN_NAME<br>		from	INFORMATION_SCHEMA.COLUMNS<br>		where	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select 	COLUMN_NAME<br>		from 	USER_TAB_COLUMNS<br>		where	upper(TABLE_NAME) = $tableName<br><br> <br>
*/
public final String SELECT_COLUMN_NAME__BY_TABLE = "SELECT_COLUMN_NAME__BY_TABLE";

/**
* para : $tableName, $tableName, $dbName<br>
* result : RESULT_TABLE_NAME=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select 	kcu.CONSTRAINT_NAME 			INDEX_NAME<br>			from 	information_schema.KEY_COLUMN_USAGE kcu<br>					, information_schema.TABLE_CONSTRAINTS tc <br>			where 	kcu.table_name = $tableName<br>			and		tc.table_name = $tableName<br>			and		tc.TABLE_SCHEMA = $dbName<br>			and		kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select 	INDEX_NAME<br>		from 	USER_INDEXES<br>		where	upper(INDEX_NAME) = $indexName<br><br> <br>
*/
public final String SELECT_INDEX_NAME__BY_NAME = "SELECT_INDEX_NAME__BY_NAME";

/**
* para : $user, $tableName<br>
* result : RESULT_TABLE_NAME=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	IDX.INDEX_NAME<br>		from 	SYSTEM_.SYS_INDICES_ IDX<br>				, SYSTEM_.SYS_TABLES_ T<br>				, SYSTEM_.SYS_USERS_ U	<br>		where   U.USER_NAME = $user<br>		and		T.USER_ID = U.USER_ID<br>		and		IDX.TABLE_ID = T.TABLE_ID<br>		and		upper(T.TABLE_NAME) = $tableName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select 	kcu.CONSTRAINT_NAME 			INDEX_NAME<br>			from 	information_schema.KEY_COLUMN_USAGE kcu<br>					, information_schema.TABLE_CONSTRAINTS tc <br>			where 	kcu.table_name = $tableName<br>			and		tc.table_name = $tableName<br>			and		tc.TABLE_SCHEMA = $dbName<br>			and		kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select 	INDEX_NAME<br>		from 	USER_INDEXES<br>		where	upper(TABLE_NAME) = $tableName<br><br> <br>
*/
public final String SELECT_INDEX_NAME__BY_TABLE = "SELECT_INDEX_NAME__BY_TABLE";

/**
* para : $user<br>
* result : RESULT_DB_SEQUENCE=subkjh.dao.def.Sequence<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select	TABLE_NAME 			SEQUENCE_NAME<br>       			, MIN_SEQ 			VALUE_MIN<br>      			, MAX_SEQ 			VALUE_MAX<br>       			, INCREMENT_SEQ 	INC_BY<br>				, IS_CYCLE<br> 		from 	V\\$SEQ S<br> 				, SYSTEM_.SYS_USERS_ U<br>       			, SYSTEM_.SYS_TABLES_ T<br> 		where	S.SEQ_OID = T.table_oid<br>  		and 	T.user_id = U.user_id<br>   		and 	U.USER_NAME = $user<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select	SEQ_NAME			SEQUENCE_NAME		<br>       			, VALUE_MIN<br>      			, VALUE_MAX<br>       			, INC_BY<br>				, IS_CYCLE<br>				, VALUE_NEXT<br> 		from 	FX_TBL_SEQ<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select	SEQ_NAME			SEQUENCE_NAME		<br>       			, VALUE_MIN<br>      			, VALUE_MAX<br>       			, INC_BY<br>				, IS_CYCLE<br>				, VALUE_NEXT<br> 		from 	FX_TBL_SEQ<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select	SEQUENCE_NAME		<br>       			, MIN_VALUE			VALUE_MIN<br>      			, MAX_VALUE			VALUE_MAX<br>       			, INCREMENT_BY	 	INC_BY<br>				, CYCLE_FLAG		IS_CYCLE<br>				, LAST_NUMBER + INCREMENT_BY			VALUE_NEXT<br> 		from 	USER_SEQUENCES<br><br> <br>
*/
public final String SELECT_SEQUENCE__ALL = "SELECT_SEQUENCE__ALL";

/**
* para : $user<br>
* result : RESULT_TABLE_NAME=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	TABLE_NAME<br>		from 	SYSTEM_.SYS_TABLES_	T<br>				, SYSTEM_.SYS_USERS_ U	<br>		where   U.USER_NAME = $user<br>		and		T.USER_ID = U.USER_ID<br>		and		T.TABLE_TYPE = 'T'<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select 	TABLE_NAME<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where	TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select 	TABLE_NAME<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where	TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>SELECT 	TABLE_NAME<br>		FROM 	ALL_TABLES<br><br> <br>
*/
public final String SELECT_TABLE_NAME__ALL = "SELECT_TABLE_NAME__ALL";

/**
* para : $user, $tableName<br>
* result : RESULT_TABLE_NAME=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	TABLE_NAME<br>		from 	SYSTEM_.SYS_TABLES_	T<br>				, SYSTEM_.SYS_USERS_ U	<br>		where   U.USER_NAME = $user<br>		and		T.USER_ID = U.USER_ID<br>		and		upper(T.TABLE_NAME) = $tableName<br>		and		T.TABLE_TYPE = 'T'<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select 	upper(TABLE_NAME)	as TABLE_NAME<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where 	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select 	upper(TABLE_NAME)	as TABLE_NAME<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where 	upper(TABLE_NAME) = $tableName<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>SELECT 	TABLE_NAME<br>		FROM 	ALL_TABLES<br>		WHERE	TABLE_NAME = $tableName<br><br> <br>
*/
public final String SELECT_TABLE_NAME__BY_NAME = "SELECT_TABLE_NAME__BY_NAME";

/**
* para : $user, #tableName<br>
* result : RESULT_TABLE_NAME=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	TABLE_NAME<br>		from 	SYSTEM_.SYS_TABLES_	T<br>				, SYSTEM_.SYS_USERS_ U	<br>		where   U.USER_NAME = $user<br>		and		T.USER_ID = U.USER_ID<br>		and		upper(T.TABLE_NAME) like '%#tableName%'<br>		and		T.TABLE_TYPE = 'T'<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mariadb<br>
* sql <br><br>
 * <br>select 	upper(TABLE_NAME)	as TABLE_NAME<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where 	upper(TABLE_NAME) like '%#tableName%'<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : mysql<br>
* sql <br><br>
 * <br>select 	upper(TABLE_NAME)	as TABLE_NAME<br>		from 	INFORMATION_SCHEMA.TABLES<br>		where 	upper(TABLE_NAME) like '%#tableName%'<br>		and		TABLE_SCHEMA = $dbName<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select 	TABLE_NAME<br>		from 	ALL_TABLES<br>		where 	upper(TABLE_NAME) like '%#tableName%'<br><br> <br>
*/
public final String SELECT_TABLE_NAME__LIKE = "SELECT_TABLE_NAME__LIKE";

/**
* para : $VERSION<br>
* result : RESULT_VERSION=java.lang.String<br>
* ---------------------------------------------------------------------------------- <br>
* database : altibase<br>
* sql <br><br>
 * <br>select 	product_version<br>		from 	V$VERSION<br><br> <br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select 	* <br>		from 	v$version <br>		where 	banner LIKE 'Oracle%'<br><br> <br>
*/
public final String SELECT_VERSION = "SELECT_VERSION";

/**
* para : <br>
* result : RESULT_DB_VIEW=subkjh.dao.def.View<br>
* ---------------------------------------------------------------------------------- <br>
* database : oracle<br>
* sql <br><br>
 * <br>select	VIEW_NAME		NAME<br>				, TEXT			QUERY<br> 		from 	USER_VIEWS<br><br> <br>
*/
public final String SELECT_VIEW__ALL = "SELECT_VIEW__ALL";

}
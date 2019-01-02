package subkjh.bas.dao.control;

/**
 * File : queries.xml<br>
 * 
 * @since 2015-06-25 10:50:21
 * @author subkjh
 * 
 */

public class QueriesQid {

	/** 쿼리 모임 화일명. queries.xml */
	public static final String QUERY_XML_FILE = "queries.xml";

	public QueriesQid() {
	}

	/**
	 * para : $seqName, $valueMin, $valueMax, $valueNext, $incBy, $isCycle<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : maria<br>
	 * sql <br>
	 * <br>
	 * insert into SEQUENCE.SEQ_TAB (<br>
	 * SEQ_NAME<br>
	 * , VALUE_MIN<br>
	 * , VALUE_MAX<br>
	 * , VALUE_NEXT<br>
	 * , INC_BY<br>
	 * , IS_CYCLE<br>
	 * ) values (<br>
	 * $seqName<br>
	 * , $valueMin<br>
	 * , $valueMax<br>
	 * , $valueNext<br>
	 * , $incBy<br>
	 * , $isCycle<br>
	 * ) <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * insert into SEQUENCE.SEQ_TAB (<br>
	 * SEQ_NAME<br>
	 * , VALUE_MIN<br>
	 * , VALUE_MAX<br>
	 * , VALUE_NEXT<br>
	 * , INC_BY<br>
	 * , IS_CYCLE<br>
	 * ) values (<br>
	 * $seqName<br>
	 * , $valueMin<br>
	 * , $valueMax<br>
	 * , $valueNext<br>
	 * , $incBy<br>
	 * , $isCycle<br>
	 * ) <br>
	 */
	public final String CREATE_SEQUENCE = "CREATE_SEQUENCE";

	/**
	 * para : <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : maria<br>
	 * sql <br>
	 * <br>
	 * delete <br>
	 * from SEQUENCE.SEQ_TAB <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * delete <br>
	 * from SEQUENCE.SEQ_TAB <br>
	 */
	public final String DELETE_SEQUENCE_ALL = "DELETE_SEQUENCE_ALL";

	/**
	 * para : <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : maria<br>
	 * sql <br>
	 * <br>
	 * CREATE DATABASE 'SEQUENCE' <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * CREATE DATABASE 'SEQUENCE' <br>
	 */
	public final String INIT_SEQUENCE__1 = "INIT_SEQUENCE__1";

	/**
	 * para : <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : maria<br>
	 * sql <br>
	 * <br>
	 * CREATE TABLE SEQUENCE.SEQ_TAB (<br>
	 * SEQ_NAME varchar(100) NOT NULL<br>
	 * , INC_BY int(11) unsigned NOT NULL DEFAULT 1<br>
	 * , VALUE_MIN int(11) unsigned NOT NULL DEFAULT 1<br>
	 * , VALUE_MAX bigint(20) unsigned NOT NULL DEFAULT 18446744073709551615<br>
	 * , VALUE_NEXT bigint(20) unsigned DEFAULT 1<br>
	 * , IS_CYCLE char(1) NOT NULL DEFAULT 'Y'<br>
	 * , PRIMARY KEY ( SEQ_NAME )<br>
	 * ) <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * CREATE TABLE SEQUENCE.SEQ_TAB (<br>
	 * SEQ_NAME varchar(100) NOT NULL<br>
	 * , INC_BY int(11) unsigned NOT NULL DEFAULT 1<br>
	 * , VALUE_MIN int(11) unsigned NOT NULL DEFAULT 1<br>
	 * , VALUE_MAX bigint(20) unsigned NOT NULL DEFAULT 18446744073709551615<br>
	 * , VALUE_NEXT bigint(20) unsigned DEFAULT 1<br>
	 * , IS_CYCLE char(1) NOT NULL DEFAULT 'Y'<br>
	 * , PRIMARY KEY ( SEQ_NAME )<br>
	 * ) <br>
	 */
	public final String INIT_SEQUENCE__2 = "INIT_SEQUENCE__2";

	/**
	 * para : <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : maria<br>
	 * sql <br>
	 * <br>
	 * CREATE FUNCTION nextval (name varchar(100))<br>
	 * <br>
	 * RETURNS bigint(20) READS SQL DATA<br>
	 * <br>
	 * BEGIN<br>
	 * DECLARE cur_val bigint(20);<br>
	 * <br>
	 * SELECT<br>
	 * VALUE_NEXT INTO cur_val<br>
	 * FROM<br>
	 * SEQUENCE.SEQ_TAB<br>
	 * WHERE<br>
	 * SEQ_NAME = name<br>
	 * ;<br>
	 * <br>
	 * IF cur_val IS NOT NULL THEN<br>
	 * UPDATE<br>
	 * SEQUENCE.SEQ_TAB<br>
	 * SET<br>
	 * VALUE_NEXT = IF (<br>
	 * (VALUE_NEXT + INC_BY) > VALUE_MAX<br>
	 * , IF ( IS_CYCLE = 'Y', VALUE_MIN, NULL )<br>
	 * , VALUE_NEXT + INC_BY<br>
	 * )<br>
	 * WHERE<br>
	 * SEQ_NAME = name<br>
	 * ;<br>
	 * END IF;<br>
	 * RETURN cur_val;<br>
	 * END <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * CREATE FUNCTION nextval (name varchar(100))<br>
	 * <br>
	 * RETURNS bigint(20) READS SQL DATA<br>
	 * <br>
	 * BEGIN<br>
	 * DECLARE cur_val bigint(20);<br>
	 * <br>
	 * SELECT<br>
	 * VALUE_NEXT INTO cur_val<br>
	 * FROM<br>
	 * SEQUENCE.SEQ_TAB<br>
	 * WHERE<br>
	 * SEQ_NAME = name<br>
	 * ;<br>
	 * <br>
	 * IF cur_val IS NOT NULL THEN<br>
	 * UPDATE<br>
	 * SEQUENCE.SEQ_TAB<br>
	 * SET<br>
	 * VALUE_NEXT = IF (<br>
	 * (VALUE_NEXT + INC_BY) > VALUE_MAX<br>
	 * , IF ( IS_CYCLE = 'Y', VALUE_MIN, NULL )<br>
	 * , VALUE_NEXT + INC_BY<br>
	 * )<br>
	 * WHERE<br>
	 * SEQ_NAME = name<br>
	 * ;<br>
	 * END IF;<br>
	 * RETURN cur_val;<br>
	 * END <br>
	 */
	public final String INIT_SEQUENCE__3 = "INIT_SEQUENCE__3";

	/**
	 * para : $tableName, $user<br>
	 * result : RID_TABLE_COMMENT=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select T.TABLE_NAME COMMENTS<br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U<br>
	 * where T.TABLE_NAME = $tableName<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and U.USER_NAME = $user <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mariadb<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * TABLE_COMMENT<br>
	 * FROM <br>
	 * INFORMATION_SCHEMA.TABLES<br>
	 * WHERE <br>
	 * TABLE_NAME = $tableName<br>
	 * -- AND TABLE_SCHEMA = $tableSchema <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * TABLE_COMMENT<br>
	 * FROM <br>
	 * INFORMATION_SCHEMA.TABLES<br>
	 * WHERE <br>
	 * TABLE_NAME = $tableName<br>
	 * -- AND TABLE_SCHEMA = $tableSchema <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * COMMENTS<br>
	 * FROM <br>
	 * USER_TAB_COMMENTS<br>
	 * WHERE <br>
	 * TABLE_NAME = $tableName <br>
	 */
	public final String QID_SELECT_TABLE_COMMENT = "QID_SELECT_TABLE_COMMENT";

	/**
	 * para : $tableName, $user, $tableName, $user<br>
	 * result : RID_TABLE_INDEX=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select INDEX_NAME<br>
	 * , IDX_TYPE<br>
	 * , COLUMN_NAME<br>
	 * from ( <br>
	 * select IDX.INDEX_NAME<br>
	 * , decode(CONSTRAINT_NAME, null, decode(IS_UNIQUE, 'T', 'UK', 'KEY'),
	 * 'PK') IDX_TYPE<br>
	 * , C.COLUMN_NAME<br>
	 * , IDXC.INDEX_COL_ORDER ORDERBY<br>
	 * from SYSTEM_.SYS_INDICES_ IDX<br>
	 * left join SYSTEM_.SYS_CONSTRAINTS_ SC on SC.CONSTRAINT_NAME =
	 * IDX.INDEX_NAME and SC.TABLE_ID = IDX.TABLE_ID<br>
	 * , SYSTEM_.SYS_INDEX_COLUMNS_ IDXC<br>
	 * , SYSTEM_.SYS_COLUMNS_ C<br>
	 * where IDX.TABLE_ID in ( select T.TABLE_ID <br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U<br>
	 * where T.TABLE_NAME = $tableName<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and U.USER_NAME = $user <br>
	 * )<br>
	 * and IDX.INDEX_ID = IDXC.INDEX_ID<br>
	 * and C.COLUMN_ID = IDXC.COLUMN_ID <br>
	 * <br>
	 * union all<br>
	 * <br>
	 * select IDX.CONSTRAINT_NAME INDEX_NAME<br>
	 * , 'FK' IDX_TYPE<br>
	 * , CC2.COLUMN_NAME || ':' || C.COLUMN_NAME || '@' || T.TABLE_NAME<br>
	 * , IDXC.INDEX_COL_ORDER ORDERBY<br>
	 * from SYSTEM_.SYS_CONSTRAINTS_ IDX<br>
	 * , SYSTEM_.SYS_CONSTRAINT_COLUMNS_ CC<br>
	 * , SYSTEM_.SYS_INDEX_COLUMNS_ IDXC<br>
	 * , SYSTEM_.SYS_COLUMNS_ C<br>
	 * , SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_COLUMNS_ CC2<br>
	 * where <br>
	 * IDX.TABLE_ID in ( select T.TABLE_ID <br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U<br>
	 * where T.TABLE_NAME = $tableName<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and U.USER_NAME = $user <br>
	 * )<br>
	 * and CONSTRAINT_TYPE = 0<br>
	 * and IDX.REFERENCED_INDEX_ID = IDXC.INDEX_ID<br>
	 * and C.COLUMN_ID = IDXC.COLUMN_ID <br>
	 * and T.TABLE_ID = IDX.REFERENCED_TABLE_ID<br>
	 * and CC.CONSTRAINT_ID = IDX.CONSTRAINT_ID<br>
	 * and CC2.COLUMN_ID = CC.COLUMN_ID<br>
	 * )<br>
	 * <br>
	 * order by <br>
	 * INDEX_NAME<br>
	 * , ORDERBY <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : informix<br>
	 * sql <br>
	 * <br>
	 * SELECT IDXNAME INDEX_NAME,<br>
	 * 'KEY' IDX_TYPE,<br>
	 * '' COLUMN_NAME<br>
	 * FROM <br>
	 * INFORMIX.SYSINDEXES I,<br>
	 * INFORMIX.SYSTABLES T<br>
	 * WHERE <br>
	 * T.TABNAME = '테이블 지정 안함 왜냐구 인덱스 구하기 싫으니깐..'<br>
	 * AND I.TABID = T.TABID <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mariadb<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * kcu.CONSTRAINT_NAME, <br>
	 * IF(tc.constraint_type = 'PRIMARY KEY', 'PK', 'KEY') IDX_TYPE,<br>
	 * kcu.column_name<br>
	 * FROM <br>
	 * information_schema.KEY_COLUMN_USAGE kcu,<br>
	 * information_schema.TABLE_CONSTRAINTS tc <br>
	 * WHERE <br>
	 * kcu.table_name = $tableName<br>
	 * AND tc.table_name = $tableName<br>
	 * AND kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * kcu.CONSTRAINT_NAME, <br>
	 * IF(tc.constraint_type = 'PRIMARY KEY', 'PK', 'KEY') IDX_TYPE,<br>
	 * kcu.column_name<br>
	 * FROM <br>
	 * information_schema.KEY_COLUMN_USAGE kcu,<br>
	 * information_schema.TABLE_CONSTRAINTS tc <br>
	 * WHERE <br>
	 * kcu.table_name = $tableName<br>
	 * AND tc.table_name = $tableName<br>
	 * AND kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * select INDEX_NAME<br>
	 * , IDX_TYPE<br>
	 * , COLUMN_NAME<br>
	 * from ( <br>
	 * select IDX.INDEX_NAME<br>
	 * , DECODE(C.CONSTRAINT_TYPE, NULL, decode(IDX.UNIQUENESS, 'UNIQUE', 'UK',
	 * 'KEY'), 'P', 'PK', 'R', 'FK', 'KEY' ) IDX_TYPE <br>
	 * , COL.COLUMN_NAME<br>
	 * , COL.COLUMN_POSITION<br>
	 * from <br>
	 * USER_INDEXES IDX<br>
	 * left join USER_CONSTRAINTS C on C.CONSTRAINT_NAME = IDX.INDEX_NAME<br>
	 * , USER_IND_COLUMNS COL<br>
	 * where IDX.INDEX_NAME = COL.INDEX_NAME<br>
	 * and IDX.TABLE_NAME = $tableName<br>
	 * <br>
	 * <br>
	 * union all<br>
	 * <br>
	 * select C.CONSTRAINT_NAME INDEX_NAME<br>
	 * , 'FK' IDX_TYPE<br>
	 * , COL.COLUMN_NAME || '@' || IDX.TABLE_NAME COLUMN_NAME <br>
	 * , COL.COLUMN_POSITION<br>
	 * from USER_CONSTRAINTS C<br>
	 * , USER_INDEXES IDX<br>
	 * , USER_IND_COLUMNS COL<br>
	 * where C.TABLE_NAME = $tableName<br>
	 * and C.CONSTRAINT_TYPE = 'R'<br>
	 * and IDX.INDEX_NAME = C.R_CONSTRAINT_NAME<br>
	 * and COL.INDEX_NAME = IDX.INDEX_NAME <br>
	 * )<br>
	 * order by INDEX_NAME, COLUMN_POSITION <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : sqlserver<br>
	 * sql <br>
	 * <br>
	 * SELECT CONSTRAINT_NAME INDEX_NAME,<br>
	 * 'KEY' IDX_TYPE,<br>
	 * COLUMN_NAME COLUMN_NAME<br>
	 * FROM <br>
	 * information_schema.key_column_usage<br>
	 * WHERE <br>
	 * TABLE_NAME = $tableName <br>
	 */
	public final String QID_SELECT_TABLE_INDEX = "QID_SELECT_TABLE_INDEX";

	/**
	 * para : $tableName, $user<br>
	 * result : RESULT_TABLE_INFO=subkjh.dao.beans.Column<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select COLUMN_ORDER COLUMN_NO<br>
	 * , COLUMN_NAME<br>
	 * , 'N' IS_PK<br>
	 * , DECODE(IS_NULLABLE, 'T', 'Y', 'N') <br>
	 * IS_NULLABLE<br>
	 * , '' COMMENTS<br>
	 * , decode(DATA_TYPE<br>
	 * , 2, 'NUMERIC'<br>
	 * , 1, 'CHAR'<br>
	 * , 'VARCHAR') DATA_TYPE<br>
	 * , NVL(PRECISION, 0) DATA_LENGTH<br>
	 * , NVL(SCALE, 0) DATA_SCALE<br>
	 * , DEFAULT_VAL DATA_DEFAULT <br>
	 * from <br>
	 * SYSTEM_.SYS_COLUMNS_<br>
	 * where <br>
	 * TABLE_ID in ( select T.TABLE_ID <br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U<br>
	 * where T.TABLE_NAME = $tableName<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and U.USER_NAME = $user <br>
	 * )<br>
	 * order by <br>
	 * COLUMN_ORDER <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : informix<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * COLNO COLUMN_NO,<br>
	 * COLNAME COLUMN_NAME,<br>
	 * 'N' IS_PK,<br>
	 * 'Y' IS_NULLABLE,<br>
	 * '' COMMENTS,<br>
	 * COLTYPE DATA_TYPE,<br>
	 * COLLENGTH DATA_LENGTH,<br>
	 * 0 DATA_SCALE,<br>
	 * '' DATA_DEFAULT <br>
	 * FROM <br>
	 * INFORMIX.SYSCOLUMNS COL,<br>
	 * INFORMIX.SYSTABLES TBL<br>
	 * WHERE <br>
	 * TBL.TABNAME = $tableName<br>
	 * AND COL.TABID = TBL.TABID<br>
	 * ORDER BY <br>
	 * COLNO <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mariadb<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * ORDINAL_POSITION COLUMN_NO,<br>
	 * COLUMN_NAME,<br>
	 * IF(COLUMN_KEY = 'PRI', 'Y', 'N') <br>
	 * IS_PK,<br>
	 * IS_NULLABLE,<br>
	 * COLUMN_COMMENT COMMENTS,<br>
	 * DATA_TYPE,<br>
	 * IFNULL(CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION) <br>
	 * DATA_LENGTH,<br>
	 * IFNULL(NUMERIC_SCALE, -1) DATA_SCALE,<br>
	 * COLUMN_DEFAULT DATA_DEFAULT <br>
	 * FROM <br>
	 * INFORMATION_SCHEMA.COLUMNS<br>
	 * WHERE <br>
	 * TABLE_NAME = $tableName<br>
	 * -- AND TABLE_SCHEMA = $tableSchema<br>
	 * <br>
	 * ORDER BY <br>
	 * ORDINAL_POSITION <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * ORDINAL_POSITION COLUMN_NO,<br>
	 * COLUMN_NAME,<br>
	 * IF(COLUMN_KEY = 'PRI', 'Y', 'N') <br>
	 * IS_PK,<br>
	 * IS_NULLABLE,<br>
	 * COLUMN_COMMENT COMMENTS,<br>
	 * DATA_TYPE,<br>
	 * IFNULL(CHARACTER_MAXIMUM_LENGTH, NUMERIC_PRECISION) <br>
	 * DATA_LENGTH,<br>
	 * IFNULL(NUMERIC_SCALE, -1) DATA_SCALE,<br>
	 * COLUMN_DEFAULT DATA_DEFAULT <br>
	 * FROM <br>
	 * INFORMATION_SCHEMA.COLUMNS<br>
	 * WHERE <br>
	 * TABLE_NAME = $tableName<br>
	 * -- AND TABLE_SCHEMA = $tableSchema<br>
	 * <br>
	 * ORDER BY <br>
	 * ORDINAL_POSITION <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * a.COLUMN_ID COLUMN_NO,<br>
	 * a.COLUMN_NAME,<br>
	 * decode(b.CONSTRAINT_TYPE,null,'N','Y') IS_PK, <br>
	 * a.NULLABLE IS_NULLABLE,<br>
	 * c.COMMENTS,<br>
	 * a.DATA_TYPE,<br>
	 * NVL(a.DATA_PRECISION, a.DATA_LENGTH ) DATA_LENGTH,<br>
	 * NVL(a.DATA_SCALE, -1) DATA_SCALE,<br>
	 * a.DATA_DEFAULT<br>
	 * FROM <br>
	 * USER_TAB_COLUMNS a, <br>
	 * ( SELECT <br>
	 * a.TABLE_NAME,<br>
	 * a.COLUMN_NAME,<br>
	 * b.CONSTRAINT_TYPE<br>
	 * FROM <br>
	 * USER_CONS_COLUMNS a, <br>
	 * USER_CONSTRAINTS b<br>
	 * WHERE <br>
	 * a.TABLE_NAME = b.TABLE_NAME<br>
	 * AND <br>
	 * a.CONSTRAINT_NAME = b.CONSTRAINT_NAME<br>
	 * AND <br>
	 * b.CONSTRAINT_TYPE='P'<br>
	 * ) b, USER_COL_COMMENTS c<br>
	 * WHERE <br>
	 * a.TABLE_NAME = b.TABLE_NAME (+)<br>
	 * AND <br>
	 * a.COLUMN_NAME = b.COLUMN_NAME (+)<br>
	 * AND <br>
	 * a.TABLE_NAME = c.TABLE_NAME (+)<br>
	 * AND <br>
	 * a.COLUMN_NAME = c.COLUMN_NAME (+)<br>
	 * AND <br>
	 * a.TABLE_NAME = $tableName<br>
	 * ORDER BY <br>
	 * a.COLUMN_ID <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : sqlserver<br>
	 * sql <br>
	 * <br>
	 * SELECT <br>
	 * ORDINAL_POSITION COLUMN_NO,<br>
	 * COLUMN_NAME COLUMN_NAME,<br>
	 * 'N' IS_PK,<br>
	 * IS_NULLABLE IS_NULLABLE,<br>
	 * '' COMMENTS,<br>
	 * DATA_TYPE DATA_TYPE,<br>
	 * ISNULL(NUMERIC_PRECISION, 0) DATA_LENGTH,<br>
	 * ISNULL(NUMERIC_SCALE, 0) DATA_SCALE,<br>
	 * '' DATA_DEFAULT <br>
	 * FROM <br>
	 * information_schema.COLUMNS <br>
	 * WHERE <br>
	 * TABLE_NAME = $tableName<br>
	 * ORDER BY <br>
	 * ORDINAL_POSITION <br>
	 */
	public final String QID_SELECT_TABLE_INFO = "QID_SELECT_TABLE_INFO";

	/**
	 * para : #tableName<br>
	 * result : RID_TABLE_SCHEMA=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * show create table #tableName <br>
	 */
	public final String QID_SELECT_TABLE_SCHEMA = "QID_SELECT_TABLE_SCHEMA";

	/**
	 * para : $tableName, $user<br>
	 * result : RESULT_TABLE_NAME=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select COLUMN_NAME<br>
	 * from SYSTEM_.SYS_COLUMNS_<br>
	 * where TABLE_ID in ( select T.TABLE_ID <br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U<br>
	 * where upper(T.TABLE_NAME) = $tableName<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and U.USER_NAME = $user <br>
	 * ) <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * select COLUMN_NAME<br>
	 * from USER_TAB_COLUMNS<br>
	 * where upper(TABLE_NAME) = $tableName <br>
	 */
	public final String SELECT_COLUMN_NAME__BY_TABLE = "SELECT_COLUMN_NAME__BY_TABLE";

	/**
	 * para : $user, $tableName<br>
	 * result : RESULT_TABLE_NAME=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select IDX.INDEX_NAME<br>
	 * from SYSTEM_.SYS_INDICES_ IDX<br>
	 * , SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U <br>
	 * where U.USER_NAME = $user<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and IDX.TABLE_ID = T.TABLE_ID<br>
	 * and upper(T.TABLE_NAME) = $tableName <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * select INDEX_NAME<br>
	 * from USER_INDEXES<br>
	 * where upper(TABLE_NAME) = $tableName <br>
	 */
	public final String SELECT_INDEX_NAME__BY_TABLE = "SELECT_INDEX_NAME__BY_TABLE";

	/**
	 * para : $user<br>
	 * result : RESULT_DB_SEQUENCE=subkjh.dao.beans.Sequence<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME SEQUENCE_NAME<br>
	 * , MIN_SEQ VALUE_MIN<br>
	 * , MAX_SEQ VALUE_MAX<br>
	 * , INCREMENT_SEQ INC_BY<br>
	 * , IS_CYCLE<br>
	 * from V\\$SEQ S<br>
	 * , SYSTEM_.SYS_USERS_ U<br>
	 * , SYSTEM_.SYS_TABLES_ T<br>
	 * where S.SEQ_OID = T.table_oid<br>
	 * and T.user_id = U.user_id<br>
	 * and U.USER_NAME = $user <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * select SEQUENCE_NAME <br>
	 * , MIN_VALUE VALUE_MIN<br>
	 * , MAX_VALUE VALUE_MAX<br>
	 * , INCREMENT_BY INC_BY<br>
	 * , CYCLE_FLAG IS_CYCLE<br>
	 * from USER_SEQUENCES <br>
	 */
	public final String SELECT_SEQUENCE__ALL = "SELECT_SEQUENCE__ALL";

	/**
	 * para : $user<br>
	 * result : RESULT_TABLE_NAME=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U <br>
	 * where U.USER_NAME = $user<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and T.TABLE_TYPE = 'T' <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mariadb<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from INFORMATION_SCHEMA.TABLES <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from INFORMATION_SCHEMA.TABLES <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * SELECT TNAME<br>
	 * FROM TAB <br>
	 */
	public final String SELECT_TABLE_NAME__ALL = "SELECT_TABLE_NAME__ALL";

	/**
	 * para : $user, $tableName<br>
	 * result : RESULT_TABLE_NAME=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U <br>
	 * where U.USER_NAME = $user<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and upper(T.TABLE_NAME) = $tableName<br>
	 * and T.TABLE_TYPE = 'T' <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mariadb<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from INFORMATION_SCHEMA.TABLES<br>
	 * where TABLE_NAME = $tableName <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from INFORMATION_SCHEMA.TABLES<br>
	 * where TABLE_NAME = $tableName <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * SELECT TNAME<br>
	 * FROM TAB<br>
	 * WHERE TNAME = $tableName <br>
	 */
	public final String SELECT_TABLE_NAME__BY_NAME = "SELECT_TABLE_NAME__BY_NAME";

	/**
	 * para : $user, #tableName<br>
	 * result : RESULT_TABLE_NAME=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from SYSTEM_.SYS_TABLES_ T<br>
	 * , SYSTEM_.SYS_USERS_ U <br>
	 * where U.USER_NAME = $user<br>
	 * and T.USER_ID = U.USER_ID<br>
	 * and upper(T.TABLE_NAME) like '%#tableName%'<br>
	 * and T.TABLE_TYPE = 'T' <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mariadb<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from INFORMATION_SCHEMA.TABLES<br>
	 * where TABLE_NAME like '%#tableName%' <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : mysql<br>
	 * sql <br>
	 * <br>
	 * select TABLE_NAME<br>
	 * from INFORMATION_SCHEMA.TABLES<br>
	 * where TABLE_NAME like '%#tableName%' <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * select TNAME<br>
	 * from TAB<br>
	 * where upper(TNAME) like '%#tableName%' <br>
	 */
	public final String SELECT_TABLE_NAME__LIKE = "SELECT_TABLE_NAME__LIKE";

	/**
	 * para : $VERSION<br>
	 * result : RESULT_VERSION=java.lang.String<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : altibase<br>
	 * sql <br>
	 * <br>
	 * select product_version<br>
	 * from V$VERSION <br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * select * <br>
	 * from v$version <br>
	 * where banner LIKE 'Oracle%' <br>
	 */
	public final String SELECT_VERSION = "SELECT_VERSION";

	/**
	 * para : <br>
	 * result : RESULT_DB_VIEW=subkjh.dao.beans.View<br>
	 * ------------------------------------------------------------------------
	 * ---------- <br>
	 * database : oracle<br>
	 * sql <br>
	 * <br>
	 * select VIEW_NAME NAME<br>
	 * , TEXT QUERY<br>
	 * from USER_VIEWS <br>
	 */
	public final String SELECT_VIEW__ALL = "SELECT_VIEW__ALL";

}
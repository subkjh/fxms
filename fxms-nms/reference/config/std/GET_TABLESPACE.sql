--- informix ---


	private final String INFORMIX_TABLE = "" // \
			+ " " //
			+ "" //
			+ " " //
			+ " " //
			+ " " //
			+ " " //
			+ " ";

--- oracle ---
			
	private final String ORACLE_TABLE = "select tablespace_name" //
			+ " , sum(sizeTotal)" //
			+ " , sum(sizeFree) " //
			+ " from  ( "
			+ " select tablespace_name, sum(bytes) sizeTotal, 0 sizeFree from DBA_DATA_FILES where bytes > 0 group by tablespace_name "
			+ " union all"
			+ " select tablespace_name, 0 sizeTotal, sum(bytes) sizeFree from DBA_FREE_SPACE where bytes > 0 group by tablespace_name " //
			+ " )" //
			+ " group by tablespace_name";


--- altibase --- 
			
SELECT RTRIM(NAME) AS TBS_NAME,
            MAXSIZE/1024/1024 AS 'MAXSIZE(MB)',
            USEDSIZE/1024/1024 AS 'USEDSIZE(MB)',
            CURRSIZE/1024/1024 AS 'CURRSIZE(MB)'
       FROM (
            SELECT B.SPACEID,
                   B.MAXSIZE,
                   A.USEDSIZE,
                   CURRSIZE
              FROM (
                   SELECT A.SPACE_ID,
                          NVL(sum(B.ALLOC), 0) ALLOCSIZE,
                          NVL(sum(B.USED), 0) USEDSIZE
                     FROM V$MEM_TABLESPACES A
                     LEFT JOIN
                          (
                          SELECT A.TABLESPACE_ID,
                                 B.TABLE_NAME,
                                 SUM(A.FIXED_ALLOC_MEM+A.VAR_ALLOC_MEM) ALLOC,
                                 SUM(A.FIXED_USED_MEM+A.VAR_USED_MEM) USED
                            FROM V$MEMTBL_INFO A,
                                 SYSTEM_.SYS_TABLES_ B
                           WHERE A.TABLE_OID=B.TABLE_OID
                           GROUP BY A.TABLESPACE_ID, B.TABLE_NAME
                          ) B
                       ON A.SPACE_ID=B.TABLESPACE_ID
                    WHERE A.SPACE_ID != 0
                    group by a.space_id
                   ) A,
                   (
                   SELECT 1 SPACEID,
                               MEM_MAX_DB_SIZE MAXSIZE,
                               MEM_ALLOC_PAGE_COUNT * 32 * 1024 ALLOCSIZE,
                               MEM_ALLOC_PAGE_COUNT * 32 * 1024 CURRSIZE
                          FROM V$DATABASE
                  ) B
                    UNION ALL
                    SELECT DF.SPACEID SPACEID,
                               df.MAXSIZE,
                               NVL(UF.USEDSIZE,'N/A') USEDSIZE,
                               df.CURRSIZE
                          FROM (
                                SELECT SPACEID, 
                                       SUM(MAXSIZE) * 8 * 1024 MAXSIZE,
                                       SUM(CURRSIZE) * 8 * 1024 CURRSIZE
                                  FROM X$DATAFILES
                                 GROUP BY SPACEID
                               ) DF,
                               (
                                SELECT ID SPACEID,
                                       to_char(ALLOCATED_PAGE_COUNT * 8 * 1024) as USEDSIZE
                                  FROM v$TABLESPACES 
                                 WHERE ID NOT IN
                                       (
                                       SELECT SPACE_ID
                                         FROM V$MEM_TABLESPACES
                                       )
                                ) UF
                   WHERE DF.SPACEID = UF.SPACEID
                 ) TBS_SZ
        LEFT OUTER JOIN
             V$TABLESPACES TBS_INFO
          ON TBS_SZ.SPACEID = TBS_INFO.ID
       ORDER BY TBS_SZ.SPACEID;
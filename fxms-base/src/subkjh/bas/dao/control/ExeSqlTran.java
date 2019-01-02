package subkjh.bas.dao.control;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import subkjh.bas.dao.model.SqlBean;

/**
 * 일괄 처리를 위한 DAO<br>
 * 
 * 사용 방법<br>
 * ExeSqlTran SQL = getTransaction();<br>
 * SQL.begin(SQLID, .. );<br>
 * SQL.addBatch(...);<br>
 * SQL.runBatch();<br>
 * SQL.end();<br>
 * 
 * @author subkjh
 * 
 */
public class ExeSqlTran extends CommDao {

	private PreparedStatement pstmt;
	private QidPool queries;
	private String sql;
	private String sqlId = "";

	public ExeSqlTran(QidPool sqlPool) {
		queries = sqlPool;
	}

	/**
	 * 일괄처리를 위해 하나의 열을 추가합니다.<br>
	 * 모든 열 추가가 종료되면 반드시 runBatch를 호출해야 실제 처리를 합니다.
	 * 
	 * @param para
	 * @throws Exception
	 */
	public void addBatch(Object... para) throws Exception {
		setPreSt(pstmt, para);
		pstmt.addBatch();
	}

	/**
	 * 하나의 열을 추가합니다.<br>
	 * 
	 * @param para
	 * @return 처리 결과
	 */
	public int addRow(Object... para) {
		try {
			setPreSt(pstmt, para);
			int ret = pstmt.executeUpdate();
			return ret;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * SQLID를 이용한 배치 작업을 시작합니다.
	 * 
	 * @param id
	 * @param replaceObj
	 * @throws Exception
	 */
	public synchronized void begin(String id, Object replaceObj) throws IOException, Exception {
		sqlId = id;
		SqlBean sql = queries.getSqlBean(id, database);
		if (sql == null) {
			throw new Exception("QID(" + id + ") NOT FOUND");
		}
		beginSql(sql.getSql(replaceObj));
	}

	/**
	 * 트랜잭션 시작<br>
	 * SQL문을 이용하여 처리하기 위합니다.<br>
	 * SQL문에는 ?로 값을 설정합니다. <br>
	 * 예) insert into test ( t1, t2, t3 ) values ( ?, ?, ? )
	 * 
	 */
	public synchronized void beginSql(String sql) throws IOException, Exception  {
		connect();
		this.sql = sql;
		pstmt = getPrepareStatement(sql);
	}

	/**
	 * 트랜잭션 종료
	 */
	public synchronized void end() {

		if (pstmt != null) {
			try {
				free(pstmt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			disconnect(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * addBatch() 후 일괄적으로 실행합니다.<br>
	 * 
	 * @return 처리 결과
	 */
	public int runBatch() throws Exception {
		int cnt[];
		try {
			cnt = pstmt.executeBatch();
			int total = 0;
			for (int i = 0; i < cnt.length; i++) {
				if (cnt[i] > 0)
					total += cnt[i];
				else if (cnt[i] == Statement.SUCCESS_NO_INFO) {
					total++;
				}
			}

			return total;
		} catch (SQLException e) {
			throw database.makeException(e, "[SQLID=" + sqlId + "]\n\t[SQL=" + sql + "]");
		}
	}

}

package subkjh.bas.dao.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.model.SqlBean;
import subkjh.bas.log.Logger;

/**
 * 데이터 베이스와 미리 연결하여 풀 형식으로 사용되는 객체
 * 
 * @author subkjh
 * 
 */
public class SqlDump extends CommDao {

	protected boolean autoCommit;

	/**
	 * 접속 데이터베이스
	 */
	protected DataBase database;

	private QidPool sqlPool;

	public SqlDump(QidPool sqlPool) {
		this.sqlPool = sqlPool;
	}

	public int dumpByQid(String qid, Object para, File destFile, String separator, boolean isAppend) throws Exception {
		SqlBean sqlBean = sqlPool.getSqlBean(qid, database);

		Connection c = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		long mstime;
		try {
			c = _connect();
			preStmt = c.prepareStatement(sqlBean.getSql(para));
			setPreSt(preStmt, sqlBean.getPara(para));

			mstime = System.currentTimeMillis();
			Logger.logger.debug("Quering...");
			rs = preStmt.executeQuery();
			Logger.logger.debug("Finished query " + (System.currentTimeMillis() - mstime) + "(ms)");

			return dump(rs, destFile, separator, isAppend);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preStmt != null)
					preStmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * SQL 문의 결과를 f에 기록합니다. 각 필드의 분리 문자는 separator를 이용합니다.
	 * 
	 * @param sql
	 * @param f
	 * @param separator
	 * @return 처리 건수
	 */
	public int dumpBySql(String sql, File f, String separator, boolean isAppend) throws Exception {

		Connection c = null;
		Statement stat = null;
		ResultSet rs = null;

		try {
			c = _connect();
			stat = c.createStatement();
			rs = stat.executeQuery(sql);
			return dump(rs, f, separator, isAppend);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stat != null)
					stat.close();
				if (c != null)
					c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 화일의 라인을 분리자로 구분하여 SQL문의 ? 위치에 값을 치환하여 SQL을 실행합니다.
	 * 
	 * @param f
	 * @param sql
	 * @param separator
	 * @return 처리 건수
	 */
	public int restore(File f, String sql, String separator) {
		BufferedReader in = null;
		String val;
		int count = 0;
		int totalCount = 0;
		ExeSqlTran exec = new ExeSqlTran(sqlPool);
		exec.setDatabase(database);

		try {
			in = new BufferedReader(new FileReader(f));

			exec.beginSql(sql);

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					if (val.trim().length() == 0)
						continue;

					if (val.startsWith("#"))
						continue;

					String ss[] = val.split(separator);

					exec.addBatch((Object[]) ss);
					count++;

					if (count > 1000) {
						exec.runBatch();
						totalCount += count;
						count = 0;
					}

				} catch (IOException e) {
					break;
				}
			}

			if (count > 1000) {
				exec.runBatch();
				totalCount += count;
			}

			return totalCount;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}

			exec.end();
		}

	}

	/**
	 * 데이터 베이스를 설정한다.
	 * 
	 * @param db
	 */
	public void setDatabase(DataBase db) {
		database = db;
		autoCommit = db.isAutoCommit();
	}

	private synchronized Connection _connect() throws Exception {
		try {
			Class.forName(database.getDriver()).newInstance();

			Connection c;

			if (database.getUser() != null) {
				c = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());

			} else {
				c = DriverManager.getConnection(database.getUrl());
			}

			c.setAutoCommit(autoCommit);
			return c;
		} catch (Exception ex) {
			throw ex;
		}
	}

	private int dump(ResultSet rs, File destFile, String separator, boolean isAppend) throws Exception {
		int count = -1;
		int colCnt = -1;

		FileOutputStream outStream = null;

		try {
			outStream = new FileOutputStream(destFile, isAppend);
			colCnt = rs.getMetaData().getColumnCount();
			String line = "";

			for (int i = 1; i <= colCnt; i++) {
				try {
					line += rs.getMetaData().getColumnName(i);
					line += separator;
				} catch (SQLException e) {
					throw e;
				}
			}

			try {
				outStream.write(String.valueOf(colCnt).getBytes());
				outStream.write("\n".getBytes());
				outStream.write(line.getBytes());
				outStream.write("\n".getBytes());
			} catch (IOException e1) {
				throw e1;
			}

			try {
				while (rs.next()) {
					count++;

					line = "";
					for (int i = 1; i <= colCnt; i++) {
						line += rs.getObject(i);
						line += separator;
					}

					outStream.write(line.getBytes());
					outStream.write("\n".getBytes());

					if (count % 1000 == 0) {
						Logger.logger.debug("index = " + count);
					}
				}

			} catch (Exception e) {
				throw e;
			}
			return count;

		} catch (Exception e) {
			throw e;
		} finally {
			if (outStream != null) {
				try {

					outStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

}

package subkjh.bas.dao.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.model.ResultBean;
import subkjh.bas.dao.model.SqlBean;
import subkjh.bas.dao.model.SqlDeleteBean;
import subkjh.bas.dao.model.SqlInsertBean;
import subkjh.bas.dao.model.SqlSelectBean;
import subkjh.bas.dao.model.SqlUpdateBean;
import subkjh.bas.fxdao.beans.QueryResult;
import subkjh.bas.log.Logger;

public class SyncQidDao<DATA> extends CommDao {

	private int countSelect, countInsert, countUpdate, countDelete;
	private SqlBean destInsertSql, destUpdateSql, destDeleteSql;
	private Object entryObj;
	private Boolean isAutoCommit;
	private ResultBean resultBean;
	private Connection srcConnection, destConnection;
	private DataBase srcDataBase, destDataBase;
	private Object selectPara, insertPara, updatePara, deletePara;
	private PreparedStatement selectPs, insertPs, updatePs, deletePs;
	private ResultSet srcRs;
	private SqlSelectBean srcSql;
	private long startTime;

	public SyncQidDao(QidPool sqlPool, //
			DataBase srcDataBase, //
			String selectQid, Object selectPara, //
			DataBase destDataBase, //
			String insertQid, Object insertPara, //
			String updateQid, Object updatePara, //
			String deleteQid, Object deletePara, //
			Boolean isAutoCommit //
	) throws Exception {

		this.srcDataBase = srcDataBase;
		this.destDataBase = destDataBase;
		this.isAutoCommit = isAutoCommit;

		srcSql = (SqlSelectBean) sqlPool.getSqlBean(selectQid, srcDataBase);
		if (srcSql == null) {
			throw new Exception("QID not defined [" + selectQid + "]");
		}
		entryObj = srcSql.getResultMap().getJavaClass().newInstance();

		if (insertQid != null) {
			destInsertSql = (SqlInsertBean) sqlPool.getSqlBean(insertQid, destDataBase);
			if (destInsertSql == null) {
				throw new Exception("QID not defined [" + insertQid + "]");
			}
		}

		if (updateQid != null) {
			destUpdateSql = (SqlUpdateBean) sqlPool.getSqlBean(updateQid, destDataBase);
			if (destUpdateSql == null) {
				throw new Exception("QID not defined [" + updateQid + "]");
			}
		}

		if (deleteQid != null) {
			destDeleteSql = (SqlDeleteBean) sqlPool.getSqlBean(deleteQid, destDataBase);
			if (destDeleteSql == null) {
				throw new Exception("QID not defined [" + deleteQid + "]");
			}

		}

		this.selectPara = selectPara;
		this.insertPara = insertPara;
		this.updatePara = updatePara;
		this.deletePara = deletePara;
	}

	public void close() {
		try {
			if (srcRs != null) srcRs.close();
		} catch (Exception e) {
		}

		try {
			if (selectPs != null) selectPs.close();
		} catch (Exception e) {
		}

		try {
			if (srcConnection != null) srcConnection.close();
		} catch (Exception e) {
		}

		try {
			if (insertPs != null) insertPs.close();
		} catch (Exception e) {
		}

		try {
			if (updatePs != null) updatePs.close();
		} catch (Exception e) {
		}

		try {
			if (deletePs != null) deletePs.close();
		} catch (Exception e) {
		}

		try {
			if (destConnection != null) {
				destConnection.commit();
				destConnection.close();
			}
		} catch (Exception e) {
		}

		Logger.logger.debug(//
				(srcSql != null ? srcSql.getQid() + "=" + getCountSelect() : "") + "," + //
						(destInsertSql != null ? destInsertSql.getQid() + "=" + getCountInsert() : "") + "," + //
						(destUpdateSql != null ? destUpdateSql.getQid() + "=" + getCountUpdate() : "") + "," + //
						(destDeleteSql != null ? destDeleteSql.getQid() + "=" + getCountDelete() : "") + //
						" stime=" + (System.currentTimeMillis() - startTime) + "(ms)");

	}

	public int delete(Object para) throws Exception {
		try {
			setPreSt(deletePs, destDeleteSql.getPara(para));
			int ret = deletePs.executeUpdate();
			if (ret > 0) countDelete += ret;
			return ret;
		} catch (Exception e) {
			throw destDataBase.makeException(e, null);
		}
	}

	/**
	 * @return the countDelete
	 */
	public int getCountDelete() {
		return countDelete;
	}

	/**
	 * @return the countInsert
	 */
	public int getCountInsert() {
		return countInsert;
	}

	/**
	 * @return the countSelect
	 */
	public int getCountSelect() {
		return countSelect;
	}

	/**
	 * @return the countUpdate
	 */
	public int getCountUpdate() {
		return countUpdate;
	}

	public int insert(DATA data) throws Exception {
		try {
			Object para[] = destInsertSql.getPara(data);

			setPreSt(insertPs, para);

			int ret = insertPs.executeUpdate();
			if (ret == 1) countInsert += 1;
			return ret;
		} catch (Exception e) {
			throw destDataBase.makeException(e, null);
		}
	}

	@SuppressWarnings("unchecked")
	public DATA next() throws Exception {

		try {
			if (srcRs.next() == false) return null;
			DATA ret = (DATA) getBean(srcRs, entryObj, resultBean);
			if (ret != null) countSelect += 1;
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void open() throws Exception {

		startTime = System.currentTimeMillis();

		try {
			srcConnection = getConnection(srcDataBase);
		} catch (Exception e) {
			throw e;
		}

		try {
			destConnection = getConnection(destDataBase);
		} catch (Exception e) {
			close();
			throw e;
		}

		try {
			resultBean = srcSql.getResultMap();
			selectPs = makeDestPs(srcConnection, srcSql, selectPara);
			setPreSt(selectPs, srcSql.getPara(selectPara));
			srcRs = selectPs.executeQuery();
			entryObj = resultBean.getJavaClass().newInstance();

			if (destInsertSql != null) insertPs = makeDestPs(destConnection, destInsertSql, insertPara);
			if (destUpdateSql != null) updatePs = makeDestPs(destConnection, destUpdateSql, updatePara);
			if (destDeleteSql != null) deletePs = makeDestPs(destConnection, destDeleteSql, deletePara);

		} catch (Exception e) {
			close();
			throw e;
		}

	}

	public int update(DATA data) throws Exception {
		try {
			Object para[] = destUpdateSql.getPara(data);

			setPreSt(updatePs, para);

			int ret = updatePs.executeUpdate();
			if (ret == 1) countUpdate += 1;
			return ret;
		} catch (Exception e) {
			throw destDataBase.makeException(e, null);
		}
	}

	protected PreparedStatement getPrepareStatement(Connection c, String sql) throws Exception {

		PreparedStatement preStmt = null;

		try {
			preStmt = c.prepareStatement(sql);
			if (preStmt == null) {
				c.close();
			}
			return preStmt;
		} catch (Exception ex) {
			throw ex;
		}
	}

	private synchronized Connection getConnection(DataBase database) throws Exception {
		try {
			Class.forName(database.getDriver()).newInstance();

			Connection c;

			if (database.getUser() != null && database.getUser().length() > 0) {
				c = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
			} else {
				c = DriverManager.getConnection(database.getUrl());
			}

			c.setAutoCommit(isAutoCommit == null ? database.isAutoCommit() : isAutoCommit);
			c.setReadOnly(database.isReadOnly());

			return c;
		} catch (Exception ex) {
			throw ex;
		}
	}

	private PreparedStatement makeDestPs(Connection c, SqlBean sqlBean, Object data) throws Exception {
		QueryResult esql = sqlBean.getOneSql(data);
		PreparedStatement ps = getPrepareStatement(c, esql.getSql());

		Logger.logger.trace(sqlBean.getQid() + "=" + data);

		return ps;
	}
}

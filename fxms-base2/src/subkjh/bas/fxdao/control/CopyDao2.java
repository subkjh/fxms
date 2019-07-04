package subkjh.bas.fxdao.control;

import java.util.List;

import subkjh.bas.dao.control.DbTrans;

public class CopyDao2 {

	private DbTrans srcTran;
	private DbTrans dstTran;

	private int boxSize = 1000;

	public CopyDao2(DbTrans srcTran, DbTrans dstTran) {

		this.srcTran = srcTran;
		this.dstTran = dstTran;

	}

	/**
	 * 
	 * @param qid
	 * @param table
	 * @throws Exception
	 */
	public void copy(String qid, String table, String qid2) throws Exception {

		try {
			srcTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		try {
			dstTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			srcTran.stop();
			throw e;
		}

		CopyTempListener daoListener = new CopyTempListener(dstTran, "T_" + table + "_TMP", boxSize, true);
		srcTran.setDaoListener(daoListener);

		try {
			srcTran.selectQid2Obj(qid, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				srcTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		CopyListener l2 = new CopyListener(dstTran, table, boxSize);
		dstTran.setDaoListener(l2);
		dstTran.selectQid2Obj(qid2, null);

		if (dstTran != null) {
			try {
				dstTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void copy(String table, String qid, String qids[]) throws Exception {

		try {
			srcTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		try {
			dstTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			srcTran.stop();
			throw e;
		}

		CopyTempListener daoListener = new CopyTempListener(dstTran, "T_" + table + "_TMP", boxSize, true);
		srcTran.setDaoListener(daoListener);

		try {
			srcTran.selectQid2Obj(qid, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				srcTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dstTran.commit(); // TODO -remove

		dstTran.setDaoListener(null);
		try {
			for (String q : qids) {
				dstTran.execute(q, null);
			}
			dstTran.commit();
		} catch (Exception ex) {
			dstTran.rollback();
			ex.printStackTrace();
		}

		if (dstTran != null) {
			try {
				dstTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void copy4Query(String table, String selectSql, List<String> executeSql, boolean isTempTable)
			throws Exception {

		try {
			srcTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		try {
			dstTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			srcTran.stop();
			throw e;
		}

		CopyTempListener daoListener = new CopyTempListener(dstTran, "FX_TMP_" + table, boxSize, isTempTable);
		srcTran.setDaoListener(daoListener);

		try {
			srcTran.selectSql2Obj(selectSql, null);
		} catch (Exception e) {
			System.out.println("ERROR-SQL{" + selectSql + "}");
			e.printStackTrace();
			throw e;
		} finally {
			try {
				srcTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (isTempTable == false) {
			dstTran.commit();
		}

		if (executeSql.size() > 0) {

			dstTran.setDaoListener(null);
			try {
				for (String exeSql : executeSql) {
					dstTran.executeSql(exeSql, null);
				}
				dstTran.commit();
			} catch (Exception ex) {
				dstTran.rollback();
				ex.printStackTrace();
			}

		}

		if (dstTran != null) {
			try {
				dstTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setBoxSize(int boxSize) {
		this.boxSize = boxSize;
	}

}

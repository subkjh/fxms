package subkjh.bas.fxdao.control;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.control.DaoListener;
import subkjh.bas.dao.control.DbTrans;

public class CopyDao {

	class Listner implements DaoListener {

		@Override
		public void onFinish(Exception ex) throws Exception {

			if (ex != null) {
				dstTran.rollback();
				return;
			}

			if (valueList.size() > 0) {
				try {
					insert();
				} catch (Exception e) {
					dstTran.rollback();
					throw e;
				}
			}

			dstTran.commit();

		}

		@Override
		public void onSelected(int rowNo, Object obj) throws Exception {

			valueList.add((Object[]) obj);

			if (valueList.size() >= boxSize) {
				insert();
			}

		}
		
		public void onExecuted(Object data, Exception ex) throws Exception
		{
			
		}


		@Override
		public void onStart(String colNames[]) throws Exception {

			StringBuffer name = new StringBuffer();
			StringBuffer value = new StringBuffer();

			for (String col : colNames) {
				if (name.length() > 0) {
					name.append(", ");
					value.append(", ");
				}

				name.append(col);
				value.append("?");

			}

			insertSql = new StringBuffer();
			insertSql.append("insert into ");
			insertSql.append(dstTab);
			insertSql.append("(");
			insertSql.append(name);
			insertSql.append(") values ( ");
			insertSql.append(value);
			insertSql.append(")");

			System.out.println(insertSql);

		}
	}

	private StringBuffer insertSql;
	private String dstTab;
	private DbTrans srcTran;
	private DbTrans dstTran;
	private List<Object[]> valueList;

	private int boxSize = 1000;

	public CopyDao(DbTrans srcTran, DbTrans dstTran) {

		this.srcTran = srcTran;
		this.dstTran = dstTran;

		valueList = new ArrayList<Object[]>();
	}

	private void close() {
		if (srcTran != null) {
			try {
				srcTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
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

	/**
	 * 
	 * @param qid
	 * @param table
	 * @throws Exception
	 */
	public void copy(String qid, String table) throws Exception {

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

		dstTab = table;

		Listner daoListener = new Listner();
		srcTran.setDaoListener(daoListener);

		try {
			srcTran.selectQid2Obj(qid, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public void copyByTable(String srcTable, String dstTable) throws Exception {

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

		dstTab = dstTable;

		Listner daoListener = new Listner();
		srcTran.setDaoListener(daoListener);

		try {
			srcTran.selectSql2Obj("select * from " + srcTable, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private void insert() throws Exception {
		try {
			dstTran.executeSql(insertSql.toString(), valueList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			valueList.clear();
		}
	}

	public void setBoxSize(int boxSize) {
		this.boxSize = boxSize;
	}

}

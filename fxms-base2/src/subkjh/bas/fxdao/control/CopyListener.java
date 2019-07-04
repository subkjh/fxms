package subkjh.bas.fxdao.control;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.control.DaoListener;
import subkjh.bas.dao.control.DbTrans;

public class CopyListener implements DaoListener {

	private List<Object[]> valueList;
	private DbTrans dstTran;
	private String dstTable;
	private int boxSize;

	public CopyListener(DbTrans dstTran, String dstTable, int boxSize) {
		this.dstTran = dstTran;
		this.dstTable = dstTable;
		this.boxSize = boxSize;
	}
	public void onExecuted(Object data, Exception ex) throws Exception
	{
		
	}
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
		insertSql.append(dstTable);
		insertSql.append(" ( ");
		insertSql.append(name);
		insertSql.append(" ) values ( ");
		insertSql.append(value);
		insertSql.append(" )");

		System.out.println(insertSql);
		
		valueList = new ArrayList<Object[]>();
	}

	private StringBuffer insertSql;

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
}

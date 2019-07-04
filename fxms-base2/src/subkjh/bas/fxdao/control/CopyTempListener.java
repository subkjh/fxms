package subkjh.bas.fxdao.control;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.control.DaoListener;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.exception.DBObjectDupException;
import subkjh.bas.dao.exception.TableNotFoundException;

public class CopyTempListener implements DaoListener {

	private List<Object[]> valueList;
	private DbTrans dstTran;
	private String dstTable;
	private int boxSize;
	private boolean isTempTable = true;

	public CopyTempListener(DbTrans dstTran, String dstTable, int boxSize, boolean isTempTable) {
		this.dstTran = dstTran;
		this.dstTable = dstTable;
		this.boxSize = boxSize;
		this.isTempTable = isTempTable;
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

		createTempTableSql = new StringBuffer();

		try {
			dstTran.executeSql("drop table " + dstTable, null);
		} catch (TableNotFoundException e) {
		} catch (Exception e) {
			throw e;
		}

		if (isTempTable) {
			createTempTableSql.append("CREATE GLOBAL TEMPORARY TABLE ");
		} else {
			createTempTableSql.append("CREATE TABLE ");
		}
		createTempTableSql.append(dstTable);
		createTempTableSql.append(" ( ");

		for (String col : colNames) {
			if (name.length() > 0) {
				name.append(", ");
				value.append(", ");
				createTempTableSql.append(", ");
			}

			name.append(col);
			value.append("?");
			createTempTableSql.append(col + " varchar2(1000)");

		}

		if (isTempTable) {
			createTempTableSql.append(") on commit delete rows");
		} else {
			createTempTableSql.append(")");
		}

		System.out.println(createTempTableSql);

		try {
			dstTran.executeSql(createTempTableSql.toString(), null);
		} catch (DBObjectDupException e) {
		} catch (Exception e) {
			throw e;
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

	private StringBuffer createTempTableSql;

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

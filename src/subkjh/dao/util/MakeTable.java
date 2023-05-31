package subkjh.dao.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.dao.QidDao;
import subkjh.dao.database.DataBase;
import subkjh.dao.database.MariaDb;
import subkjh.dao.def.Column;
import subkjh.dao.def.Index;
import subkjh.dao.def.Table;
import subkjh.dao.util.QueryMaker.VAR_TYPE;

public class MakeTable {

	public static void main(String[] args) {

		MakeTable p = new MakeTable(new MariaDb());

		try {
			// p.createTable(database, new File("datas/create_table.txt"),
			// true);
			// p.printDropTableQuery(new File("datas/create_table.txt"));
			p.printCreateTableQuery(new File("datas/create_table.txt"));
			//
			// p.printInsertQuery(new File("datas/insert_data.txt"));
			// p.printUpdateQuery(new File("datas/data.txt"));

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private final DataBase database;
	private final String LINE = "\n---------------------------------------------------------------------";

	public MakeTable(DataBase db) {
		this.database = db;
	}

	/**
	 * 테이블 생성
	 * 
	 * @throws Exception
	 */
	public void createTable(List<Table> tabList, boolean renew) throws Exception {

		System.out.println(LINE);
		System.out.println("staring create tables... " + tabList.size());
		List<String> createdList = new ArrayList<String>();

		QidDao tran = database.createQidDao();
		try {
			tran.start();

			if (renew) {

				System.out.println(LINE);
				System.out.println("drop tables ... ");

				for (int index = tabList.size() - 1; index >= 0; index--) {
					if (database.existTable(tran, tabList.get(index).getName())) {
						tran.executeSql("drop table " + tabList.get(index).getName(), null);
						System.out.println("drop table " + tabList.get(index).getName());
					}
				}
			}

			String sql;
			String prefix;
			int seqno = 0;
			System.out.println(LINE);
			System.out.println("create tables... ");

			for (Table tab : tabList) {

				seqno++;

				prefix = seqno + "/" + tabList.size() + " :";

				try {
					createTable(prefix, tab, tran, renew);
					createdList.add(tab.getName());
				} catch (Exception e) {
					sql = "drop table " + tab.getName();
					tran.executeSql(sql, null);
					System.out.println("drop table " + tab.getName());
				}
			}

			tran.commit();

		} catch (Exception e) {

			System.out.println(LINE);
			System.out.println("Rollback create tables...");

			for (int index = createdList.size() - 1; index >= 0; index--) {
				String sql = "drop table " + createdList.get(index);
				tran.executeSql(sql, null);
				System.out.println("drop table " + createdList.get(index));
			}

			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public String getAddColumnQuery(List<Table> tableList) throws Exception {

		StringBuffer ret = new StringBuffer();

		for (Table table : tableList) {
			for (Column col : table.getColumns()) {
				ret.append(database.getSqlAdd(col, table.getName()));
				ret.append(";\n\n");
			}
		}
		return ret.toString();
	}

	public String getCreateTableQuery(List<Table> tableList) throws Exception {

		StringBuffer ret = new StringBuffer();

		for (Table table : tableList) {

			ret.append(database.getSqlCreate(table));
			ret.append(";\n\n");

			List<String> sList = database.getSqlCreateIndex(table);
			if (sList != null) {
				for (String s : sList) {
					ret.append(s);
					ret.append(";\n\n");
				}
			}

			sList = database.getSqlComment(table);
			if (sList != null) {
				for (String s : sList) {
					printSql(s);
					ret.append(s);
					ret.append(";\n\n");
				}
			}
		}
		return ret.toString();
	}

	public void printConst(File f, String type, int indexOfName, int indexOfValue, int indexOfComment)
			throws Exception {

		Map<Table, List<Object[]>> map = new SqlUtil().getDataList(f);
		String tag = type.contains("String") ? "\"" : "";

		for (Table table : map.keySet()) {

			for (Object[] data : map.get(table)) {

				System.out.println("/** " + data[indexOfComment] + " (" + data[indexOfValue] + ")  */");
				System.out.println("public static final " + type + " " + data[indexOfName] + " = " + tag
						+ data[indexOfValue] + tag + ";");
			}
		}
	}

	public void printCreateTableQuery(File f) throws Exception {

		List<Table> tableList = new SqlUtil().getTableList(f);
		for (Table table : tableList) {

			printSql(database.getSqlCreate(table));

			List<String> sList = database.getSqlCreateIndex(table);
			if (sList != null) {
				for (String s : sList) {
					printSql(s);
				}
			}

			sList = database.getSqlComment(table);
			if (sList != null) {
				for (String s : sList) {
					printSql(s);
				}
			}
		}
	}

	public void printDropTableQuery(DataBase database, File f) throws Exception {

		List<Table> tableList = new SqlUtil().getTableList(f);
		for (Table table : tableList) {

			printSql(database.getSqlDrop(table));

		}
	}

	public void printInsertQuery(File f, StringAdapter adapter) throws Exception {
		printInsertQuery(f, adapter, null);
	}

	public void printInsertQuery(File f, StringAdapter adapter, SqlAdapter sqlAdapter) throws Exception {
		Map<Table, List<Object[]>> map = new SqlUtil().getDataList(f);
		printInsertQuery(map, adapter, sqlAdapter);
	}

	public void printInsertQuery(Map<Table, List<Object[]>> map, StringAdapter adapter, SqlAdapter sqlAdapter)
			throws Exception {

		QueryMaker maker = new QueryMaker(adapter, VAR_TYPE.Dollar);
		String sql;

		for (Table table : map.keySet()) {

			for (Object[] data : map.get(table)) {

				sql = maker.getInsertSql(table, data);
				if (adapter != null) {
					sql = adapter.convert(sql);
				}
				if (sqlAdapter != null) {
					sqlAdapter.onSql(sql);
				} else {
					System.out.println(sql + ";");
				}
			}
		}
	}

	public void printUpdateQuery(File f, String... colNms) throws Exception {

		QueryMaker maker = new QueryMaker();
		Map<Table, List<Object[]>> map = new SqlUtil().getDataList(f);

		for (Table table : map.keySet()) {

			for (Object[] data : map.get(table)) {

				System.out.println(maker.getUpdateSql(table, data, colNms) + ";");
			}
		}
	}

	/**
	 * 테이블을 생성합니다.
	 * 
	 * @param table
	 * @param tran
	 * @throws Exception
	 */
	private void createTable(String prefix, Table table, QidDao tran, boolean renew) throws Exception {
		String sql = null;
		List<Index> indexList;

		if (tran.getDatabase().existTable(tran, table.getName())) {
			if (renew) {
				tran.executeSql("drop table " + table.getName(), null);
				System.out.println(prefix + " drop table " + table.getName());
			} else {
				System.out.println("Exist " + table.getName());
				return;
			}
		}

		sql = tran.getDatabase().getSqlCreate(table);

		tran.executeSql(sql, null);
		System.out.println(prefix + " create table " + table.getName());

		indexList = table.getIndexList();
		if (indexList != null && indexList.size() > 0) {
			for (Index idx : indexList) {

				if (idx.isFk() || idx.isPk())
					continue;

				sql = tran.getDatabase().getSqlCreate(idx, table);

				tran.executeSql(sql, null);
				System.out.println(prefix + " create index " + idx.getIndexName());
			}
		}

		List<String> commentList = tran.getDatabase().getSqlComment(table);
		if (commentList != null && commentList.size() > 0) {
			for (String comment : commentList) {
				try {
					tran.executeSql(comment, null);
					System.out.println(prefix + " set comment " + table.getName());
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

	}

	private void printSql(String sql) {
		System.out.print(sql);
		System.out.println(";");
	}
}

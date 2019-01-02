package subkjh.bas.dao.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.control.sync.SyncDao;
import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.Query;
import subkjh.bas.dao.data.Sequence;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.data.View;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.database.Oracle;
import subkjh.bas.fxdao.control.QueryMaker;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.FileUtil;

public class SetupDataBase {

	class InsDbInfo {

		/** 기본 테이블 정보 */
		File fileTable = null;
		/** 기본 데이터 정보 */
		File fileData = null;
		/** 사이트 특화된 테이블 정보 테이블 */
		File fileTableSite = null;
		/** 사이트 특화된 데이터 정보 테이블 */
		File fileDataSite = null;
		/** 기본 데이터 테이블 스페이스 */
		boolean isRenew = false;

		boolean batchInsert = false;

	}

	private InsDbInfo dbInfo;
	private DataBase database;
	protected static final String LINE = "\n---------------------------------------------------------------------";
	private List<List<String>> sheetList;
	private SqlUtil schemaUtil;
	private String tsDataDef = null;
	private String tsIdxDef = null;

	public static void main(String[] args) throws FileNotFoundException, Exception {

		try {
			DBManager.getMgr().addDataBase(new File("deploy/conf/databases.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		SetupDataBase dao = new SetupDataBase("DFC_EMS_NPRISM", new File("datas/tables.xlsx"),
				new File("datas/datas.xlsx"), "LGUIX_DAT", "LGUIX_IDX", true);
		// dao.createAll(true);
		dao.printAll(new Oracle());
	}

	public SetupDataBase(String dbname, File tables, File datas, String tsData, String tsIdx, boolean isRenew)
			throws FileNotFoundException, Exception {

		dbInfo = new InsDbInfo();
		dbInfo.fileTable = tables;
		dbInfo.fileData = datas;
		dbInfo.fileTableSite = null;
		dbInfo.fileDataSite = null;
		dbInfo.isRenew = isRenew;
		tsDataDef = tsData;
		tsIdxDef = tsIdx;

		if (dbInfo.fileTable.exists() == false)
			throw new FileNotFoundException(dbInfo.fileTable.getPath());
		if (dbInfo.fileData.exists() == false)
			throw new FileNotFoundException(dbInfo.fileData.getPath());

		database = DBManager.getMgr().getDataBase(dbname);

		if (database == null) {
			throw new Exception(dbname + " is not defined in " + DBManager.getMgr().getDataBaseDefineFile());
		}

		sheetList = readTableInfo();
		schemaUtil = new SqlUtil();
	}

	public void createAll(boolean isRenew) throws Exception {

		dbInfo.isRenew = isRenew;

		createSequence(getSequenceList());

		List<Table> tabList = getTableList();

		createTable(tabList);

		createView(getViewList());

		insertData(tabList, getData());

		createQuery(getQueryList());

	}

	public void printAll(DataBase db) throws Exception {

		StringBuffer sb = new StringBuffer();
		List<Sequence> seqList = getSequenceList();
		for (Sequence seq : seqList) {
			sb.append(db.getSqlCreate(seq) + ";\n");
		}
		FileUtil.writeToFile("sequence.sql", sb.toString(), false);
		//

		sb = new StringBuffer();
		List<Table> tabList = getTableList();
		for (Table tab : tabList) {
			sb.append(db.getSqlCreate(tab) + ";\n");
			List<String> sList = db.getSqlComment(tab);
			for (String s : sList) {
				sb.append(s + ";\n");
			}
		}
		FileUtil.writeToFile("table.sql", sb.toString(), false);

		sb = new StringBuffer();
		List<View> viewList = getViewList();
		for (View v : viewList) {
			sb.append(db.getSqlCreate(v) + ";\n");
		}

		FileUtil.writeToFile("view.sql", sb.toString(), false);
		//
		print(tabList, getData());
		//
		// createQuery(getQueryList());
	}

	/**
	 * 시퀀스 생성
	 * 
	 * @throws Exception
	 */
	public void createSequence(List<Sequence> sequenceList) throws Exception {

		System.out.println(LINE);
		System.out.println("create sequence ... ");

		new SyncDao().prepareSequence(database);

		DbTrans tran = database.createDbTrans();
		DataBase database = tran.getDatabase();
		int ret;

		try {
			tran.start();

			if (dbInfo.isRenew) {
				for (Sequence sequence : sequenceList) {
					ret = tran.executeSql(database.getSqlDrop(sequence), null);
					System.out.println("drop sequece " + sequence.getSequenceName() + " : " + getString(ret));
				}
			}

			List<Sequence> createdList = new ArrayList<Sequence>();
			for (Sequence sequence : sequenceList) {
				ret = tran.executeSql(database.getSqlCreate(sequence), null);
				System.out.println("create sequece " + sequence.getSequenceName() + " : " + getString(ret));
				createdList.add(sequence);
			}

			// if (ret.isOk() == false) {
			//
			// System.out.println();
			// System.out.println("Rollback Sequence....");
			//
			// for (int index = 0; index < createdList.size(); index++) {
			// ret =
			// tran.executeSql(database.getSqlDrop(createdList.get(index)),
			// null);
			// System.out.println(
			// "drop sequece " + createdList.get(index).getSequenceName() + " :
			// " + getString(ret));
			// }
			//
			// throw new Exception("ERROR : create sequences... ");
			// }

			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			System.out.println(e.getMessage());
			throw new Exception("Create Sequence" + "\n" + e.getMessage());
		} finally {
			tran.stop();
		}

	}

	public void createView(List<View> viewList) throws Exception {

		System.out.println(LINE);
		System.out.println("create views ... ");

		DbTrans tran = database.createDbTrans();

		try {
			tran.start();
			for (View view : viewList) {

				if (view.getQuery() == null || view.getQuery().trim().length() == 0) {
					System.out.println(view.getName() + " has empty query = ERROR");
					continue;
				}

				tran.executeSql(view.getQuery(), null);
				System.out.println("create view " + view.getName());

			}

			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	public Map<Table, List<Object[]>> getData() throws Exception {

		List<List<Object[]>> dataList = readDataInfo();

		Map<Table, List<Object[]>> dataMap = getDataList(dataList);

		List<Table> list = new ArrayList<Table>();

		for (Table tab : dataMap.keySet()) {
			list.add(tab);
		}

		Collections.sort(list, new Comparator<Table>() {
			@Override
			public int compare(Table t1, Table t2) {
				return t1.getName().compareTo(t2.getName());
			}
		});

		for (Table tab : list) {
			System.out.println(tab.getName() + " = " + dataMap.get(tab).size());
		}

		return dataMap;
	}

	public List<Query> getQueryList() throws Exception {
		return schemaUtil.getQueryList(sheetList);
	}

	public List<Sequence> getSequenceList() throws Exception {
		return schemaUtil.getSequenceList(sheetList);
	}

	public List<Table> getTableList() throws Exception {

		List<Table> tabList = schemaUtil.getTableList4Excel(sheetList);
		for (Table tab : tabList) {
			tab.setTableSpaceData(tsDataDef);
			tab.setTableSpaceIndex(tsIdxDef);
		}
		return tabList;
	}

	public List<View> getViewList() throws Exception {
		return schemaUtil.getViewList(sheetList);
	}

	public void insertData(List<Table> tabList, Map<Table, List<Object[]>> dataMap) throws Exception {

		System.out.println(LINE);
		System.out.println("insert datas ... ");

		List<Object[]> dataList;
		String sql;
		int ret;
		DbTrans tran = database.createDbTrans();
		QueryMaker maker = new QueryMaker();

		try {
			tran.start();

			if (dbInfo.isRenew) {
				for (int index = tabList.size() - 1; index >= 0; index--) {
					for (Table dataTab : dataMap.keySet()) {
						if (dataTab.getName().equals(tabList.get(index).getName())) {
							sql = maker.getDeleteSql(tabList.get(index));
							tran.executeSql(sql, null);
						}
					}
				}
			}

			for (Table tab : tabList) {
				for (Table dataTab : dataMap.keySet()) {
					if (dataTab.getName().equals(tab.getName())) {

						dataList = dataMap.get(dataTab);

						System.out.println(LINE);
						StringBuffer sb = new StringBuffer();
						for (Column c : dataTab.getColumnList()) {
							sb.append(c.getName() + ", ");
						}
						System.out.println(sb.toString());

						if (dbInfo.batchInsert) {

							sql = null;
							// TODO
							// sql = dataTab.getSqlPrepareInsert();
							List<Object[]> paraList = new ArrayList<Object[]>();
							for (Object data[] : dataList) {
								paraList.add(maker.getInsertPara(dataTab, data));
							}
							ret = tran.executeSql(sql, paraList);

							System.out.println("insert into " + dataTab.getName() + " = " + ret);
						} else {
							for (Object data[] : dataList) {
								sql = maker.getInsertSql(dataTab, data);
								tran.executeSql(sql, null);
							}
						}
					}
				}
			}

			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	public void print(List<Table> tabList, Map<Table, List<Object[]>> dataMap) throws Exception {

		List<Object[]> dataList;
		String sql;

		FileUtil.writeToFile("data.sql", "# by subkjh - datas\n\n", false);

		StringBuffer sb = new StringBuffer();
		QueryMaker maker = new QueryMaker();

		for (Table tab : tabList) {

			sb = new StringBuffer();

			for (Table dataTab : dataMap.keySet()) {
				if (dataTab.getName().equals(tab.getName())) {

					dataList = dataMap.get(dataTab);

					for (Object data[] : dataList) {
						sql = maker.getInsertSql(dataTab, data);
						sb.append(sql + ";\n");
					}
				}
			}

			FileUtil.writeToFile("data.sql", sb.toString(), true);
		}

	}

	private Object convert(Object obj) {
		if (obj instanceof Double) {
			Double v = (Double) obj;
			if (v == v.longValue())
				return new Long(v.longValue());
			return v;
		}

		return obj;
	}

	private void createQuery(List<Query> queryList) throws Exception {

		DbTrans tran = database.createDbTrans();
		String ss[];
		try {
			tran.start();
			for (Query query : queryList) {

				if (query == null || query.getQuery().trim().length() == 0) {
					continue;
				}

				ss = query.getQuery().split(";");
				for (String s : ss) {
					if (s.trim().length() > 0) {
						tran.executeSql(s, null);
					}
				}
			}

			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 테이블 생성
	 * 
	 * @throws Exception
	 */
	private void createTable(List<Table> tabList) throws Exception {

		System.out.println(LINE);
		System.out.println("staring create tables... " + tabList.size());
		List<String> createdList = new ArrayList<String>();

		DbTrans tran = database.createDbTrans();
		try {
			tran.start();

			if (dbInfo.isRenew) {

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
					createTable(prefix, tab, tran);
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

	/**
	 * 테이블을 생성합니다.
	 * 
	 * @param table
	 * @param tran
	 * @throws Exception
	 */
	private void createTable(String prefix, Table table, DbTrans tran) throws Exception {
		String sql = null;
		List<Index> indexList;

		if (tran.getDatabase().existTable(tran, table.getName())) {
			if (dbInfo.isRenew) {
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
				System.out.println(prefix + " create index " + idx.getName());
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

	private int getCountNotNull(Object[] cell) {
		int count = 0;
		for (Object obj : cell) {
			if (obj != null)
				count++;
		}

		return count;
	}

	/**
	 * 
	 * @param sheetList
	 * @return
	 * @throws Exception
	 */
	private Map<Table, List<Object[]>> getDataList(List<List<Object[]>> sheetList) throws Exception {
		int index = 0;
		Table table = null;
		String step = "";
		Column column;
		String colunmName;
		Map<Table, List<Object[]>> dataMap = new HashMap<Table, List<Object[]>>();
		List<Object[]> dataList = null;

		for (List<Object[]> list : sheetList) {

			step = "";

			for (Object obj[] : list) {
				if (step.equals("table")) {
					for (int col = 0; col < obj.length; col++) {
						colunmName = getString(obj[col]);
						if (colunmName.trim().length() > 0) {
							if (colunmName.indexOf("삭제") < 0) {
								column = new Column();
								column.setColumnNo(col);
								column.setName(colunmName);
								table.add(column);
							}
						}
					}
					step = "column";
				} else if ("table name".equalsIgnoreCase(getString(obj[0]))) {
					table = new Table();
					table.setName(getString(obj[1]));
					step = "table";
					table.setTableNo(index);
					dataList = new ArrayList<Object[]>();
					dataMap.put(table, dataList);
					index++;

				} else if (step.equals("column")) {

					// System.out.println(CClass.toString(obj));

					if (getCountNotNull(obj) > 1) {

						for (int i = 0; i < table.sizeCol(); i++)
							obj[i] = convert(obj[i]);

						dataList.add(Arrays.copyOf(obj, table.sizeCol()));

					} else if (getCountNotNull(obj) == 1) {
						step = "";
					}
				}
			}
		}

		return dataMap;
	}

	private String getString(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	List<String> getStringList(Object[] obj) {

		List<String> list = new ArrayList<String>();

		if (obj != null) {
			for (Object o : obj) {
				if (o != null) {
					if (o instanceof Double) {
						Double v = (Double) o;
						if (v == v.longValue()) {
							list.add(v.longValue() + "");
						} else {
							list.add(v.floatValue() + "");
						}
					} else {
						list.add(o.toString().trim());
					}
				}
			}
		}

		return list;
	}

	private List<List<Object[]>> readDataInfo() throws Exception {

		// TODO
		return null;

		// System.out.println("read datas from excel...");
		//
		// if (dbInfo.fileData.exists() == false)
		// throw new Exception("File not found " + dbInfo.fileData.getPath());
		//
		// List<List<Object[]>> sheetList =
		// ExcelReader.getDatas(dbInfo.fileData);
		//
		// if (dbInfo.fileDataSite != null) {
		// if (dbInfo.fileDataSite.exists() == false)
		// throw new Exception("File not found " +
		// dbInfo.fileDataSite.getPath());
		//
		// List<List<Object[]>> sheetList2 =
		// ExcelReader.getDatas(dbInfo.fileDataSite);
		// sheetList.addAll(sheetList2);
		// }
		//
		// return sheetList;
	}

	private List<List<String>> readTableInfo() throws Exception {
		return null;

		// TODO

		// List<List<String>> tableInfoList = new ArrayList<List<String>>();
		//
		// if (dbInfo.fileTable.exists() == false)
		// throw new Exception("File not found " + dbInfo.fileTable.getPath());
		//
		// List<List<Object[]>> sheetList =
		// ExcelReader.getDatas(dbInfo.fileTable);
		// for (List<Object[]> list : sheetList) {
		// for (Object[] obj : list) {
		// tableInfoList.add(getStringList(obj));
		// }
		// }
		//
		// if (dbInfo.fileTableSite != null) {
		//
		// if (dbInfo.fileTableSite.exists() == false)
		// throw new Exception("File not found " +
		// dbInfo.fileTableSite.getPath());
		//
		// sheetList = ExcelReader.getDatas(dbInfo.fileTableSite);
		// for (List<Object[]> list : sheetList) {
		// for (Object[] obj : list) {
		// tableInfoList.add(getStringList(obj));
		// }
		// }
		// }
		//
		// return tableInfoList;
	}
}

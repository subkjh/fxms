package subkjh.bas.dao.control.sync;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.log.ProgressLogger;
import subkjh.bas.co.utils.FileUtil;
import subkjh.bas.dao.control.CommDao;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.Sequence;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.database.Oracle;

public class SyncDao extends CommDao {

	public static void main(String[] args) {

		if (args.length > 0) {
			BasCfg.setHome(args[0]);
			args = Arrays.copyOfRange(args, 1, args.length);
		}

		Logger l = Logger.createLogger(".", "sync");
		l.setLevel(LOG_LEVEL.debug);
		Logger.logger = l;

		System.out.println("HOME=" + BasCfg.getHome());

		try {
			DBManager.getMgr().addDataBase(new File(BasCfg.getHome() + File.separator + "deploy/conf/databases.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		SyncDao dao = new SyncDao(args);

		dao.doWork();
	}

	/** 채워질 데이터베이스 */
	private DataBase dbDst;
	/** 복사대상 데이터베이스 */
	private DataBase dbSrc;
	private boolean exclude = false;
	private String folder = "datas";
	private boolean include = false;
	private boolean includeData;
	private boolean includeSequence;
	private boolean includeTable;
	private List<String> inList = new ArrayList<String>();
	private boolean isDump;
	private boolean isRestore;
	private boolean isSync;
	private boolean isTruncate = false;
	private String regex = null;
	private String tablespaceDat;
	private String tablespaceIdx;
	private List<Table> tabList = null;
	private boolean isPrint;

	public SyncDao() {
	}

	public SyncDao(String args[]) {
		parse(args);
	}

	public void doWork() {

		try {
			if (isSync) {
				sync();
			} else if (isDump) {
				dump();
			} else if (isRestore) {
				restore();
			} else if (isTruncate) {
				truncateDatabse();
			} else if (isPrint) {
				print();
			} else {
				usage("set work type.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.logger.error(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void print() throws Exception {
		try {
			tabList = (List<Table>) FileUtil.readObjectToFile(folder + File.separator + "table.jof");
			if (tabList == null || tabList.size() == 0) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		tabList = sort(tabList);

		Table tabArr[] = filterTable(tabList);

		for (Table tab : tabArr) {
			System.out.println("---------------------------------------------------------------");
			System.out.println(tab.getName());
			System.out.println("---------------------------------------------------------------");
			System.out.println(dbDst.getSqlCreate(tab));
		}

	}

	/**
	 * 지정된 폴더로 dump
	 * 
	 * @throws Exception
	 */
	public void dump() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append(LINE + "\ndump " + dbSrc.getName() + "\n" + LINE);
		System.out.println(sb.toString());
		Logger.logger.info("\n" + sb.toString());

		if (dbSrc == null)
			throw new Exception("Source Database must be defined.");

		if (includeTable) {
			dumpTable(getTabToWork());
		}

		if (includeSequence) {
			try {
				dumpSequence();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (includeData) {
			dumpData(getTabToWork());
		}
	}

	/**
	 * 복제할 데이터베이스에서 테이블 정보를 읽어 담을 데이터베이스에 맞는 쿼리를 생성하야 제공합니다.
	 * 
	 * @param src
	 *            복제할 데이터베이스
	 * @param dest
	 *            담을 데이터베이스
	 * @param nameList
	 *            처리할 테이블 목록
	 * @return 쿼리문
	 * @throws Exception
	 */
	public List<String> makeCreateSql(DataBase src, DataBase dest, List<String> nameList) throws Exception {

		dbSrc = src;
		dbDst = dest;

		List<String> sqlList = new ArrayList<String>();
		String sql;
		List<Table> tabList = getTableList(src, nameList);
		Table tableDst;

		try {
			for (Table tab : tabList) {

				tableDst = (Table) tab.clone();

				try {
					sql = dbDst.getSqlCreate(tableDst);
					sqlList.add(sql);

					System.out.println("\n#\n# " + tab.getName());
					System.out.println("\n#\n");
					System.out.println(sql);
					System.out.println(";\n\n");

					for (String e : dbDst.getSqlCreateIndex(tableDst)) {
						System.out.println("# ");
						sqlList.add(e);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

		return sqlList;

	}

	/**
	 * 
	 * @param dst
	 * @throws Exception
	 */
	public void prepareSequence(DataBase dst) throws Exception {

		DbTrans tran = dst.createDbTrans();

		try {
			tran.start();
			String qid = "INIT_SEQUENCE";
			int index = 1;

			while (true) {
				qid = "INIT_SEQUENCE__" + index;
				if (tran.getSqlBean(qid) == null)
					break;

				tran.execute(qid, null);

				index++;
			}

			return;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 지정된 폴더에서 데이터를 dump
	 * 
	 * @throws Exception
	 */
	public void restore() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append(LINE + "\nrestore to " + dbDst.getName());
		if (includeTable)
			sb.append(" TABLES");
		if (includeSequence)
			sb.append(" SEQUENCES");
		if (includeData)
			sb.append(" DATAS");
		sb.append("\n" + LINE);
		System.out.println(sb.toString());
		Logger.logger.info("\n" + sb.toString());

		if (dbDst == null)
			throw new Exception("Destination Database must be defined.");

		if (includeTable)
			restoreTable();
		if (includeSequence)
			restoreSequence();
		if (includeData)
			restoreData();
	}

	public void sync() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append(LINE + "\nsync from " + dbSrc.getName() + " to " + dbDst.getName() + "\n" + LINE);
		System.out.println(sb.toString());
		Logger.logger.info("\n" + sb.toString());

		if (dbSrc == null || dbDst == null)
			throw new Exception("Source and Destination Database must be defined.");

		if (includeTable)
			syncTable(null);

		if (includeSequence)
			syncSequence();

		if (includeData)
			syncData(null);

	}

	public void syncSequence(DataBase src, DataBase dst) throws Exception {
		syncSequence(src.getSequenceList(null), dst);
	}

	public void syncSequence(List<Sequence> seqList, DataBase dst) throws Exception {

		DbTrans tran = dst.createDbTrans();

		try {
			tran.start();
			String qid = "INIT_SEQUENCE";
			int index = 1;

			while (true) {
				qid = "INIT_SEQUENCE__" + index;
				if (tran.getSqlBean(qid) == null)
					break;

				tran.execute(qid, null);

				index++;
			}

			tran.execute("DELETE_SEQUENCE_ALL", null);

			Map<String, Object> para = new HashMap<String, Object>();

			for (Sequence seq : seqList) {
				para.put("seqName", seq.getSequenceName());
				para.put("valueMin", seq.getValueMin());
				para.put("valueMax", seq.getValueMax());
				para.put("valueNext", seq.getValueNext());
				para.put("incBy", seq.getIncBy());
				para.put("isCycle", seq.isCycle() ? "Y" : "N");

				tran.execute("CREATE_SEQUENCE", para);
			}

			return;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

	public void truncateDatabse() {

		System.out.print("Do you really want to truncate " + dbDst.getName() + " database?");
		System.out.flush();
		byte bytes[] = new byte[1024];
		String yesno = "no";
		try {
			int len = System.in.read(bytes);
			if ("yes".equals(new String(bytes, 0, len))) {
				yesno = "yes";
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if ("yes".equals(yesno) == false) {
			System.out.println("CANCELED");
			return;
		}

		// int ret;
		List<Table> tabList;
		try {
			tabList = getTabToWork();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		DbTrans tran = dbDst.createDbTrans();
		try {
			tran.start();

			for (int i = tabList.size() - 1; i >= 0; i--) {
				ProgressLogger.start("dropping " + tabList.get(i).getName());
				if (dbDst.existTable(tran, tabList.get(i).getName())) {
					tran.executeSql(dbDst.getSqlDrop(tabList.get(i)), null);
					// ProgressLogger.end(ret.isOk() ? "ok" : ("fail - " +
					// ret.getMsg()));
				} else {
					ProgressLogger.end("not found");
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}
	}

	protected String[] filter(List<String> nameList) throws Exception {

		List<String> ret = new ArrayList<String>();

		boolean toFilter = include || exclude || regex != null;

		if (dbSrc instanceof Oracle) {
			for (int i = nameList.size() - 1; i >= 0; i--) {
				if (nameList.get(i).startsWith("BIN"))
					nameList.remove(i);
			}
		}

		if (toFilter) {

			if (include) {
				for (String name : nameList) {
					for (String a : inList) {
						if (name.startsWith(a)) {
							ret.add(name);
							break;
						}
					}
				}
			}

			if (regex != null) {
				for (String name : nameList) {
					if (name.matches(regex) == exclude)
						continue;
					ret.add(name);
				}
			}

			if (exclude) {
				TAB: for (String name : nameList) {
					for (String a : inList) {
						if (name.startsWith(a))
							continue TAB;
					}
					ret.add(name);
				}
			}
			return ret.toArray(new String[ret.size()]);
		} else {
			return nameList.toArray(new String[nameList.size()]);
		}

	}

	protected Table[] filterTable(List<Table> tabList) throws Exception {

		List<Table> ret = new ArrayList<Table>();

		boolean toFilter = include || exclude || regex != null;

		if (toFilter) {

			if (include) {
				for (Table tab : tabList) {
					for (String a : inList) {
						if (tab.getName().startsWith(a)) {
							ret.add(tab);
							break;
						}
					}
				}
			}

			if (regex != null) {
				for (Table tab : tabList) {
					if (tab.getName().matches(regex) == exclude)
						continue;
					ret.add(tab);
				}
			}

			if (exclude) {
				TAB: for (Table tab : tabList) {
					for (String a : inList) {
						if (tab.getName().startsWith(a))
							continue TAB;
					}
					ret.add(tab);
				}
			}
			return ret.toArray(new Table[ret.size()]);
		} else {
			return tabList.toArray(new Table[tabList.size()]);
		}

	}

	protected void parse(String args[]) {

		System.out.println(Arrays.toString(args));
		Logger.logger.info(Arrays.toString(args));

		String src = null, dst = null;

		for (String a : args) {
			if (a.equals("--include")) {
				include = true;
				exclude = false;
			} else if (a.equals("--exclude")) {
				include = false;
				exclude = true;
			} else if (a.startsWith("-regex")) {
				regex = a.replaceFirst("-regex=", "");
			} else if (a.equals("--table")) {
				includeTable = true;
			} else if (a.equals("--sequence")) {
				includeSequence = true;
			} else if (a.equals("--print")) {
				isPrint = true;
			} else if (a.equals("--data")) {
				includeData = true;
			} else if (a.equals("--dump")) {
				isDump = true;
				isRestore = false;
				isSync = false;
			} else if (a.equals("--restore")) {
				isDump = false;
				isRestore = true;
				isSync = false;
			} else if (a.equals("--sync")) {
				isDump = false;
				isRestore = false;
				isSync = true;
			} else if (a.equals("--truncate")) {
				isTruncate = true;
			} else if (a.startsWith("-folder"))
				folder = a.replaceFirst("-folder=", "");
			else if (a.startsWith("-src")) {
				src = a.replaceFirst("-src=", "");
			} else if (a.startsWith("-dst")) {
				dst = a.replaceFirst("-dst=", "");
			} else if (a.startsWith("-batchsize")) {
				batchsize = Integer.parseInt(a.replaceFirst("-batchsize=", ""));
			} else if (a.startsWith("-tablespace-data"))
				tablespaceDat = a.replaceFirst("-tablespace-data=", "");
			else if (a.startsWith("-tablespace-index"))
				tablespaceIdx = a.replaceFirst("-tablespace-index=", "");

			else if (a.charAt(0) != '-') {
				inList.add(a);
			} else {
				usage("undefined parameter : " + "'" + a + "'");
			}

		}

		if (src != null) {
			dbSrc = DBManager.getMgr().getDataBase(src);
			if (dbSrc == null) {
				System.out.println("error : Not Defined Source DataBase : " + src);
				System.exit(0);
			}

			DbTrans tran = null;
			try {
				tran = dbSrc.createDbTrans();
			} catch (Exception e) {
				System.out.println("error : Cannot connected : " + src);
				System.exit(0);
			} finally {
				tran.stop();
			}
		}

		if (dst != null) {
			dbDst = DBManager.getMgr().getDataBase(dst);
			if (dbDst == null) {
				System.out.println("error : Not Defined Destination DataBase : " + dst);
				System.exit(0);
			}

			DbTrans tran = null;
			try {
				tran = dbDst.createDbTrans();
			} catch (Exception e) {
				System.out.println("error : Cannot connected : " + dst);
				System.exit(0);
			} finally {
				tran.stop();
			}
		}

		new File(folder).mkdirs();
	}

	protected void printAndExit(String tabArr[]) {
		System.out.println("size : " + tabArr.length);
		System.out.println("------------------------------------------------------------------");
		for (String tab : tabArr) {
			System.out.println(tab);
		}
		System.out.println("------------------------------------------------------------------");
		System.exit(0);
	}

	protected void usage(String msg) {

		System.out.println(msg);
		System.out.println();
		System.out.println(getClass().getSimpleName() + " :");
		System.out.println("--sync|--restore|--dump");
		System.out.println("-src=<source-database>");
		System.out.println("-dst=<destination-database>");
		System.out.println("--include|--execlude <name ...>");
		System.out.println("-regex=<regex>");
		System.out.println("--check : only show to sychronized table list");
		System.out.println("-batchsize=<batch size> : batch ");
		System.out.println("-tablespace-data=<table space for data>");
		System.out.println("-tablespace-index=<table space for index>");
		System.out.println("--table		: only tables");
		System.out.println("--sequence	: only sequences");
		System.out.println("--data		: only datas");

		System.exit(0);
	}

	private void checkExistDstTable(Table table, DbTrans tranDst) throws Exception {

		int ret;

		String sql;
		if (dbDst.existTable(tranDst, table.getName())) {
			sql = "delete from " + table.getName();

			ProgressLogger.start("deleting " + table.getName() + " table");

			Logger.logger.debug("deleting...", sql);
			ret = tranDst.executeSql(sql, null);
			Logger.logger.info(sql, ret);

			ProgressLogger.end("ok");
		} else {

			sql = dbDst.getSqlCreate(table);
			Logger.logger.debug("creating...", table.getName());

			ProgressLogger.start("creating " + table.getName() + " table");

			ret = tranDst.executeSql(sql, null);
			Logger.logger.info(sql);

			ProgressLogger.end("ok");

			List<String> idxList = dbDst.getSqlCreateIndex(table);

			if (idxList.size() > 0) {
				System.out.print("creating indexes on " + table.getName() + "... ");
				System.out.flush();
			}

			for (String e : idxList) {
				ret = tranDst.executeSql(e, null);
				Logger.logger.info(e);
			}

			if (idxList.size() > 0) {
				System.out.println("OK");
			}

		}

	}

	private void checkForeignKey(Table tab, Map<String, Table> tabMap, List<Table> tabList) throws Exception {
		if (tab.getIndexList() == null || tab.getIndexList().size() == 0) {
			if (contains(tabList, tab.getName()) == false)
				tabList.add(tab);
		} else {
			for (Index idx : tab.getIndexList()) {

				if (idx.isFk()) {
					if (contains(tabList, idx.getFkTable()) == false) {

						Table tabParent = tabMap.get(idx.getFkTable());
						if (tabParent == null) {
							tabParent = dbSrc.getTable(null, idx.getFkTable());
							if (tabParent == null)
								throw new Exception(idx.getFkTable() + " not found");
							tabMap.put(tabParent.getName(), tabParent);
						}
						checkForeignKey(tabParent, tabMap, tabList);

					}
				}
			}

			if (contains(tabList, tab.getName()) == false)
				tabList.add(tab);
		}
	}

	private boolean contains(List<Table> tabList, String name) {
		for (Table tab : tabList) {
			if (tab.getName().equals(name))
				return true;
		}

		return false;
	}

	private void dumpData(List<Table> tabList) throws Exception {
		try {

			DbTrans tran = dbSrc.createDbTrans();
			try {
				tran.start();
				for (Table tab : tabList) {
					dumpData(tab, tran);
				}
			} catch (Exception e) {
				Logger.logger.error(e);
				throw new Exception("" + "\n" + e.getMessage());
			} finally {
				tran.stop();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 테이블의 내용을 복사합니다.
	 * 
	 * @param tabSrc
	 *            복사할 테이블
	 * @param tabDst
	 *            복사 내용을 기록할 테이블
	 * @throws Exception
	 */
	private void dumpData(Table tab, DbTrans tranSrc) throws Exception {

		List<Object[]> list = tranSrc.selectSql2Obj("select count(*) from " + tab.getName(), null);
		int size;
		if (list.get(0)[0] instanceof Number) {
			size = ((Number) list.get(0)[0]).intValue();
		} else {
			try {
				size = Integer.parseInt(list.get(0)[0].toString());
			} catch (Exception e) {
				Logger.logger.error(e);
				size = -1;
			}
		}
		String sql = "select * from " + tab.getName();

		DumpDaoLsnr lsnr = new DumpDaoLsnr(folder, tab, size);
		lsnr.batchsize = batchsize;

		tranSrc.setDaoListener(lsnr);
		tranSrc.selectSql(sql, null);
		tranSrc.setDaoListener(null);

	}

	private void dumpSequence() throws Exception {
		List<Sequence> seqList = dbSrc.getSequenceList(null);
		FileUtil.writeObjectToFile(folder + File.separator + "sequence.jof", seqList);
	}

	private void dumpTable(List<Table> tabList) throws Exception {
		FileUtil.writeObjectToFile(folder + File.separator + "table.jof", tabList);

		Logger.logger.info("dump " + tabList.size() + " tables ");
	}

	private List<Table> getTableInfo(DataBase db, List<String> nameList) throws Exception {
		DbTrans tran = db.createDbTrans();

		try {
			tran.start();
			List<Table> tabList = new ArrayList<Table>();
			int index = 1;

			for (String name : nameList) {

				ProgressLogger
						.start("selecting " + name + " table information. " + index + "/" + nameList.size() + ") ");

				try {
					tabList.add(db.getTable(tran, name));
					ProgressLogger.end("ok");
				} catch (Exception e) {
					e.printStackTrace();
					tabList.add(null);
					ProgressLogger.end("fail");
				}

				index++;

			}
			return tabList;
		} catch (Exception e1) {
			throw e1;
		} finally {
			tran.stop();
		}
	}

	/**
	 * DB에서 테이블 목록을 찾습니다.<br>
	 * 
	 * @param db
	 * @param tabArr
	 * @return key : 테이블명, value : Table
	 * @throws Exception
	 */
	private List<Table> getTableList(DataBase db, List<String> nameList) throws Exception {

		Logger.logger.info("selecting tables ...");

		Map<String, Table> tabMap = new HashMap<String, Table>();

		List<Table> tabList = getTableInfo(db, nameList);

		for (Table tab : tabList) {
			tab.setTableSpaceData(tablespaceDat);
			tab.setTableSpaceIndex(tablespaceIdx);
			tabMap.put(tab.getName(), tab);
		}

		List<Table> ret = new ArrayList<Table>();
		Table tab;
		String tabNameArr[] = tabMap.keySet().toArray(new String[tabMap.size()]);
		for (String name : tabNameArr) {
			tab = tabMap.get(name);
			checkForeignKey(tab, tabMap, ret);
		}

		System.out.println("selected " + ret.size() + " tables");
		Logger.logger.info("selected " + ret.size() + " tables");

		return ret;
	}

	private List<String> getTabNameList(DataBase src) throws Exception {

		DbTrans tran = src.createDbTrans();

		try {
			ProgressLogger.start("selecting table names ");
			tran.start();
			List<String> nameList = src.getTabNameList(tran, null);
			Collections.sort(nameList);

			ProgressLogger.end("selected " + nameList.size() + " table names");

			return nameList;
		} catch (Exception e) {
			ProgressLogger.end("fail - " + e.getMessage());
			Logger.logger.error(e);
			throw new Exception("" + "\n" + e.getMessage());
		} finally {
			tran.stop();
		}
	}

	/**
	 * 처리할 테이블 목록을 조회하고 키를 고려하여 자료를 넣을 때 오류가 발생되지 않은 순서로 가져옵니다.
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<Table> getTabToWork() throws Exception {

		if (tabList != null)
			return tabList;

		List<String> nameList = null;
		String tabArr[];

		DataBase db = dbSrc == null ? dbDst : dbSrc;

		nameList = getTabNameList(db);

		tabArr = filter(nameList);
		System.out.println("filtered " + tabArr.length + " table names");
		tabList = getTableList(db, Arrays.asList(tabArr));

		tabList = sort(tabList);

		return tabList;
	}

	@SuppressWarnings("unchecked")
	private void restoreData() throws Exception {

		File f;
		List<Object[]> dataList;

		DbTrans tran = dbDst.createDbTrans();

		List<Table> tabList = getTabToWork();

		System.out.println("SQL TRANSACTION : " + dbDst.getName() + " : " + tabList.size() + " tables");

		try {
			tran.start();
			for (Table tab : tabList) {
				f = new File(folder + File.separator + tab.getName() + ".data.jof");
				if (f.exists()) {
					ProgressLogger.start("reading " + f.getName());
					dataList = (List<Object[]>) FileUtil.readObjectToFile(f.getPath());
					ProgressLogger.end(dataList.size() - 1 + " datas");
					restoreData(tran, tab.getName(), dataList);
				}
			}

			tran.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	private void restoreData(DbTrans tran, String tableName, List<Object[]> dataList) throws Exception {

		String cols = null;
		String vals = null;
		for (Object col : dataList.get(0)) {
			if (cols == null) {
				cols = col.toString();
				vals = " ? ";
			} else {
				cols += ", " + col.toString();
				vals += " , ?";
			}
		}
		String sql = "insert into " + tableName + " ( " + cols + " ) values ( " + vals + " )";

		PreparedStatement ps = tran.getPrepareStatement(sql);

		ProgressLogger.start("inserting " + tableName);

		Logger.logger.info(tableName);
		Logger.logger.info(sql);
		int count = 0;
		int size = dataList.size() - 1;

		for (int i = 1; i < dataList.size(); i++) {
			try {

				setPreSt(ps, dataList.get(i));

				count++;

				if (batchsize <= 0) {
					ps.executeUpdate();
					String msg = makeString(tableName, count, size);
					ProgressLogger.doIn(msg);
					System.out.println(msg);
					Logger.logger.debug(msg);
				} else {
					ps.addBatch();
					if (count % batchsize == 0 || count >= size) {
						String msg = makeString(tableName, count, size);
						System.out.println(msg);
						Logger.logger.debug(msg);
						ps.executeBatch();
						tran.commit();
					}
				}

			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		try {
			tran.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.logger.error(e);
		}

		try {
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.logger.error(e);
		}

		ProgressLogger.end("ok");
	}

	@SuppressWarnings("unchecked")
	private void restoreSequence() throws Exception {
		List<Sequence> seqList = (List<Sequence>) FileUtil.readObjectToFile(folder + File.separator + "sequence.jof");
		syncSequence(seqList, dbDst);
	}

	@SuppressWarnings("unchecked")
	private void restoreTable() throws Exception {

		try {
			tabList = (List<Table>) FileUtil.readObjectToFile(folder + File.separator + "table.jof");
			if (tabList == null || tabList.size() == 0) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		tabList = sort(tabList);

		DbTrans tran = dbDst.createDbTrans();
		try {
			tran.start();

			for (Table tab : tabList) {
				checkExistDstTable(tab, tran);
			}
			return;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * foregin key 고려하여 생성하는 순서대로 정렬합니다.
	 * 
	 * @param tabList
	 * @return
	 * @throws Exception
	 */
	private List<Table> sort(List<Table> tabList) throws Exception {

		Collections.sort(tabList, new Comparator<Table>() {

			@Override
			public int compare(Table o1, Table o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});

		List<String> nameList = new ArrayList<String>();
		for (Table tab : tabList) {
			nameList.add(tab.getName());
		}

		List<Table> sortedList = new ArrayList<Table>();
		List<String> createdTabList = new ArrayList<String>();
		List<Table> pList = sortCreateTable(tabList, createdTabList, sortedList, nameList);
		while (pList.size() > 0) {
			pList = sortCreateTable(pList, createdTabList, sortedList, nameList);
		}

		return sortedList;

	}

	private List<Table> sortCreateTable(List<Table> tabList, List<String> createdTabList, List<Table> sortedList,
			List<String> allList) throws Exception {

		List<Table> retList = new ArrayList<Table>();
		TAB: for (Table tab : tabList) {
			if (tab.isContainsFk()) {
				for (Index idx : tab.getIndexList()) {
					if (idx.isFk() && createdTabList.contains(idx.getFkTable()) == false) {
						retList.add(tab);
						if (allList.contains(idx.getFkTable()) == false) {
							throw new Exception("table not found" + " " + tab.getName() + ", FK=" + idx.getFkTable());
						}
						continue TAB;
					}
				}
			}

			sortedList.add(tab);
			createdTabList.add(tab.getName());
		}

		return retList;
	}

	private void syncData(String prefix) throws Exception {

		List<Table> tabList = getTabToWork();
		String tabName;

		DbTrans tranSrc = dbSrc.createDbTrans();
		DbTrans tranDst = dbDst.createDbTrans();

		try {
			tranSrc.start();
			tranDst.start();

			int index = 0;

			for (Table table : tabList) {

				if (prefix != null && prefix.length() > 0) {
					tabName = prefix + "_" + table.getName();
				} else {
					tabName = table.getName();
				}

				Logger.logger.info(index + "/" + tabList.size() + "\tTABLE\t" + table.getName() + " -> " + tabName);

				try {

					List<Object[]> list = tranSrc.selectSql2Obj("select count(*) from " + table.getName(), null);
					int size;
					if (list.get(0)[0] instanceof Number) {
						size = ((Number) list.get(0)[0]).intValue();
					} else {
						try {
							size = Integer.parseInt(list.get(0)[0].toString());
						} catch (Exception e) {
							Logger.logger.error(e);
							size = -1;
						}
					}
					String sql = "select * from " + table.getName();

					SyncDaoLsnr lsnr = new SyncDaoLsnr(table, tranDst, size);
					lsnr.batchsize = batchsize;

					tranSrc.setDaoListener(lsnr);
					tranSrc.selectSql(sql, null);
					tranSrc.setDaoListener(null);
				} catch (Exception e) {
					Logger.logger.fail(table.getName() + " : " + e.getMessage());
				}

			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tranSrc.stop();
			tranDst.stop();
		}

	}

	private void syncSequence() throws Exception {
		syncSequence(dbSrc, dbDst);
	}

	private void syncTable(String prefix) throws Exception {

		List<Table> tabList = getTabToWork();

		String tabName;
		Table tableDst, table;

		DbTrans tranSrc = dbSrc.createDbTrans();
		DbTrans tranDst = dbDst.createDbTrans();

		try {
			tranSrc.start();
			tranDst.start();

			// foregin key를 고려하여 뒤에서부터 삭제 또는 생성합니다.
			for (int i = tabList.size() - 1; i >= 0; i--) {

				table = tabList.get(i);

				if (prefix != null && prefix.length() > 0) {
					tabName = prefix + "_" + table.getName();
				} else {
					tabName = table.getName();
				}

				tableDst = (Table) table.clone();

				tableDst.setName(tabName);

				try {
					checkExistDstTable(tableDst, tranDst);
				} catch (Exception e) {
					e.printStackTrace();
					Logger.logger.fail(table.getName() + " : " + e.getMessage());
				}

			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tranSrc.stop();
			tranDst.stop();
		}

	}

}

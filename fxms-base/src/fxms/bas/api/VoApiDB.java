package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.define.PS_TYPE;
import fxms.bas.fxo.FxCfg;
import fxms.bas.pso.item.PsItem;
import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.fxdao.exception.NotFxTableException;
import subkjh.bas.log.Logger;

public class VoApiDB extends VoApi {
	
	public static final Column MO_NO = new Column("MO_NO", "Number", 19, 0, false);
	public static final Column MO_INSTANCE = new Column("MO_INSTANCE", "Varchar2", 100, 0, true);
	public static final Column PS_DATE = new Column("PS_DATE", "Number", 14, 0, false);
	public static final Column DATA_COUNT = new Column("DATA_COUNT", "Number", 9, 0, false);
	public static final Column INS_DATE = new Column("INS_DATE", "Number", 14, 0, true);


	@Override
	public String makePsTables() throws Exception {

		StringBuffer sb = new StringBuffer();
		String ret;
		List<PS_TYPE> pstypeList = getPstypeList();
		if (pstypeList != null) {

			ret = makePsTables(PS_TYPE.RAW, FxApi.getDate(0));
			sb.append(ret);
			ret = makePsTables(PS_TYPE.RAW, PS_TYPE.RAW.getHstimeNextGroup(FxApi.getDate(0), 1));
			sb.append(ret);

			for (PS_TYPE pstype : pstypeList) {
				ret = makePsTables(pstype, FxApi.getDate(0));
				sb.append(ret);
				ret = makePsTables(pstype, pstype.getHstimeNextGroup(FxApi.getDate(0), 1));
				sb.append(ret);
			}
		}

		return sb.toString();
	}

	@Override
	public String makePsTables(PS_TYPE pstype, long hstime) throws Exception {
		StringBuffer sb = new StringBuffer();

		PsItem items[] = PsApi.getApi().getPsItems();
		if (items == null) {
			sb.append("PS NOT DEFINED");
			return sb.toString();
		}

		List<Column> colList;
		Map<String, Table> tableMap = new HashMap<String, Table>();
		Table table;
		Index index;
		String tableName;
		boolean ret;

		for (PsItem item : items) {

			if (item.getPsTable() == null || item.getPsTable().length() == 0) {
				continue;
			}

			tableName = item.getPsTable() + pstype.getTableTag(hstime);

			table = tableMap.get(tableName);

			if (table == null) {
				table = new Table();
				table.setName(tableName);
				tableMap.put(table.getName(), table);

				table.add(MO_NO);

				table.add(MO_INSTANCE);

				table.add(PS_DATE);

				if (pstype != PS_TYPE.RAW) {
					table.add(DATA_COUNT);
					table.add(INS_DATE);
				}

				index = new Index(table.getName() + "__KEY", INDEX_TYPE.KEY.name(), PS_DATE.getName());
				table.add(index);
			}

			colList = item.makeColumns(pstype);
			for (Column col : colList) {
				table.add(col);
			}

		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			tran.start();
			for (Table t : tableMap.values()) {
				ret = tran.createTable(t);
				sb.append(Logger.fill(t.getName(), 32, '.'));
				sb.append(ret ? " CREATED" : " EXIST");
				sb.append("\n");
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}

		return sb.toString();
	}

	@Override
	public String dropPsTables(PS_TYPE pstype, long hstime) throws Exception {

		PsItem items[] = PsApi.getApi().getPsItems();
		if (items == null) {
			Logger.logger.fail("PS-ITEM NOT DEFINED");
			return "PS-ITEM NOT DEFINED";
		}

		Map<String, Table> tableMap = new HashMap<String, Table>();
		Table table;
		String tableName;

		for (PsItem item : items) {

			if (item.getPsTable() == null || item.getPsTable().length() == 0) {
				continue;
			}

			tableName = item.getPsTable() + pstype.getTableTag(hstime);

			table = tableMap.get(tableName);

			if (table == null) {
				table = new Table();
				table.setName(tableName);
				tableMap.put(table.getName(), table);

				// List<String> nameList =
				// DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).getTabNameList(null,
				// item.getPsTable());
				// for (String name : nameList) {
				// if (tableName.compareTo(name) >= 0) {
				// table = new Table();
				// table.setName(name);
				// tableMap.put(table.getName(), table);
				// }
				// }
			}
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			String msg;
			tran.start();

			StringBuffer sb = new StringBuffer();

			for (Table t : tableMap.values()) {

				try {
					tran.dropTable(t);
					msg = " DROPPED";
				} catch (NotFxTableException e) {
					msg = " NOT FOUND";
				} catch (Exception e) {
					msg = " ERROR";
				}

				sb.append(Logger.fill(t.getName(), 32, '.'));
				sb.append(msg);
				sb.append("\n");
			}

			return sb.toString();

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	public String dropPsTables() throws Exception {

		StringBuffer sb = new StringBuffer();
		String ret;

		int n = ServiceApi.getApi().getVarValue("ps-value-term-raw", 21);
		ret = dropPsTables(PS_TYPE.RAW, FxApi.getDate(System.currentTimeMillis() - (86400000L * n)));
		sb.append(ret);

		n = ServiceApi.getApi().getVarValue("ps-value-term-5min", 21);
		ret = dropPsTables(PS_TYPE.MIN5, PS_TYPE.MIN5.getHstimeNextGroup(FxApi.getDate(0), -1 * n));
		sb.append(ret);

		n = ServiceApi.getApi().getVarValue("ps-value-term-hour", 3);
		ret = dropPsTables(PS_TYPE.HOUR1, PS_TYPE.HOUR1.getHstimeNextGroup(FxApi.getDate(0), -1 * n));
		sb.append(ret);

		n = ServiceApi.getApi().getVarValue("ps-value-term-day", 3);
		ret = dropPsTables(PS_TYPE.DAY1, PS_TYPE.DAY1.getHstimeNextGroup(FxApi.getDate(0), -1 * n));
		sb.append(ret);

		return sb.toString();
	}

}

package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.def.Column;
import subkjh.dao.def.Index;
import subkjh.dao.def.Index.INDEX_TYPE;
import subkjh.dao.def.Table;

public class PsCreateTableDfo extends PsDpo implements FxDfo<Void, Integer> {

	private String createTable(PsKind psKind, long hstime) throws Exception {

		StringBuffer sb = new StringBuffer();

		PsItem items[] = PsApi.getApi().getPsItems();
		if (items == null) {
			Logger.logger.fail("PS-ITEM NOT DEFINED");
			return "PS-ITEM NOT DEFINED";
		}

		Map<String, Table> tableMap = new HashMap<String, Table>();
		Table table;
		String tableName;
		boolean ret;

		for (PsItem item : items) {

			if (item.hasTable() == false) {
				continue;
			}

			tableName = psKind.getTableName(item, hstime);
			table = tableMap.get(tableName);

			if (table == null) {
				table = this.makeTable(tableName, item, psKind);
				tableMap.put(table.getName(), table);
			}

			// 동일 테이블의 여러개의 컬럼을 추가할 수 있다.
			List<Column> colList = item.makeColumns(psKind);
			for (Column col : colList) {
				table.addColumn(col);
			}

		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createClassDao();

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

	private Table makeTable(String tableName, PsItem psItem, PsKind psKind) {
		Index index;

		Table table = new Table();
		table.setName(tableName);
		table.addColumn(MO_NO);
		table.addColumn(MO_INSTANCE);
		table.addColumn(PS_DATE);

		if (psKind.isRaw() == false) {
			table.addColumn(DATA_COUNT);
			table.addColumn(INS_DATE);
		}

		index = new Index(table.getName() + "__KEY", INDEX_TYPE.KEY);
		index.addColumn(PS_DATE.getName());
		table.addIndex(index);

		return table;
	}

	@Override
	public Integer call(FxFact fact, Void data) throws Exception {
		return null;
	}

	/**
	 * 
	 * @param psItem
	 * @return
	 * @throws Exception
	 */
	public String createTables(PsItem psItem) throws Exception {

		StringBuffer sb = new StringBuffer();

		if (psItem.hasTable() == false) {
			sb.append(Logger.fill(psItem.getPsName(), 32, '.'));
			sb.append(" NO TABLE INFO\n");
			return sb.toString();
		}

		Map<String, Table> tableMap = new HashMap<String, Table>();
		Table table;
		List<String> tableNames = new ArrayList<String>();
		boolean ret;
		long hstime = DateUtil.getDtm();

		for (PsKind psKind : getPsKinds()) {

			tableNames.add(psKind.getTableName(psItem, hstime));

			if (psKind.isPartition())
				tableNames.add(psKind.getTableName(psItem, psKind.getHstimeNextGroup(hstime, 1)));

			for (String tableName : tableNames) {
				table = tableMap.get(tableName);
				if (table == null) {
					table = makeTable(tableName, psItem, psKind);
					tableMap.put(table.getName(), table);
				}
				// 컬럼을 추가한다.
				List<Column> colList = psItem.makeColumns(psKind);
				for (Column col : colList) {
					table.addColumn(col);
				}
			}
		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createClassDao();

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

	public String makeTables() throws Exception {

		List<PsKind> psKindList = getPsKinds();

		StringBuffer sb = new StringBuffer();
		String ret;
		if (psKindList.size() > 0) {

			long dtm = DateUtil.getDtm();

			for (PsKind psKind : psKindList) {

				ret = createTable(psKind, dtm);

				sb.append(ret);

				// 파티션 테이블이면 테이블을 다음것 까지 생성한다.
				if (psKind.isPartition()) {
					ret = createTable(psKind, psKind.getHstimeNextGroup(dtm, 1));
					sb.append(ret);
				}
			}
		}

		return sb.toString();
	}
}

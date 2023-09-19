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
import subkjh.dao.def.Table;

/**
 * 수집 데이터를 기록한 테이블을 생성한다.
 * 
 * @author subkjh
 *
 */
public class PsTableCreateDfo extends PsDpo implements FxDfo<Void, Integer> {

	public static void main(String[] args) {
		PsTableCreateDfo dfo = new PsTableCreateDfo();
		try {
			dfo.makeTables();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public String createTable(PsItem psItem) throws Exception {

		StringBuffer sb = new StringBuffer();

		if (psItem.hasTable() == false) {
			sb.append(Logger.fill(psItem.getPsName(), 32, '.'));
			sb.append(" NO TABLE INFO\n");
			return sb.toString();
		}

		PsTableGetInfoDfo dfo = new PsTableGetInfoDfo();
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
					table = dfo.makeTableInfo(tableName, psItem, psKind);
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

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
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

	private String createTable(PsKind psKind, long hstime) throws Exception {

		StringBuffer sb = new StringBuffer();
		boolean ret;
		PsItem items[] = PsApi.getApi().getPsItems();
		if (items == null) {
			Logger.logger.fail("PS-ITEM NOT DEFINED");
			return "PS-ITEM NOT DEFINED";
		}

		List<Table> tables = new PsTableGetInfoDfo().getTableInfo(psKind, hstime);

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createClassDao();

		try {
			tran.start();
			for (Table t : tables) {
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

}

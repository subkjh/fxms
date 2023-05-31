package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.Comparator;
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
import subkjh.dao.def.Table;
import subkjh.dao.exp.NotFxTableException;

/**
 * 
 * @author subkjh
 *
 */
public class PsRemoveExpiredDataDfo extends PsDpo implements FxDfo<Void, String> {

	public static void main(String[] args) {
		PsRemoveExpiredDataDfo dfo = new PsRemoveExpiredDataDfo();
		try {
			System.out.println(dfo.removeExpiredDatas());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String call(FxFact fact, Void data) throws Exception {
		return removeExpiredDatas();
	}

	/**
	 * 유효기간이 지난 수집 데이터를 삭제한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeExpiredDatas() throws Exception {

		List<PsKind> list = getPsKinds();

		StringBuffer ret = new StringBuffer();
		String msg;
		for (PsKind psKind : list) {
			msg = removeExpiredDatas(psKind);
			ret.append(msg);
		}
		return ret.toString();
	}

	/**
	 * 
	 * @param psKind
	 * @return
	 * @throws Exception
	 */
	private String removeExpiredDatas(PsKind psKind) throws Exception {

		PsItem items[] = PsApi.getApi().getPsItems();
		if (items == null) {
			Logger.logger.fail("PS-ITEM NOT DEFINED");
			return "PS-ITEM NOT DEFINED";
		}

		if (psKind.isPartition() == false) {
			return deleteDatas(psKind, items); // 데이터이면 테이블에서 데이터 삭제
		} else {
			return dropTable(psKind, items); // 테이블이면 테이블 DROP
		}

	}

	/**
	 * 테이블에서 데이터를 삭제한다.
	 * 
	 * @param psKind
	 * @param items
	 * @return
	 * @throws Exception
	 */
	private String deleteDatas(PsKind psKind, PsItem items[]) throws Exception {

		String date = psKind.getExpiredDate();
		long hstime = Long.parseLong(date + "000000");
		Logger.logger.info("psKind={}, days={}, hstime={}", psKind.getPsKindName(), psKind.getDataStoreDays(), hstime);

		Map<String, String> tableMap = new HashMap<String, String>();
		String tableName;

		for (PsItem item : items) {
			if (item.hasTable() == false) {
				continue;
			}
			tableName = psKind.getTableName(item, hstime);
			if (tableMap.get(tableName) == null) {
				tableMap.put(tableName, tableName);
			}
		}

		List<String> tables = new ArrayList<>(tableMap.values());
		tables.sort(new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				return arg0.compareTo(arg1);
			}
		});

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createClassDao();

		try {
			tran.start();

			StringBuffer sb = new StringBuffer();
			int cnt;
			long mstime = System.currentTimeMillis();

			for (String name : tables) {

				sb.append(Logger.fill(name, 32, '.'));

				try {
					StringBuffer sql = new StringBuffer();
					sql.append("delete from ").append(name);
					sql.append(" where ").append(PS_DATE.getName()).append(" < ").append(hstime);

					mstime = System.currentTimeMillis();
					cnt = tran.executeSql(sql.toString());

					sb.append(cnt).append(" Deleted (").append(date).append(") ")
							.append(System.currentTimeMillis() - mstime).append(" msec");

					tran.commit();

				} catch (NotFxTableException e) {
					sb.append(" NOT FOUND");
				} catch (Exception e) {
					sb.append(" ERROR");
				}

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

	/**
	 * 유효기간이 지난 테이블 DROP
	 * 
	 * @param psKind
	 * @param items
	 * @return
	 * @throws Exception
	 */
	private String dropTable(PsKind psKind, PsItem items[]) throws Exception {

		long hstime = psKind.getHstimeNextGroup(DateUtil.getDtm(), -1 * psKind.getTblPartStoreCnt());

		Logger.logger.info("psKind={}, storeCnt={}, hstime={}", psKind.getPsKindName(), psKind.getTblPartStoreCnt(),
				hstime);

		Map<String, Table> tableMap = new HashMap<String, Table>();
		Table table;
		String tableName;

		for (PsItem item : items) {

			if (item.hasTable() == false) {
				continue;
			}

			tableName = psKind.getTableName(item, hstime);

			table = tableMap.get(tableName);

			if (table == null) {
				table = new Table();
				table.setName(tableName);
				tableMap.put(table.getName(), table);

//				List<String> nameList = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).getTabNameList(null,
//						item.getPsTable());
//				for (String name : nameList) {
//					if (tableName.compareTo(name) >= 0) {
//						table = new Table();
//						table.setName(name);
//						tableMap.put(table.getName(), table);
//					}
//				}
			}
		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createClassDao();

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

}

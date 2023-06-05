package fxms.bas.impl.ext.influxdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi.StatFunction;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;

/**
 * 수집 데이터 통계를 조회한다.
 * 
 * @author subkjh
 *
 */
public class SelectStatValuesInfluxDfo implements FxDfo<Void, Map<Long, Number>> {

	public static void main(String[] args) throws Exception {
		String psId = "E02V4";
		long startDtm = Long.valueOf(DateUtil.getYmd() + "000000");
		long endDtm = DateUtil.getDtm();
		PsItem item = PsApi.getApi().getPsItem(psId);

		SelectStatValuesInfluxDfo dfo = new SelectStatValuesInfluxDfo();
		System.out.println(FxmsUtil.toJson(dfo.selectStatValue(item, startDtm, endDtm, StatFunction.Avg)));
	}

	@Override
	public Map<Long, Number> call(FxFact fact, Void data) throws Exception {

		StatFunction stat = fact.getObject(StatFunction.class, "stat");
		PsItem psItem = fact.getObject(PsItem.class, "psItem");
		long startDtm = fact.getLong("startDtm");
		long endDtm = fact.getLong("endDtm");

		return selectStatValue(psItem, startDtm, endDtm, stat);
	}

	/**
	 * 관리대상의 통계 데이터를 조회한다.
	 * 
	 * @param psItem   성능항목
	 * @param startDtm 조회시작일시
	 * @param endDtm   조회종료일시
	 * @param statFunc 통계 종료
	 * @return 관리대상번호와 통계값
	 * @throws Exception
	 */
	public Map<Long, Number> selectStatValue(PsItem psItem, long startDtm, long endDtm, StatFunction statFunc)
			throws Exception {

		String selectSql = this.getSqlSelect(psItem, startDtm, endDtm, statFunc);

		final Map<Long, Number> ret = new HashMap<>();
		final DataBase database = DBManager.getMgr().getDataBase("FXMSTSDB");
		final InfluxDB influxDB = InfluxDBFactory.connect(database.getUrl(), database.getUser(),
				database.getPassword());
		influxDB.setDatabase(database.getDbName());

		Logger.logger.debug("\n{}", selectSql);

		try {
			Query query = new Query(selectSql);
			QueryResult queryResult = influxDB.query(query, TimeUnit.MILLISECONDS);
			System.out.println(queryResult);

			long moNo;

			for (Result r : queryResult.getResults()) {
				if (r.getSeries() != null) {
					for (Series s : r.getSeries()) {
						moNo = Long.parseLong(s.getTags().get("moNo").toString());

						for (List<Object> list : s.getValues()) {
							if (list != null) {
								ret.put(moNo, (Number) list.get(1));
							}
						}
					}
				}
			}

			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			influxDB.close();
		}

	}

	private String getSqlSelect(PsItem psItem, long startDtm, long endDtm, StatFunction statFunc) {

		String tableName = psItem.getPsTable();
		String colName;
		long mstimeStart = DateUtil.toMstime(startDtm);
		long mstimeEnd = DateUtil.toMstime(endDtm);

		String startTime = String.valueOf(mstimeStart) + "000000"; // microseconds
		String endTime = String.valueOf(mstimeEnd) + "000000";

		Logger.logger.debug(">= {} and < {}", DateUtil.toHstime(mstimeStart), DateUtil.toHstime(mstimeEnd));
		Logger.logger.debug(">= {} and < {}", mstimeStart, mstimeEnd);

		StringBuffer sql = new StringBuffer();

		String func;
		func = subkjh.dao.database.InfluxDB.toFunction(statFunc.name());
		colName = psItem.getPsColumn();

		if (sql.length() == 0) {
			sql.append("select ").append("\n");
		} else {
			sql.append(", ");
		}

		sql.append(func).append("(").append(psItem.getPsColumn()).append(") as ").append(colName).append("\n");

		sql.append("from ").append(tableName).append("\n");
		sql.append("where time >= ").append(startTime).append(" and time < ").append(endTime).append("\n");

		sql.append("group by moNo");

		return sql.toString();

	}
}
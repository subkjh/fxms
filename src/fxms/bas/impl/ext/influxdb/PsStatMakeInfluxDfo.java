package fxms.bas.impl.ext.influxdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
//import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.StatMakeReqDbo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.ps.PsDpo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;

/**
 * 함수를 이용하여 통계 생성한다.
 * 
 * @author subkjh
 *
 */
public class PsStatMakeInfluxDfo extends PsDpo implements FxDfo<StatMakeReqDbo, Integer> {

	class Data {
		String sql;
		int colSize;

		Data(String sql, int colSize) {
			this.sql = sql;
			this.colSize = colSize;
		}
	}

	public static void main(String[] args) throws Exception {
		PsStatMakeInfluxDfo dfo = new PsStatMakeInfluxDfo();
		dfo.generateStatistics("FX_V_MOST", PsKind.PSKIND_5M, 20230530121000L);
	}

	private final int IDX_MO = 0;

	private final int IDX_PSDATE = 1;

	private final int IDX_DATA_COUNT = 2;

	private final int IDX_INSDATE = 3;

	@Override
	public Integer call(FxFact fact, StatMakeReqDbo data) throws Exception {
		return generateStatistics(data);
	}

	public int generateStatistics(StatMakeReqDbo req) throws Exception {
		return generateStatistics(req.getPsTbl(), req.getPsDataCd(), req.getPsDtm());
	}

	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws Exception {

		String selectSql;
		String delSql;
		Data insertSql;
		PsKind psKindDst, psKindSrc;

		psKindDst = PsApi.getApi().getPsKind(psKindName); // 대상
		psKindSrc = PsApi.getApi().getPsKind(psKindDst.getPsDataSrc()); // 원천

		selectSql = getSqlSelect(psTbl, psKindSrc, psKindDst, psDtm); // select문
		insertSql = getSqlInsert(psTbl, psKindDst, psDtm); // insert문
		delSql = getSqlDelete(psTbl, psKindDst, psDtm); // delete문

		Collection<Object[]> datas = getDatas(selectSql, insertSql.colSize, DateUtil.getDtm());
//		for (Object[] o : datas) {
//			for (Object e : o) {
//				System.out.print(e + "\t");
//			}
//			System.out.println();
//		}

		ClassDaoEx dao = ClassDaoEx.open(FxCfg.DB_PSVALUE).executeSql(delSql).executeSql(insertSql.sql, datas).close();
		return dao.getProcessedCount();

//		return -1;
	}

	private List<Object[]> getDatas(String sql, int size, long insDate) throws Exception {

		final List<Object[]> ret = new ArrayList<>();
		Object[] row;
		final DataBase database = DBManager.getMgr().getDataBase("FXMSTSDB");
		final InfluxDB influxDB = InfluxDBFactory.connect(database.getUrl(), database.getUser(),
				database.getPassword());
		influxDB.setDatabase(database.getDbName());

		Logger.logger.debug("\n{}", sql);

		try {
			Query query = new Query(sql);
			QueryResult queryResult = influxDB.query(query, TimeUnit.MILLISECONDS);
//			System.out.println(queryResult);

			Object moNo;

			for (Result r : queryResult.getResults()) {
				if (r.getSeries() != null) {
					for (Series s : r.getSeries()) {
						moNo = s.getTags().get("moNo");
						row = new Object[size];
						row[IDX_MO] = Long.parseLong(moNo.toString());
						row[IDX_INSDATE] = insDate;
						for (List<Object> list : s.getValues()) {
							if (list != null) {
								for (int i = 0; i < list.size(); i++) {
									if (i == 0) {
										row[IDX_PSDATE] = DateUtil.toHstime(((Number) list.get(i)).longValue());
									} else if (i == 1) {
										row[IDX_DATA_COUNT] = ((Number) list.get(i)).intValue();
									} else {
										row[IDX_INSDATE + (i - 1)] = list.get(i); // 시간, 건수, 통계값
									}
								}
							}
						}
						ret.add(row);
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

	private String getSqlDelete(String psTable, PsKind psKind, long psDate) {

		StringBuffer sql = new StringBuffer();

		sql.append("delete from ");
		sql.append(psKind.getTableName(psTable, psDate));
		sql.append(" where " + PsDpo.PS_DATE.getName() + "=" + psKind.getHstimeStart(psDate));

		return sql.toString();
	}

	@SuppressWarnings("unused")
	private Data getSqlInsert(String psTable, PsKind psKindDst, long psDtm) throws Exception {

		int colSize = 4;
		List<PsItem> itemList = PsApi.getApi().getPsItemList(psTable);
		StringBuffer dest = new StringBuffer();
		String destTable = psKindDst.getTableName(psTable, psDtm);

		dest.append("insert into ").append(destTable).append(" ( ").append("\n");
		dest.append(PsDpo.MO_NO.getName()).append("\n");
//		dest.append(", ").append(PsDpo.MO_INSTANCE.getName()).append("\n");
		dest.append(", ").append(PsDpo.PS_DATE.getName()).append("\n");
		dest.append(", ").append(PsDpo.DATA_COUNT.getName()).append("\n");
		dest.append(", ").append(PsDpo.INS_DATE.getName()).append("\n");
		for (PsItem item : itemList) {
			for (String func : item.getPsKindCols()) {
				String colName = item.getPsColumn() + "_" + func;
				dest.append(", ").append(colName).append("\n");
			}
		}
		dest.append(") values ( ?, ?, ?, ?");
		for (PsItem item : itemList) {
			for (String func : item.getPsKindCols()) {
				dest.append(", ?");
				colSize++;
			}
		}
		dest.append(")");

		return new Data(dest.toString(), colSize);

	}

	private String getSqlSelect(String psTable, PsKind psKindSrc, PsKind psKindDst, long psDtm) {

		String colName;
		long mstimeStart = DateUtil.toMstime(psKindDst.getHstimeStart(psDtm));
		long mstimeEnd = DateUtil.toMstime(psKindDst.getHstimeEnd(psDtm)) + 1000;

		List<PsItem> itemList = PsApi.getApi().getPsItemList(psTable);
		String startTime = String.valueOf(mstimeStart) + "000000"; // microseconds
		String endTime = String.valueOf(mstimeEnd) + "000000";
		String interval = psKindDst.getInterval();

		Logger.logger.debug(">= {} and < {}", DateUtil.toHstime(mstimeStart), DateUtil.toHstime(mstimeEnd));
		Logger.logger.debug(">= {} and < {}", mstimeStart, mstimeEnd);

		StringBuffer sql = new StringBuffer();
		sql.append("select ").append("\n");
		// 각 성능항목에 대한 통계 함수를 이용한 컬럼을 추가한다.
		for (PsItem item : itemList) {

			sql.append("count(").append(item.getPsColumn()).append(") as ").append(item.getPsColumn()).append("_COUNT");

			for (String func : item.getPsKindCols()) {
				func = subkjh.dao.database.InfluxDB.toFunction(func);
				colName = item.getPsColumn() + "_" + func;

				sql.append(",");
				sql.append(func).append("(").append(item.getPsColumn()).append(") as ").append(colName).append("\n");
			}
		}
		sql.append("from ").append(psTable).append("\n");
		sql.append("where time >= ").append(startTime).append(" and time < ").append(endTime).append("\n");
		sql.append("group by time(").append(interval).append("), moNo fill(null)");

		return sql.toString();

	}
}

package fxms.bas.impl.ext.influxdb;

import java.util.ArrayList;
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

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.vo.SelectValuesDfo;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValue;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;

/**
 * TSDB를 이용하여 수집 내역을 조회한다.
 *
 * @author subkjh
 *
 */
public class SelectValuesInfluxDfo implements FxDfo<Void, List<PsValues>> {

	/**
	 * 성능항목 조회 조건
	 *
	 * @author subkjh
	 *
	 */
	class CondGetData {

		final PsItem item;
		final String psKindCols[];

		CondGetData(PsItem item, String psKindCols[]) {
			this.item = item;
			this.psKindCols = psKindCols;
		}
	}

	interface Listener {
		void onValue(long moNo, String columns[], List<Object> values);
	}

	public static void main(String[] args) throws Exception {
		SelectValuesInfluxDfo dfo = new SelectValuesInfluxDfo();
		SelectValuesDfo dfo2 = new SelectValuesDfo();
		long startDtm = Long.valueOf(DateUtil.getYmd() + "000000");
		long endDtm = DateUtil.getDtm();
		long moNo = 1000;
		String psId = "E02V4";
		String psKindName = "MIN15";
		String psKindCol = "AVG";
		String psKindCols[] = new String[] { "MIN", "AVG" };

		moNo = 1002149;
		psKindName = "HOUR1";

		MoApi.api = new MoApiDfo();
		PsKind psKind = PsApi.getApi().getPsKind(psKindName);
		PsItem psItem = PsApi.getApi().getPsItem(psId);
		Mo mo = MoApi.getApi().getMo(moNo);
		psKindCol = psItem.getDefKindCol();
//
//		List<PsValues> list = dfo.selectValues(mo, psItem, psKind, psKindCol, startDtm, endDtm);
//		List<PsValues> list2 = dfo2.selectValues(mo, psItem, psKind, psKindCol, startDtm, endDtm);
//
//		System.out.println(list.size() + ", " + list2.size());
//
//		Map<Long, Number> map = new HashMap<>();
//
//		for (PsValues values : list) {
//			for (PsValue value : values.getValues()) {
//				map.put(value.getPsDtm(), value.getValue());
//			}
//		}
//
//		for (PsValues values : list2) {
//			for (PsValue value : values.getValues()) {
//				System.out.println(value.getPsDtm() + "=" + value.getValue() + " : " + map.get(value.getPsDtm()));
//			}
//		}

		List<PsValueSeries> list = dfo.selectSeriesValues(mo, psItem, psKind, psKindCols, startDtm, endDtm);
		List<PsValueSeries> list2 = dfo2.selectSeriesValues(mo, psItem, psKind, psKindCols, startDtm, endDtm);

		System.out.println(list.size() + ", " + list2.size());

		Map<Long, Number> map = new HashMap<>();

		for (PsValueSeries values : list) {
			for (Number numbers[] : values.getValueList()) {
				System.out.println(FxmsUtil.toJson(numbers));
			}
		}
		System.out.println("-------------------------------------");
		for (PsValueSeries values : list2) {
			for (Number numbers[] : values.getValueList()) {
				System.out.println(FxmsUtil.toJson(numbers));
			}
		}
	}

	@Override
	public List<PsValues> call(FxFact fact, Void data) throws Exception {

		Mo mo = fact.getObject(Mo.class, "mo");
		PsItem item = fact.getObject(PsItem.class, "psItem");
		PsKind psKind = fact.getObject(PsKind.class, "psKind");
		String psKindCol = fact.getString("psKindCol");
		long startDtm = fact.getLong("startDtm");
		long endDtm = fact.getLong("endDtm");

		return selectValues(mo, item, psKind, psKindCol, startDtm, endDtm);
	}

	/**
	 * 
	 * @param mo
	 * @param item
	 * @param psKind
	 * @param psKindCol
	 * @param startDtm
	 * @param endDtm
	 * @return
	 * @throws Exception
	 */
	public List<PsValueSeries> selectSeriesValues(Mo mo, PsItem item, PsKind psKind, String psKindCol[], long startDtm,
			long endDtm) throws Exception {

		CondGetData conds[] = new CondGetData[1];
		conds[0] = new CondGetData(item, psKindCol);
		Map<Long, PsValueSeries> retMap = new HashMap<>();

		String selectSql = this.getSqlSelect(psKind, startDtm, endDtm, mo, conds);

		this.getDatas(selectSql, new Listener() {

			public void onValue(long moNo, String columns[], List<Object> values) {
				PsValueSeries series = retMap.get(moNo);
				if (series == null) {

					try {
						Mo mo = MoApi.getApi().getMo(moNo);

						series = new PsValueSeries();
						series.setMoNo(moNo);
						series.setMo(mo);
						series.setPsItem(item);
						series.setPsDataCd(psKind.getPsKindName());
						series.setPsStatFuncArray(psKindCol);
						retMap.put(moNo, series);
					} catch (Exception e) {
					}

				}
				series.add(getPsDtm(values), getValues(values));
			}
		});

		return new ArrayList<>(retMap.values());

	}

	/**
	 * 
	 * @param mo
	 * @param item
	 * @param psKind
	 * @param psKindCol
	 * @param startDtm
	 * @param endDtm
	 * @return
	 * @throws Exception
	 */
	public List<PsValues> selectValues(Mo mo, PsItem item, PsKind psKind, String psKindCol, long startDtm, long endDtm)
			throws Exception {

		CondGetData conds[] = new CondGetData[1];
		conds[0] = new CondGetData(item, new String[] { psKindCol });

		Map<Long, PsValues> retMap = new HashMap<>();

		String selectSql = this.getSqlSelect(psKind, startDtm, endDtm, mo, conds);

		this.getDatas(selectSql, new Listener() {

			@Override
			public void onValue(long moNo, String columns[], List<Object> values) {
				PsValues list = retMap.get(moNo);
				if (list == null) {
					try {
						list = new PsValues(MoApi.getApi().getMo(moNo), item);
					} catch (Exception e) {
						return;
					}
					retMap.put(moNo, list);
				}
				list.getValues().add(new PsValue(getPsDtm(values), ((Number) values.get(1))));
			}

		});

		return new ArrayList<>(retMap.values());

	}

	/**
	 * 
	 * @param mo
	 * @param psKind
	 * @param startDtm
	 * @param endDtm
	 * @return
	 * @throws Exception
	 */
	public List<PsValues> selectValues(Mo mo, PsKind psKind, long startDtm, long endDtm) throws Exception {

		Map<String, PsValues> retMap = new HashMap<>();
		List<PsItem> itemList = PsApi.getApi().getPsItemList(mo.getMoClass(), mo.getMoType());

		for (PsItem item : itemList) {

			CondGetData conds[] = new CondGetData[1];
			conds[0] = new CondGetData(item, new String[] { item.getDefKindCol() });

			final PsValues ret = new PsValues(mo, item);
			retMap.put(ret.getPsItem().getPsId(), ret);

			String selectSql = this.getSqlSelect(psKind, startDtm, endDtm, mo, conds);
			this.getDatas(selectSql, new Listener() {

				@Override
				public void onValue(long moNo, String columns[], List<Object> values) {
					ret.getValues().add(new PsValue(getPsDtm(values), ((Number) values.get(1))));
				}

			});

		}
		return new ArrayList<>(retMap.values());
	}

	/**
	 * 
	 * @param item
	 * @param psKind
	 * @param psKindCol
	 * @param startDtm
	 * @param endDtm
	 * @return
	 * @throws Exception
	 */
	public List<PsValues> selectValues(PsItem item, PsKind psKind, String psKindCol, long startDtm, long endDtm)
			throws Exception {

		CondGetData conds[] = new CondGetData[1];
		conds[0] = new CondGetData(item, new String[] { psKindCol });
		Map<Long, PsValues> retMap = new HashMap<>();

		String selectSql = this.getSqlSelect(psKind, startDtm, endDtm, null, conds);

		this.getDatas(selectSql, new Listener() {

			@Override
			public void onValue(long moNo, String columns[], List<Object> values) {

				PsValues list = retMap.get(moNo);
				if (list == null) {
					Mo mo;
					try {
						mo = MoApi.getApi().getMo(moNo);
						list = new PsValues(mo, item);
						retMap.put(moNo, list);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				list.getValues().add(new PsValue(getPsDtm(values), ((Number) values.get(1))));
			}
		});

		return new ArrayList<>(retMap.values());

	}

	private List<Object[]> getDatas(String sql, Listener listener) throws Exception {

		final List<Object[]> ret = new ArrayList<>();
		final DataBase database = DBManager.getMgr().getDataBase("FXMSTSDB");
		final InfluxDB influxDB = InfluxDBFactory.connect(database.getUrl(), database.getUser(),
				database.getPassword());
		influxDB.setDatabase(database.getDbName());

		Logger.logger.debug("\n{}", sql);

		try {
			Query query = new Query(sql);
			QueryResult queryResult = influxDB.query(query, TimeUnit.MILLISECONDS);
			System.out.println(queryResult);

			long moNo;
			String columns[];

			for (Result r : queryResult.getResults()) {
				if (r.getSeries() != null) {
					for (Series s : r.getSeries()) {
						moNo = Long.parseLong(s.getTags().get("moNo").toString());
						columns = s.getColumns().toArray(new String[s.getColumns().size()]);

						for (List<Object> list : s.getValues()) {
							if (list != null) {
								listener.onValue(moNo, columns, list);
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

	private long getPsDtm(List<Object> values) {
		return DateUtil.toHstime(((Number) values.get(0)).longValue());
	}

	/**
	 * 
	 * @param psKind
	 * @param startDtm
	 * @param endDtm
	 * @param mo
	 * @param conds
	 * @return
	 */
	private String getSqlSelect(PsKind psKind, long startDtm, long endDtm, Mo mo, CondGetData conds[]) {

		String tableName = conds[0].item.getPsTable();
		String colName;
		long mstimeStart = DateUtil.toMstime(startDtm);
		long mstimeEnd = DateUtil.toMstime(endDtm);

		String startTime = String.valueOf(mstimeStart) + "000000"; // microseconds
		String endTime = String.valueOf(mstimeEnd) + "000000";
		String interval = psKind.getInterval();

		Logger.logger.debug(">= {} and < {}", DateUtil.toHstime(mstimeStart), DateUtil.toHstime(mstimeEnd));
		Logger.logger.debug(">= {} and < {}", mstimeStart, mstimeEnd);

		StringBuffer sql = new StringBuffer();

		// 각 성능항목에 대한 통계 함수를 이용한 컬럼을 추가한다.
		if (psKind.isRaw()) {
			for (CondGetData c : conds) {
				if (sql.length() == 0) {
					sql.append("select ").append("\n");
				} else {
					sql.append(", ");
				}
				sql.append(c.item.getPsColumn()).append("\n");
			}
		} else {
			String func;
			for (CondGetData c : conds) {
				for (String psKindCol : c.psKindCols) {
					func = subkjh.dao.database.InfluxDB.toFunction(psKindCol);
					colName = c.item.makeColumn(psKind, psKindCol).getName();

					if (sql.length() == 0) {
						sql.append("select ").append("\n");
					} else {
						sql.append(", ");
					}

					sql.append(func).append("(").append(c.item.getPsColumn()).append(") as ").append(colName)
							.append("\n");
				}
			}
		}

		sql.append("from ").append(tableName).append("\n");
		sql.append("where time >= ").append(startTime).append(" and time < ").append(endTime).append("\n");

		if (mo != null) {
			sql.append("and moNo = '").append(mo.getMoNo()).append("'\n");
		}

		sql.append("group by time(").append(interval).append("), moNo fill(null)");

		return sql.toString();

	}

	private Number[] getValues(List<Object> values) {
		Number ret[] = new Number[values.size() - 1];
		for (int i = 1; i < values.size(); i++) {
			ret[i - 1] = (Number) values.get(i);
		}
		return ret;
	}
}
package fxms.bas.impl.dpo.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi.StatFunction;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.ps.PsDpo;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValue;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;
import subkjh.dao.def.Column;
import subkjh.dao.def.DaoListener;
import subkjh.dao.def.Table;
import subkjh.dao.exp.TableNotFoundException;
import subkjh.dao.model.QueryResult;
import subkjh.dao.util.QueryMaker;

/**
 * 수집 데이터 알람 확인
 *
 * @author subkjh
 *
 */
public class SelectValuesDfo implements FxDfo<Void, List<PsValues>> {

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
		void onValue(long moNo, long psDtm, String columnId[], Number value[]);
	}

	/**
	 * 수집 데이터 조회
	 *
	 * @param tran
	 * @param mo         관리대상
	 * @param moInstance MO인스턴스
	 * @param psKind     성능종류
	 * @param startDtm   시작일시
	 * @param endDtm     종료일시
	 * @param conds      수집항목
	 * @param listener   조회 데이터 받을 리슨너
	 * @throws Exception
	 */
	private void selectValues(ClassDao tran, Mo mo, String moInstance, PsKind psKind, long startDtm, long endDtm,
			CondGetData conds[], Listener listener) throws Exception {

		Table table = new Table();
		String tableName;
		QueryResult queryInfo;
		QueryMaker maker = new QueryMaker();
		String orderby[] = new String[] { PsDpo.PS_DATE.getName() };

		table.addColumn(PsDpo.MO_NO);
		table.addColumn(PsDpo.PS_DATE);

		// 조회할 컬럼 추가
		List<String> valIdList = new ArrayList<>();
		if (psKind.isRaw()) {
			for (CondGetData c : conds) {
				table.addColumn(c.item.makeColumn(psKind, null));
				valIdList.add(c.item.getPsId());
			}
		} else {
			for (CondGetData c : conds) {
				for (String psKindCol : c.psKindCols) {
					table.addColumn(c.item.makeColumn(psKind, psKindCol));
					valIdList.add(c.item.getPsId() + "_" + psKindCol);
				}
			}
		}

		Map<String, Object> para = new HashMap<>();
		if (mo != null) {
			para.put(PsDpo.MO_NO.getName(), mo.getMoNo());
		}
		if (moInstance != null) {
			para.put(PsDpo.MO_INSTANCE.getName(), moInstance);
		}
		para.put(PsDpo.PS_DATE.getName() + " >= ", startDtm);
		para.put(PsDpo.PS_DATE.getName() + "<= ", endDtm);

		long nowDate = startDtm;
		while (nowDate <= endDtm) {
			tableName = psKind.getTableName(conds[0].item, nowDate);
			table.setName(tableName);

			queryInfo = maker.getSelectQueryResult(table, para, orderby);

			try {
				tran.setDaoListener(new DaoListener() {

					@Override
					public void onExecuted(Object data, Exception ex) throws Exception {
					}

					@Override
					public void onFinish(Exception ex) throws Exception {
					}

					@Override
					public void onSelected(int rowNo, Object data) throws Exception {
						Object[] datas = (Object[]) data;
						long psMoNo;
						long psDate;
						String colNames[] = new String[valIdList.size()];
						Number values[] = new Number[valIdList.size()];

						psMoNo = ((Number) datas[0]).longValue();
						psDate = ((Number) datas[1]).longValue();
						for (int i = 2; i < datas.length; i++) {
							colNames[i - 2] = valIdList.get(i - 2);
							values[i - 2] = (Number) datas[i];
						}
						listener.onValue(psMoNo, psDate, colNames, values);
					}

					@Override
					public void onStart(String[] colNames) throws Exception {
						Logger.logger.debug("columns {}", Arrays.toString(colNames));
					}

				});

				tran.selectSql(queryInfo.getSql(), queryInfo.getParaArray());

			} catch (TableNotFoundException e) {
				// 테이블이 없으면 무시한다.
			} finally {
				tran.setDaoListener(null);
			}

			if (!psKind.isPartition()) {
				break;
			}

			nowDate = psKind.getHstimeNextGroup(nowDate, 1);
		}

	}

	List<Column> getColumns(PsKind psKind, CondGetData conds[]) {
		// 조회할 컬럼 추가
		List<Column> columns = new ArrayList<>();
		if (psKind.isRaw()) {
			for (CondGetData c : conds) {
				columns.add(c.item.makeColumn(psKind, null));
			}
		} else {
			for (CondGetData c : conds) {
				for (String psKindCol : c.psKindCols) {
					columns.add(c.item.makeColumn(psKind, psKindCol));
				}
			}
		}
		return columns;
	}

	void selectGroupBy(ClassDao tran, PsKind psKind, long startDtm, long endDtm, String psTable, List<Column> columns,
			StatFunction func, String whereInMo, Listener listener) throws Exception {

		long nowDate = startDtm;
		while (nowDate <= endDtm) {

			StringBuffer sql = new StringBuffer();

			sql.append("select PS_DATE ");
			for (Column col : columns) {
				sql.append(", ").append(func.name()).append("(").append(col.getName()).append(") as ")
						.append(col.getName() + "_" + func.name().toUpperCase());
			}
			sql.append("\n from   ").append(psKind.getTableName(psTable, endDtm));
			sql.append("\n where  PS_DATE >= ").append(startDtm);
			sql.append("\n and    PS_DATE <= ").append(endDtm);
			if (whereInMo != null) {
				sql.append("\n and	MO_NO in (").append(whereInMo).append(")");
			}
			sql.append("\n group by PS_DATE");
			sql.append("\n order by PS_DATE");

			try {
				tran.setDaoListener(new DaoListener() {

					private String colNames[];

					@Override
					public void onExecuted(Object data, Exception ex) throws Exception {
					}

					@Override
					public void onFinish(Exception ex) throws Exception {
					}

					@Override
					public void onSelected(int rowNo, Object data) throws Exception {
						Object[] datas = (Object[]) data;
						Number values[] = new Number[this.colNames.length];
						long psDate = ((Number) datas[0]).longValue();
						for (int i = 1; i < datas.length; i++) {
							values[i - 1] = (Number) datas[i];
						}
						listener.onValue(-1, psDate, this.colNames, values);
					}

					@Override
					public void onStart(String[] colNames) throws Exception {
						this.colNames = Arrays.copyOfRange(colNames, 1, colNames.length);
						Logger.logger.debug("columns {} : {}", Arrays.toString(colNames),
								Arrays.toString(this.colNames));
					}

				});

				tran.selectSql(sql.toString(), null);

			} catch (TableNotFoundException e) {
				// 테이블이 없으면 무시한다.
			} finally {
				tran.setDaoListener(null);
			}

			if (!psKind.isPartition()) {
				break;
			}

			nowDate = psKind.getHstimeNextGroup(nowDate, 1);
		}

	}

	void selectValues2(ClassDao tran, Mo mo, PsKind psKind, long startDtm, long endDtm, CondGetData conds[],
			Listener listener) throws Exception {

		Table table = new Table();
		String tableName;
		QueryResult queryInfo;
		QueryMaker maker = new QueryMaker();
		String orderby[] = new String[] { PsDpo.PS_DATE.getName() };

		table.addColumn(PsDpo.MO_NO);
		table.addColumn(PsDpo.PS_DATE);

		// 조회할 컬럼 추가
		List<String> valIdList = new ArrayList<>();
		if (psKind.isRaw()) {
			for (CondGetData c : conds) {
				table.addColumn(c.item.makeColumn(psKind, null));
				valIdList.add(c.item.getPsId());
			}
		} else {
			for (CondGetData c : conds) {
				for (String psKindCol : c.psKindCols) {
					table.addColumn(c.item.makeColumn(psKind, psKindCol));
					valIdList.add(c.item.getPsId() + "_" + psKindCol);
				}
			}
		}

		Map<String, Object> para = new HashMap<>();
		if (mo != null) {
			para.put(PsDpo.MO_NO.getName(), mo.getMoNo());
		}
		para.put(PsDpo.PS_DATE.getName() + " >= ", startDtm);
		para.put(PsDpo.PS_DATE.getName() + "<= ", endDtm);

		long nowDate = startDtm;
		while (nowDate <= endDtm) {
			tableName = psKind.getTableName(conds[0].item, nowDate);
			table.setName(tableName);

			queryInfo = maker.getSelectQueryResult(table, para, orderby);

			try {
				tran.setDaoListener(new DaoListener() {

					@Override
					public void onExecuted(Object data, Exception ex) throws Exception {
					}

					@Override
					public void onFinish(Exception ex) throws Exception {
					}

					@Override
					public void onSelected(int rowNo, Object data) throws Exception {
						Object[] datas = (Object[]) data;
						long psMoNo;
						long psDate;
						String colNames[] = new String[valIdList.size()];
						Number values[] = new Number[valIdList.size()];

						psMoNo = ((Number) datas[0]).longValue();
						psDate = ((Number) datas[1]).longValue();
						for (int i = 2; i < datas.length; i++) {
							colNames[i - 2] = valIdList.get(i - 2);
							values[i - 2] = (Number) datas[i];
						}
						listener.onValue(psMoNo, psDate, colNames, values);
					}

					@Override
					public void onStart(String[] colNames) throws Exception {
						Logger.logger.debug("columns {}", Arrays.toString(colNames));
					}

				});

				tran.selectSql(queryInfo.getSql(), queryInfo.getParaArray());

			} catch (TableNotFoundException e) {
				// 테이블이 없으면 무시한다.
			} finally {
				tran.setDaoListener(null);
			}

			if (!psKind.isPartition()) {
				break;
			}

			nowDate = psKind.getHstimeNextGroup(nowDate, 1);
		}

	}

	@Override
	public List<PsValues> call(FxFact fact, Void data) throws Exception {

		Mo mo = fact.getObject(Mo.class, "mo");
		PsItem item = fact.getObject(PsItem.class, "psItem");
		PsKind psKind = fact.getObject(PsKind.class, "psKind");
		String moInstance = fact.getString("moInstance");
		String psKindCol = fact.getString("psKindCol");
		long startDtm = fact.getLong("startDtm");
		long endDtm = fact.getLong("endDtm");

		return selectValues(mo, moInstance, item, psKind, psKindCol, startDtm, endDtm);
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

		DataBase database = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE);
		ClassDao tran = database.createClassDao();

		try {
			tran.start();

			Map<Long, PsValueSeries> retMap = new HashMap<>();

			this.selectValues(tran, mo, null, psKind, startDtm, endDtm, conds, new Listener() {

				@Override
				public void onValue(long moNo, long psDtm, String columnIds[], Number datas[]) {
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
					series.add(psDtm, datas);
				}
			});

			return new ArrayList<>(retMap.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 
	 * @param mo
	 * @param item
	 * @param psKind
	 * @param psKindCol
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<PsValues> selectValues(Mo mo, String moInstance, PsItem item, PsKind psKind, String psKindCol, long startDate,
			long endDate) throws Exception {

		CondGetData conds[] = new CondGetData[1];
		conds[0] = new CondGetData(item, new String[] { psKindCol });

		DataBase database = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE);
		ClassDao tran = database.createClassDao();

		try {
			tran.start();

			Map<Long, PsValues> retMap = new HashMap<>();

			this.selectValues(tran, mo, moInstance, psKind, startDate, endDate, conds, new Listener() {

				@Override
				public void onValue(long moNo, long psDtm, String columnIds[], Number values[]) {
					PsValues list = retMap.get(moNo);
					if (list == null) {
						try {
							list = new PsValues(MoApi.getApi().getMo(moNo), item);
						} catch (Exception e) {
							return;
						}
						retMap.put(moNo, list);
					}
					list.getValues().add(new PsValue(psDtm, values[0]));
				}

			});

			return new ArrayList<>(retMap.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 
	 * @param mo
	 * @param psKind
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<PsValues> selectValues(Mo mo, PsKind psKind, long startDate, long endDate) throws Exception {

		DataBase database = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE);
		ClassDao tran = database.createClassDao();

		try {
			tran.start();

			Map<String, PsValues> retMap = new HashMap<>();
			List<PsItem> itemList = PsApi.getApi().getPsItemList(mo.getMoClass(), mo.getMoType());

			for (PsItem item : itemList) {

				CondGetData conds[] = new CondGetData[1];
				conds[0] = new CondGetData(item, new String[] { item.getDefKindCol() });

				final PsValues values = new PsValues(mo, item);
				retMap.put(values.getPsItem().getPsId(), values);
				this.selectValues(tran, mo, null, psKind, startDate, endDate, conds, new Listener() {
					@Override
					public void onValue(long moNo, long psDtm, String columnIds[], Number datas[]) {
						values.getValues().add(new PsValue(psDtm, datas[0]));
					}
				});
			}
			return new ArrayList<>(retMap.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 
	 * @param item
	 * @param psKind
	 * @param psKindCol
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<PsValues> selectValues(PsItem item, PsKind psKind, String psKindCol, long startDate, long endDate)
			throws Exception {

		DataBase database = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE);
		ClassDao tran = database.createClassDao();
		CondGetData conds[] = new CondGetData[1];
		conds[0] = new CondGetData(item, new String[] { psKindCol });

		try {
			tran.start();

			Map<Long, PsValues> retMap = new HashMap<>();

			this.selectValues(tran, null, null, psKind, startDate, endDate, conds, new Listener() {
				@Override
				public void onValue(long moNo, long psDtm, String columnIds[], Number datas[]) {

					if (datas[0] == null)
						return;

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
					list.getValues().add(new PsValue(psDtm, datas[0]));
				}
			});

			return new ArrayList<>(retMap.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}
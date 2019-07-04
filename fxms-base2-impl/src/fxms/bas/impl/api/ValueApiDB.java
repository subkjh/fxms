package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.control.DaoExecutor;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.beans.QueryResult;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.fxdao.control.QueryMaker;
import fxms.bas.api.FxApi;
import fxms.bas.api.PsApi;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.FX_CF_FXSERVICE_PS;
import fxms.bas.impl.po.WriteValueDao;
import fxms.bas.po.PsVo;
import fxms.bas.po.TimeSeriesVo;
import fxms.bas.po.ValueApi;
import fxms.bas.po.VoList;
import fxms.bas.po.item.PsItem;
import fxms.bas.po.vo.FxServicePsVo;
import fxms.bas.po.vo.UpdateDataVo;

public class ValueApiDB extends ValueApi {

	public static void main(String[] args) throws Exception {

		ValueApiDB api = new ValueApiDB();
		api.getValueList(1000073, "TEMP", PS_TYPE.RAW, FxApi.getDate(System.currentTimeMillis() - 3600000L),
				FxApi.getDate(System.currentTimeMillis()));

	}

	@Override
	public Map<String, Integer> doInsertValue(VoList voList) throws Exception {

		if (voList == null || voList.size() == 0)
			return null;

		PsVo valArr[] = voList.toArray(new PsVo[voList.size()]);

		long hstime = FxApi.getDate(voList.getMstime());

		WriteValueDao dao = new WriteValueDao();

		return dao.write(hstime, valArr, true);

	}

	@Override
	public List<PsVo> doSelectValueCur() throws Exception {

		PsItem items[] = PsApi.getApi().getPsItems();

		DaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createDao();

		List<PsVo> valueList = new ArrayList<PsVo>();

		try {
			tran.start();
			String sql;
			List<Object[]> statusList;

			for (PsItem item : items) {

				if (item.isUpdate() == false) {
					continue;
				}

				sql = "select MO_NO, " + item.getMoColumn() + " STATUS from " + item.getMoTable();
				try {

					statusList = tran.selectSql(sql, null);

					Logger.logger.debug(item.getMoTable() + "=" + statusList.size());

					for (Object col[] : statusList) {
						if (col[1] instanceof Number) {
							valueList.add(new PsVo(Long.parseLong(col[0] + ""), null, item.getPsCode(), ((Number) col[1])));
						} else {
							try {
								valueList.add(new PsVo(Long.parseLong(col[0] + ""), null, item.getPsCode(), Float.parseFloat(col[1]
										+ "")));
							} catch (Exception e) {
							}
						}
					}
				} catch (Exception e) {
					Logger.logger.error(e);
					continue;
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

		return valueList;
	}

	@Override
	public void doUpdateColumn(List<UpdateDataVo> updateList) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			for (UpdateDataVo updateColumn : updateList) {
				try {
					tran.executeSql(updateColumn.getSqlUpdate());
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

			tran.commit();
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<TimeSeriesVo> doSelectPsValue(long moNo, PsItem item, PS_TYPE pstype, long startDate, long endDate)
			throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			tran.start();

			Table table = new Table();
			String tableName;
			List<Object[]> valueList;
			Map<String, TimeSeriesVo> instanceValueMap = new HashMap<String, TimeSeriesVo>();
			TimeSeriesVo tsList;
			QueryResult queryInfo;
			QueryMaker maker = new QueryMaker();
			String orderby[] = new String[] { VoApiDB.PS_DATE.getName() };

			table.addColumn(VoApiDB.MO_INSTANCE);
			table.addColumn(VoApiDB.PS_DATE);
			table.addColumn(item.makeColumn(pstype));
			table.addColumn(VoApiDB.MO_NO);

			Map<String, Object> para = new HashMap<String, Object>();
			para.put(VoApiDB.MO_NO.getName(), moNo);
			para.put(VoApiDB.PS_DATE.getName() + " >= ", startDate);
			para.put(VoApiDB.PS_DATE.getName() + "<= ", endDate);

			long nowDate = startDate;
			while (nowDate <= endDate) {
				tableName = item.getPsTable() + pstype.getTableTag(nowDate);
				table.setName(tableName);

				queryInfo = maker.getSelectQueryResult(table, para, orderby);

				valueList = tran.selectSql(queryInfo.getSql(), queryInfo.getParaArray());

				for (Object[] value : valueList) {
					tsList = instanceValueMap.get(value[0] == null ? "" : value[0].toString());
					if (tsList == null) {
						tsList = new TimeSeriesVo();
						tsList.setMoNo(moNo);
						tsList.setMoInstance(value[0] == null ? "" : value[0].toString());
						tsList.setPsCode(item.getPsCode());
						instanceValueMap.put(tsList.getMoInstance(), tsList);
					}

					tsList.add(((Number) value[1]).longValue(), (Number) value[2]);
				}

				nowDate = pstype.getHstimeNextGroup(nowDate, 1);
			}

			return new ArrayList<TimeSeriesVo>(instanceValueMap.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	public void doUpdateServicePsCode() throws Exception {

		if (getCollectedPsCodeMap().size() > 0) {
			FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

			try {
				tran.start();

				for (FxServicePsVo e : getCollectedPsCodeMap().values()) {
					try {
						if (tran.updateOfClass(FxServicePsVo.class, e, null) == 0) {
							tran.insertOfClass(FxServicePsVo.class, e);
						}
					} catch (Exception e2) {
						Logger.logger.error(e2);
					}
				}

				tran.commit();

			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			} finally {
				tran.stop();
			}
		}

	}

	@Override
	public List<FxServicePsVo> doSelectServicePs(String msIpaddr, String serviceName) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("msIpaddr", msIpaddr);
		para.put("serviceName", serviceName);
		List<FX_CF_FXSERVICE_PS> list = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao()
				.select(FX_CF_FXSERVICE_PS.class, para);
		List<FxServicePsVo> ret = new ArrayList<FxServicePsVo>();
		for (FX_CF_FXSERVICE_PS e : list) {
			ret.add(new FxServicePsVo(e.getServiceName(), e.getMsIpaddr(), e.getPsCode(), e.getFxactorJavaClass(), e.getLastDate()));
		}
		return ret;
	}
}

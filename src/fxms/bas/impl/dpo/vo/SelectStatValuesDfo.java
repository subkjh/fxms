package fxms.bas.impl.dpo.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.ValueApi.StatFunction;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValues;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;
import subkjh.dao.def.Column;
import subkjh.dao.def.DaoListener;

/**
 * 수집 데이터 통계를 조회한다.
 * 
 * @author subkjh
 *
 */
public class SelectStatValuesDfo implements FxDfo<Void, Map<Long, Number>> {

	@Override
	public Map<Long, Number> call(FxFact fact, Void data) throws Exception {

		StatFunction stat = fact.getObject(StatFunction.class, "stat");
		PsItem psItem = fact.getObject(PsItem.class, "psItem");
		PsKind psKind = fact.getObject(PsKind.class, "psKind");
		long startDtm = fact.getLong("startDtm");
		long endDtm = fact.getLong("endDtm");

		return selectStatValue(psItem, psKind, startDtm, endDtm, stat);
	}

	/**
	 * 
	 * @param psItem
	 * @param psKind
	 * @param psKindCol
	 * @param startDtm
	 * @param endDtm
	 * @param statFunc
	 * @return
	 * @throws Exception
	 */
	public Map<Long, Number> selectStatValue(PsItem psItem, PsKind psKind, long startDtm, long endDtm,
			StatFunction statFunc) throws Exception {

		if (psKind.isPartition()) {

			List<PsValues> list = new SelectValuesDfo().selectValues(psItem, psKind, psItem.getDefKindCol(), startDtm,
					endDtm);

			Map<Long, Number> retMap = new HashMap<Long, Number>();
			for (PsValues value : list) {
				retMap.put(value.getMoNo(), statFunc.getValue(value.getValueOnly()));
			}
			return retMap;

		} else {

			return selectGroupBy(psItem, psKind, psItem.getDefKindCol(), startDtm, endDtm, statFunc, null);

		}

	}

	private Map<Long, Number> selectGroupBy(PsItem item, PsKind psKind, String psKindCol, long startDtm, long endDtm,
			StatFunction func, String whereInMo) throws Exception {

		DataBase database = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE);
		ClassDao tran = database.createClassDao();
		Map<Long, Number> retMap = new HashMap<>();
		StringBuffer sql = new StringBuffer();

		Column col = item.makeColumn(psKind, psKindCol);

		sql.append("select MO_NO ");
		sql.append(", ").append(func.name().toLowerCase()).append("(").append(col.getName()).append(") as ")
				.append(col.getName());
		sql.append("\n from   ").append(psKind.getTableName(item.getPsTable(), endDtm));
		sql.append("\n where  PS_DATE >= ").append(startDtm);
		sql.append("\n and    PS_DATE <= ").append(endDtm);
		if (whereInMo != null) {
			sql.append("\n and	MO_NO in (").append(whereInMo).append(")");
		}
		sql.append("\n group by MO_NO");

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
					if (datas[1] != null) {
						retMap.put(((Number) datas[0]).longValue(), (Number) datas[1]);
					}
				}

				@Override
				public void onStart(String[] colNames) throws Exception {
				}

			});

			tran.selectSql(sql.toString(), null);
			return retMap;

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}
}
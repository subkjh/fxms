package fxms.bas.impl.dpo.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.ps.PsValMakeTableDfo;
import fxms.bas.impl.vo.PsValTable;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;
import subkjh.dao.DaoExecutor;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;
import subkjh.dao.exp.DBObjectDupException;

/**
 * 수집 데이터를 기록한다.
 * 
 * @author subkjh
 *
 */
public class ValueWriteDfo implements FxDfo<PsVoList, Map<String, Integer>> {

	public static void main(String[] args) {

		MoApi.api = new MoApiDfo();

		ValueWriteDfo dfo = new ValueWriteDfo();
		PsVoList datas = new PsVoList("test", System.currentTimeMillis(), null);

		try {
			datas.add(2, MoApi.getApi().getMo(1000), PsApi.getApi().getPsItem("ePowerFactor"));
			datas.add(2, MoApi.getApi().getMo(1000), PsApi.getApi().getPsItem("ePower"));
			System.out.println(FxmsUtil.toJson(dfo.write(datas)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Integer> call(FxFact fact, PsVoList data) throws Exception {
		return write(data);
	}

	/**
	 * 
	 * @param voList
	 * @return 데이터가 추가된 테이블 목록
	 * @throws Exception
	 */
	public Map<String, Integer> write(PsVoList datas) throws Exception {

		WriteValueDao dao = new WriteValueDao();

		// 테이블별 추가 건수
		Map<String, Integer> ret = dao.write(datas);

		return ret;
	}

	class ValueBatchSaver extends DaoExecutor {

		private PsValTable psTable;

		public ValueBatchSaver(DataBase database, PsValTable psTable) {
			this.setDatabase(database);
			this.psTable = psTable;
		}

		/**
		 * 일괄처리 작업을 한 후 자원을 모두 반환합니다.
		 * 
		 * @return
		 */
		public int executeBatch() throws Exception {

			try {
				start();
				int ret = executeSql(psTable.getSqlInsert(), psTable.getValueArrayList());
				commit();
				return ret;
			} catch (DBObjectDupException e) {

				int count = 0;
				for (Object[] datas : psTable.getValueArrayList()) {
					try {
						int ret = executeSql(psTable.getSqlInsert(), datas);
						if (ret > 0) {
							count += ret;
						}
					} catch (Exception e2) {
						Logger.logger.fail(e2.getMessage());
					}
				}
				commit();
				return count;

			} catch (Exception e) {
				Logger.logger.fail("SQL={}", psTable.getSqlInsert());
				Logger.logger.error(e);
				throw e;
			} finally {
				stop();
			}

		}

	}

	class WriteValueDao {

		public WriteValueDao() {
		}

		/**
		 * 
		 * @param psDate
		 * @param valueList
		 * @param isRaw
		 * @return
		 * @throws Exception
		 */
		public Map<String, Integer> write(PsVoList datas) throws Exception {

			List<PsValTable> tableList;
			int count = 0;
			long ptime = System.currentTimeMillis();
			ValueBatchSaver saver;
			Map<String, Integer> map = new HashMap<String, Integer>();

			try {
				tableList = new PsValMakeTableDfo().getTables(datas);

				for (PsValTable psTable : tableList) {

					saver = new ValueBatchSaver(DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE), psTable);

					count = saver.executeBatch();

					Logger.logger.info("table={}, hstime={}, row={}, value={} ADDED. {}(ms)", psTable.getRealPsTable(),
							datas.getHstime(), count, datas.size(), System.currentTimeMillis() - ptime);

					map.put(psTable.getPsTable(), count);

				}

				return map;
			} catch (Exception e1) {
				Logger.logger.error(e1);
				throw e1;
			}

		}

	}

}
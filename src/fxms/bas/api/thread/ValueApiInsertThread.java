package fxms.bas.api.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AppApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.AppService;
import fxms.bas.impl.dpo.ps.PsValMakeTableDfo;
import fxms.bas.impl.vo.PsValTable;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsStatReqVo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.DaoExecutor;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;

/**
 * 수집된 성능을 기록하는 스레드
 * 
 * @author subkjh
 * 
 */
public class ValueApiInsertThread extends ValueApiBasThread {

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
			} catch (Exception e) {
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
			ValueBatchSaver saver;
			Map<String, Integer> map = new HashMap<String, Integer>();

			try {
				tableList = new PsValMakeTableDfo().getTables(datas);

				for (PsValTable psTable : tableList) {

					saver = new ValueBatchSaver(DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE)//
							, psTable);

					count = saver.executeBatch();

					Logger.logger.info("TABLE({}) DATAS({}) ADDED", psTable.getRealPsTable(), count);

					map.put(psTable.getPsTable(), count);

				}

				return map;
			} catch (Exception e1) {
				Logger.logger.error(e1);
				throw e1;
			}

		}

	}

	private final Map<String, PsStatReqVo> statReqMap = new HashMap<String, PsStatReqVo>();

	/**
	 * 
	 * @param name 스레드명
	 */
	public ValueApiInsertThread() {
		super(ValueApiInsertThread.class.getSimpleName());
	}

	/**
	 * 통계 생성을 요청한다.
	 */
	private void requestMakeStat() {

		if (this.statReqMap.size() == 0) {
			return;
		}

		synchronized (this.statReqMap) {
			List<PsStatReqVo> list = new ArrayList<PsStatReqVo>(this.statReqMap.values());

			try {
				AppApi.getApi().requestMakeStat(list);
				this.statReqMap.clear();
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

	}

	// @Override
	// protected BackupSender<VoList> makeBackupSender(String name) {
	//
	// if (ValueApi.getApi().isEnableBackupSender() == false)
	// return null;
	//
	// return new BackupSender<VoList>(name, new
	// File(ValueApi.getApi().getFolderBackup()), VoList.class) {
	// @Override
	// protected boolean send(VoList valueList) {
	// try {
	// ValueApi.getApi().doInsertValue(valueList.getMstime(),
	// valueList.getValueList());
	// return true;
	// } catch (Exception e) {
	// Logger.logger.error(e);
	// }
	// return false;
	// }
	// };
	//
	// }

	/**
	 * 통계 생성 요청을 확인한다.
	 * 
	 * @param voList
	 * @param retMap
	 */
	private void checkStatReq(PsVoList voList, Map<String, Integer> retMap) {

		try {

			// 원천 데이터가 들어오면 그에 해당되는 통계를 생성 요청한다.
			if (retMap != null) {

				List<PsKind> psKindList = PsApi.getApi().getPsKindList();
				long psDate;
				PsKind psKindSrc;
				PsStatReqVo newData;

				for (String psTable : retMap.keySet()) {

					if (retMap.get(psTable) > 0) {

						for (PsKind psKind : psKindList) {

							// 원천에 대한 통계는 생성하지 않는다.
							if (psKind.isRaw()) {
								continue;
							}

							// 원천 데이터를 기록했기 때문에 원천데이터를 이용하여 통계를 생성하는 대상만 통계 요청한다.
							try {
								psKindSrc = PsApi.getApi().getPsKind(psKind.getPsDataSrc());
							} catch (NotFoundException e) {
								continue;
							}

							if (psKindSrc.isRaw()) {

								// 통계 일시
								psDate = psKind.getHstimeStart(DateUtil.getDtm(voList.getMstime()));

								newData = new PsStatReqVo(psTable, psKind.getPsKindName(),
										psKind.getHstimeStart(psDate));

								synchronized (this.statReqMap) {
									this.statReqMap.put(newData.getKey(), newData);
								}

							}

						}
					}
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	/**
	 * 원천 데이터를 기록한다.
	 * 
	 * @param voList
	 * @return
	 * @throws Exception
	 */
	private Map<String, Integer> insertValue(PsVoList datas) throws Exception {

		if (datas == null || datas.size() == 0)
			return null;

		WriteValueDao dao = new WriteValueDao();
		return dao.write(datas);

	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(PsVoList voList) throws Exception {

		// 원천 데이터를 기록한다.
		Map<String, Integer> retMap = insertValue(voList);

		checkStatReq(voList, retMap);

		requestMakeStat();
	}

	@Override
	protected void onNoDatas(long index) {

		requestMakeStat();

	}

	@Override
	public String getState(LOG_LEVEL level) {

		StringBuffer sb = new StringBuffer();
		sb.append("REQ-REMAKE-STAT(" + statReqMap.size() + ")");
		sb.append(super.getState(level));

		return super.toString() + sb.toString();
	}
}

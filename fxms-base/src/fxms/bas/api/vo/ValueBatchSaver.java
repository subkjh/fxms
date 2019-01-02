package fxms.bas.api.vo;

import subkjh.bas.dao.control.DaoExecutor;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.log.Logger;

public class ValueBatchSaver extends DaoExecutor {

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
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			stop();
		}

	}

}

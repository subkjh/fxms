package subkjh.dao;

import java.util.List;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.co.log.Logger;
import subkjh.dao.database.DBManager;

public class QidDaoEx {

	public static QidDaoEx open(String... files) throws Exception {
		QidDaoEx dao = new QidDaoEx();
		dao.start(files);
		return dao;
	}

	private QidDao tran;
	private StringBuffer msg = new StringBuffer();
	private Exception exception = null;
	private int processedCount; // 최근 처리 건수

	public void close() throws Exception {
		close(exception);
	}

	public QidDaoEx commit() {
		tran.commit();
		return this;
	}

	public QidDaoEx execute(String qid, Object datas) throws Exception {

		int ret;
		try {
			ret = tran.execute(qid, datas);
		} catch (Exception e) {
			close(e);
			throw e;
		}

		if (msg.length() > 0)
			msg.append(", ");
		msg.append("qid=").append(ret);
		return this;
	}

	public QidDaoEx executeSql(String sql, Object para) throws Exception {
		try {
			processedCount = tran.executeSql(sql, para);
		} catch (Exception e) {
			close(e);
			throw e;
		}

		return this;
	}

	/**
	 * 
	 * @return 최근 처리 건수
	 */
	public int getProcessedCount() {
		return processedCount;
	}

	@SuppressWarnings("rawtypes")
	public List selectQid2Res(String qid, Object para) throws Exception {
		try {
			return tran.selectQid2Res(qid, para);
		} catch (Exception e) {
			close(e);
			throw e;
		}
	}

	private void close(Exception ex) throws Exception {

		if (tran == null) {
			return;
		}

		try {
			if (ex == null) {

				tran.commit();

				if (msg.length() > 0) {
					Logger.logger.info("{}", msg);
				}

			} else {
				Logger.logger.error(ex);
				tran.rollback();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
			tran = null;
		}

	}

	private QidDaoEx start(String... files) throws Exception {
		tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createQidDao(files);
		return this;
	}

}

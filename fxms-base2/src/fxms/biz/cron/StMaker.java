package fxms.biz.cron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.co.cron.Crontab;
import fxms.bas.co.exp.NotDefineException;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;

/**
 * 통계 생성자
 * 
 * @author SUBKJH-DEV
 *
 */
public class StMaker extends Crontab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7016247883177372233L;

	@Override
	public void cron() throws Exception {

		Object file = this.getFxPara().get("file");
		if (file == null) {
			throw new NotDefineException("file");
		}

		Object qidPara = this.getFxPara().get("qid");
		if (qidPara == null) {
			throw new NotDefineException("qid");
		}

		List<String> qidList = new ArrayList<String>();

		if (qidPara instanceof String) {
			qidList.add((String) qidPara);
		} else if (qidPara instanceof List) {
			for (Object e : (List) qidPara) {
				qidList.add(e.toString());
			}
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createFxDao(BasCfg.getHomeDeployConfSql(file.toString()));

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("regDate", FxApi.getDate());
			para.put("stDate", FxApi.getYmd(System.currentTimeMillis() -  86400000L));

			for (String qid : qidList) {
				if (tran.getSqlBean(qid) == null) {
					Logger.logger.fail("QID='{}' not found", qid);
				} else {
					tran.execute(qid, para);
				}
			}
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	public String getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLog() {
		return null;
	}

	@Override
	public int getOpcode() {
		return 0;
	}

}

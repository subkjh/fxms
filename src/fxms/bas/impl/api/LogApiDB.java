package fxms.bas.impl.api;

import java.util.Map;

import fxms.bas.api.LogApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_EV_CRON_LOG;
import fxms.bas.impl.dbo.all.FX_EV_LOG;
import fxms.bas.impl.dpo.user.LogUserWorkHstDfo;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 저장소 작업이 완료된 MoApi
 * 
 * @author subkjh
 *
 */
public class LogApiDB extends LogApi {

	public static void main(String[] args) {
		LogApiDB api = new LogApiDB();
		long a;
		try {
			a = api.doOpenCronLog("test", null);
			api.doCloseCronLog(a, true, 123, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected long doOpenCronLog(String cronName, Map<String, Object> inPara) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {

			FX_EV_CRON_LOG data = new FX_EV_CRON_LOG();
			data.setCronName(cronName);
			data.setStrtDtm(DateUtil.getDtm());
			data.setInParaJson(inPara == null ? "{}" : FxmsUtil.toJson(inPara));
			data.setOkYn("R");

			tran.start();

			data.setCronRunNo(tran.getNextVal(FX_EV_CRON_LOG.FX_SEQ_CRONRUNNO, Long.class));

			tran.insertOfClass(FX_EV_CRON_LOG.class, data);

			tran.commit();

			return data.getCronRunNo();

		} catch (Exception e) {
			tran.rollback();
			throw e;

		} finally {
			tran.stop();
		}

	}

	@Override
	protected void doCloseCronLog(long cronRunNo, boolean isOk, int spentTime, Map<String, Object> outPara)
			throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {

			tran.start();

			FX_EV_CRON_LOG data = tran.selectOne(FX_EV_CRON_LOG.class, makePara("cronRunNo", cronRunNo));
			if (data != null) {
				long time = System.currentTimeMillis();
				data.setFnshDtm(DateUtil.getDtm(time));
				data.setOutParaJson(outPara == null ? "{}" : FxmsUtil.toJson(outPara));
				data.setOkYn(isOk ? "Y" : "N");
				data.setTimeTakenMsec(spentTime);
				tran.updateOfClass(FX_EV_CRON_LOG.class, data);
				tran.commit();
			}

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	protected void doAddSystemLog(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {

			FX_EV_LOG data = new FX_EV_LOG();
			ObjectUtil.toObject(para, data);
			data.setEvtNo(tran.getNextVal(FX_EV_LOG.FX_SEQ_EVTNO, Long.class));

			tran.insertOfClass(FX_EV_LOG.class, data);

			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	public void logUserWorkHst(int userNo, String userName, String sessionId, String opId, String inPara, String outRet,
			int retNo, String retMsg, long mstimeStart, String opObjType, Object opObjNo, String opObjName) {

		new LogUserWorkHstDfo().log(userNo, userName, sessionId, opId, inPara, outRet, retNo, retMsg, mstimeStart,
				opObjType, opObjNo, opObjName);

	}
}

package fxms.bas.impl.api;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.LogApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_EV_CRON_LOG;
import fxms.bas.impl.dbo.all.FX_EV_LOG;
import fxms.bas.impl.dpo.user.LogUserWorkHstDfo;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;

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
	public void logUserWorkHst(int userNo, String userName, String sessionId, String opId, String inPara, String outRet,
			int retNo, String retMsg, long mstimeStart, String opObjType, Object opObjNo, String opObjName) {

		new LogUserWorkHstDfo().log(userNo, userName, sessionId, opId, inPara, outRet, retNo, retMsg, mstimeStart,
				opObjType, opObjNo, opObjName);

	}

	@Override
	protected void doAddSystemLog(Map<String, Object> para) throws Exception {
		
		FX_EV_LOG data = new FX_EV_LOG();
		ObjectUtil.toObject(para, data);

		ClassDaoEx dao = ClassDaoEx.open();
		data.setEvtNo(dao.getNextVal(FX_EV_LOG.FX_SEQ_EVTNO, Long.class));
		dao.insertOfClass(FX_EV_LOG.class, data).close();
		
	}

	@Override
	protected void doCloseCronLog(long cronRunNo, boolean isOk, int spentTime, Object outPara) throws Exception {

		Map<String, Object> data = FxApi.makePara("fnshDtm", DateUtil.getDtm()//
				, "outParaJson", (outPara == null ? "{}" : FxmsUtil.toJson(outPara)) //
				, "okYn", (isOk ? "Y" : "N") //
				, "timeTakenMsec", spentTime);

		ClassDaoEx.open().setOfClass(FX_EV_CRON_LOG.class, makePara("cronRunNo", cronRunNo), ObjectUtil.toMap(data))
				.close();

	}

	@Override
	protected long doOpenCronLog(String cronName, Object inPara) throws Exception {

		FX_EV_CRON_LOG data = new FX_EV_CRON_LOG();
		data.setCronName(cronName);
		data.setStrtDtm(DateUtil.getDtm());
		data.setInParaJson(inPara == null ? "{}" : FxmsUtil.toJson(inPara));
		data.setOkYn("R");

		ClassDaoEx dao = ClassDaoEx.open();
		data.setCronRunNo(dao.getNextVal(FX_EV_CRON_LOG.FX_SEQ_CRONRUNNO, Long.class));
		dao.insertOfClass(FX_EV_CRON_LOG.class, data);
		dao.close();

		return data.getCronRunNo();
	}
}

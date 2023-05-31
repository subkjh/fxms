package fxms.bas.impl.dpo.user;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_UR_USER_WORK_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 
 * @author subkjh
 *
 */
public class LogUserWorkHstDfo implements FxDfo<FX_UR_USER_WORK_HST, Boolean> {

	@Override
	public Boolean call(FxFact fact, FX_UR_USER_WORK_HST data) throws Exception {

		log(data);

		return true;
	}

	public void log(FX_UR_USER_WORK_HST hst) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			long opSeqno = tran.getNextVal(FX_UR_USER_WORK_HST.FX_SEQ_OPSEQNO, Long.class);
			hst.setOpSeqNo(opSeqno);
			FxTableMaker.initRegChg(-1, hst);
			tran.insertOfClass(FX_UR_USER_WORK_HST.class, hst);

			tran.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

	public void log(int userNo, String userName, String sessionId, String opId, String inPara, String outRet, int retNo,
			String retMsg, long mstimeStart, String opObjType, Object opObjNo, String opObjName) {

		FX_UR_USER_WORK_HST hst = new FX_UR_USER_WORK_HST();

		try {
			hst.setUserNo(userNo);
			hst.setUserName(userName);
			hst.setSessionId(sessionId);
			hst.setOpId(opId);
			hst.setInPara(inPara);
			hst.setOutRet(outRet);
			hst.setRstNo(retNo);
			hst.setRstCont(retMsg);
			hst.setStrtDtm(DateUtil.toHstime(mstimeStart));
			hst.setOpObjType(opObjType);
			if (opObjNo != null)
				hst.setOpObjNo(opObjNo.toString());
			hst.setOpObjName(opObjName);

			hst.setEndDtm(DateUtil.getDtm());

			log(hst);

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}
}

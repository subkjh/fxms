package fxms.bas.impl.dpo.mo;

import fxms.bas.co.CoCode.MO_WORK_TYPE_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MX_WORK_HST;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 관리대상에 대한 동기화를 요청한다.
 * 
 * @author subkjh
 *
 */
public class ReqSyncMoDpo implements FxDpo<Long, Boolean> {

	@Override
	public Boolean run(FxFact fact, Long moNo) throws Exception {
		return requestSync(moNo);
	}

	public Boolean requestSync(long moNo) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_MX_WORK_HST obj = new FX_MX_WORK_HST();

			Long workHstNo = tran.getNextVal(FX_MX_WORK_HST.FX_SEQ_WORKHSTNO, Long.class);
			obj.setWorkHstNo(workHstNo);
			obj.setMoNo(moNo);
			obj.setMoWorkTypeCd(MO_WORK_TYPE_CD.Sync.getCode());
			obj.setRstNo(0);

			FxTableMaker.initRegChg(User.USER_NO_SYSTEM, obj);

			tran.insertOfClass(FX_MX_WORK_HST.class, obj);

			tran.commit();

			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}
}

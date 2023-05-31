package fxms.bas.impl.dpo.mo;

import fxms.bas.api.FxApi;
import fxms.bas.event.FxEvent;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.SyncMo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 관리대상에 대한 동기화를 진행한다.
 * 
 * @author subkjh
 *
 */
public class UpdateSyncMoDfo implements FxDfo<SyncMo, Boolean> {

	public boolean update(SyncMo syncMo, boolean updateUpper) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		Mo parent = syncMo.getUpper();
		long moNo;

		try {
			tran.start();

			for (Mo mo : syncMo.getMoListAll()) {
				if (mo.getStatus() == FxEvent.STATUS.deleted) {
					try {
						tran.deleteOfClass(mo.getClass(), FxApi.makePara("moNo", mo.getMoNo()));
					} catch (Exception e) {
						Logger.logger.trace("{} {}", mo, mo.getClass().getName());
						throw e;
					}
				}
			}

			for (Mo mo : syncMo.getMoListAll()) {
				if (mo.getStatus() == FxEvent.STATUS.changed) {
					FxTableMaker.initRegChg(User.USER_NO_SYSTEM, mo);
					tran.updateOfClass(mo.getClass(), mo);
				}
			}

			for (Mo mo : syncMo.getMoListAll()) {

				if (mo.getStatus() == FxEvent.STATUS.added) {

					moNo = tran.getNextVal(FX_MO.FX_SEQ_MONO, Long.class);

					Logger.logger.trace("{} for new mono {}", mo.getMoName(), moNo);

					mo.setMoNo(moNo);

					FxTableMaker.initRegChg(User.USER_NO_SYSTEM, mo);
					mo.setMoAddJson(FxmsUtil.toJson(mo.getAttrMap()));

					tran.insertOfClass(mo.getClass(), mo);

				}
			}

			if (updateUpper) {
				FxTableMaker.initRegChg(User.USER_NO_SYSTEM, parent);
				parent.setMoAddJson(FxmsUtil.toJson(parent.getAttrMap()));
				tran.updateOfClass(parent.getClass(), parent);
			}

			tran.commit();
			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	public Boolean call(FxFact fact, SyncMo data) throws Exception {
		return update(data, true);
	}

}

package fxms.bas.impl.dpo.mo;

import fxms.bas.api.FxApi;
import fxms.bas.co.DATA_STATUS;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MAPP_PS;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.mapp.MappData;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 관리대상 + 수집ID = 맵핑ID 데이터를 조회한다.
 * 
 * @author subkjh
 *
 */
public class MoPsIdDeleteDfo implements FxDfo<MappData, DATA_STATUS> {

	@Override
	public DATA_STATUS call(FxFact fact, MappData data) throws Exception {
		return delete(data);
	}

	public DATA_STATUS delete(MappData mapp) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			int ret = tran.deleteOfClass(FX_MAPP_PS.class,
					FxApi.makePara("mngDiv", mapp.getMngDiv(), "mappData", mapp.getMappData()));

			tran.commit();

			return ret > 0 ? DATA_STATUS.deleted : DATA_STATUS.nothing;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}
}

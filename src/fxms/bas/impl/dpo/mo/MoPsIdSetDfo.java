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
import subkjh.dao.util.FxTableMaker;

/**
 * 관리대상 + 수집ID = 맵핑ID 데이터를 조회한다.
 * 
 * @author subkjh
 *
 */
public class MoPsIdSetDfo implements FxDfo<FX_MAPP_PS, DATA_STATUS> {

	@Override
	public DATA_STATUS call(FxFact fact, FX_MAPP_PS data) throws Exception {
		return set(data);
	}

	public DATA_STATUS set(int userNo, MappData mappData, long moNo, String moName, String psId, String psName)
			throws Exception {

		FX_MAPP_PS mapp = new FX_MAPP_PS();
		mapp.setMngDiv(mappData.getMngDiv());
		mapp.setMappData(mappData.getMappData());
		mapp.setMoNo(moNo);
		mapp.setMoName(moName);
		mapp.setPsId(psId);
		mapp.setPsName(psName);
		mapp.setMappDescr(mappData.getMappDescr());
		mapp.setMappId(mappData.getMappId().toString());

		return set(mapp);

	}

	public DATA_STATUS set(FX_MAPP_PS mapp) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_MAPP_PS old = tran.selectOne(FX_MAPP_PS.class,
					FxApi.makePara("mngDiv", mapp.getMngDiv(), "mappData", mapp.getMappData()));
			if (old != null) {
				mapp = old;
			}

			FxTableMaker.initRegChg(0, mapp);

			if (old != null) {
				tran.updateOfClass(FX_MAPP_PS.class, mapp);
			} else {
				tran.insertOfClass(FX_MAPP_PS.class, mapp);
			}

			tran.commit();

			return old == null ? DATA_STATUS.added : DATA_STATUS.updated;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}
}

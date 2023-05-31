package fxms.bas.impl.dpo.model;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MAPP_ETC;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.mapp.MappData;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

public class SetModelMappDfo implements FxDfo<MappData, Boolean> {

	@Override
	public Boolean call(FxFact fact, MappData data) throws Exception {

		int modelNo = fact.getInt("modelNo");
		int userNo = fact.getUserNo();

		return setMapping(userNo, data, modelNo, null);
	}

	public boolean setMapping(int userNo, MappData mappData, int modelNo, String modelName) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			FX_MAPP_ETC mapp = new FX_MAPP_ETC();
			mapp.setMngDiv(mappData.getMngDiv());
			mapp.setMappData(mappData.getMappData());

			FX_MAPP_ETC old = tran.selectOne(FX_MAPP_ETC.class,
					FxApi.makePara("mngDiv", mapp.getMngDiv(), "mappId", mapp.getMappId()));
			if (old != null) {
				mapp = old;
			}

			mapp.setMappDescr(mappData.getMappDescr());
			mapp.setMappId(mappData.getMappId().toString());
			mapp.setObjData(modelNo + "");
			mapp.setObjDescr(modelName);
			FxTableMaker.initRegChg(userNo, mapp);

			if (old != null) {
				tran.updateOfClass(FX_MAPP_ETC.class, mapp);
			} else {
				tran.insertOfClass(FX_MAPP_ETC.class, mapp);
			}
			tran.commit();
			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

}

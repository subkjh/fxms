package fxms.bas.impl.dpo.model;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_MODEL;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class UpdateModelDfo implements FxDfo<Map<String, Object>, Boolean> {

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {

		int modelNo = fact.getInt("modelNo");
		int userNo = fact.getUserNo();

		return updateModel(userNo, modelNo, data);
	}

	public boolean updateModel(int userNo, int modelNo, Map<String, Object> datas) throws Exception {

		if (datas == null || datas.size() == 0)
			return false;

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_CF_MODEL old = tran.selectOne(FX_CF_MODEL.class, FxApi.makePara("modelNo", modelNo));
			if (old != null) {
				FxDpo.initRegChg(userNo, datas);
				tran.updateOfClass(FX_CF_MODEL.class, datas);
				tran.commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}
}

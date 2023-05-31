package fxms.bas.impl.dpo.model;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_MODEL;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 모델 삭제
 * 
 * @author subkjh
 *
 */
public class DeleteModelDfo implements FxDfo<Integer, Boolean> {

	@Override
	public Boolean call(FxFact fact, Integer modelNo) throws Exception {
		String modelName = fact.getString("modelName");
		return deleteModel(modelNo, modelName);
	}

	public boolean deleteModel(int modelNo, String modelName) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, Object> wherePara = FxApi.makePara("modelNo", modelNo, "modelName", modelName);
			if (tran.selectOne(FX_CF_MODEL.class, wherePara) == null) {
				throw new NotFoundException("model", modelNo);
			}

			int count = tran.selectCount(FX_MO.class, FxApi.makePara("modelNo", modelNo));
			if (count > 0) {
				throw new Exception(
						Lang.get("The model could not be deleted because the management object in use exists.", count));
			}

			tran.deleteOfClass(FX_CF_MODEL.class, wherePara);

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

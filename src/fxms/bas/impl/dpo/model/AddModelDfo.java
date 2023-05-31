package fxms.bas.impl.dpo.model;

import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_MODEL;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

public class AddModelDfo implements FxDfo<Map<String, Object>, Integer> {

	@Override
	public Integer call(FxFact fact, Map<String, Object> datas) throws Exception {
		int userNo = fact.getUserNo();
		return addModel(userNo, datas);
	}

	public int addModel(int userNo, Map<String, Object> para) throws Exception {

		FX_CF_MODEL data = new FX_CF_MODEL();
		data.setModelClCd("NONE");

		ObjectUtil.toObject(para, data);
		FxTableMaker.initRegChg(userNo, data);

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			int modelNo = tran.getNextVal(FX_CF_MODEL.FX_SEQ_MODELNO, Integer.class);

			data.setModelNo(modelNo);

			tran.insertOfClass(data.getClass(), data);

			tran.commit();

			return modelNo;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}

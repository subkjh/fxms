package fxms.bas.impl.dpo.ps;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_PS_ITEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 
 * @author subkjh
 *
 */
public class PsItemAddDfo implements FxDfo<Void, Boolean> {

	@Override
	public Boolean call(FxFact fact, Void data) throws Exception {
		return null;
	}

	public Boolean addPsItem(int userNo, String psId, String psName, Map<String, Object> para) throws Exception {

		FX_PS_ITEM item = FxTableMaker.toObject(para, FX_PS_ITEM.class, false);
		FxTableMaker.initRegChg(userNo, item);

		item.setPsId(psId);
		item.setPsName(psName);

		if (FxApi.isEmpty(item.getPsTbl())) {
			item.setPsTbl("FX_V_" + psId.toUpperCase());
		}
		if (FxApi.isEmpty(item.getPsTbl())) {
			item.setPsCol(psId.toUpperCase());
		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			tran.insertOfClass(FX_PS_ITEM.class, item);
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

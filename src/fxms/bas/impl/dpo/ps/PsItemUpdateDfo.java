package fxms.bas.impl.dpo.ps;

import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_PS_ITEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 
 * @author subkjh
 *
 */
public class PsItemUpdateDfo implements FxDfo<Map<String, Object>, Boolean> {

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {
		Object psId = data.get("psId");
		return updatePsItem(fact.getUserNo(), psId.toString(), data);
	}

	public Boolean updatePsItem(int userNo, String psId, Map<String, Object> datas) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FxDpo.initRegChg(userNo, datas);
			tran.updateOfClass(FX_PS_ITEM.class, datas);

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

package fxms.bas.impl.dpo.var;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.co.CoCode.VAR_TYPE_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_VAR;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 환경변수 값을 설정한다.
 * 
 * @author subkjh
 *
 */
public class SetVarDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) {

		SetVarDfo dfo = new SetVarDfo();
		FxFact fact = new FxFact("varName", "TEST");
		try {
			dfo.call(fact, FxApi.makePara("varVal", "aaaa"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {

		String varName = fact.getString("varName");

		return setVar(varName, data);
	}

	public boolean setVar(String varName, Map<String, Object> para) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			FX_CF_VAR data;
			FX_CF_VAR oldData = tran.selectOne(FX_CF_VAR.class, FxApi.makePara("varName", varName));
			if (oldData == null) {
				data = new FX_CF_VAR();
				data.setVarName(varName);
				data.setVarTypeCd(VAR_TYPE_CD.String.getCode());
			} else {
				data = oldData;
			}

			ObjectUtil.toObject(para, data);
			FxTableMaker.initRegChg(User.USER_NO_SYSTEM, data);

			if (oldData == null) {
				tran.insertOfClass(FX_CF_VAR.class, data);
			} else {
				tran.updateOfClass(FX_CF_VAR.class, data);
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
}

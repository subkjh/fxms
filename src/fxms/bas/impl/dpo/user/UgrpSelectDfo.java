package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_UR_UGRP;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.Ugrp;
import subkjh.bas.co.user.exception.UgrpNotFoundException;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class UgrpSelectDfo implements FxDfo<Integer, Ugrp> {

	public static void main(String[] args) {
		UgrpSelectDfo dfo = new UgrpSelectDfo();
		try {
			dfo.select(1060);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Ugrp call(FxFact fact, Integer ugrpNo) throws Exception {
		return select(ugrpNo);
	}

	public Ugrp select(int ugrpNo) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, Object> para = FxApi.makePara("ugrpNo", ugrpNo);
			FX_UR_UGRP ret = tran.selectOne(FX_UR_UGRP.class, para);

			if (ret == null) {
				throw new UgrpNotFoundException(ugrpNo);
			}

			return new Ugrp(ret.getUgrpNo(), ret.getInloNo(), ret.getUgrpName());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}
}
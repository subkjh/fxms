package fxms.bas.impl.dpo.mo;

import java.util.List;
import java.util.Map;

import fxms.bas.impl.dbo.all.FX_MX_WORK_HST;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDaoEx;

public class GetMoWorkHstDpo implements FxDpo<Map<String, Object>, List<FX_MX_WORK_HST>> {

	@Override
	public List<FX_MX_WORK_HST> run(FxFact fact, Map<String, Object> data) throws Exception {

		try {
			List<FX_MX_WORK_HST> list = ClassDaoEx.SelectDatas(FX_MX_WORK_HST.class, data);
			return list;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

}

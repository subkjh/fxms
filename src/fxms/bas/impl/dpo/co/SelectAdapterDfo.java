package fxms.bas.impl.dpo.co;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.impl.dbo.all.FX_CF_ADAPT;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDaoEx;

public class SelectAdapterDfo implements FxDfo<Void, List<FX_CF_ADAPT>> {

	public static void main(String[] args) {

		SelectAdapterDfo dfo = new SelectAdapterDfo();
		FxFact fact = new FxFact();
		try {
			FxmsUtil.print(dfo.call(fact, null));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<FX_CF_ADAPT> call(FxFact fact, Void data) throws Exception {
		return selectAdapters();
	}

	public List<FX_CF_ADAPT> selectAdapters() throws Exception {

		List<FX_CF_ADAPT> list = ClassDaoEx.SelectDatas(FX_CF_ADAPT.class, ClassDaoEx.makePara("useYn", "Y"),
				FX_CF_ADAPT.class);

		List<FX_CF_ADAPT> ret = new ArrayList<FX_CF_ADAPT>();
		for (FX_CF_ADAPT a : list) {
			try {
				if (FxServiceImpl.serviceName.equals(a.getFxsvcName()) || "all".equalsIgnoreCase(a.getFxsvcName())) {
					ret.add(a);
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return ret;

	}
}
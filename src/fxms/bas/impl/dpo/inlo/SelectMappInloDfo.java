package fxms.bas.impl.dpo.inlo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_MAPP_INLO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;

public class SelectMappInloDfo implements FxDfo<String, Map<String, Integer>> {

	public static void main(String[] args) {
		try {
			SelectMappInloDfo dfo = new SelectMappInloDfo();

			System.out.println(FxmsUtil.toJson(dfo.selectMappInlo("fxms")));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public Map<String, Integer> call(FxFact fact, String mngDiv) throws Exception {
		return selectMappInlo(mngDiv);
	}

	public Map<String, Integer> selectMappInlo(String mngDiv) throws Exception {
		List<FX_MAPP_INLO> list = ClassDaoEx.SelectDatas(FX_MAPP_INLO.class, FxApi.makePara("mngDiv", mngDiv));
		Map<String, Integer> retMap = new HashMap<>();
		for (FX_MAPP_INLO mapp : list) {
			if (mapp.getInloNo() > 0) {
				retMap.put(mapp.getMappId(), mapp.getInloNo());
			}
		}
		return retMap;
	}
}

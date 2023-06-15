package fxms.bas.impl.dpo.mo;

import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import subkjh.bas.co.lang.Lang;

public class GetMoDfo implements FxDfo<Long, Mo> {

	@Override
	public Mo call(FxFact fact, Long moNo) throws Exception {
		return selectMo(moNo);
	}

	public Mo selectMo(Map<String, Object> para) throws Exception {
		List<Mo> list = new GetMoListDfo().selectMoList(para);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() == 0) {
			throw new MoNotFoundException(Lang.get("Could not find management object.", para));
		} else {
			throw new Exception(Lang.get("Too many management objects have been searched.", para));
		}
	}

	public Mo selectMo(long moNo) throws Exception {
		return selectMo(FxApi.makePara("moNo", moNo));
	}

}

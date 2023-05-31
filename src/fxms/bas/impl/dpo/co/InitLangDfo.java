package fxms.bas.impl.dpo.co;

import java.util.List;

import fxms.bas.impl.dbo.all.FX_CO_LANG;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.lang.Lang;

public class InitLangDfo implements FxDfo<List<FX_CO_LANG>, Boolean> {

	@Override
	public Boolean call(FxFact fact, List<FX_CO_LANG> data) throws Exception {
		return initLang(data);
	}

	public boolean initLang(List<FX_CO_LANG> list) {

		for (FX_CO_LANG o : list) {
			Lang.put(o.getLangKey(), o.getLangValue());
		}

		return true;

	}
}
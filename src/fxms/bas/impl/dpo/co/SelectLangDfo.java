package fxms.bas.impl.dpo.co;

import java.util.List;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_CO_LANG;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;

public class SelectLangDfo implements FxDfo<String, List<FX_CO_LANG>> {

	public static void main(String[] args) {

		SelectLangDfo dfo = new SelectLangDfo();
		FxFact fact = new FxFact();
		try {
			FxmsUtil.print(dfo.call(fact, "ko"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<FX_CO_LANG> call(FxFact fact, String langCd) throws Exception {
		return selectLang(langCd);
	}

	public List<FX_CO_LANG> selectLang(String langCd) throws Exception {
		return ClassDaoEx.SelectDatas(FX_CO_LANG.class, ClassDaoEx.makePara("langCd", langCd));
	}
}
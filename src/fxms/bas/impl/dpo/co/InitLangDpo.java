package fxms.bas.impl.dpo.co;

import java.util.List;

import fxms.bas.impl.dbo.all.FX_CO_LANG;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;

/**
 * 알람 생성
 * 
 * @author subkjh
 *
 */
public class InitLangDpo implements FxDpo<String, Boolean> {

	public static void main(String[] args) {
		InitLangDpo dpo = new InitLangDpo();
		FxFact fact = new FxFact();

		try {
			dpo.run(fact, "ko");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean run(FxFact fact, String langCd) throws Exception {
		return initLang(langCd);

	}

	public Boolean initLang() {
		String langCd = System.getProperty("user.language");
		return initLang(langCd);
	}

	public Boolean initLang(String langCd) {

		List<FX_CO_LANG> list;
		try {
			list = new SelectLangDfo().selectLang(langCd); // 가져오기
			return new InitLangDfo().initLang(list); // 적용하기
		} catch (Exception e) {
			return false;
		}

	}

}

package fxms.bas.impl.dpo.user;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;

/**
 * 인증번호 생성 함수
 * 
 * @author subkjh
 *
 */
public class CreateCertfNumDfo implements FxDfo<Void, String> {

	public static void main(String[] args) {
		CreateCertfNumDfo dfo = new CreateCertfNumDfo();
		try {
			System.out.println(dfo.call(null, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String call(FxFact fact, Void datas) throws Exception {
		return createCertfNum();
	}

	public String createCertfNum() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		String str = "";

		int idx = 0;
		for (int i = 0; i < 6; i++) {
			idx = (int) (charSet.length * Math.random());
			str += charSet[idx];
		}
		return str;
	}
}

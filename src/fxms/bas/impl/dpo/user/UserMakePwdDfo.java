package fxms.bas.impl.dpo.user;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;

/**
 * 운용자의 암호를 변경하는 함수
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserMakePwdDfo implements FxDfo<Void, String> {

	public static void main(String[] args) throws Exception {
		UserMakePwdDfo dfo = new UserMakePwdDfo();
		for (int i = 0; i < 10; i++) {
			System.out.println(dfo.makeNewPassword());
		}
		for (int i = 0; i < 100000; i++) {
			dfo.makeNewPassword();
		}
	}

	@Override
	public String call(FxFact fact, Void data) throws Exception {
		return makeNewPassword();
	}

	public String makeNewPassword() throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 12; i++) {

			int n = (int) (Math.random() * 12);

			if (n <= 2) {
				sb.append(get(uppers));
			} else if (n <= 3) {
				sb.append(get(specifils));
			} else if (n <= 8) {
				sb.append(get(numbers));
			} else {
				sb.append(get(lowers));
			}
		}
		return sb.toString();
	}

	final char[] lowers = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	final char[] uppers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	final char[] numbers = "0123456789".toCharArray();
	final char[] specifils = "!@#$%^&*()-=_+~".toCharArray();

	private char get(char chs[]) {
		int index = (int) (Math.random() * chs.length);
		return chs[index];
	}

}

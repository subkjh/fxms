package fxms.bas.impl.dpo.user;

import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.MailSendDfo;
import fxms.bas.impl.dto.UserDto;
import subkjh.bas.co.user.User;

/**
 * 운용자의 암호를 변경하는 함수
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserPwdResetDpo implements FxDpo<UserDto, Boolean> {

	public static void main(String[] args) {
		UserPwdResetDpo dpo = new UserPwdResetDpo();
		try {
			dpo.resetUserPwd(1101, "test8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean run(FxFact fact, UserDto data) throws Exception {
		return resetUserPwd(data.getUserNo(), data.getUserId());
	}

	/**
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @param newPasswd
	 * @param newPasswdChk
	 * @return
	 * @throws Exception
	 */
	public boolean resetUserPwd(int userNo, String userId) throws Exception {

		User user = new UserSelectDfo().select(userNo, userId); // 사용자조회

		String newPwd = new UserMakePwdDfo().makeNewPassword(); // 암호 생성

		new UserPwdChangeDfo().changePwd(user, newPwd, newPwd); // 암호 변경

		new MailSendDfo().send(userId, "Reset Password", newPwd); // 암호 전달

		return true;
	}
}

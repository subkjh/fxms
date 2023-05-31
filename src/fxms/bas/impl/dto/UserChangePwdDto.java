package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class UserChangePwdDto {

	@FxAttr(description = "사용자암호", example = "1234")
	private String userPwd;

	@FxAttr(description = "신규사용자암호", example = "1234")
	private String newUserPwd;

	@FxAttr(description = "신규사용자암호(재입력)", example = "1234")
	private String newUserPwdAgain;

	public String getUserPwd() {
		return userPwd;
	}

	public String getNewUserPwd() {
		return newUserPwd;
	}

	public String getNewUserPwdAgain() {
		return newUserPwdAgain;
	}

}

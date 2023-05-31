package test.bas.impl.handler;

import java.util.Map;

import fxms.bas.co.CoCode.NEW_USER_REG_ST_CD;

public class UserHandlerTest extends HandlerTest {

	public static void main(String[] args) {
		UserHandlerTest c = null;
		try {
			c = new UserHandlerTest();
//			c.selectUserGroupList();
//			c.applyNewUser();
//			c.confirmNewUser();
//			c.updateUser();
//			c.selectUserInfo();
//			c.resetUserPwd();
//			c.changeUserPwd();
//			c.deleteUser();
//			c.updateUserUseDate();
//			c.updateUserUseDisable();

//			c.insertUser();
//			c.selectUserGridList();
//			c.insertUserGroup();
//			c.updateUserGroup();
//			c.selectUserGroupGridList();
//			c.deleteUserGroup();

			c.updateUserGroupOpEnable();
//			c.updateUserGroupOpDisable();

//			c.selectUserOpList();
//			c.selectUserMenuList();

//			c.updateMySession();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {

			Thread.sleep(5000);
			c.logout();
			Thread.sleep(5000);

			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private final String testUserId = "2222";

	private final int testUserNo = 1004;

	public UserHandlerTest() throws Exception {
		super("localhost", "user");
	}

	public void applyNewUser() throws Exception {

		Map<String, Object> para = makePara();
		para.put("applyUserId", testUserId);
		para.put("applyUserName", "applyUserName1234");
		para.put("applyUserMail", "applyUserMail1234");
		para.put("applyTelNo", "applyTelNo1234");
		para.put("applyInloNo", "applyInloNo1234");
		call("apply-new-user", para);
	}

	public void changeMyUserPwd() throws Exception {

//		userPwd 사용자암호 Y
//		newUserPwd 신규사용자암호 Y
//		newUserPwdAgain 신규사용자암호 Y
//		
		Map<String, Object> para = makePara();
		para.put("userPwd", "1111");
		para.put("newUserPwd", "1111");
		para.put("newUserPwdAgain", "1111");
		call("change-my-user-pwd", para);
	}

	public void confirmNewUser() throws Exception {
		Map<String, Object> para = makePara();

		para.put("applyDtm", 20220531102022L);
		para.put("applyUserId", testUserId);
		para.put("inloNo", 2000);
		para.put("ugrpNo", 102);
		para.put("procRsn", "테스트");
		para.put("newUserRegStCd", NEW_USER_REG_ST_CD.Approval.getCode());
		call("confirm-new-user", para);
	}

	public void deleteUser() throws Exception {
//		userNo 운용자번호 Y
//		userId 사용자ID Y
//		userName 사용자명 Y
		Map<String, Object> para = makePara();
		para.put("userId", testUserId);
		para.put("userNo", testUserId);
		para.put("userName", testUserId);

		call("delete-user", para);
	}

	public void deleteUserGroup() throws Exception {
//		ugrpNo 사용자그룹번호 Y

		Map<String, Object> para = makePara();
		para.put("ugrpNo", 1004);

		call("delete-user-group", para);
	}

	public void insertUser() throws Exception {

//		userId 사용자ID Y
//		userName 사용자명 Y
//		userPwd 사용자암호 Y
//		userMail 사용자메일 N
//		userTelNo 사용자전화번호 N
//		ugrpNo 사용자그룹번호 Y
//		inloNo 설치위치번호 N

		Map<String, Object> para = makePara();
		para.put("userId", "aaa");
		para.put("userName", "테스트사용자");
		para.put("userPwd", "1234");
		para.put("userMail", "222");
		para.put("userTelNo", "111");
		para.put("ugrpNo", 102);
		para.put("inloNo", 2000);

		call("insert-user", para);
	}

	public void insertUserGroup() throws Exception {

//		ugrpName 사용자그룹명 Y
//		ugrpDesc 사용자그룹설명 N
//		inloNo 설치위치번호 N

		Map<String, Object> para = makePara();
		para.put("ugrpName", "테스44");
		para.put("ugrpDesc", "테스44");
		para.put("inloNo", 2000);

		call("insert-user-group", para);

	}

	public void resetUserPwd() throws Exception {
//		userId 사용자ID Y
//		userPwd 사용자암호 N
//		chgUserNo 수정사용자번호 N
//		chgDtm 수정일시 N

		call("reset-user-pwd", makePara("userId", testUserId));
	}

	public void selectUserAttrList() throws Exception {
	}

	public void selectUserGridList() throws Exception {

//		ugrpNo 사용자그룹번호 N
//		inloNo 설치위치번호 N
//		userId 사용자ID N
//		userName 사용자명 N

		Map<String, Object> para = makePara();

		call("select-user-grid-list", para);
	}

	public void selectUserGroupGridList() throws Exception {
		call("select-user-group-grid-list", makePara());
	}

	public void selectUserGroupList() throws Exception {
		call("select-user-group-list", makePara("inloNo", "2000"));
	}

	public void selectUserInfo() throws Exception {
//		userId 사용자ID Y;
		call("select-user-info", makePara("userId", testUserId));
	}

	public void selectUserMenuList() throws Exception {

		call("select-my-menu-list", makePara());

	}

	public void selectUserOpList() throws Exception {

		call("select-my-op-list", makePara());

	}

	public void selectUserUiList() throws Exception {

	}

	public void updateUserGroup() throws Exception {

//		ugrpNo 사용자그룹번호 Y
//		ugrpName 사용자그룹명 Y
//		ugrpDesc 사용자그룹설명 N
//		inloNo 설치위치번호 Y

		Map<String, Object> para = makePara();
		para.put("ugrpNo", 100);
		para.put("ugrpName", "테스트그룹2");
		para.put("ugrpDesc", "테스트사용자2");
		para.put("inloNo", 2000);
		call("update-user-group", para);
	}

	public void updateUserGroupOpDisable() throws Exception {
//		ugrpNo 사용자그룹번호 Y
//		opId 기능ID Y

		Map<String, Object> para = makePara();
		para.put("ugrpNo", 102);
		para.put("opId", "select-my-op-list212");
		call("update-user-group-op-disable", para);
	}

	public void updateUserGroupOpEnable() throws Exception {

//		ugrpNo 사용자그룹번호 Y
//		opId 기능ID Y
//		regMemo 등록사유 N

		Map<String, Object> para = makePara();
		para.put("ugrpNo", 102);
		para.put("opId", "get-login-user-list");
		para.put("regMemo", "테스트사용자2");
		call("update-user-group-op-enable", para);

	}

	public void updateUserInfo() throws Exception {

//		userNo 사용자번호 Y
//		userName 사용자명 Y
//		userMail 사용자메일 N
//		userTelNo 사용자전화번호 N
//		accsNetwork 접속NETWORK N
//		accsNetmask 접속NETMASK N

		Map<String, Object> para = makePara();
		para.put("userNo", testUserNo);
		para.put("userName", "홍길동");
		para.put("userMail", "test@test.com");
		para.put("userTelNo", "1234-1234");
		call("update-user-info", para);
	}

	public void updateUserUseDate() throws Exception {
//		userNo 사용자번호 Y
//		useYn 사용여부 N
//		chgUserNo 수정사용자번호 N
//		chgDtm 수정일시 N
//		useEndDate 사용종료일자 N
//		strDate 사용종료일자(1m|3m|6m|12m) Y

		call("update-user-use-date", makePara("userNo", 1006, "strDate", "3m"));
	}

	public void updateUserUseDisable() throws Exception {

//		userNo 사용자번호 Y
//		useYn 사용여부 N

		call("update-user-use-disable", makePara("userNo", 1006));
	}

	public void updateMySession() throws Exception {
		call("update-my-session", makePara());
	}
}

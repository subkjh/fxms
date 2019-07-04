package subkjh.bas.co.user;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable {

	public enum USER_TYPE {
		operator(1), user(2), admin(3);

		public static USER_TYPE getUserType(int code) {
			for (USER_TYPE type : USER_TYPE.values()) {
				if (type.getCode() == code) {
					return type;
				}
			}

			return operator;
		}

		private int code;

		private USER_TYPE(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3647717662741240931L;
	public static final String USER_ID_SYSTEM = "SYSTEM";
	public static final String USER_NAME_SYSTEM = "SYSTEM";

	public static final int USER_NO_SYSTEM = 0;
	public static final int USER_GROUP_ALL = 0;
	public static final int USER_GROUP_EMPTY = 1;

	public static String encodingPassword(String s) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(s.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(User.encodingPassword("guest123"));
		System.out.println(User.encodingPassword("SYSTEM"));
	}

	private int userNo;
	private String userName;
	private int userGroupNo;
	private int mngInloNo = 0;
	private USER_TYPE userType;
	private String userId;

	public User() {
		userType = USER_TYPE.operator;
	}

	public User(String userId, int userNo, String userName, int userGroupNo, USER_TYPE type, int mngInloNo) {
		this.userId = userId;
		this.userNo = userNo;
		this.userName = userName;
		this.userGroupNo = userGroupNo;
		this.mngInloNo = mngInloNo;
		this.userType = type;
	}

	public int getMngInloNo() {
		return mngInloNo;
	}

	public int getUserGroupNo() {
		return userGroupNo;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public int getUserNo() {
		return userNo;
	}

	public USER_TYPE getUserType() {
		return userType;
	}

	public boolean isAccesable(int opNo) {
		return true;
	}

	public void setMngInloNo(int mngInloNo) {
		this.mngInloNo = mngInloNo;
	}

	public void setUserGroupNo(int userGroupNo) {
		this.userGroupNo = userGroupNo;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public void setUserType(USER_TYPE userType) {
		this.userType = userType;
	}

}

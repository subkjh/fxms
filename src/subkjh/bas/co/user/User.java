package subkjh.bas.co.user;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable {

	/**
	 * 운영자유형코드
	 * 
	 * @author subkjh
	 *
	 */
	public enum USER_TYPE_CD {

		Admin("A"), Operator("O"), SuperAdmin("S"), User("U");

		public static USER_TYPE_CD get(String code) {
			for (USER_TYPE_CD e : USER_TYPE_CD.values()) {
				if (e.code.equals(code)) {
					return e;
				}
			}

			return null;
		}

		private String code;

		private USER_TYPE_CD(String code) {
			this.code = code;
		}

		public String getCode() {
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

	private static final char PWD[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()"
			.toCharArray();

	public static String makeNewUserPwd() {
		StringBuffer sb = new StringBuffer();

		int index;
		for (int i = 0; i < 12; i++) {
			index = (int) (Math.random() * PWD.length);
			if (index < PWD.length) {
				sb.append(PWD[index]);
			}
		}
		return sb.toString();

	}

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
		System.out.println(User.encodingPassword("1111"));
		System.out.println(User.encodingPassword("SYSTEM"));
		System.out.println(makeNewUserPwd());
	}

	private final int userNo;
	private final String userName;
	private final int ugrpNo;
	private final int inloNo;
	private final USER_TYPE_CD userType;
	private final String userId;

	/**
	 * 
	 * @param userId
	 * @param userNo
	 * @param userName
	 * @param ugrpNo
	 * @param type
	 * @param inloNo
	 */
	public User(String userId, int userNo, String userName, int ugrpNo, String userTypeCd, int inloNo) {
		this.userId = userId;
		this.userNo = userNo;
		this.userName = userName;
		this.ugrpNo = ugrpNo;
		this.inloNo = inloNo;
		this.userType = USER_TYPE_CD.get(userTypeCd);
	}

	public static String getUserIdSystem() {
		return USER_ID_SYSTEM;
	}

	public static String getUserNameSystem() {
		return USER_NAME_SYSTEM;
	}

	public static int getUserNoSystem() {
		return USER_NO_SYSTEM;
	}

	public static int getUserGroupAll() {
		return USER_GROUP_ALL;
	}

	public static int getUserGroupEmpty() {
		return USER_GROUP_EMPTY;
	}

	public int getUserNo() {
		return userNo;
	}

	public String getUserName() {
		return userName;
	}

	public int getUgrpNo() {
		return ugrpNo;
	}

	public int getInloNo() {
		return inloNo;
	}

	public USER_TYPE_CD getUserType() {
		return userType;
	}

	public String getUserId() {
		return userId;
	}

}

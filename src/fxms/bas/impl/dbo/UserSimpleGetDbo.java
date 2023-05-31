package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_USER", comment = "운용자테이블")
public class UserSimpleGetDbo {

	public UserSimpleGetDbo() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "USER_ID", size = 20, comment = "로그인ID")
	private String userId;

	@FxColumn(name = "USER_NAME", size = 50, comment = "운용자명")
	private String userName;

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	/**
	 * 로그인ID
	 * 
	 * @return 로그인ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 로그인ID
	 * 
	 * @param userId
	 *            로그인ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 운용자명
	 * 
	 * @return 운용자명
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 운용자명
	 * 
	 * @param userName
	 *            운용자명
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}

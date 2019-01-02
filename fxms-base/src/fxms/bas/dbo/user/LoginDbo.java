package fxms.bas.dbo.user;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_USER", comment = "운용자테이블")
@FxIndex(name = "FX_UR_USER__PK", type = INDEX_TYPE.PK, columns = { "USER_ID" })
public class LoginDbo {

	public LoginDbo() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo;

	@FxColumn(name = "USER_ID", size = 20, comment = "로그인ID")
	private String userId;

	@FxColumn(name = "USER_NAME", size = 50, comment = "운용자명")
	private String userName;

	@FxColumn(name = "USER_PASSWD", size = 255, comment = "운용자 암호")
	private String userPasswd;

	@FxColumn(name = "USER_TYPE", size = 1, nullable = true, comment = "운용자 유형", defValue = "1")
	private int userType = 1;

	@FxColumn(name = "TEL_NO", size = 50, nullable = true, comment = "전화번호")
	private String telNo;

	@FxColumn(name = "UGRP_NO", size = 9, comment = "운용자그룹번호", defValue = "0")
	private int ugrpNo = 0;

	@FxColumn(name = "MNG_INLO_NO", size = 9, nullable = true, comment = "관리설치위치번호", defValue = "0")
	private int mngInloNo = 0;

	@FxColumn(name = "USE_SRT_YMD", size = 8, nullable = true, comment = "사용가능일시(시작)", defValue = "20000101")
	private int useSrtYmd = 20000101;

	@FxColumn(name = "USE_END_YMD", size = 8, nullable = true, comment = "사용가능일시(종료)", defValue = "39991231")
	private int useEndYmd = 39991231;

	@FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접근가능 네트워크")
	private String accsNetwork;

	@FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접근가능 넷마스크")
	private String accsNetmask;

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

	/**
	 * 운용자 암호
	 * 
	 * @return 운용자 암호
	 */
	public String getUserPasswd() {
		return userPasswd;
	}

	/**
	 * 운용자 암호
	 * 
	 * @param userPasswd
	 *            운용자 암호
	 */
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	/**
	 * 운용자 유형
	 * 
	 * @return 운용자 유형
	 */
	public int getUserType() {
		return userType;
	}

	/**
	 * 운용자 유형
	 * 
	 * @param userType
	 *            운용자 유형
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

	/**
	 * 전화번호
	 * 
	 * @return 전화번호
	 */
	public String getTelNo() {
		return telNo;
	}

	/**
	 * 전화번호
	 * 
	 * @param telNo
	 *            전화번호
	 */
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	/**
	 * 관리설치위치번호
	 * 
	 * @return 관리설치위치번호
	 */
	public int getMngInloNo() {
		return mngInloNo;
	}

	/**
	 * 관리설치위치번호
	 * 
	 * @param mngInloNo
	 *            관리설치위치번호
	 */
	public void setMngInloNo(int mngInloNo) {
		this.mngInloNo = mngInloNo;
	}

	public int getUseSrtYmd() {
		return useSrtYmd;
	}

	public void setUseSrtYmd(int useSrtYmd) {
		this.useSrtYmd = useSrtYmd;
	}

	public int getUseEndYmd() {
		return useEndYmd;
	}

	public void setUseEndYmd(int useEndYmd) {
		this.useEndYmd = useEndYmd;
	}

	public String getAccsNetwork() {
		return accsNetwork;
	}

	public void setAccsNetwork(String accsNetwork) {
		this.accsNetwork = accsNetwork;
	}

	public String getAccsNetmask() {
		return accsNetmask;
	}

	public void setAccsNetmask(String accsNetmask) {
		this.accsNetmask = accsNetmask;
	}

}

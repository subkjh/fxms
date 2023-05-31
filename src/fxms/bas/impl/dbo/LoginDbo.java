package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

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

	@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo;

	@FxColumn(name = "USER_ID", size = 20, comment = "사용자ID")
	private String userId;

	@FxColumn(name = "USER_NAME", size = 50, comment = "사용자명")
	private String userName;

	@FxColumn(name = "USER_PWD", size = 255, comment = "사용자암호")
	private String userPwd;

	@FxColumn(name = "USER_TYPE_CD", size = 1, comment = "사용자유형코드", defValue = "U")
	private String userTypeCd = "U";

	@FxColumn(name = "UGRP_NO", size = 9, comment = "사용자그룹번호", defValue = "0")
	private int ugrpNo = 0;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private boolean useYn = true;

	@FxColumn(name = "USE_STRT_DATE", size = 8, nullable = true, comment = "사용시작일자", defValue = "20000101")
	private int useStrtDate = 20000101;

	@FxColumn(name = "USE_END_DATE", size = 8, nullable = true, comment = "사용종료일자", defValue = "39991231")
	private int useEndDate = 39991231;

	@FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접속NETWORK")
	private String accsNetwork;

	@FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접속NETMASK")
	private String accsNetmask;
	
	@FxColumn(name = "AUTHORITY", size = 10, comment = "권한")
	private String authority;

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
	 * @param userNo 운용자번호
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
	 * @param userId 로그인ID
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
	 * @param userName 운용자명
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserTypeCd() {
		return userTypeCd;
	}

	public void setUserTypeCd(String userTypeCd) {
		this.userTypeCd = userTypeCd;
	}

	public int getUgrpNo() {
		return ugrpNo;
	}

	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	public int getInloNo() {
		return inloNo;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public int getUseStrtDate() {
		return useStrtDate;
	}

	public void setUseStrtDate(int useStrtDate) {
		this.useStrtDate = useStrtDate;
	}

	public int getUseEndDate() {
		return useEndDate;
	}

	public void setUseEndDate(int useEndDate) {
		this.useEndDate = useEndDate;
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

	public boolean isUseYn() {
		return useYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	
}

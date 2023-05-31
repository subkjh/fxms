package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UR_USER", comment = "운용자테이블")
@FxIndex(name = "FX_UR_USER__PK", type = INDEX_TYPE.PK, columns = { "USER_NO" })
public class UpdateUserDbo {

	public UpdateUserDbo() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo;

	@FxColumn(name = "USER_NAME", size = 50, comment = "운용자명")
	private String userName;

	@FxColumn(name = "USER_MAIL", size = 100, nullable = true, comment = "운용자 메일")
	private String userMail;

	@FxColumn(name = "USER_TYPE", size = 1, nullable = true, comment = "운용자 유형", defValue = "1")
	private int userType = 1;

	@FxColumn(name = "USER_FLAG", size = 10, nullable = true, comment = "운용자 플래그")
	private String userFlag;

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

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

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
	 * 운용자 메일
	 * 
	 * @return 운용자 메일
	 */
	public String getUserMail() {
		return userMail;
	}

	/**
	 * 운용자 메일
	 * 
	 * @param userMail
	 *            운용자 메일
	 */
	public void setUserMail(String userMail) {
		this.userMail = userMail;
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
	 * 운용자 플래그
	 * 
	 * @return 운용자 플래그
	 */
	public String getUserFlag() {
		return userFlag;
	}

	/**
	 * 운용자 플래그
	 * 
	 * @param userFlag
	 *            운용자 플래그
	 */
	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
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

	/**
	 * 접근가능 네트워크
	 * 
	 * @return 접근가능 네트워크
	 */
	public String getAccsNetwork() {
		return accsNetwork;
	}

	/**
	 * 접근가능 네트워크
	 * 
	 * @param accsNetwork
	 *            접근가능 네트워크
	 */
	public void setAccsNetwork(String accsNetwork) {
		this.accsNetwork = accsNetwork;
	}

	/**
	 * 접근가능 넷마스크
	 * 
	 * @return 접근가능 넷마스크
	 */
	public String getAccsNetmask() {
		return accsNetmask;
	}

	/**
	 * 접근가능 넷마스크
	 * 
	 * @param accsNetmask
	 *            접근가능 넷마스크
	 */
	public void setAccsNetmask(String accsNetmask) {
		this.accsNetmask = accsNetmask;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
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

}
package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.24 10:23
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_USER", comment = "사용자테이블")
@FxIndex(name = "FX_UR_USER__PK", type = INDEX_TYPE.PK, columns = { "USER_NO" })
@FxIndex(name = "FX_UR_USER__FK_GRP", type = INDEX_TYPE.FK, columns = {
		"UGRP_NO" }, fkTable = "FX_UR_UGRP", fkColumn = "UGRP_NO")
@FxIndex(name = "FX_UR_USER__UK", type = INDEX_TYPE.UK, columns = { "USER_ID" })
public class FX_UR_USER implements Serializable {

	public FX_UR_USER() {
	}

	public static final String FX_SEQ_USERNO = "FX_SEQ_USERNO";
	@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo = -1;

	@FxColumn(name = "USER_ID", size = 20, comment = "사용자ID")
	private String userId;

	@FxColumn(name = "USER_NAME", size = 50, comment = "사용자명")
	private String userName;

	@FxColumn(name = "USER_PWD", size = 255, comment = "사용자암호")
	private String userPwd;

	@FxColumn(name = "USER_MAIL", size = 100, nullable = true, comment = "사용자메일")
	private String userMail;

	@FxColumn(name = "USER_TYPE_CD", size = 1, comment = "사용자유형코드", defValue = "U")
	private String userTypeCd = "U";

	@FxColumn(name = "USER_TEL_NO", size = 50, nullable = true, comment = "사용자전화번호")
	private String userTelNo;

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

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0", operator = COLUMN_OP.insert)
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시", operator = COLUMN_OP.insert)
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	@FxColumn(name = "AUTHORITY", size = 10, comment = "권한")
	private String authority;

	/**
	 * 사용자번호
	 * 
	 * @return 사용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 사용자번호
	 * 
	 * @param userNo 사용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	/**
	 * 사용자ID
	 * 
	 * @return 사용자ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 사용자ID
	 * 
	 * @param userId 사용자ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 사용자명
	 * 
	 * @return 사용자명
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 사용자명
	 * 
	 * @param userName 사용자명
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 사용자암호
	 * 
	 * @return 사용자암호
	 */
	public String getUserPwd() {
		return userPwd;
	}

	/**
	 * 사용자암호
	 * 
	 * @param userPwd 사용자암호
	 */
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	/**
	 * 사용자메일
	 * 
	 * @return 사용자메일
	 */
	public String getUserMail() {
		return userMail;
	}

	/**
	 * 사용자메일
	 * 
	 * @param userMail 사용자메일
	 */
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	/**
	 * 사용자유형코드
	 * 
	 * @return 사용자유형코드
	 */
	public String getUserTypeCd() {
		return userTypeCd;
	}

	/**
	 * 사용자유형코드
	 * 
	 * @param userTypeCd 사용자유형코드
	 */
	public void setUserTypeCd(String userTypeCd) {
		this.userTypeCd = userTypeCd;
	}

	/**
	 * 사용자전화번호
	 * 
	 * @return 사용자전화번호
	 */
	public String getUserTelNo() {
		return userTelNo;
	}

	/**
	 * 사용자전화번호
	 * 
	 * @param userTelNo 사용자전화번호
	 */
	public void setUserTelNo(String userTelNo) {
		this.userTelNo = userTelNo;
	}

	/**
	 * 사용자그룹번호
	 * 
	 * @return 사용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	/**
	 * 사용자그룹번호
	 * 
	 * @param ugrpNo 사용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public boolean isUseYn() {
		return useYn;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn 사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	/**
	 * 사용시작일자
	 * 
	 * @return 사용시작일자
	 */
	public int getUseStrtDate() {
		return useStrtDate;
	}

	/**
	 * 사용시작일자
	 * 
	 * @param useStrtDate 사용시작일자
	 */
	public void setUseStrtDate(int useStrtDate) {
		this.useStrtDate = useStrtDate;
	}

	/**
	 * 사용종료일자
	 * 
	 * @return 사용종료일자
	 */
	public int getUseEndDate() {
		return useEndDate;
	}

	/**
	 * 사용종료일자
	 * 
	 * @param useEndDate 사용종료일자
	 */
	public void setUseEndDate(int useEndDate) {
		this.useEndDate = useEndDate;
	}

	/**
	 * 접속NETWORK
	 * 
	 * @return 접속NETWORK
	 */
	public String getAccsNetwork() {
		return accsNetwork;
	}

	/**
	 * 접속NETWORK
	 * 
	 * @param accsNetwork 접속NETWORK
	 */
	public void setAccsNetwork(String accsNetwork) {
		this.accsNetwork = accsNetwork;
	}

	/**
	 * 접속NETMASK
	 * 
	 * @return 접속NETMASK
	 */
	public String getAccsNetmask() {
		return accsNetmask;
	}

	/**
	 * 접속NETMASK
	 * 
	 * @param accsNetmask 접속NETMASK
	 */
	public void setAccsNetmask(String accsNetmask) {
		this.accsNetmask = accsNetmask;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}

package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.06.05 15:41
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_NEW", comment = "운용자신규테이블")
@FxIndex(name = "FX_UR_NEW__KEY", type = INDEX_TYPE.KEY, columns = { "REG_DATE" })
@FxIndex(name = "FX_UR_NEW__KEY2", type = INDEX_TYPE.KEY, columns = { "USER_ID" })
public class FX_UR_NEW {

	public FX_UR_NEW() {
	}

	@FxColumn(name = "USER_ID", size = 20, comment = "로그인ID")
	private String userId;

	@FxColumn(name = "USER_NAME", size = 50, comment = "운용자명")
	private String userName;

	@FxColumn(name = "USER_MAIL", size = 100, comment = "운용자 메일")
	private String userMail;

	@FxColumn(name = "TEL_NO", size = 50, comment = "전화번호")
	private String telNo;

	@FxColumn(name = "INLO_NAME", size = 50, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "USE_SRT_YMD", size = 8, nullable = true, comment = "사용가능일자(시작)", defValue = "20000101")
	private int useSrtYmd = 20000101;

	@FxColumn(name = "USE_END_YMD", size = 8, nullable = true, comment = "사용가능일자(종료)", defValue = "39991231")
	private int useEndYmd = 39991231;

	@FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접근가능 네트워크")
	private String accsNetwork;

	@FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접근가능 넷마스크")
	private String accsNetmask;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시", defValue = "0")
	private long regDate = 0;

	@FxColumn(name = "PROCESS_USER_NO", size = 9, nullable = true, comment = "처리운용자번호", defValue = "0")
	private int processUserNo = 0;

	@FxColumn(name = "PROCESS_DATE", size = 14, nullable = true, comment = "처리일시", defValue = "0")
	private long processDate = 0;

	@FxColumn(name = "PROCESS_REASON", size = 200, nullable = true, comment = "처리사유")
	private String processReason;

	@FxColumn(name = "PROCESS_STATE", size = 1, nullable = true, comment = "처리상태", defValue = "'S'")
	private String processState = "S";

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
	 * 설치위치명
	 * 
	 * @return 설치위치명
	 */
	public String getInloName() {
		return inloName;
	}

	/**
	 * 설치위치명
	 * 
	 * @param inloName
	 *            설치위치명
	 */
	public void setInloName(String inloName) {
		this.inloName = inloName;
	}

	/**
	 * 사용가능일자(시작)
	 * 
	 * @return 사용가능일자(시작)
	 */
	public int getUseSrtYmd() {
		return useSrtYmd;
	}

	/**
	 * 사용가능일자(시작)
	 * 
	 * @param useSrtYmd
	 *            사용가능일자(시작)
	 */
	public void setUseSrtYmd(int useSrtYmd) {
		this.useSrtYmd = useSrtYmd;
	}

	/**
	 * 사용가능일자(종료)
	 * 
	 * @return 사용가능일자(종료)
	 */
	public int getUseEndYmd() {
		return useEndYmd;
	}

	/**
	 * 사용가능일자(종료)
	 * 
	 * @param useEndYmd
	 *            사용가능일자(종료)
	 */
	public void setUseEndYmd(int useEndYmd) {
		this.useEndYmd = useEndYmd;
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
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 처리운용자번호
	 * 
	 * @return 처리운용자번호
	 */
	public int getProcessUserNo() {
		return processUserNo;
	}

	/**
	 * 처리운용자번호
	 * 
	 * @param processUserNo
	 *            처리운용자번호
	 */
	public void setProcessUserNo(int processUserNo) {
		this.processUserNo = processUserNo;
	}

	/**
	 * 처리일시
	 * 
	 * @return 처리일시
	 */
	public long getProcessDate() {
		return processDate;
	}

	/**
	 * 처리일시
	 * 
	 * @param processDate
	 *            처리일시
	 */
	public void setProcessDate(long processDate) {
		this.processDate = processDate;
	}

	/**
	 * 처리사유
	 * 
	 * @return 처리사유
	 */
	public String getProcessReason() {
		return processReason;
	}

	/**
	 * 처리사유
	 * 
	 * @param processReason
	 *            처리사유
	 */
	public void setProcessReason(String processReason) {
		this.processReason = processReason;
	}

	/**
	 * 처리상태
	 * 
	 * @return 처리상태
	 */
	public String getProcessState() {
		return processState;
	}

	/**
	 * 처리상태
	 * 
	 * @param processState
	 *            처리상태
	 */
	public void setProcessState(String processState) {
		this.processState = processState;
	}
}

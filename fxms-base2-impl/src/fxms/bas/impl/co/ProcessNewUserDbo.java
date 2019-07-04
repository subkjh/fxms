package fxms.bas.impl.co;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_UR_NEW", comment = "운용자신규테이블")
@FxIndex(name = "FX_UR_NEW__PK", type = INDEX_TYPE.PK, columns = { "USER_ID", "REG_DATE" })

public class ProcessNewUserDbo {

	@FxColumn(name = "USER_ID", size = 20, comment = "로그인ID")
	private String userId;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시", defValue = "0")
	private long regDate = 0;

	@FxColumn(name = "PROCESS_USER_NO", size = 9, nullable = true, comment = "처리운용자번호", defValue = "0")
	private int processUserNo = 0;

	@FxColumn(name = "PROCESS_DATE", size = 14, nullable = true, comment = "처리일시", defValue = "0")
	private long processDate = 0;

	@FxColumn(name = "PROCESS_STATE", size = 1, nullable = true, comment = "처리상태", defValue = "S")
	private String processState = "S";

	@FxColumn(name = "USE_SRT_YMD", size = 8, nullable = true, comment = "사용가능일자(시작)", defValue = "20000101")
	private int useSrtYmd = 20000101;

	@FxColumn(name = "USE_END_YMD", size = 8, nullable = true, comment = "사용가능일자(종료)", defValue = "39991231")
	private int useEndYmd = 39991231;

	@FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접근가능 네트워크")
	private String accsNetwork;

	@FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접근가능 넷마스크")
	private String accsNetmask;

	@FxColumn(name = "PROCESS_REASON", size = 200, nullable = true, comment = "처리사유")
	private String processReason;

	public String getAccsNetmask() {
		return accsNetmask;
	}

	public String getAccsNetwork() {
		return accsNetwork;
	}

	public long getProcessDate() {
		return processDate;
	}

	public String getProcessReason() {
		return processReason;
	}

	public String getProcessState() {
		return processState;
	}

	public int getProcessUserNo() {
		return processUserNo;
	}

	public long getRegDate() {
		return regDate;
	}

	public int getUseEndYmd() {
		return useEndYmd;
	}

	public String getUserId() {
		return userId;
	}

	public int getUseSrtYmd() {
		return useSrtYmd;
	}

	public void setAccsNetmask(String accsNetmask) {
		this.accsNetmask = accsNetmask;
	}

	public void setAccsNetwork(String accsNetwork) {
		this.accsNetwork = accsNetwork;
	}

	public void setProcessDate(long processDate) {
		this.processDate = processDate;
	}

	public void setProcessReason(String processReason) {
		this.processReason = processReason;
	}

	public void setProcessState(String processState) {
		this.processState = processState;
	}

	public void setProcessUserNo(int processUserNo) {
		this.processUserNo = processUserNo;
	}

	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	public void setUseEndYmd(int useEndYmd) {
		this.useEndYmd = useEndYmd;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUseSrtYmd(int useSrtYmd) {
		this.useSrtYmd = useSrtYmd;
	}

}

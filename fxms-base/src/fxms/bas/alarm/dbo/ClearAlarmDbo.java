package fxms.bas.alarm.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_AL_HST", comment = "경보(이력)테이블")
@FxIndex(name = "FX_AL_HST__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class ClearAlarmDbo implements AlarmTag {

	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호", sequence = "FX_SEQ_ALARMNO")
	private long alarmNo;

	@FxColumn(name = "CLEAR_DATE", size = 19, nullable = true, comment = "경보해제일시")
	private long clearDate;

	@FxColumn(name = "CLEAR_USER_NO", size = 9, nullable = true, comment = "경보해제운용자번호")
	private int clearUserNo;

	@FxColumn(name = "CLEAR_RSN_NO", size = 9, nullable = true, comment = "경보해제원인번호")
	private int clearRsnNo;

	@FxColumn(name = "CLEAR_RSN_NAME", size = 100, nullable = true, comment = "경보해제원인명")
	private String clearRsnName;

	@FxColumn(name = "CLEAR_MEMO", size = 200, nullable = true, comment = "경보해제메모")
	private String clearMemo;

	@FxColumn(name = "CLEAR_YN", size = 1, comment = "경보해제여부", defValue = "N")
	private boolean clearYn = true;

	public ClearAlarmDbo() {

	}

	public ClearAlarmDbo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public long getClearDate() {
		return clearDate;
	}

	public String getClearMemo() {
		return clearMemo;
	}

	public String getClearRsnName() {
		return clearRsnName;
	}

	public int getClearRsnNo() {
		return clearRsnNo;
	}

	public int getClearUserNo() {
		return clearUserNo;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public void setClearDate(long clearDate) {
		this.clearDate = clearDate;
	}

	public void setClearMemo(String clearMemo) {
		this.clearMemo = clearMemo;
	}

	public void setClearRsnName(String clearRsnName) {
		this.clearRsnName = clearRsnName;
	}

	public void setClearRsnNo(int clearRsnNo) {
		this.clearRsnNo = clearRsnNo;
	}

	public void setClearUserNo(int clearUserNo) {
		this.clearUserNo = clearUserNo;
	}

}

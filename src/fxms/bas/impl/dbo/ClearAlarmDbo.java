package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_AL_ALARM_HST", comment = "경보알람이력테이블")
@FxIndex(name = "FX_AL_HST__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class ClearAlarmDbo {

	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호", sequence = "FX_SEQ_ALARMNO")
	private long alarmNo;

	@FxColumn(name = "RLSE_YN", size = 1, comment = "해제여부", defValue = "N")
	private boolean rlseYn = true;

	@FxColumn(name = "RLSE_DTM", size = 14, nullable = true, comment = "해제일시")
	private long rlseDtm;

	@FxColumn(name = "RLSE_USER_NO", size = 9, nullable = true, comment = "해제사용자번호")
	private int rlseUserNo;

	@FxColumn(name = "ALARM_RLSE_RSN_CD", size = 10, nullable = true, comment = "알람해제원인코드")
	private String alarmRlseRsnCd;

	@FxColumn(name = "ALARM_RLSE_RSN_NAME", size = 100, nullable = true, comment = "알람해제원인명")
	private String alarmRlseRsnName;

	@FxColumn(name = "RLSE_MEMO", size = 200, nullable = true, comment = "해제메모")
	private String rlseMemo;

	public ClearAlarmDbo() {

	}

	public ClearAlarmDbo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public boolean isRlseYn() {
		return rlseYn;
	}

	public void setRlseYn(boolean rlseYn) {
		this.rlseYn = rlseYn;
	}

	public long getRlseDtm() {
		return rlseDtm;
	}

	public void setRlseDtm(long rlseDtm) {
		this.rlseDtm = rlseDtm;
	}

	public int getRlseUserNo() {
		return rlseUserNo;
	}

	public void setRlseUserNo(int rlseUserNo) {
		this.rlseUserNo = rlseUserNo;
	}

	public String getAlarmRlseRsnCd() {
		return alarmRlseRsnCd;
	}

	public void setAlarmRlseRsnCd(String alarmRlseRsnCd) {
		this.alarmRlseRsnCd = alarmRlseRsnCd;
	}

	public String getAlarmRlseRsnName() {
		return alarmRlseRsnName;
	}

	public void setAlarmRlseRsnName(String alarmRlseRsnName) {
		this.alarmRlseRsnName = alarmRlseRsnName;
	}

	public String getRlseMemo() {
		return rlseMemo;
	}

	public void setRlseMemo(String rlseMemo) {
		this.rlseMemo = rlseMemo;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

}

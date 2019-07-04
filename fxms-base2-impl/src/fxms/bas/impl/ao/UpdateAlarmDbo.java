package fxms.bas.impl.ao;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;
import fxms.bas.ao.vo.IsAlarm;

@FxTable(name = "FX_AL_HST", comment = "경보(이력)테이블")
@FxIndex(name = "FX_AL_HST__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class UpdateAlarmDbo implements IsAlarm {

	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호")
	private long alarmNo;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "변경일시")
	private long chgDate;

	@FxColumn(name = "ALARM_MSG", size = 1000, nullable = true, comment = "경보메세지")
	private String alarmMsg;

	@FxColumn(name = "ALARM_LEVEL", size = 1, nullable = true, comment = "경보등급")
	private int alarmLevel;

	public UpdateAlarmDbo() {

	}

	public UpdateAlarmDbo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public String getAlarmMsg() {
		return alarmMsg;
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public long getChgDate() {
		return chgDate;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

}

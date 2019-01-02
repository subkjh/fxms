package fxms.bas.alarm.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_AL_CUR", comment = "경보(이력)테이블")
@FxIndex(name = "FX_AL_CUR__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class DeleteAlarmDbo implements AlarmTag {

	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호")
	private long alarmNo;

	public DeleteAlarmDbo() {

	}

	public DeleteAlarmDbo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

}

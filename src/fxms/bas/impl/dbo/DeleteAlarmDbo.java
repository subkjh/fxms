package fxms.bas.impl.dbo;

import fxms.bas.vo.Alarmable;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_AL_ALARM_CUR", comment = "경보알람현재테이블")
@FxIndex(name = "FX_AL_ALARM_CUR__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class DeleteAlarmDbo implements Alarmable {

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

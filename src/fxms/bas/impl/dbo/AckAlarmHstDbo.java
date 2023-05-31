package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_AL_ALARM_HST", comment = "경보알람이력테이블")
@FxIndex(name = "FX_AL_ALARM_HST__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class AckAlarmHstDbo {

	@FxColumn(name = "ALARM_NO", size = 19, comment = "알람번호")
	private long alarmNo;

	@FxColumn(name = "ACK_DTM", size = 14, nullable = true, comment = "확인일시")
	private long ackDtm;

	@FxColumn(name = "ACK_USER_NO", size = 9, nullable = true, comment = "확인사용자번호")
	private int ackUserNo;

	public long getAlarmNo() {
		return alarmNo;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public long getAckDtm() {
		return ackDtm;
	}

	public void setAckDtm(long ackDtm) {
		this.ackDtm = ackDtm;
	}

	public int getAckUserNo() {
		return ackUserNo;
	}

	public void setAckUserNo(int ackUserNo) {
		this.ackUserNo = ackUserNo;
	}

}

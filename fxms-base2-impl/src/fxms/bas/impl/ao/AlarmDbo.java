package fxms.bas.impl.ao;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.FxEventImpl;

@FxTable(name = "FX_AL_CUR", comment = "경보(현재)테이블")
@FxIndex(name = "FX_AL_CUR__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class AlarmDbo extends FxEventImpl implements Alarm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5625836235625053347L;

	public static final String FX_SEQ_ALARMNO = "FX_SEQ_ALARMNO";
	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호", sequence = "FX_SEQ_ALARMNO")
	private long alarmNo;

	@FxColumn(name = "ALARM_KEY", size = 100, nullable = true, comment = "경보구분고유값")
	private String alarmKey;

	@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드번호")
	private int alcdNo;

	@FxColumn(name = "MO_NO", size = 19, nullable = true, comment = "MO번호")
	private long moNo;

	@FxColumn(name = "MO_NAME", size = 200, comment = "MO명")
	private String moName;

	@FxColumn(name = "MO_INSTANCE", size = 50, nullable = true, comment = "MO인스턴스")
	private String moInstance;

	@FxColumn(name = "UPPER_MO_NO", size = 19, nullable = true, comment = "상위MO번호")
	private long upperMoNo;

	@FxColumn(name = "UPPER_MO_NAME", size = 200, comment = "상위MO명")
	private String upperMoName;

	@FxColumn(name = "ALARM_LEVEL", size = 1, nullable = true, comment = "경보등급")
	private int alarmLevel;

	@FxColumn(name = "OCU_DATE", size = 19, nullable = true, comment = "발생일시")
	private long ocuDate;

	@FxColumn(name = "ACK_DATE", size = 19, nullable = true, comment = "확인일시")
	private long ackDate;

	@FxColumn(name = "ACK_USER_NO", size = 9, nullable = true, comment = "확인운용자번호")
	private int ackUserNo;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호")
	private int inloNo;

	public AlarmDbo() {

	}

	public long getAckDate() {
		return ackDate;
	}

	public int getAckUserNo() {
		return ackUserNo;
	}

	public String getAlarmKey() {
		return alarmKey;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public int getInloNo() {
		return inloNo;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public String getMoName() {
		return moName;
	}

	@Override
	public long getMoNo() {
		return moNo;
	}

	public long getOcuDate() {
		return ocuDate;
	}

	public String getUpperMoName() {
		return upperMoName;
	}

	@Override
	public long getUpperMoNo() {
		return upperMoNo;
	}

	public boolean isCleared() {
		return getStatus() == FxEvent.STATUS.deleted;
	}

	public void setAckDate(long ackDate) {
		this.ackDate = ackDate;
	}

	public void setAckUserNo(int ackUserNo) {
		this.ackUserNo = ackUserNo;
	}

	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	public void setMoName(String moName) {
		this.moName = moName;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setOcuDate(long ocuDate) {
		this.ocuDate = ocuDate;
	}

	public void setUpperMoName(String upperMoName) {
		this.upperMoName = upperMoName;
	}

	public void setUpperMoNo(long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Alarm[");

		if (getAlarmKey() != null) {
			sb.append("key(" + getAlarmKey() + ")");
		} else {
			if (getAlarmNo() > 0)
				sb.append("no(" + getAlarmNo() + ")");
			sb.append("code(" + getAlcdNo() + ")");
			sb.append("level(" + getAlarmLevel() + ")");
			if (getMoNo() > 0)
				sb.append("mo-no(" + getMoNo() + ")");
			if (getUpperMoNo() > 0)
				sb.append("upper-mo-no" + getUpperMoNo() + ")");
		}

		sb.append(isCleared() ? " released" : " occured");

		sb.append("]");

		return sb.toString();
	}
}

package fxms.nms.co.snmp.trap.vo;

import java.io.Serializable;

import fxms.bas.co.noti.FxEventImpl;

/**
 * TRAP 경보 조건 매핑 내역
 * 
 * @author subkjh
 * 
 */
public class TrapThrAttr extends FxEventImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4177076712110221617L;

	/** 경보 코드 */
	private int alarmCode;

	/**
	 * 경보코드번호
	 * 
	 * @return 경보코드번호
	 */
	public int getAlarmCode() {
		return alarmCode;
	}

	/**
	 * 경보코드번호
	 * 
	 * @param alarmCode
	 *            경보코드번호
	 */
	public void setAlarmCode(int alarmCode) {
		this.alarmCode = alarmCode;
	}

	@Override
	public String toString() {
		return alarmCode + "|" + super.toString();
	}
}

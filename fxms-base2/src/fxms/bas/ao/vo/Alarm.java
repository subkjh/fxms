package fxms.bas.ao.vo;

import fxms.bas.co.noti.FxEvent;
import fxms.bas.mo.property.MoOwnership;
import fxms.bas.mo.property.Moable;

/**
 * 
 * @author subkjh(김종훈)
 *
 */
public interface Alarm extends FxEvent, Moable, MoOwnership, IsAlarm {

	/**
	 * 
	 * @return 알람 고유 키
	 */
	public String getAlarmKey();

	public int getAlcdNo();

	public int getAlarmLevel();

	public long getOcuDate();

	public String getMoInstance();

	public void setAlarmLevel(int alarmLevel);

	public void setAlarmKey(String alarmKey);

	public void setAlcdNo(int alcdNo);

	public void setMoInstance(String moInstance);

	public void setMoNo(long moNo);

	public void setUpperMoNo(long upperMoNo);

	public boolean isCleared();

}

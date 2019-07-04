package fxms.bas.ao.vo;

public interface ClearAlarm extends IsAlarm {

	public long getClearDate();

	public String getClearMemo();

	public String getClearRsnName();

	public int getClearRsnNo();

	public int getClearUserNo();

	public void setAlarmNo(long alarmNo);

	public void setClearDate(long clearDate);

	public void setClearMemo(String clearMemo);

	public void setClearRsnName(String clearRsnName);

	public void setClearRsnNo(int clearRsnNo);

	public void setClearUserNo(int clearUserNo);
}

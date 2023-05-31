package fxms.bas.impl.dbo;

public class UpdateAlarmPara {

	private long alarmNo;

	private Long chgDtm;

	private String alarmMsg;

	private int alarmLevel;

	private Integer occurCnt = 0;

	public UpdateAlarmPara() {

	}

	public UpdateAlarmPara(long alarmNo) {
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

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public Integer getOccurCnt() {
		return occurCnt;
	}

	public void setOccurCnt(Integer occurCnt) {
		this.occurCnt = occurCnt;
	}

	public Long getChgDtm() {
		return chgDtm;
	}

	public void setChgDtm(Long chgDtm) {
		this.chgDtm = chgDtm;
	}

}

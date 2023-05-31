package fxms.bas.vo;

/**
 * 알람조건 매칭된 정보
 * 
 * @author subkjh
 *
 */
public class AlarmCfgMemMatched {

	private int alcdNo;

	private double cmprVal;

	private Number psVal;

	private int alarmLevel;

	private String fpactCd;

	private int repeatTimes = 1;

	public AlarmCfgMemMatched(AlarmCfgMem mem, int alarmLevel, double cmprVal, Number psVal) {
		this.alcdNo = mem.getAlcdNo();
		this.fpactCd = mem.getFpactCd();
		this.repeatTimes = mem.getRepeatTimes();

		this.alarmLevel = alarmLevel;
		this.cmprVal = cmprVal;
		this.psVal = psVal;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	/**
	 * 
	 * @return 비교값
	 */
	public double getCmprVal() {
		return cmprVal;
	}

	public String getFpactCd() {
		return fpactCd;
	}

	/**
	 * 
	 * @return 수집되어 계산된 값
	 */
	public Number getPsVal() {
		return psVal;
	}

	public int getRepeatTimes() {
		return repeatTimes;
	}
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("alcdNo(" + getAlcdNo() + ")");
		sb.append("level(").append(alarmLevel).append(")");
		sb.append("value(").append(cmprVal).append(":").append(psVal).append(")");

		return sb.toString();
	}


}

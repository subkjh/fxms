package fxms.bas.vo;

/**
 * 알람조건 매칭된 정보
 * 
 * @author subkjh
 *
 */
public class AlarmCfgMemMatched {

	private final int alcdNo;
	private final Number cmprVal;
	private final Number psVal;
	private final int alarmLevel;
	private final String fpactCd;
	private final int repeatTimes;

	public AlarmCfgMemMatched(AlarmCfgMem mem, int alarmLevel, Number cmprVal, Number psVal) {

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
	public Number getCmprVal() {
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

package fxms.bas.vo;

/**
 * 해제 알람
 * 
 * @author subkjh
 *
 */
public class ClearAlarm implements Alarmable {

	// 알람번호
	private long alarmNo;

// 해제여부
	private boolean rlseYn = false;

// 해제일시
	private long rlseDtm;

// 해제사용자번호
	private int rlseUserNo;

// 알람해제원인코드
	private String alarmRlseRsnCd;

// 알람해제원인명
	private String alarmRlseRsnName;

// 해제메모
	private String rlseMemo;

	/**
	 * 알람번호
	 * 
	 * @return 알람번호
	 */
	public long getAlarmNo() {
		return alarmNo;
	}

	/**
	 * 알람번호
	 * 
	 * @param alarmNo 알람번호
	 */
	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	/**
	 * 해제여부
	 * 
	 * @return 해제여부
	 */
	public boolean isRlseYn() {
		return rlseYn;
	}

	/**
	 * 해제여부
	 * 
	 * @param rlseYn 해제여부
	 */
	public void setRlseYn(boolean rlseYn) {
		this.rlseYn = rlseYn;
	}

	/**
	 * 해제일시
	 * 
	 * @return 해제일시
	 */
	public long getRlseDtm() {
		return rlseDtm;
	}

	/**
	 * 해제일시
	 * 
	 * @param rlseDtm 해제일시
	 */
	public void setRlseDtm(long rlseDtm) {
		this.rlseDtm = rlseDtm;
	}

	/**
	 * 해제사용자번호
	 * 
	 * @return 해제사용자번호
	 */
	public int getRlseUserNo() {
		return rlseUserNo;
	}

	/**
	 * 해제사용자번호
	 * 
	 * @param rlseUserNo 해제사용자번호
	 */
	public void setRlseUserNo(int rlseUserNo) {
		this.rlseUserNo = rlseUserNo;
	}

	/**
	 * 알람해제원인코드
	 * 
	 * @return 알람해제원인코드
	 */
	public String getAlarmRlseRsnCd() {
		return alarmRlseRsnCd;
	}

	/**
	 * 알람해제원인코드
	 * 
	 * @param alarmRlseRsnCd 알람해제원인코드
	 */
	public void setAlarmRlseRsnCd(String alarmRlseRsnCd) {
		this.alarmRlseRsnCd = alarmRlseRsnCd;
	}

	/**
	 * 알람해제원인명
	 * 
	 * @return 알람해제원인명
	 */
	public String getAlarmRlseRsnName() {
		return alarmRlseRsnName;
	}

	/**
	 * 알람해제원인명
	 * 
	 * @param alarmRlseRsnName 알람해제원인명
	 */
	public void setAlarmRlseRsnName(String alarmRlseRsnName) {
		this.alarmRlseRsnName = alarmRlseRsnName;
	}

	/**
	 * 해제메모
	 * 
	 * @return 해제메모
	 */
	public String getRlseMemo() {
		return rlseMemo;
	}

	/**
	 * 해제메모
	 * 
	 * @param rlseMemo 해제메모
	 */
	public void setRlseMemo(String rlseMemo) {
		this.rlseMemo = rlseMemo;
	}
}

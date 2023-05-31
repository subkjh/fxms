package fxms.bas.signal;

/**
 * 입력된 MO_NO에 대한 동기화하라는 요청
 * 
 * @author subkjh
 * @since 2012.12.06
 */
public class SyncSignal extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923464746989250518L;

	private long moNo;
	private String moClass;
	private String reason;
	private String ipAddress;

	public SyncSignal() {
		moNo = -1;
	}

	/**
	 * 
	 * @param moNo
	 *            MO관리번호
	 * @param ipAddress
	 *            IP주소
	 * @param reason
	 *            이유
	 */
	public SyncSignal(long moNo, String ipAddress, String reason, String moClass) {
		this.moNo = moNo;
		this.ipAddress = ipAddress;
		this.reason = reason;
		this.moClass = moClass;

		setTarget(String.valueOf(moNo));
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getMoClass() {
		return moClass;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getReason() {
		return reason;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return moNo + "|" + reason;
	}

}

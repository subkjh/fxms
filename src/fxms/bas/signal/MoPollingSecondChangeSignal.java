package fxms.bas.signal;

public class MoPollingSecondChangeSignal extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -787434050757844159L;

	private long moNo;
	private int secPolling;

	public MoPollingSecondChangeSignal() {

	}

	public MoPollingSecondChangeSignal(long moNo, int secPolling) {
		this.moNo = moNo;
		this.secPolling = secPolling;
		setTarget(String.valueOf(moNo));
	}

	public long getMoNo() {
		return moNo;
	}

	public int getSecPolling() {
		return secPolling;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setSecPolling(int secPolling) {
		this.secPolling = secPolling;
	}

	@Override
	public String toString() {
		return super.toString() + "=" + moNo + ",sec=" + secPolling;
	}
}

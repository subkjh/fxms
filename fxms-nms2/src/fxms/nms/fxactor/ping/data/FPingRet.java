package fxms.nms.fxactor.ping.data;

public class FPingRet {
	private int countLoss;
	private int countTotal;
	/** IP 주소 */
	private String ipAddress;
	/** 응답시간 (ms) micron seconds */
	private float rttMs = -1;

	public FPingRet(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void count(boolean loss) {
		countTotal++;
		if (loss)
			countLoss++;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FPingRet) {
			FPingRet target = (FPingRet) obj;
			if (target.ipAddress != null && ipAddress != null && target.ipAddress.equals(ipAddress))
				return true;
		}
		return super.equals(obj);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getLossRate() {
		return countLoss * 100 / countTotal;
	}

	public float getRttMs() {
		return rttMs;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	public boolean isOnline() {
		return countTotal - countLoss > 0;
	}

	public void setRtt(float rtt) {
		if (rtt > rttMs)
			rttMs = rtt;
	}
}

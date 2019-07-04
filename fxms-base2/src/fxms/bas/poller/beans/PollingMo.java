package fxms.bas.poller.beans;

import fxms.bas.api.FxApi;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoPollable;

/**
 * Polling Object MO
 * 
 * @author subkjh
 * 
 */
public class PollingMo implements PollingItem {

	/** 수집 주기 고정 여부 */
	private boolean fixedSecPolling = false;

	private int pollingCycle = 0;

	private Mo mo;

	/** 정상처리수 */
	private int countOk;

	/** 폴링 요청 횟수 */
	private int countReq;

	private long mstimeNext;

	private Status status;

	private MoState moState = MoState.init;

	/** 전체 처리 시간 */
	private long totTime;

	public PollingMo(Mo mo) {

		this.mo = mo;

		if (mo instanceof MoPollable) {
			pollingCycle = ((MoPollable) mo).getPollingCycle();
		}

	}

	public void addReq() {
		countReq++;
	}

	public void addTime(long stime) {
		totTime += stime;
		countOk++;
	}

	public String getCount() {
		StringBuffer sb = new StringBuffer();

		sb.append("count=");
		sb.append(countOk);
		sb.append("/");
		sb.append(countReq);
		if (countOk > 0) {
			sb.append(",avg-time=");
			sb.append(totTime / countOk);
		}
		sb.append(",");
		sb.append(moState);
		sb.append(",");
		sb.append(status);
		sb.append(",next=");
		sb.append(FxApi.getTime(mstimeNext));

		return sb.toString();
	}

	public int getCountReq() {
		return countReq;
	}

	public Mo getMo() {
		return mo;
	}

	public long getMoNo() {
		return mo.getMoNo();
	}

	public MoState getMoState() {
		return moState;
	}

	public long getMstimePoll() {
		return mstimeNext;
	}

	public int getPollingCycle() {
		return pollingCycle;
	}

	public Status getStatus() {
		return status;
	}

	public boolean isFixedSecPolling() {
		return fixedSecPolling;
	}

	public boolean onTime(long mstime) {
		return mstimeNext <= mstime;
	}

	public void setFixedSecPolling(boolean fixedSecPolling) {
		this.fixedSecPolling = fixedSecPolling;
	}

	public void setMo(Mo mo) {
		this.mo = mo;
	}

	public void setMoState(MoState moState) {
		this.moState = moState;
	}

	public void setNext(long mstime) {

		int cycle = pollingCycle == 0 ? 60 : pollingCycle;

		mstimeNext = mstime / (cycle * 1000L);
		mstimeNext *= (cycle * 1000L);
		mstimeNext += (cycle * 1000L);
	}

	public void setPollingCycle(int pollingCycle) {
		this.pollingCycle = pollingCycle;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return mo + " " + status;
	}
}

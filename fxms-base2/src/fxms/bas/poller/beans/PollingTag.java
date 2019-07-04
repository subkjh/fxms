package fxms.bas.poller.beans;

import fxms.bas.api.FxApi;

public class PollingTag  implements PollingItem {

	private int size;
	private long mstimePoll;
	private int secPolling;
	private long index;
	private long mstimeStart;
	private long mstimeEnd;

	public PollingTag(int secPolling) {
		this.secPolling = secPolling;
	}

	public long getMstimeEnd() {
		return mstimeEnd;
	}

	public long getMstimePoll() {
		return mstimePoll;
	}

	public long getMstimeStart() {
		return mstimeStart;
	}

	public int getPollingCycle() {
		return secPolling;
	}

	public int getSize() {
		return size;
	}

	public Status getState() {
		return Status.InQueue;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public void setMstime(long mstime) {
		mstimePoll = mstime / (secPolling * 1000L);
		mstimePoll *= (secPolling * 1000L);
	}

	public void setMstimeEnd(long mstimeEnd) {
		this.mstimeEnd = mstimeEnd;
	}

	public void setMstimePoll(long mstimePoll) {
		this.mstimePoll = mstimePoll;
	}

	public void setMstimeStart(long mstimeStart) {
		this.mstimeStart = mstimeStart;
	}

	public void setSecPolling(int secPolling) {
		this.secPolling = secPolling;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setState(long mstime, Status state) {
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CYCLE(" + secPolling + ")");
		sb.append("SIZE(" + size + ")");
		sb.append("INDEX(" + index + ")");

		sb.append("TIME(POLLING(" + FxApi.getDate(mstimePoll) + ")");
		if (mstimeStart > 0) sb.append("START(" + FxApi.getDate(mstimeStart) + ")");
		if (mstimeEnd > 0) sb.append("END(" + FxApi.getDate(mstimeEnd) + ")");
		sb.append(")");

		return sb.toString();
	}



}

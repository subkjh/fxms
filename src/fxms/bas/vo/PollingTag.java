package fxms.bas.vo;

import subkjh.bas.co.utils.DateUtil;

/**
 * 
 * @author subkjh
 *
 */
public class PollingTag implements PollItem {

	private int size;
	private long mstimePoll;
	private int secPoll;
	private long index;
	private long mstimeStart;
	private long mstimeEnd;

	public PollingTag(int secPoll) {
		this.secPoll = secPoll;
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

	@Override
	public int getPollCycle() {
		return secPoll;
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
		mstimePoll = mstime / (secPoll * 1000L);
		mstimePoll *= (secPoll * 1000L);
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

	public void setSecPoll(int secPoll) {
		this.secPoll = secPoll;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setState(long mstime, Status state) {
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CYCLE(" + secPoll + ")");
		sb.append("SIZE(" + size + ")");
		sb.append("INDEX(" + index + ")");

		sb.append("TIME(POLLING(" + DateUtil.getDtm(mstimePoll) + ")");
		if (mstimeStart > 0)
			sb.append("START(" + DateUtil.getDtm(mstimeStart) + ")");
		if (mstimeEnd > 0)
			sb.append("END(" + DateUtil.getDtm(mstimeEnd) + ")");
		sb.append(")");

		return sb.toString();
	}

}

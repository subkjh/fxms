package fxms.bas.vo;

import fxms.bas.mo.Mo;
import subkjh.bas.co.utils.DateUtil;

/**
 * Polling Object MO
 * 
 * @author subkjh
 * 
 */
public class PollMo implements PollItem {

	/** 수집 주기 고정 여부 */
	private boolean fixedSecPolling = false;

	private int pollCycle;

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

	public PollMo(Mo mo) {
		this.mo = mo;
		this.pollCycle = 60;
	}
	
	public PollMo(Mo mo, int pollCycle) {
		this.mo = mo;
		this.pollCycle = pollCycle;
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
		sb.append(DateUtil.getTime(mstimeNext));

		return sb.toString();
	}

	public int getCountReq() {
		return countReq;
	}

	public Mo getMo() {
		return mo;
	}

	public long getMoNo() {
		return mo == null ? -1 : mo.getMoNo();
	}

	public MoState getMoState() {
		return moState;
	}

	public long getMstimePoll() {
		return mstimeNext;
	}

	public int getPollCycle() {
		return pollCycle;
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

	public void setMoState(MoState moState) {
		this.moState = moState;
	}

	public void setNext(long mstime) {

		int cycle = pollCycle == 0 ? 60 : pollCycle;

		mstimeNext = mstime / (cycle * 1000L);
		mstimeNext *= (cycle * 1000L);
		mstimeNext += (cycle * 1000L);
	}

	public void setPollCycle(int pollCycle) {
		this.pollCycle = pollCycle;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return mo + " " + status;
	}

	public void setMo(Mo mo) {
		this.mo = mo;
	}

	
}

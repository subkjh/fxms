package fxms.bas.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import fxms.bas.poller.Poller;
import subkjh.bas.co.utils.DateUtil;

/**
 * 수집 정보
 * 
 * @author subkjh
 * 
 */
public class PollerStatus {

	private int secPolling;

	/** 처리 스레드 목록 */
	private final List<Poller> pollerList;

	/** 처리횟수 */
	private long count;

	/** 사용 큐 */
	private LinkedBlockingQueue<PollItem> queue;

	/** 마지막 처리 일시 */
	private long mstimeLast;

	private long mstimeStart;

	public PollerStatus(int secPolling, LinkedBlockingQueue<PollItem> queue) {
		this.mstimeStart = System.currentTimeMillis();
		this.secPolling = secPolling;
		this.queue = queue;
		this.pollerList = new ArrayList<Poller>();
	}

	public List<Poller> getPollerList() {
		return this.pollerList;
	}

	public void addCount() {
		count++;
	}

	public long getCount() {
		return count;
	}

	public long getMstimeLast() {
		return mstimeLast;
	}

	public LinkedBlockingQueue<PollItem> getQueue() {
		return queue;
	}

	public int getSecPolling() {
		return secPolling;
	}

	public synchronized void setMstimeLast(long mstimeLast) {
		this.mstimeLast = mstimeLast;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("size-in-queue=" + queue.size());
		sb.append(",hstime-start=" + DateUtil.getDtm(mstimeStart));
		sb.append(",hstime-last=" + DateUtil.getDtm(mstimeLast));
		sb.append(",polling-count=" + count);
		sb.append(",count-to-expect=" + ((System.currentTimeMillis() - mstimeStart) / (secPolling * 1000)));
		sb.append(",thread-count=" + getPollerList().size());

		return sb.toString();
	}
}

package fxms.bas.poller.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import fxms.bas.api.FxApi;
import fxms.bas.poller.Poller;

/**
 * 수집 정보
 * 
 * @author subkjh
 * 
 */
public class PollerStatus {

	private int secPolling;

	/** 처리 스레드 목록 */
	private List<Poller<?>> thList;

	/** 처리횟수 */
	private long count;

	/** 사용 큐 */
	private LinkedBlockingQueue<PollingItem> queue;

	/** 마지막 처리 일시 */
	private long mstimeLast;

	private long mstimeStart;

	public PollerStatus(int secPolling, LinkedBlockingQueue<PollingItem> queue) {
		this.mstimeStart = System.currentTimeMillis();
		this.secPolling = secPolling;
		this.queue = queue;
	}

	public synchronized List<Poller<?>> getPollerList() {
		if (thList == null) {
			thList = new ArrayList<Poller<?>>();
		}
		return thList;
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

	public LinkedBlockingQueue<PollingItem> getQueue() {
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
		sb.append(",hstime-start=" + FxApi.getDate(mstimeStart));
		sb.append(",hstime-last=" + FxApi.getDate(mstimeLast));
		sb.append(",polling-count=" + count);
		sb.append(",count-to-expect=" + ((System.currentTimeMillis() - mstimeStart) / (secPolling * 1000)));
		sb.append(",thread-count=" + getPollerList().size());

		return sb.toString();
	}
}

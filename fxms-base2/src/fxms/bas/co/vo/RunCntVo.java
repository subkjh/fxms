package fxms.bas.co.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 실행 통계를 가지는 클래스
 * 
 * @author subkjh(김종훈)
 *
 */
public class RunCntVo {

	private static final SimpleDateFormat DDHHMM = new SimpleDateFormat("dd.HH:mm:ss");

	public static String getDDHHMMSS(long mstime) {
		return DDHHMM.format(new Date(mstime));
	}

	/** 처리 횟수 */
	private long countRun = 0;
	/** 처리하지 않은 횟수 */
	private long countSkip = 0;
	private long mstimeAvg;
	private long mstimeLast;
	private long mstimeMax = -1;
	private long mstimeMin = -1;

	private ThreadStatus threadStatus;

	public RunCntVo() {
		mstimeLast = System.currentTimeMillis();
		threadStatus = ThreadStatus.Ready;
	}

	public void addCountSkip() {
		countSkip++;
	}

	/**
	 * 처리 시간을 더합니다.
	 * 
	 * @param mstime
	 */
	public synchronized void addSpentTime(long mstime) {

		mstimeMin = mstimeMin == -1 ? mstime : Math.min(mstimeMin, mstime);
		mstimeMax = mstimeMax == -1 ? mstime : Math.max(mstimeMax, mstime);
		mstimeAvg = (mstimeAvg * countRun + mstime) / (countRun + 1);
		countRun++;
		mstimeLast = System.currentTimeMillis();
	}

	/**
	 * 
	 * @return 실행 횟수
	 */
	public long getCountRun() {
		return countRun;
	}

	public long getMstimeAvg() {
		return mstimeAvg;
	}

	public ThreadStatus getThreadStatus() {
		return threadStatus;
	}

	public void setThreadStatus(ThreadStatus threadStatus) {
		this.threadStatus = threadStatus;
	}

	@Override
	public String toString() {
		return DDHHMM.format(new Date(mstimeLast)) + " (" + threadStatus //
				+ " count=" + countRun + (countSkip <= 0 ? "" : "/" + countSkip) //
				+ " min=" + (mstimeMin < 0 ? 0 : mstimeMin) //
				+ " max=" + (mstimeMax < 0 ? 0 : mstimeMax) //
				+ " avg=" + mstimeAvg + ")";
	}

}

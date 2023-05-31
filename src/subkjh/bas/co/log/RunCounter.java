package subkjh.bas.co.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import fxms.bas.fxo.thread.FXTHREAD_STATUS;

public class RunCounter<STATUS> {

	public void setCntTotal(long cntTotal) {
		this.cntTotal = cntTotal;
	}

	private static final SimpleDateFormat DDHHMM = new SimpleDateFormat("dd HH:mm:ss");

	public static void main(String[] args) {
		RunCounter<FXTHREAD_STATUS> trot = new RunCounter<FXTHREAD_STATUS>();

		trot.addFail();
		trot.setStatus(FXTHREAD_STATUS.Finished);
		trot.addOk(1000);
		trot.addOk(2000);
		trot.addOk(5000);

		System.out.println(trot);
	}

	private long cntOk;
	private long cntFail;
	private long cntSkip;
	private long cntTotal;

	private long mstimeAvg;
	private long mstimeLast;
	private long mstimeMax = -1;
	private long mstimeMin = -1;

	private STATUS status;

	public RunCounter() {
	}

	public void addFail() {
		cntFail++;
		cntTotal++;
		mstimeLast = System.currentTimeMillis();
	}

	/**
	 * 처리 시간을 더합니다.
	 * 
	 * @param stime
	 *            spent time
	 */
	public synchronized void addOk(long stime) {

		mstimeMin = mstimeMin == -1 ? stime : Math.min(mstimeMin, stime);
		mstimeMax = mstimeMax == -1 ? stime : Math.max(mstimeMax, stime);
		mstimeAvg = (mstimeAvg * cntOk + stime) / (cntOk + 1);
		cntOk++;
		cntTotal++;
		mstimeLast = System.currentTimeMillis();
	}

	public void addSkip() {
		cntSkip++;
	}

	public STATUS getStatus() {
		return status;
	}

	public long getCntTotal() {
		return cntTotal;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(status == null ? "" : status);

		if (mstimeLast > 0)
			sb.append(",lasttime=" + DDHHMM.format(new Date(mstimeLast)));

		if (cntOk + cntFail + cntSkip > 0) {
			if (cntOk > 0)
				sb.append(",ok=" + cntOk);
			if (cntFail > 0)
				sb.append(",fail=" + cntFail);
			if (cntSkip > 0)
				sb.append(",skip=" + cntSkip);

			sb.append(",avg-time=" + mstimeAvg);
			sb.append(",max-time=" + mstimeMax);
			sb.append(",min-time=" + mstimeMin);

		}

		return sb.toString().trim();
	}

}

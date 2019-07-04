package fxms.nms.co.vo;

import java.io.Serializable;

public class LogCntVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6201129856492781538L;

	private int count;

	private long hstimeLastRecv;

	private long moNo;

	public LogCntVo() {

	}

	public LogCntVo(long moNo, int count, long hstimeLastRecv) {
		this.moNo = moNo;
		this.count = count;
		this.hstimeLastRecv = hstimeLastRecv;
	}

	public int getCount() {
		return count;
	}

	public long getHstimeLastRecv() {
		return hstimeLastRecv;
	}

	public long getMoNo() {
		return moNo;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setHstimeLastRecv(long hstimeLastRecv) {
		this.hstimeLastRecv = hstimeLastRecv;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

}

package fxms.bas.poller.vo;

import java.io.Serializable;

public abstract class ValueCounter<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2451672689754941470L;

	class TimeValue implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3285335946555093920L;

		public long unixtime;

		public T value;

		public TimeValue() {

		}

		public TimeValue(long unixtime, T value) {
			this.unixtime = unixtime;
			this.value = value;
		}

	}

	/** 처음으로 추가됨 ( 0 ) */
	public static final int VALUE_FIRST = 0;
	/** 리셋후의 값임 ( 2 ) */
	public static final int VALUE_RESET = 2;
	/** 정상적으로 추가됨 ( 1 ) */
	public static final int VALUE_NORMAL = 1;
	/** 음수값임 ( -1 ) */
	public static final int VALUE_NEGATIVE = -1;

	protected TimeValue prev;

	protected TimeValue cur;

	/**
	 * 실제 조회된 값을 추가합니다.
	 * 
	 * @param unixtime
	 *            조회할때 unixtime
	 * @param valueCurRaw
	 *            실제조회된 값
	 * @return VALUE_FIRST : 처음으로 추가됨<br>
	 *         VALUE_RESET : 리셋후의 값임 <br>
	 *         VALUE_NORMAL : 정상적으로 추가됨<br>
	 *         VALUE_NEGATIVE : 음수값임<br>
	 */
	public int add(long unixtime, T value) {

		if (cur == null) {
			cur = new TimeValue(unixtime, value);
			return VALUE_FIRST;
		}

		prev = cur;
		cur = new TimeValue(unixtime, value);

		long counter = getCounter();
		if (counter < 0)
			return VALUE_RESET;

		return VALUE_NORMAL;
	}

	public double doubleValue() {

		long counter = getCounter();
		if (counter < 0)
			return -1;

		if (getDeltaTime() <= 0)
			return counter;

		return counter / ((double) getDeltaTime());
	}

	/**
	 * 
	 * @return 현재값 - 이전값
	 */
	public abstract long getCounter();

	/**
	 * 
	 * @return 이전값과 현재값
	 */
	public abstract String getPrevCur();

	/**
	 * 
	 * @return 현재시간 - 이전시간
	 */
	public long getDeltaTime() {
		if (isValid() == false)
			return -1;
		return cur.unixtime - prev.unixtime;
	}

	/**
	 * 
	 * @return ( 현재값 - 이전값 ) / delta time
	 */
	public long getValue() {

		long counter = getCounter();
		if (counter < 0)
			return -1;

		if (getDeltaTime() <= 0)
			return counter;

		return counter / getDeltaTime();
	}

	/**
	 * 
	 * @return 이전, 현재 값 모두 존재할 경우 true
	 */
	public boolean isValid() {
		return prev != null && cur != null;
	}

	public long getUnixtime() {
		return cur == null ? -1 : cur.unixtime;
	}

	@Override
	public String toString() {
		return "DELTA-TIME(" + getDeltaTime() + ")COUNTER(" + getCounter() + ")";
	}

}

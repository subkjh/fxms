package fxms.bas.poller.vo;

import java.math.BigInteger;

public class ValueCounter32 extends ValueCounter<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7976111647006766854L;

	/** COUNTER 32 최대값 */
	public static final long MAX_COUNTER32 = new BigInteger("FFFFFFFF", 16).longValue();

	public static void main(String[] args) {
		// ValueCounter counter = new ValueCounter();
		// long value;
		//
		// for (int i = 0; i < 10; i++) {
		// value = (long) (Math.random() * 1000000L);
		// counter.add(System.currentTimeMillis() / 1000, value);
		// System.out.println(value + "\t" + counter.getPrev() + ", " +
		// counter.getCur() + ", " + counter.isValid() + ", "
		// + counter.getAvg());
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		System.out.println(ValueCounter32.make(0, 1000, 100) == 10);
		System.out.println(ValueCounter32.make(1000, 2000, 100) == 10);
		System.out.println(ValueCounter32.make(0, 1000, 0) == 1000);
		System.out.println(ValueCounter32.make(0, 1000, 100) == 10);
		System.out.println(ValueCounter32.make(MAX_COUNTER32 - 1000, 1000, 100) == 20);
		System.out.println(ValueCounter32.make(MAX_COUNTER32 - 1000, 1000, 0) == 2000);
	}

	/**
	 * 이전값과 현재값의 차이을 경과 시간으로 나눈 값<br>
	 * 현재값이 이전값보다 작으면 MAX_COUNTER32 - 이전값 + 현재값을 차이로 봄.
	 * 
	 * @param valuePrev
	 * @param valueCur
	 * @param secInterval
	 * @return
	 */
	public static long make(long valuePrev, long valueCur, long secInterval) {

		if (secInterval <= 0) {
			secInterval = 1;
		}

		long val = 0;

		if (valuePrev < 0) {
			val = 0;
		}
		else {
			if (valueCur >= valuePrev) {
				val = valueCur - valuePrev;
			}
			else {
				val = MAX_COUNTER32 - valuePrev + valueCur;
			}
		}

		val = val / secInterval;

		if (val < 0) {
			val = 0;
		}

		return val;
	}

	@Override
	public long getCounter() {

		if (isValid() == false) return -1;

//		if (cur.value >= prev.value) {
//			return cur.value - prev.value;
//		}
//		else {
//			return (MAX_COUNTER32 - prev.value) + cur.value;
//		}

		 return cur.value.longValue() - prev.value.longValue();
	}

	@Override
	public String getPrevCur() {
		return prev.value.longValue() + ":" + cur.value.longValue();
	}

}

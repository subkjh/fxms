package fxms.bas.poller.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ValueCounter64 extends ValueCounter<BigInteger> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5299059723009728799L;

	public static final BigDecimal MAX_COUNTER64 = new BigDecimal("18446744073709551615");
	public static final BigInteger MAX_COUNTER64_Int = new BigInteger("FFFFFFFFFFFFFFFF", 16);

	public static void main(String[] args) {

		System.out.println(new BigInteger("FFFFFFFFFFFFFFFF", 16));
		System.out.println(new BigInteger("FFFFFFFF", 16).longValue());

		ValueCounter64 value = new ValueCounter64();

		long unixtime = System.currentTimeMillis() / 1000L;
		long s;
		BigInteger prev, cur, v;

		prev = new BigInteger(((long) (Math.random() * Long.MAX_VALUE) + ""), 10);
		prev = new BigInteger("18446744073709500000");

		while (true) {
			s = (long) (Math.random() * 1000);
			try {
				Thread.sleep(s);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			unixtime = System.currentTimeMillis() / 1000L;

			cur = new BigInteger(((long) (Math.random() * 10000L) + ""), 10);
			v = cur.add(prev);
			if (v.compareTo(MAX_COUNTER64_Int) > 0) {
				v = cur;
			}

			value.add(unixtime, v);

			System.out.println("----------------------------------------");
			System.out.format("%30s\n", prev);
			System.out.format("%30s\n", v);
			System.out.println(value);

			prev = v;
		}

		// ValueCounter64 counter = new ValueCounter64();
		// BigInteger value;
		//
		// for (int i = 0; i < 10; i++) {
		// value = new BigInteger(((long) (Math.random() * 1000000L) + ""), 10);
		//
		// counter.add(System.currentTimeMillis() / 1000, value);
		// System.out.println(value.longValue() + "\t" + counter.getPrev() +
		// ", " + counter.getCur() + ", " + counter.isValid()
		// + ", " + counter.getAvg());
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		//
		// System.out.println(ValueCounter64.make(new BigInteger("0"), new
		// BigInteger("1000"), 100) == 10);
		// System.out.println(ValueCounter64.make(new BigInteger("1000"), new
		// BigInteger("2000"), 100) == 10);
		// System.out.println(ValueCounter64.make(new BigInteger("0"), new
		// BigInteger("1000"), 0) == 1000);
		// System.out.println(ValueCounter64.make(new BigInteger("0"), new
		// BigInteger("1000"), 100) == 10);
		// System.out
		// .println(ValueCounter64.make(new BigInteger("18446744073709550615"),
		// new BigInteger("1000"), 100) == 20);
		// System.out
		// .println(ValueCounter64.make(new BigInteger("18446744073709550615"),
		// new BigInteger("1000"), 0) == 2000);
		// System.out.println(ValueCounter64.make(new
		// BigInteger("18446744073509551615"), new BigInteger(
		// "18446744073609551615"), 1000) == 100000);
		// System.out.println(ValueCounter64.make(new
		// BigInteger("18446744073509551615"), new BigInteger(
		// "18446744073659551615"), 1000) == 150000);

	}

	/**
	 * 이전값과 현재값의 차이을 경과 시간으로 나눈 값<br>
	 * 현재값이 이전값보다 작으면 MAX_COUNTER64 - 이전값 + 현재값을 차이로 봄.
	 * 
	 * @param valuePrev
	 * @param valueCur
	 * @param secInterval
	 * @return
	 */
	public static long make(BigInteger valuePrev, BigInteger valueCur, long secInterval) {

		if (secInterval <= 0) {
			secInterval = 1;
		}

		// BigDecimal val1 = new BigDecimal(valuePrev);
		// System.out.println(valuePrev.longValue());
		// System.out.println(val1.doubleValue());
		// System.out.println(valuePrev.compareTo(valueCur));
		// System.out.println(
		// MAX_COUNTER64_Int.subtract(valuePrev).add(valueCur));

		long val = 0;
		if (valuePrev == null) {
			val = -1;
		}
		else {
			if (valuePrev.compareTo(valueCur) <= 0) {
				val = valueCur.subtract(valuePrev).longValue();
			}
			else {
				val = MAX_COUNTER64_Int.subtract(valuePrev).add(valueCur).longValue();
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
		
		// 현재값이 과거값보다 작아도 최대값을 이용한 계산을 하지 않는 이유는
		// 장비가 reset 되었을때 이런 현상이 일어나면 틀린 값이 처리되기 때문임

		// if (prev.value.compareTo(cur.value) <= 0) {
		// return cur.value.subtract(prev.value).longValue();
		// }
		// else {
		// return
		// MAX_COUNTER64_Int.subtract(prev.value).add(cur.value).longValue();
		// }

		return cur.value.subtract(prev.value).longValue();
	}

	@Override
	public String getPrevCur() {
		return prev.value.toString() + ":" + cur.value.toString();
	}

}

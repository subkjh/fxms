package fxms.bas.poller.vo;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fxms.bas.exp.FirstCounterNotFoundException;
import fxms.bas.exp.ResetCounterException;
import fxms.bas.fxo.thread.CycleFxThread;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

/**
 * 카운터 값을 관리하는 객체
 * 
 * @author subkjh
 * 
 */
public class CounterMgr extends CycleFxThread {
	private static CounterMgr mgr;

	public static CounterMgr getMgr() {
		if (mgr == null) {
			try {
				mgr = new CounterMgr();
				mgr.start();
			} catch (Exception e) {
				// 이곳으로 호출 될 확률이 0임.
				Logger.logger.error(e);
			}
		}
		return mgr;
	}

	private Map<String, ValueCounter<?>> valueMapForCounter;

	private final long KEEP_TERM_1_HOUR = 60 * 60 * 1000L;

	public CounterMgr() throws Exception {

		super(CounterMgr.class.getSimpleName(), "period 60");
		valueMapForCounter = Collections.synchronizedMap(new HashMap<String, ValueCounter<?>>());
	}

	public int addCounter(long moNo, int perfNo, long mstime, Number value) {
		String key = moNo + "." + perfNo;
		try {
			addCounter(key, mstime, value);
			return ValueCounter.VALUE_NORMAL;
		} catch (FirstCounterNotFoundException e) {
			return ValueCounter.VALUE_FIRST;

		} catch (ResetCounterException e) {
			return ValueCounter.VALUE_RESET;
		} catch (Exception e) {
			return ValueCounter.VALUE_RESET;
		}
	}

	public ValueCounter<?> addCounter(String key, long mstime, Number value)
			throws FirstCounterNotFoundException, ResetCounterException, Exception {
		getValueCounter(key, mstime, value);
		return getValueCounter(key);
	}

	public long getValueCounter(long moNo, int perfNo, long mstime, Number value)
			throws FirstCounterNotFoundException, ResetCounterException, Exception {
		return getValueCounter(moNo + "." + perfNo, mstime, value);
	}

	public ValueCounter<?> getValueCounter(String key) {
		return valueMapForCounter.get(key);
	}

	public ValueCounter<?> getValueCounter(long moNo, int perfNo) {
		return valueMapForCounter.get(moNo + "." + perfNo);
	}

	/**
	 * 
	 * @param key    키
	 * @param mstime 수집일시
	 * @param value  수집된 값
	 * @return 처리 값
	 * @throws FirstCounterNotFoundException
	 * @throws ResetCounterException
	 * @throws Exception
	 */
	public synchronized long getValueCounter(String key, long mstime, Number value)
			throws FirstCounterNotFoundException, ResetCounterException, Exception {

		long valueRet;
		int retAdd;

		if (value instanceof BigInteger) {
			ValueCounter64 counterValue = (ValueCounter64) valueMapForCounter.get(key);
			if (counterValue == null) {
				counterValue = new ValueCounter64();
				valueMapForCounter.put(key, counterValue);
			}

			retAdd = counterValue.add(mstime / 1000, (BigInteger) value);
			if (retAdd == ValueCounter32.VALUE_FIRST) {
				throw new FirstCounterNotFoundException(key);
			} else if (retAdd == ValueCounter32.VALUE_RESET) {
				throw new ResetCounterException(key + "=" + counterValue.getPrevCur());
			}

			valueRet = counterValue.getValue();
		} else {
			ValueCounter32 counterValue = (ValueCounter32) valueMapForCounter.get(key);
			if (counterValue == null) {
				counterValue = new ValueCounter32();
				valueMapForCounter.put(key, counterValue);
			}

			retAdd = counterValue.add(mstime / 1000, value.longValue());

			if (retAdd == ValueCounter32.VALUE_FIRST) {
				throw new FirstCounterNotFoundException(key);
			} else if (retAdd == ValueCounter32.VALUE_RESET) {
				throw new ResetCounterException(key + "=" + counterValue.getPrevCur());
			}

			valueRet = counterValue.getValue();
		}

		return valueRet;
	}

	@Override
	public String toString() {
		return "COUNTER-VALUE-SIZE(" + valueMapForCounter.size() + ")";
	}

	@Override
	protected void doCycle(long mstime) {
		removeGarbage(mstime);
	}

	/**
	 * 보관 유효기간이 지난 카운터 값을 삭제합니다.
	 */
	private void removeGarbage(long ptime) {

		long unixtime;
		ValueCounter<?> value;
		int countDelete = 0;

		// 1시간
		unixtime = (ptime - KEEP_TERM_1_HOUR) / 1000;

		try {

			String keyArray[] = valueMapForCounter.keySet().toArray(new String[valueMapForCounter.keySet().size()]);
			for (String key : keyArray) {
				value = valueMapForCounter.get(key);
				if (value instanceof ValueCounter32) {
					if (((ValueCounter32) value).getUnixtime() < unixtime) {
						valueMapForCounter.remove(key);
						countDelete++;
					}
				} else if (value instanceof ValueCounter64) {
					if (((ValueCounter64) value).getUnixtime() < unixtime) {
						valueMapForCounter.remove(key);
						countDelete++;
					}
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		if (countDelete > 0)
			Logger.logger.info("date={}, count={}", DateUtil.getDtm(unixtime * 1000L), countDelete);

	}

}

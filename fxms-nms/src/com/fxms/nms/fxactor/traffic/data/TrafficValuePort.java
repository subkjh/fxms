package com.fxms.nms.fxactor.traffic.data;

import com.fxms.nms.fxactor.traffic.NeIfTrafficIndex;
import com.fxms.nms.mo.NeIfMo;

import fxms.bas.pso.counter.CounterMgr;
import fxms.bas.pso.counter.ValueCounter;

/**
 * 인터페이스의 수집 값
 * 
 * @author subkjh
 * 
 */
public class TrafficValuePort implements NeIfTrafficIndex {

	private long ifSpeedQos;
	private long moNo;
	private NeIfMo mo;
	private IfOctets octets;

	public NeIfMo getMo() {
		return mo;
	}

	public TrafficValuePort(NeIfMo mo) {
		this.moNo = mo.getMoNo();
		this.mo = mo;
		this.ifSpeedQos = mo.getIfSpeedReal();
	}

	public long getInBps() {
		long bps;

		ValueCounter<?> val = CounterMgr.getMgr().getValueCounter(moNo, inOctets);

		if (val != null && val.isValid()) {
			bps = (long) (val.doubleValue() * 8);
			return bps > ifSpeedQos ? ifSpeedQos : bps;
		}

		return -1;
	}

	public Number getInDiscards() {
		return getCounter(inDiscards);
	}

	public Number getInErrors() {
		return getCounter(inErrors);
	}

	public Number getInOctets() {
		return getCounter(inOctets);
	}

	/**
	 * 
	 * @return sum ( uni, non-uni, multi, broad )
	 */
	public Number getInPkts() {
		return getSumCounter(new int[] { inUcastPkts, inMulticastPkts, inBroadcastPkts, inNonUcastPkts });

	}

	public Number getInPps() {
		return getSum(new int[] { inUcastPkts, inMulticastPkts, inBroadcastPkts, inNonUcastPkts });
	}

	/**
	 * 
	 * @return non-unicast in pps
	 * @since 2013.09.03 by subkjh
	 */
	public Number getInPpsNuni() {
		return getSum(new int[] { inMulticastPkts, inBroadcastPkts, inNonUcastPkts });
	}

	/**
	 * 
	 * @return unicast in pps
	 * @since 2013.09.03 by subkjh
	 */
	public Number getInPpsUni() {
		return getSum(new int[] { inUcastPkts });
	}

	/**
	 * 
	 * @return 입력 사용률
	 */
	public float getInUsage() {

		if (ifSpeedQos > 0 && getInBps() >= 0) {
			float rate = getInBps() * 100F / ifSpeedQos;
			return rate > 100 ? 100 : rate;
		} else {
			return 0;
		}
	}

	public long getMoNo() {
		return moNo;
	}

	public IfOctets getOctets() {
		return octets;
	}

	/**
	 * 
	 * @return 출력 사용률
	 */
	public long getOutBps() {

		ValueCounter<?> val = CounterMgr.getMgr().getValueCounter(moNo, outOctets);

		if (val != null && val.isValid()) {
			long bps = (long) (val.doubleValue() * 8);
			return bps > ifSpeedQos ? ifSpeedQos : bps;
		}
		return -1;
	}

	public Number getOutDiscards() {
		return getCounter(outDiscards);
	}

	public Number getOutErrors() {
		return getCounter(outErrors);
	}

	public Number getOutOctets() {
		return getCounter(outOctets);
	}

	/**
	 * 
	 * @return sum ( uni, non-uni, multi, broad )
	 */
	public Number getOutPkts() {
		return getSumCounter(new int[] { outUcastPkts, outMulticastPkts, outBroadcastPkts, outNonUcastPkts });
	}

	public Number getOutPps() {
		return getSum(new int[] { outUcastPkts, outMulticastPkts, outBroadcastPkts, outNonUcastPkts });
	}

	/**
	 * 
	 * @return non-unicast out pps
	 */
	public Number getOutPpsNuni() {
		return getSum(new int[] { outMulticastPkts, outBroadcastPkts, outNonUcastPkts });
	}

	/**
	 * 
	 * @return unicast out pps
	 * @since 2013.09.03 by subkjh
	 */
	public Number getOutPpsUni() {
		return getSum(new int[] { outUcastPkts });
	}

	public float getOutUsage() {

		if (ifSpeedQos > 0 && getOutBps() >= 0) {
			float rate = getOutBps() * 100F / ifSpeedQos;
			return rate > 100 ? 100 : rate;
		} else {
			return 0;
		}
	}

	public void initOctets(IfOctets octets) {

		if (octets == null) {
			this.octets = new IfOctets();
			return;
		}

		this.octets = octets;
		long unixtime = octets.unixtime * 1000L;

		CounterMgr mgr = CounterMgr.getMgr();

		if (octets.ifHCInOctets != null)
			mgr.addCounter(moNo, inOctets, unixtime, octets.ifHCInOctets);
		if (octets.ifHCInBroadcastPkts != null)
			mgr.addCounter(moNo, inBroadcastPkts, unixtime, octets.ifHCInBroadcastPkts);
		if (octets.ifHCInMulticastPkts != null)
			mgr.addCounter(moNo, inMulticastPkts, unixtime, octets.ifHCInMulticastPkts);
		if (octets.ifHCInUcastPkts != null)
			mgr.addCounter(moNo, inUcastPkts, unixtime, octets.ifHCInUcastPkts);

		if (octets.ifInOctets >= 0)
			mgr.addCounter(moNo, inOctets, unixtime, octets.ifInOctets);
		if (octets.ifInUcastPkts >= 0)
			mgr.addCounter(moNo, inUcastPkts, unixtime, octets.ifInUcastPkts);
		if (octets.ifInNUcastPkts >= 0)
			mgr.addCounter(moNo, inOctets, unixtime, octets.ifInNUcastPkts);
		if (octets.ifInErrors >= 0)
			mgr.addCounter(moNo, inErrors, unixtime, octets.ifInErrors);
		if (octets.ifInDiscards >= 0)
			mgr.addCounter(moNo, inDiscards, unixtime, octets.ifInDiscards);

		if (octets.ifHCOutOctets != null)
			mgr.addCounter(moNo, outOctets, unixtime, octets.ifHCOutOctets);
		if (octets.ifHCOutBroadcastPkts != null)
			mgr.addCounter(moNo, outBroadcastPkts, unixtime, octets.ifHCOutBroadcastPkts);
		if (octets.ifHCOutMulticastPkts != null)
			mgr.addCounter(moNo, outMulticastPkts, unixtime, octets.ifHCOutMulticastPkts);
		if (octets.ifHCOutUcastPkts != null)
			mgr.addCounter(moNo, outUcastPkts, unixtime, octets.ifHCOutUcastPkts);

		if (octets.ifOutOctets >= 0)
			mgr.addCounter(moNo, outOctets, unixtime, octets.ifOutOctets);
		if (octets.ifOutUcastPkts >= 0)
			mgr.addCounter(moNo, outUcastPkts, unixtime, octets.ifOutUcastPkts);
		if (octets.ifOutNUcastPkts >= 0)
			mgr.addCounter(moNo, outOctets, unixtime, octets.ifOutNUcastPkts);
		if (octets.ifOutErrors >= 0)
			mgr.addCounter(moNo, outErrors, unixtime, octets.ifOutErrors);
		if (octets.ifOutDiscards >= 0)
			mgr.addCounter(moNo, outDiscards, unixtime, octets.ifOutDiscards);

	}

	/**
	 * 
	 * @param ifSpeedQos
	 */
	public void setIfSpeedQos(long ifSpeedQos) {
		this.ifSpeedQos = ifSpeedQos;
	}

	public void setOctets(IfOctets octets) {
		this.octets = octets;
	}

	private Number getCounter(int index) {

		ValueCounter<?> val = CounterMgr.getMgr().getValueCounter(moNo, index);

		if (val != null && val.isValid()) {
			return val.getCounter();
		}
		return null;
	}

	private Number getSum(int indexArr[]) {
		long count = 0;
		boolean valid = false;
		ValueCounter<?> val;

		for (int index : indexArr) {
			val = CounterMgr.getMgr().getValueCounter(moNo, index);
			if (val != null && val.isValid()) {
				count += val.getValue();
				valid = true;
			}
		}

		return valid ? count : null;
	}

	private Number getSumCounter(int indexArr[]) {
		long count = 0;
		Number num;
		boolean isOk = false;

		for (int index : indexArr) {
			num = getCounter(index);
			if (num != null) {
				count += num.longValue();
				isOk = true;
			}
		}

		return isOk ? count : null;
	}
}

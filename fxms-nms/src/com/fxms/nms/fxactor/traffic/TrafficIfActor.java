package com.fxms.nms.fxactor.traffic;

import java.util.ArrayList;
import java.util.List;

import com.fxms.nms.NmsCodes;
import com.fxms.nms.fxactor.traffic.data.IfOctetMgr;
import com.fxms.nms.fxactor.traffic.data.IfOctets;
import com.fxms.nms.fxactor.traffic.data.TrafficValuePort;
import com.fxms.nms.mo.NeIfMo;
import com.fxms.nms.mo.NeMo;
import com.fxms.nms.snmp.beans.OidValue;
import com.fxms.nms.snmp.exception.SnmpErrorException;
import com.fxms.nms.snmp.exception.SnmpNotFoundOidException;
import com.fxms.nms.snmp.exception.SnmpTimeoutException;
import com.fxms.nms.snmp.mib.IFMIB;

import fxms.bas.mo.Mo;
import fxms.bas.poller.exception.PollingNoTargetException;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import fxms.bas.pso.VoList;
import fxms.bas.pso.counter.CounterMgr;
import fxms.bas.pso.counter.ValueCounter;
import subkjh.bas.log.Logger;

public class TrafficIfActor extends TrafficFxActor implements NeIfTrafficIndex {

	class RetryOid {

		TrafficValuePort traffic;
		String oid;

		RetryOid(TrafficValuePort traffic, String oid) {
			this.traffic = traffic;
			this.oid = oid;
		}
	}

	private final IFMIB MIB = new IFMIB();

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes)
			throws PollingTimeoutException, PollingNoTargetException, Exception {

		if ((mo instanceof NeMo) == false) {
			return null;
		}

		NeMo node = (NeMo) mo;

		List<NeIfMo> ifList = node.getMoConfig().getChildren(NeIfMo.class);
		if (ifList == null || ifList.size() == 0) {
			throw new PollingNoTargetException(node.getMoNo(), "no interface");
		}

		List<TrafficValuePort> trafficList = new ArrayList<TrafficValuePort>();
		List<IfOctets> octetList = new ArrayList<IfOctets>();
		List<RetryOid> retryOidList = new ArrayList<RetryOid>();
		List<PsVo> voList = new ArrayList<PsVo>();
		List<PsVo> val;
		List<String> oidList;

		for (NeIfMo ifMo : ifList) {

			TrafficValuePort traffic = new TrafficValuePort(ifMo);
			traffic.initOctets(IfOctetMgr.getMgr().getOctetLast(ifMo.getMoNo()));
			trafficList.add(traffic);

			oidList = makeSnmpOid(node, ifMo, traffic);

			List<RetryOid> reoidList = snmpget(node, oidList, traffic);
			if (reoidList != null && reoidList.size() > 0) {
				retryOidList.addAll(reoidList);
			}

		}

		// 재시도할 내용이 있다면 처리합니다.
		if (retryOidList.size() > 0) {

			Thread.sleep(20000);

			for (RetryOid e : retryOidList) {
				List<String> oids = new ArrayList<String>();
				oids.add(e.oid);
				snmpget(node, oids, e.traffic);
			}
		}

		for (TrafficValuePort traffic : trafficList) {
			val = makeValue(node, traffic);
			if (val != null) {
				voList.addAll(val);
				octetList.add(traffic.getOctets());
			}
		}

		saveOctets(node, octetList);

		return voList;
	}

	private List<String> makeSnmpOid(NeMo node, NeIfMo port, TrafficValuePort traffic)
			throws SnmpTimeoutException, Exception {

		List<String> oidList = new ArrayList<String>();

		if (port.isBit64Yn()) {
			oidList.add(MIB.ifHCInOctets + "." + port.getIfIndex());
			oidList.add(MIB.ifHCInBroadcastPkts + "." + port.getIfIndex());
			oidList.add(MIB.ifHCInMulticastPkts + "." + port.getIfIndex());
			oidList.add(MIB.ifHCInUcastPkts + "." + port.getIfIndex());

			oidList.add(MIB.ifHCOutOctets + "." + port.getIfIndex());
			oidList.add(MIB.ifHCOutBroadcastPkts + "." + port.getIfIndex());
			oidList.add(MIB.ifHCOutMulticastPkts + "." + port.getIfIndex());
			oidList.add(MIB.ifHCOutUcastPkts + "." + port.getIfIndex());
		} else {
			oidList.add(MIB.ifInOctets + "." + port.getIfIndex());
			oidList.add(MIB.ifInUcastPkts + "." + port.getIfIndex());
			oidList.add(MIB.ifInNUcastPkts + "." + port.getIfIndex());
			oidList.add(MIB.ifOutOctets + "." + port.getIfIndex());
			oidList.add(MIB.ifOutUcastPkts + "." + port.getIfIndex());
			oidList.add(MIB.ifOutNUcastPkts + "." + port.getIfIndex());
		}

		oidList.add(MIB.ifInErrors + "." + port.getIfIndex());
		oidList.add(MIB.ifInDiscards + "." + port.getIfIndex());
		oidList.add(MIB.ifOutErrors + "." + port.getIfIndex());
		oidList.add(MIB.ifOutDiscards + "." + port.getIfIndex());

		return oidList;
	}

	private List<PsVo> makeValue(NeMo node, TrafficValuePort traffic) {

		NeIfMo port = traffic.getMo();
		VoList retValue = new VoList(getClass().getSimpleName(), System.currentTimeMillis());

		float ifUsage = -1;

		// INPUT Traffic

		if (traffic.getInBps() >= 0) {
			retValue.add(port, NmsCodes.PsItem.IfInBps, traffic.getInBps());
			retValue.add(port, NmsCodes.PsItem.IfInBytes, traffic.getInOctets());
			retValue.add(port, NmsCodes.PsItem.IfInUsage, traffic.getInUsage());

			if (node.getIpAddress().equals(port.getIpAddress())) {
				retValue.add(node, NmsCodes.PsItem.NeIfInUsgae, traffic.getInUsage());
				ifUsage = traffic.getInUsage();
			}
		}
		retValue.add(port, NmsCodes.PsItem.IfInEps, traffic.getInErrors());
		retValue.add(port, NmsCodes.PsItem.IfInErrors, traffic.getInErrors());
		retValue.add(port, NmsCodes.PsItem.IfInDps, traffic.getInDiscards());
		retValue.add(port, NmsCodes.PsItem.IfInPpsUnicast, traffic.getInPpsUni());
		retValue.add(port, NmsCodes.PsItem.IfInPpsNonunicast, traffic.getInPpsNuni());
		retValue.add(port, NmsCodes.PsItem.IfInPps, traffic.getInPps());
		retValue.add(port, NmsCodes.PsItem.IfInPackets, traffic.getInPkts());

		// OUTPUT Traffic

		if (traffic.getOutBps() >= 0) {
			retValue.add(port, NmsCodes.PsItem.IfOutBps, traffic.getOutBps());
			retValue.add(port, NmsCodes.PsItem.IfOutBytes, traffic.getOutOctets());
			retValue.add(port, NmsCodes.PsItem.IfOutUsage, traffic.getOutUsage());

			if (node.getIpAddress().equals(port.getIpAddress())) {
				retValue.add(node, NmsCodes.PsItem.NeIfInUsgae, traffic.getOutUsage());
				ifUsage += traffic.getOutUsage();
			}

		}
		retValue.add(port, NmsCodes.PsItem.IfOutEps, traffic.getOutErrors());
		retValue.add(port, NmsCodes.PsItem.IfOutErrors, traffic.getOutErrors());
		retValue.add(port, NmsCodes.PsItem.IfOutDps, traffic.getOutDiscards());
		retValue.add(port, NmsCodes.PsItem.IfOutPpsUnicast, traffic.getOutPpsUni());
		retValue.add(port, NmsCodes.PsItem.IfOutPpsNonunicast, traffic.getOutPpsNuni());
		retValue.add(port, NmsCodes.PsItem.IfOutPps, traffic.getOutPps());
		retValue.add(port, NmsCodes.PsItem.IfOutPackets, traffic.getOutPkts());

		if (retValue.size() > 0) {
			retValue.add(port, NmsCodes.PsItem.IfSpeedQos.name(), port.getIfSpeedReal());
		}

		if (ifUsage >= 0) {
			if (ifUsage > 100)
				ifUsage = 100f;
			retValue.add(node, NmsCodes.PsItem.IfUsage, ifUsage);
		}

		return retValue;
	}

	private void saveOctets(NeMo node, List<IfOctets> ifList) {

		IfOctetMgr mgr = IfOctetMgr.getMgr();

		long mstime = System.currentTimeMillis();

		if (mgr.getPathOctetLast() != null || mgr.getPathOctetFile() != null) {
			if (node.getNeStatus().getStatusSnmp() == NeMo.STATUS_SNMP_ONLINE) {
				List<IfOctets> octetList = new ArrayList<IfOctets>();
				for (IfOctets o : ifList) {
					octetList.add(o);
					mgr.put(o.getMoNo(), o);
				}

				if (octetList.size() > 0) {

					if (mgr.getPathOctetLast() != null) {
						mgr.writeIfOctetLast(node.getMoNo(), octetList);
					}

					if (mgr.getPathOctetFile() != null) {
						mgr.writeIfOctet2File(node.getMoNo(), mstime, octetList);
					}

				}
			}

		}

	}

	private List<RetryOid> snmpget(NeMo node, List<String> oidList, TrafficValuePort traffic)
			throws SnmpTimeoutException, Exception {

		// 지원하지 않는 OID는 제외합니다.
		for (int index = oidList.size() - 1; index >= 0; index--) {
			if (node.isNullOid(oidList.get(index))) {
				oidList.remove(index);
			}
		}

		if (oidList.size() == 0)
			return null;

		OidValue oidValArr[];
		try {
			oidValArr = snmpget(node, oidList.toArray(new String[oidList.size()]));
			if (oidValArr == null)
				return null;
		} catch (SnmpTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			Logger.logger.fail(e.getMessage());
			return null;
		}

		// 타임아웃이 있기 때문에 데이터를 모두 수집한 시간을 설정함.
		long mstime = System.currentTimeMillis();
		int ret;
		List<RetryOid> retryList = null;
		IfOctets octets = traffic.getOctets();
		octets.unixtime = mstime / 1000;
		octets.moNo = traffic.getMoNo();

		CounterMgr mgr = CounterMgr.getMgr();

		for (OidValue value : oidValArr) {

			Logger.logger.trace("ne-no='{}' if-no='{}' value='{}'", node.getMoNo(), traffic.getMoNo(), value);

			ret = ValueCounter.VALUE_NORMAL;

			// INPUT

			if (value.getOid().startsWith(MIB.ifHCInOctets) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), inOctets, mstime, value.getBigInteger());
				octets.ifHCInOctets = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifHCInBroadcastPkts) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), inBroadcastPkts, mstime, value.getBigInteger());
				octets.ifHCInBroadcastPkts = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifHCInMulticastPkts) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), inMulticastPkts, mstime, value.getBigInteger());
				octets.ifHCInMulticastPkts = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifHCInUcastPkts) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), inUcastPkts, mstime, value.getBigInteger());
				octets.ifHCInUcastPkts = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifInOctets)) {
				ret = mgr.addCounter(traffic.getMoNo(), inOctets, mstime, value.getLong());
				octets.ifInOctets = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifInUcastPkts)) {
				ret = mgr.addCounter(traffic.getMoNo(), inUcastPkts, mstime, value.getLong());
				octets.ifInUcastPkts = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifInNUcastPkts)) {
				ret = mgr.addCounter(traffic.getMoNo(), inNonUcastPkts, mstime, value.getLong());
				octets.ifInNUcastPkts = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifInErrors)) {
				ret = mgr.addCounter(traffic.getMoNo(), inErrors, mstime, value.getLong());
				octets.ifInErrors = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifInDiscards)) {
				ret = mgr.addCounter(traffic.getMoNo(), inDiscards, mstime, value.getLong());
				octets.ifInDiscards = value.getLong();
			}

			// OUTPUT
			else if (value.getOid().startsWith(MIB.ifHCOutOctets) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), outOctets, mstime, value.getBigInteger());
				octets.ifHCOutOctets = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifHCOutBroadcastPkts) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), outBroadcastPkts, mstime, value.getBigInteger());
				octets.ifHCOutBroadcastPkts = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifHCOutMulticastPkts) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), outMulticastPkts, mstime, value.getBigInteger());
				octets.ifHCOutMulticastPkts = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifHCOutUcastPkts) && value.getBigInteger() != null) {
				ret = mgr.addCounter(traffic.getMoNo(), outUcastPkts, mstime, value.getBigInteger());
				octets.ifHCOutUcastPkts = value.getBigInteger();
			} else if (value.getOid().startsWith(MIB.ifOutOctets)) {
				ret = mgr.addCounter(traffic.getMoNo(), outOctets, mstime, value.getLong());
				octets.ifOutOctets = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifOutUcastPkts)) {
				ret = mgr.addCounter(traffic.getMoNo(), outUcastPkts, mstime, value.getLong());
				octets.ifOutUcastPkts = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifOutNUcastPkts)) {
				ret = mgr.addCounter(traffic.getMoNo(), outNonUcastPkts, mstime, value.getLong());
				octets.ifOutNUcastPkts = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifOutErrors)) {
				ret = mgr.addCounter(traffic.getMoNo(), outErrors, mstime, value.getLong());
				octets.ifOutErrors = value.getLong();
			} else if (value.getOid().startsWith(MIB.ifOutDiscards)) {
				ret = mgr.addCounter(traffic.getMoNo(), outDiscards, mstime, value.getLong());
				octets.ifOutDiscards = value.getLong();
			}

			// 다시 조회할 OID 추가
			if (ret == ValueCounter.VALUE_FIRST || ret == ValueCounter.VALUE_RESET) {
				if (retryList == null)
					retryList = new ArrayList<RetryOid>();
				retryList.add(new RetryOid(traffic, value.getOid()));
			}

		}

		return retryList;
	}

	protected OidValue[] snmpget(NeMo node, String... oidArr)
			throws SnmpTimeoutException, SnmpErrorException, SnmpNotFoundOidException, Exception {

		SnmpTimeoutException ex = null;

		for (int i = 0; i < 3; i++) {
			try {
				List<OidValue> varList = getSnmpUtil().snmpget(node, oidArr);
				if (varList == null)
					return null;

				return varList.toArray(new OidValue[varList.size()]);
			} catch (SnmpTimeoutException e) {
				ex = e;
			} catch (SnmpErrorException e) {
				throw e;
			} catch (SnmpNotFoundOidException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
			}
		}

		if (ex != null)
			throw ex;

		return null;

	}
}

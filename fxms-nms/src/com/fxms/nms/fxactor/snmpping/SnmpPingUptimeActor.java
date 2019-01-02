package com.fxms.nms.fxactor.snmpping;

import java.util.ArrayList;
import java.util.List;

import com.fxms.nms.NmsCodes;
import com.fxms.nms.mo.NeMo;
import com.fxms.nms.snmp.beans.OidValue;
import com.fxms.nms.snmp.mib.SNMPV2_MIB;

import fxms.bas.api.EventApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import fxms.bas.signal.SyncSignal;

/**
 * @author subkjh
 * 
 */
public class SnmpPingUptimeActor extends SnmpPingFxActor {

	private final String sysUptime = new SNMPV2_MIB().sysUpTime;

	public SnmpPingUptimeActor() {

	}

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof NeMo) == false) {
			return null;
		}

		NeMo node = (NeMo) mo;

		long uptime;
		boolean checked = false;

		
		OidValue var = snmpget(node, sysUptime);

		if (var.isNull() == false) {
			// 초단위로 변경하기 위해 100으로 나눕니다.
			uptime = var.getLong();

			// 최초로 uptime을 조회했을때만 경보 및 노티를 하기위해서 확인합니다.
			checked = node.getNeStatus().getSysUptime() > 0;

			node.getNeStatus().setSysUptime(uptime);
			node.getNeStatus().setStatusSnmp(NeMo.STATUS_SNMP_ONLINE);

			if (checked == false) {
				
				EventApi.getApi().checkPsValue(node, null, NmsCodes.PsItem.NeSysUptime, null, node.getNeStatus().getSysUptime() , pollMsdate);

				// 1/100초이므로 100을 더 곱합니다.
				if (uptime <= (node.getPollCyclePing() * 100L)) {
					if (FxServiceImpl.fxService != null) {
						FxServiceImpl.fxService.send(new SyncSignal(node.getMoNo(), node.getIpAddress(),
								"uptime " + node.getNeStatus().getSysUptime() , NeMo.MO_CLASS));
					}
					// return RESYNCED;
				}

			}
			
		}

		List<PsVo> valueList = new ArrayList<PsVo>();

		valueList.add(new PsVo(node, null, NmsCodes.PsItem.NeSnmpStatus, node.getNeStatus().getStatusSnmp()));

		return valueList;

	}

}

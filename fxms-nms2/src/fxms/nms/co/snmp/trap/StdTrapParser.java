package fxms.nms.co.snmp.trap;

import java.util.List;

import subkjh.bas.co.log.Logger;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.api.EventApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.nms.api.TrapApi;
import fxms.nms.co.cd.NmsCode;
import fxms.nms.co.singnal.ResyncNode;
import fxms.nms.co.snmp.mib.IFMIB;
import fxms.nms.co.snmp.mo.TrapNode;
import fxms.nms.co.snmp.trap.vo.TrapVo;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.co.snmp.vo.SNMP.TrapType;

public class StdTrapParser {

	private final IFMIB IFMIB = new IFMIB();

	public StdTrapParser() {

	}

	public void parse(TrapNode node, TrapVo vo) {

		if (node == null) {
			return;
		}

		Alarm alarm = null;
		String moInstance = vo.getValueString();
		if (moInstance.length() > 100) {
			moInstance = null;
		}

		// String ip = vo.getIpAddress();

		if (vo.getTrapType() == TrapType.authenticationFailure) {

			alarm = EventApi.getApi().check(node, moInstance, NmsCode.AlarmCode.NODE_AUTH_FAIL, null, null);

		} else if (vo.getTrapType() == TrapType.coldStart) {

			if (node != null) {
				alarm = EventApi.getApi().check(node, null, NmsCode.AlarmCode.NODE_COLD_START, null, null);
			}

			// Mo mo = TrapApi.getApi().getMoByMac(new
			// GetMacAddress().getMacAddress(ip));
			// if (mo != null) {
			// FxServiceImpl.fxService.send(new ResyncNode(mo.getMoNo(), ip,
			// "coldStart"));
			// }
		} else if (vo.getTrapType() == TrapType.warmStart) {
			alarm = EventApi.getApi().check(node, null, NmsCode.AlarmCode.NODE_WARM_START, null, null);

		} else if (vo.getTrapType() == TrapType.egpNeighborLoss) {
			alarm = EventApi.getApi().check(node, moInstance, NmsCode.AlarmCode.NODE_EGP_NEIGH_LOSS, null, null);
		} else if (vo.getTrapType() == TrapType.linkUp) {
			linkUpDown(node, vo, true);
		} else if (vo.getTrapType() == TrapType.linkDown) {
			linkUpDown(node, vo, false);
		}

	}

	private Alarm linkUpDown(TrapNode node, TrapVo vo, boolean up) {
		List<OidValue> list = vo.find(IFMIB.ifIndex, true);
		Mo mo;
		Alarm alarm = null;
		List<Alarm> alarmList;
		for (OidValue e : list) {
			mo = TrapApi.getApi().getNeIf(node.getMoNo(), e.getInt(-1));
			if (mo != null) {
				Logger.logger.debug(up + ":" + mo.getMoName());
				alarmList = EventApi.getApi().checkPsValue(mo, null, NmsCode.PsCode.IF_STATUS_LINK, null, up ? 1 : 0,
						System.currentTimeMillis());
				if (alarmList != null && alarmList.size() > 0)
					alarm = alarmList.get(0);
			} else {
				Logger.logger.debug("Interface not found" + "(" + e.getInt(-1) + ")");
			}
		}
		return alarm;
	}

}

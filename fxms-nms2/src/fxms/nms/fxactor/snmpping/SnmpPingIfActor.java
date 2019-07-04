package fxms.nms.fxactor.snmpping;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.co.signal.SyncSignal;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.po.PsVo;
import fxms.bas.poller.exp.PollingTimeoutException;
import fxms.nms.NmsCodes;
import fxms.nms.co.snmp.mib.IFMIB;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.mo.NeIfMo;
import fxms.nms.mo.NeMo;

/**
 * 인터페이스의 LINK Up/Down을 관제하는 SnmpFilter<br>
 * 
 * @author subkjh
 * 
 */
public class SnmpPingIfActor extends SnmpPingFxActor {

	private final IFMIB MIB = new IFMIB();

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof NeMo) == false) {
			return null;
		}

		NeMo node = (NeMo) mo;

		List<NeIfMo> moList = node.getMoConfig().getChildren(NeIfMo.class);
		if (moList == null || moList.size() == 0)
			return null;

		boolean isChanged;
		boolean toSendResyncNoti = false;
		long uptime;
		long gap;
		long lastChange;
		String oidArr[] = new String[3];
		OidValue valArr[];

		List<PsVo> valueList = new ArrayList<PsVo>();

		for (NeIfMo port : moList) {

			if (port.isMngYn() == false)
				continue;

			oidArr[0] = MIB.ifAdminStatus + "." + port.getIfIndex();
			oidArr[1] = MIB.ifOperStatus + "." + port.getIfIndex();
			oidArr[2] = MIB.ifLastChange + "." + port.getIfIndex();

			valArr = snmpget(node, oidArr);
			if (valArr == null)
				return null;

			port.setIfStatusAdmin(valArr[0].getInt());
			port.setIfStatusOper(valArr[1].getInt());
			lastChange = valArr[2].getLong();
			isChanged = (port.getIfLastChange() > 0 && port.getIfLastChange() != lastChange);
			if (isChanged) {
				toSendResyncNoti = true;
			} else {
				if (node.getNeStatus().getSysUptime() > 0 && port.getIfLastChange() != lastChange) {
					uptime = node.getNeStatus().getSysUptime() - lastChange;
					gap = node.getPollCyclePing() * 100L; // 0.01초
					if (uptime > 0 && uptime <= gap) {
						toSendResyncNoti = true;
					}
				}
			}

			port.setIfLastChange(lastChange);

			valueList.add(new PsVo(port, null, NmsCodes.PsItem.IfOperStatus, port.getIfStatusOper()));
			valueList.add(new PsVo(port, null, NmsCodes.PsItem.IfAdminStatus, port.getIfStatusAdmin()));
		}

		if (toSendResyncNoti && FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(new SyncSignal(node.getMoNo(), node.getIpAddress(), "ifLastChange", NeIfMo.MO_CLASS));
		}

		return valueList;

	}

}

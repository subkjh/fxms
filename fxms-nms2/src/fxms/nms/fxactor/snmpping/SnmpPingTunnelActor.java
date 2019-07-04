package fxms.nms.fxactor.snmpping;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;
import fxms.bas.po.PsVo;
import fxms.bas.poller.exp.PollingTimeoutException;
import fxms.nms.NmsCodes;
import fxms.nms.co.snmp.mib.MibCiscoIpSecFlowMonitor;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.mo.NeMo;
import fxms.nms.mo.TunnelMo;

public class SnmpPingTunnelActor extends SnmpPingFxActor {

	private MibCiscoIpSecFlowMonitor OID = new MibCiscoIpSecFlowMonitor();

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof NeMo) == false) {
			return null;
		}

		NeMo node = (NeMo) mo;

		List<TunnelMo> moList = node.getMoConfig().getChildren(TunnelMo.class);
		if (moList == null || moList.size() == 0)
			return null;

		int tnIndex = 0;
		List<String> oidList = new ArrayList<String>();
		List<OidValue> varList = null;
		TunnelMo tunnel;
		int size = moList.size();

		for (TunnelMo _tunnel : moList) {
			if (_tunnel.isMngYn()) {
				oidList.add(OID.cipSecTunStatus + "." + _tunnel.getTnIndex());
			}
		}

		if (oidList.size() == 0)
			return null;

		varList = getSnmpUtil().snmpget(node, oidList.toArray(new String[oidList.size()]));
		if (varList == null)
			return null;

		for (OidValue var : varList) {

			if (var.isNull())
				continue;

			tnIndex = Integer.parseInt(var.getInstance(1));
			tunnel = null;
			for (int i = 0; i < size; i++) {
				tunnel = moList.get(i);
				if (tunnel.getTnIndex() == tnIndex)
					break;
			}

			if (tunnel.getTnIndex() == tnIndex) {
				if (var.getOid().startsWith(OID.cipSecTunStatus)) {
					tunnel.setStatusTunnel(var.getInt());
				}
			}
		}

		return makeValue(node, moList);
	}

	private List<PsVo> makeValue(NeMo node, List<TunnelMo> moList) {

		List<PsVo> valueList = new ArrayList<PsVo>();

		for (TunnelMo tunnel : moList) {
			if (tunnel.isMngYn()) {
				valueList.add(new PsVo(tunnel, null, NmsCodes.PsItem.TunnelStatus, tunnel.getStatusTunnel()));
			}
		}

		return valueList;
	}

}

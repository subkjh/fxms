package fxms.nms.fxactor.conf.snmp;

import subkjh.bas.co.log.Logger;
import fxms.bas.api.FxApi;
import fxms.bas.co.exp.FxTimeoutException;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.nms.co.snmp.exception.SnmpTimeoutException;
import fxms.nms.co.snmp.mib.SNMPV2_MIB;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.mo.NeMo;

public class ConfSystemActor extends ConfSnmpFxActor {

	@Override
	public void getConfigChildren(MoConfig configMo, String... moClasses) throws FxTimeoutException, Exception {

		if ((configMo.getParent() instanceof NeMo) == false) {
			return;
		}

		SNMPV2_MIB MIB = new SNMPV2_MIB();

		NeMo node = (NeMo) configMo.getParent();

		try {
			OidValue ovArr[] = snmpget(node, MIB.sysName, MIB.sysDescr, MIB.sysLocation, MIB.sysObjectID, MIB.sysUpTime,
					MIB.sysServices);

			node.getNeStatus().setSysName(ovArr[0].getValue());
			node.getNeStatus().setSysDescr(ovArr[1].getValue());
			node.getNeStatus().setSysLocation(ovArr[2].getValue());
			node.getNeStatus().setSysObjectId(ovArr[3].getValue());
			node.getNeStatus().setSysUptime(ovArr[4].getLong(0));
			node.getNeStatus().setSysUptimeChgDate(FxApi.getDate(System.currentTimeMillis()));
			node.getNeStatus().setSysServices(ovArr[5].getInt(0));

			// node.setHstimeSync(FxApi.getDate(0));

			// 32bit reset 되는 문제를 어떻게 해결할 것인가???
			// System.out.println(node.getSysUptime() + ", " +
			// SystemGroup.getTimeTicks(node.getSysUptime()));

			if (node.getMoName() == null)
				node.setMoName(node.getNeStatus().getSysName());

			node.getNeStatus().setStatusSnmp(Mo.STATUS_ON);
			node.getNeStatus().setStatusSnmpChgDate(FxApi.getDate(System.currentTimeMillis()));

			return;
		} catch (SnmpTimeoutException e) {
			node.getNeStatus().setStatusSnmp(Mo.STATUS_OFF);
			node.getNeStatus().setStatusSnmpChgDate(FxApi.getDate(System.currentTimeMillis()));

			Logger.logger.fail(e.getMessage() + " - " + node.getIpAddress());
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}

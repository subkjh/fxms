package fxms.nms.fxactor.snmpping;

import java.util.List;

import fxms.bas.fxo.FxActorImpl;
import fxms.nms.co.snmp.SnmpUtil;
import fxms.nms.co.snmp.exception.SnmpErrorException;
import fxms.nms.co.snmp.exception.SnmpNotFoundOidException;
import fxms.nms.co.snmp.exception.SnmpTimeoutException;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.fxactor.NeValueActor;
import fxms.nms.mo.NeMo;

public abstract class SnmpPingFxActor extends FxActorImpl implements NeValueActor {

	protected final String OK = "OK";
	protected final String NOTHING = "NOTHING";
	protected final String NOTHING2 = "NOTHING-1";
	protected final String RESYNCED = "RESYNCED";
	protected final String SNMPFAIL = "SNMPFAIL";

	protected SnmpUtil getSnmpUtil() {
		return SnmpUtil.getSnmpUtil("SnmpPingFxActor");
	}

	protected OidValue snmpget(NeMo node, String oid) throws SnmpTimeoutException, SnmpErrorException, SnmpNotFoundOidException {

		for (int i = 0; i < 2; i++) {
			try {
				List<OidValue> varList = getSnmpUtil().snmpget(node, oid);
				if (varList == null || varList.size() == 0)
					return null;
				return varList.get(0);
			} catch (SnmpTimeoutException e) {
				if (i == 1)
					throw e;
			} catch (SnmpErrorException e) {
				throw e;
			} catch (SnmpNotFoundOidException e) {
				throw e;
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
			}
		}

		return null;

	}

	protected OidValue[] snmpget(NeMo node, String... oidArr) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException {

		for (int i = 0; i < 2; i++) {
			try {
				List<OidValue> varList = getSnmpUtil().snmpget(node, oidArr);
				if (varList == null)
					return null;

				return varList.toArray(new OidValue[varList.size()]);
			} catch (SnmpTimeoutException e) {
				if (i == 1)
					throw e;
			} catch (SnmpErrorException e) {
				throw e;
			} catch (SnmpNotFoundOidException e) {
				throw e;
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
			}
		}

		return null;

	}
}

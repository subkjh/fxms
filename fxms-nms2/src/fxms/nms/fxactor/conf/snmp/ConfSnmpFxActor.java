package fxms.nms.fxactor.conf.snmp;

import java.util.List;

import fxms.bas.co.exp.FxTimeoutException;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoModelable;
import fxms.nms.co.snmp.FilterValidChecker;
import fxms.nms.co.snmp.SnmpUtil;
import fxms.nms.co.snmp.exception.SnmpErrorException;
import fxms.nms.co.snmp.exception.SnmpNotFoundOidException;
import fxms.nms.co.snmp.exception.SnmpTimeoutException;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.fxactor.NeConfigActor;
import fxms.nms.mo.property.MoSnmppable;

public abstract class ConfSnmpFxActor extends FxActorImpl implements NeConfigActor {

	private SnmpUtil snmputil;

	public MoSnmppable getSnmp(Mo mo) throws FxTimeoutException, Exception {

		if ((mo instanceof MoSnmppable) == false) {
			throw new Exception("Not MoSnmppable");
		}
		MoSnmppable node = (MoSnmppable) mo;
		String modelNo = "";

		if (mo instanceof MoModelable) {
			modelNo = ((MoModelable) mo).getModelNo() + "";
		}

		if ("localhost".equals(node.getIpAddress()) == false) {
			try {
				if (isIp(node.getIpAddress()) == false) {
					throw new Exception("IP(" + node.getIpAddress() + ") NOT IP");
				}
			} catch (Exception e1) {
				throw new Exception("IP(" + node.getIpAddress() + ") NOT IP");
			}
		}

		String oid = getOidToCheck();
		if (oid == null || oid.trim().length() == 0)
			return node;

		if (node.isSnmp() == false) {
			throw new Exception(node + " NOT Snmppable");
		}

		FilterValidChecker oidPool = FilterValidChecker.getInstance();
		try {
			if (oidPool.valid(modelNo, oid, getSnmpUtil(), node)) {
				return node;
			} else {
				throw new Exception("invalid oid:" + oid);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public SnmpUtil getSnmputil() {
		return snmputil;
	}

	public void setSnmputil(SnmpUtil snmputil) {
		this.snmputil = snmputil;
	}

	/**
	 * 
	 * 해당 필터를 지원 여부를 확인할 OID<br>
	 * NULL이거나 공백이면 무조건 처리하는 것으로 판단합니다.
	 * 
	 * @return 해당 필터를 지원 여부를 확인할 OID
	 */
	protected String getOidToCheck() {
		Object obj = getPara("oidToCheck");
		return obj == null ? null : obj.toString();
	}

	/**
	 * 사용할 SnmpUtil<br>
	 * 지정되지 않은 경우 ConfApi.api.snmputil를 제공합니다.
	 * 
	 * @return 사용할 SnmpUtil
	 */
	protected SnmpUtil getSnmpUtil() throws Exception {
		return snmputil != null ? snmputil : SnmpUtil.getSnmpUtil("ConfSnmpFxActor");
	}

	/**
	 * IP주소인지 여부
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected boolean isIp(String s) throws Exception {

		if (s == null)
			return false;
		if (s.trim().length() == 0)
			return false;

		String ss[] = s.split("\\.");
		if (ss.length != 4)
			return false;

		for (int i = 0; i < ss.length; i++) {
			if (Integer.parseInt(ss[i]) < 0 || Integer.parseInt(ss[i]) > 255)
				return false;
		}

		return true;
	}

	protected OidValue[] snmpget(MoSnmppable node, String... oidArr) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException, Exception {

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

	protected OidValue[] snmpnext(MoSnmppable node, String... oidArr) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException, Exception {

		for (int i = 0; i < 2; i++) {
			try {
				List<OidValue> varList = getSnmpUtil().snmpgetnext(node, oidArr);
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

	protected List<OidValue>[] snmpwalk(MoSnmppable node, String... oidArr) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException, Exception {

		for (int i = 0; i < 2; i++) {
			try {
				return getSnmpUtil().snmpwalk(node, oidArr);
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

	protected List<OidValue> snmpwalk(MoSnmppable node, String oid) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException, Exception {

		for (int i = 0; i < 2; i++) {
			try {
				return getSnmpUtil().snmpwalk(node, oid);
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

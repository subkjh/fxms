package fxms.nms.co.snmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import subkjh.bas.co.log.Logger;

import com.adventnet.snmp.beans.ErrorMessages;
import com.adventnet.snmp.snmp2.Snmp3Message;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpClient;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpSession;
import com.adventnet.snmp.snmp2.SnmpVarBind;

import fxms.nms.co.snmp.exception.SnmpNotFoundOidException;
import fxms.nms.co.snmp.vo.OidValue;

public class SnmpClientImpl implements SnmpClient {

	private Logger logger;
	private Map<Integer, SnmpListener> snmpListenerMap;

	public SnmpClientImpl() {
		snmpListenerMap = new HashMap<Integer, SnmpListener>();
	}

	public void addSnmpListener(int requestId, SnmpListener snmpListener) {
		snmpListenerMap.put(requestId, snmpListener);
	}

	@Override
	public boolean authenticate(SnmpPDU pdu, String community) {
		if (pdu.getVersion() == SnmpAPI.SNMP_VERSION_3) {
			return !((Snmp3Message) pdu.getMsg()).isAuthenticationFailed();
		} else {
			return (pdu.getCommunity().equals(community));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean callback(SnmpSession snmpsession, SnmpPDU pdu, int requestId) {

		SnmpListener snmpListener = snmpListenerMap.get(requestId);

		if (snmpListener == null) {
			if (logger != null) {
				logger.fail("Request No. " + requestId + " not set Listener");
				return true;
			}
		} else {
			snmpListenerMap.remove(requestId);
		}

		if (pdu.getErrstat() != 0) {

			// 여러개를 동시에 했을 경우 하나라도 없으면 오류가 발생할 때 하나씩 처리합니다.
			if (pdu.getErrstat() == ErrorMessages.SNMP_ERR_NOSUCHNAME) {
				snmpListener.add(requestId, new SnmpNotFoundOidException(pdu.getObjectID(pdu.getErrindex() - 1).toString()));
				return true;
			}

			snmpListener.add(requestId, new SnmpException(pdu.getError()));
			return true;
		}

		List<OidValue> valueList = new ArrayList<OidValue>();
		for (SnmpVarBind varBind : (Vector<SnmpVarBind>) pdu.getVariableBindings()) {
			valueList.add(SnmpUtilAdventNetImpl.makeOidValue(varBind.getObjectID().toString(), varBind.getVariable()));
		}

		snmpListener.add(requestId, valueList);

		return true;
	}

	@Override
	public void debugPrint(String s) {
		if (logger != null && logger.isTrace()) {
			logger.trace(s);
		} else {
			System.out.println(s);
		}
	}

}

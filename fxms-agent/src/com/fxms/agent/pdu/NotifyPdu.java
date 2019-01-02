package com.fxms.agent.pdu;

import java.util.HashMap;
import java.util.Map;

/**
 * FxAgent Notify PDU
 * 
 * @author SUBKJH-DEV
 *
 */
public class NotifyPdu extends FxAgentPdu {

	public NotifyPdu() {

	}

	public NotifyPdu(FxAgentPdu pdu, String method, Map<String, Object> parameters) {
		super(pdu);
		setMethod(method);
		setParameters(parameters);
	}

	@Override
	public Map<String, Object> getHeader() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pdu-type", PduType.notify.name());
		return map;
	}

}

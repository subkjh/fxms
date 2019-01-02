package com.fxms.agent.pdu;

import java.util.HashMap;
import java.util.Map;

/**
 * FxAgent 요청 PDU
 * 
 * @author SUBKJH-DEV
 *
 */
public class RequestPdu extends FxAgentPdu {

	public RequestPdu() {

	}

	public RequestPdu(FxAgentPdu pdu, String method, Map<String, Object> parameters) {
		super(pdu);
		setMethod(method);
		setParameters(parameters);
	}

	@Override
	public Map<String, Object> getHeader() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pdu-type", PduType.request.name());
		return map;
	}

}

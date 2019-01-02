package com.fxms.agent.pdu;

import java.util.HashMap;
import java.util.Map;

/**
 * FxAgent 응답 PDU
 * 
 * @author SUBKJH-DEV
 *
 */
public class ResponsePdu extends FxAgentPdu {

	private boolean ok;
	private String msg;

	public ResponsePdu() {

	}

	public boolean isOk() {
		return ok;
	}

	public String getMsg() {
		return msg;
	}

	public ResponsePdu(RequestPdu pdu, String errmsg) {
		super(pdu);
		this.msg = errmsg;
		ok = false;
	}

	public ResponsePdu(RequestPdu pdu, Map<String, Object> parameters) {
		super(pdu);
		setParameters(parameters);
		ok = true;
	}

	@Override
	public Map<String, Object> getHeader() {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pdu-type", PduType.response.name());
		map.put("result", ok ? "ok" : "error");
		map.put("msg", msg);

		return map;
	}

}

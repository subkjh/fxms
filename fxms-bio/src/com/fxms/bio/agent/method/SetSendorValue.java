package com.fxms.bio.agent.method;

import java.util.HashMap;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.pdu.FxAgentPdu;

import subkjh.bas.log.Logger;

public class SetSendorValue implements AgentMethod {

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {

		Object sensorId = pdu.getParaString("sensor-id");
		if (sensorId == null) {
			throw new Exception("sensor-id is not defined");
		}
		Object sensorValue = pdu.getParaString("sensor-value");
		if (sensorValue == null) {
			throw new Exception("sensor-value is not defined");
		}
		Object psCode = pdu.getParaString("sensor-ps-code");
		if (psCode == null) {
			throw new Exception("sensor-ps-code is not defined");
		}

		Logger.logger.info("TODO : set sensor value");

		Map<String, Object> para = new HashMap<String, Object>();

		para.put("sensor-id", sensorId);
		para.put("sensor-value", sensorValue);

		return para;
	}

	@Override
	public String getMethod() {
		return getClass().getSimpleName();
	}

}
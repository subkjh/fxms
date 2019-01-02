package com.fxms.bio.agent.method;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.pdu.FxAgentPdu;

import subkjh.bas.log.Logger;

public class GetSensorValue implements AgentMethod {

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {

		String sensorId = pdu.getParaString("sensor-id");
		if (sensorId == null) {
			throw new Exception("sensor-id is not defined");
		}

		List<?> psCodes = pdu.getParaList("ps-codes");
		if (psCodes == null) {
			throw new Exception("ps-code is not defined");
		}

		Map<String, Object> para = new HashMap<String, Object>();

		Logger.logger.info("TODO : get sensor value");

		para.put("sensor-id", sensorId);
		for (Object o : psCodes) {
			para.put(o.toString(), ((int) (Math.random() * 100)));
		}

		return para;
	}

	@Override
	public String getMethod() {
		return getClass().getSimpleName();
	}


}

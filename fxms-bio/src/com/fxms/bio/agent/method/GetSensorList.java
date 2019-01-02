package com.fxms.bio.agent.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.pdu.FxAgentPdu;

public class GetSensorList implements AgentMethod {

	private String containerId = "CON-TEST-001";

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		Map<String, Object> sensor;
		List<Map<String, Object>> sensorList = new ArrayList<Map<String, Object>>();

		para.put("sensor-list", sensorList);

		sensor = new HashMap<String, Object>();
		sensor.put("id", containerId + "." + "TEMP" + "." + "1");
		sensor.put("managed", true);
		sensorList.add(sensor);

		sensor = new HashMap<String, Object>();
		sensor.put("id", containerId + "." + "TEMP" + "." + "2");
		sensor.put("managed", true);
		sensorList.add(sensor);

		sensor = new HashMap<String, Object>();
		sensor.put("id", containerId + "." + "CO2" + "." + "1");
		sensor.put("managed", true);
		sensorList.add(sensor);

		sensor = new HashMap<String, Object>();
		sensor.put("id", containerId + "." + "HUMI" + "." + "1");
		sensor.put("managed", true);
		sensorList.add(sensor);

		sensor = new HashMap<String, Object>();
		sensor.put("id", containerId + "." + "PH" + "." + "1");
		sensor.put("managed", false);
		sensorList.add(sensor);

		return para;
	}

	@Override
	public String getMethod() {
		return getClass().getSimpleName();
	}


}

package com.fxms.bio.agent.method;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.FxAgentCode;
import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.pdu.FxAgentPdu;


public class NotifySensorValues implements AgentMethod {

	private float temp_1;
	private float temp_2;
	private float co2_1;
	private float ph_1;
	private String containerId;

	public NotifySensorValues(String containerId) {
		this.containerId = containerId;
	}

	private float getValue(float cur, float max) {

		if (cur == 0) {
			cur = (float) Math.random() * max;
		}

		float gap = (float) Math.random();
		boolean up = Math.random() >= 0.5;

		float ret = cur + (gap * (up ? 1 : -1));

		return (int) (ret * 10) / 10f;
	}

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();

		List<Map<String, Object>> psList = new ArrayList<Map<String, Object>>();

		Map<String, Object> ps = new HashMap<String, Object>();

		temp_1 = getValue(temp_1, 100);
		ps.put(FxAgentCode.PS_LIST__ATTR__MO_NAME, containerId + ".TEMP.1");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_CODE, "TEMP");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_VALUE, temp_1);
		psList.add(ps);

		ps = new HashMap<String, Object>();
		temp_2 = getValue(temp_2, 100);
		ps.put(FxAgentCode.PS_LIST__ATTR__MO_NAME, containerId + ".TEMP.2");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_CODE, "TEMP");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_VALUE, temp_2);
		psList.add(ps);

		ps = new HashMap<String, Object>();
		co2_1 = getValue(co2_1, 100);
		ps.put(FxAgentCode.PS_LIST__ATTR__MO_NAME, containerId + ".CO2.1");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_CODE, "CO2");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_VALUE, co2_1);
		psList.add(ps);

		ps = new HashMap<String, Object>();
		ph_1 = getValue(ph_1, 7);
		ps.put(FxAgentCode.PS_LIST__ATTR__MO_NAME, containerId + ".PH.1");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_CODE, "PH");
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_VALUE, ph_1);
		psList.add(ps);

		para.put("ps-list", psList);

		return para;
	}

	@Override
	public String getMethod() {
		return "ps-noti";
	}

}

package com.fxms.bio.agent.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.pdu.FxAgentPdu;

public class GetMethodList implements AgentMethod {

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();

		List<String> methodList = new ArrayList<String>();
		for (AgentMethod am : agent.getMethodMap().values()) {
			methodList.add(am.getMethod());
		}
		para.put("method-list", methodList);

		return para;
	}

	@Override
	public String getMethod() {
		return getClass().getSimpleName();
	}


}

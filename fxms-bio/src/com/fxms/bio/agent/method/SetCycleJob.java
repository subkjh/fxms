package com.fxms.bio.agent.method;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.pdu.FxAgentPdu;

public class SetCycleJob implements AgentMethod {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {

		Object script = pdu.getParameters().get("script");
		if (script == null) {
			throw new Exception("script is not defined");
		}

		if ((script instanceof List) == false) {
			throw new Exception("script is not List");

		}

		List<String> list = (List<String>) script;
		agent.getLooper().setScript(list);

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("script", "ok");
		return para;
	}

	@Override
	public String getMethod() {
		return getClass().getSimpleName();
	}


}

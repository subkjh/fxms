package com.fxms.bio.agent.method;

import java.util.HashMap;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.pdu.FxAgentPdu;

public class GetGwConfig implements AgentMethod {

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();

		para.put("remote-host", agent.getRemoteHost());
		para.put("remote-port", agent.getRemotePort());
		para.put("version", agent.getVersion());
		para.put("local-port", agent.getLocalPort());

		return para;
	}

	@Override
	public String getMethod() {
		return getClass().getSimpleName();
	}

}

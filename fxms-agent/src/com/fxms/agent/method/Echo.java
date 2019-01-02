package com.fxms.agent.method;

import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.pdu.FxAgentPdu;

public class Echo implements AgentMethod {

	@Override
	public String getMethod() {
		return AGENT_METHOD__ECHO;
	}

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {
		return pdu.getParameters();
	}

}

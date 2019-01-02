package com.fxms.agent.method;

import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.pdu.FxAgentPdu;

public class KeepAlive implements AgentMethod {

	public KeepAlive() {

	}

	@Override
	public String getMethod() {
		return AGENT_METHOD__KEEP_ALIVE;
	}

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {
		return null;
	}

}

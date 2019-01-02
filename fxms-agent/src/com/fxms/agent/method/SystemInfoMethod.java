package com.fxms.agent.method;

import java.util.HashMap;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.pdu.FxAgentPdu;
import com.fxms.agent.vo.SystemVo;

public class SystemInfoMethod  implements AgentMethod {

	public SystemInfoMethod() {

	}

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {

		SystemVo vo = new SystemVo();

		Map<String, Object> ret = new HashMap<String, Object>();

		ret.put("osName", vo.getOsName());
		ret.put("cpuArch", vo.getCpuArch());
		ret.put("cpuVersion", vo.getCpuVersion());

		return ret;
	}

	@Override
	public String getMethod() {
		return AGENT_METHOD__AGNET_INFO;
	}

}
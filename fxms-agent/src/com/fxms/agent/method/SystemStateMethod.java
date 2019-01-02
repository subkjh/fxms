package com.fxms.agent.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.FxAgentCode;
import com.fxms.agent.pdu.FxAgentPdu;
import com.fxms.agent.vo.SystemVo;

public class SystemStateMethod implements AgentMethod {

	public SystemStateMethod() {
	}

	@Override
	public Map<String, Object> call(FxAgent agent, FxAgentPdu pdu) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		List<Map<String, Object>> psList = new ArrayList<Map<String, Object>>();

		SystemVo vo = new SystemVo();

		psList.add(makePs("freePhysicalMemorySize", vo.getFreePhysicalMemorySize()));
		psList.add(makePs("freePhysicalMemorySize", vo.getFreeSwapSpaceSize()));
		psList.add(makePs("freeSwapSpaceSize", vo.getSystemCpuLoad()));
		psList.add(makePs("processCpuLoad", vo.getProcessCpuLoad()));
		psList.add(makePs("processCpuTime", vo.getProcessCpuTime()));
		psList.add(makePs("totalPhysicalMemorySize", vo.getTotalPhysicalMemorySize()));
		psList.add(makePs("totalSwapSpaceSize", vo.getTotalSwapSpaceSize()));
		psList.add(makePs("systemCpuUsage", vo.getSystemCpuUsage()));
		// psList.add(makePs("osName", vo.getOsName()));
		// psList.add(makePs("cpuArch", vo.getCpuArch()));
		// psList.add(makePs("cpuVersion", vo.getCpuVersion()));

		para.put("ps-list", psList);

		return para;
	}

	@Override
	public String getMethod() {
		return AGENT_METHOD__AGENT_STATE;
	}

	private Map<String, Object> makePs(String psCode, Object value) {
		HashMap<String, Object> ps = new HashMap<String, Object>();
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_CODE, psCode);
		ps.put(FxAgentCode.PS_LIST__ATTR__PS_VALUE, value);
		return ps;
	}
}

package com.fxms.agent.method;

import java.util.Map;

import com.fxms.agent.FxAgent;
import com.fxms.agent.pdu.FxAgentPdu;

/**
 * agent method
 * 
 * @author SUBKJH-DEV
 *
 */
public interface AgentMethod {

	public static final String AGENT_METHOD__ECHO = "echo";
	public static final String AGENT_METHOD__PS_NOTIFY = "ps-noti";
	public static final String AGENT_METHOD__AGENT_STATE = "agent-state";
	public static final String AGENT_METHOD__AGNET_INFO_NOTIFY = "agent-info-noti";
	public static final String AGENT_METHOD__AGNET_INFO = "agent-info";
	public static final String AGENT_METHOD__KEEP_ALIVE = "keep-alive";

	/**
	 * 
	 * @param agent
	 *            요청자
	 * @param reqPdu
	 *            요청 PDU
	 * @return 결과
	 * @throws Exception
	 */
	public Map<String, Object> call(FxAgent agent, FxAgentPdu reqPdu) throws Exception;

	/**
	 * 
	 * @return 기능명
	 */
	public String getMethod();

}

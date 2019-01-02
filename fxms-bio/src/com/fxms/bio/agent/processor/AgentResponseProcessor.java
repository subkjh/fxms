package com.fxms.bio.agent.processor;

import com.fxms.agent.FxAgentPduProcessor;
import com.fxms.agent.exception.FxAgentNotFoundException;
import com.fxms.agent.pdu.FxAgentPdu;
import com.fxms.agent.pdu.ResponsePdu;

import fxms.bas.exception.FxTimeoutException;
import subkjh.bas.log.Logger;

public class AgentResponseProcessor implements FxAgentPduProcessor {

	private ResponsePdu pdu = null;
	private boolean received = false;
	private String agent;

	public AgentResponseProcessor(String agent) {
		this.agent = agent;
	}

	public ResponsePdu getPdu() throws FxTimeoutException, FxAgentNotFoundException, Exception {
		return getPdu(10000);
	}

	public ResponsePdu getPdu(long timeout) throws FxTimeoutException, FxAgentNotFoundException, Exception {

		long ptime = System.currentTimeMillis() + timeout;

		while (System.currentTimeMillis() <= ptime) {

			if (pdu != null) {
				return pdu;
			}

			Thread.yield();

			if (received && pdu == null) {
				throw new FxAgentNotFoundException(agent);
			}
		}

		throw new FxTimeoutException(agent);
	}

	@Override
	public void onReceive(NotiStatus status, FxAgentPdu receivedPdu) {
		Logger.logger.debug("status={},pdu={}", status, receivedPdu);
		this.pdu = (ResponsePdu) pdu;
		received = true;
	}

}

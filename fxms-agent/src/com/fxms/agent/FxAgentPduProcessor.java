package com.fxms.agent;

import com.fxms.agent.pdu.FxAgentPdu;

/**
 * FxAgentManager가 받은 패킷을 전달받는 리슨터
 * 
 * @author SUBKJH-DEV
 *
 */
public interface FxAgentPduProcessor {

	public enum NotiStatus {

		/** 에이전트를 찾지 못함 */
		NotFoundAgent,

		/** 받은 PDU */
		RecvPdu,

		/** 받안 Notify PDU */
		NotifyPdu,

		/** 보내기 오류 */
		SendError

	}

	/**
	 * 받은 패킷을 처리한다.
	 * 
	 * @param status
	 *            상태
	 * @param receivedPdu
	 *            데이터
	 */
	public void onReceive(NotiStatus status, FxAgentPdu receivedPdu);
}

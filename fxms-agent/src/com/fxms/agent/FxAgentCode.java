package com.fxms.agent;

public class FxAgentCode {

	public static final String PS_LIST__ATTR__MO_NAME = "mo-name";
	public static final String PS_LIST__ATTR__MO_INSTANCE = "mo-instance";
	public static final String PS_LIST__ATTR__PS_CODE = "ps-code";
	public static final String PS_LIST__ATTR__PS_VALUE = "ps-value";

	public static final String charset = "utf-8";

	/** HoleAgent가 Notify를 보내는 주기 */
	public static int cycleNotifyAgent = 30;

	/** 가베지값을 처리할 에이전트 수 */
	public static int sizeAgentToCheck = 1000;

}

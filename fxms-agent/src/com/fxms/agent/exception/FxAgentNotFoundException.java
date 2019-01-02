package com.fxms.agent.exception;

/**
 * FxAgent를 못찾았을 때 발생하는 예외
 * 
 * @author SUBKJH-DEV
 *
 */
public class FxAgentNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6231381502306083258L;

	public FxAgentNotFoundException(String msg) {
		super(msg);
	}

}

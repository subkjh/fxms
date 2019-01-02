package com.fxms.nms.snmp.exception;

import fxms.bas.poller.exception.PollingTimeoutException;

public class SnmpTimeoutException extends PollingTimeoutException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8839577310178065520L;

	public SnmpTimeoutException(long moNo, String msg) {
		super(moNo, msg);
	}

	public SnmpTimeoutException(String msg) {
		super(0, msg);
	}
}

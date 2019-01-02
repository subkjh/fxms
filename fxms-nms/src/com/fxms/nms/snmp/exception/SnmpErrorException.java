package com.fxms.nms.snmp.exception;

public class SnmpErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4778452996242661651L;

	private int errIndex;

	private int errno;

	public SnmpErrorException(int errno, int errIndex, String errmsg) {
		super(errmsg);
		this.errIndex = errIndex;
		this.errno = errno;
	}

	public SnmpErrorException(String errmsg) {
		super(errmsg);
	}

	public int getErrIndex() {
		return errIndex;
	}

	public int getErrno() {
		return errno;
	}

}

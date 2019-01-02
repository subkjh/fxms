package com.fxms.ui;

public enum OP_TYPE {

	rep(0), ui(1), show(2), add(3), update(4), delete(5), dx(6), etc(9);

	private int code;

	private OP_TYPE(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static OP_TYPE getOpType(int code) {
		for (OP_TYPE type : OP_TYPE.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return etc;
	}
}

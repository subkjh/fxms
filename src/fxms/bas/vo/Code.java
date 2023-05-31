package fxms.bas.vo;

import java.io.Serializable;

public class Code implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3317364794358096358L;

	private final String cdClass;

	private final String cdName;

	private final String cdCode;

	public Code(String cdClass, String cdCode, String cdName) {
		this.cdClass = cdClass;
		this.cdCode = cdCode;
		this.cdName = cdName;
	}

	public String getCdClass() {
		return cdClass;
	}

	public String getCdName() {
		return cdName;
	}

	public String getCdCode() {
		return cdCode;
	}

}

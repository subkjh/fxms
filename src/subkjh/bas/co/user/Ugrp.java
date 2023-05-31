package subkjh.bas.co.user;

public class Ugrp {

	private final int ugrpNo;
	private final int inloNo;
	private final String ugrpName;

	public Ugrp(int ugrpNo, int inloNo, String ugrpName) {
		this.ugrpNo = ugrpNo;
		this.inloNo = inloNo;
		this.ugrpName = ugrpName;
	}

	public int getUgrpNo() {
		return ugrpNo;
	}

	public int getInloNo() {
		return inloNo;
	}

	public String getUgrpName() {
		return ugrpName;
	}

}

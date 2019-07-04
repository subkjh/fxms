package fxms.bas.co;

public class OpCode {

	private int opNo;

	private String opName;

	private int upperOpNo;

	private int userGroupNo;

	private String dataType;

	public OpCode(int opNo, String opName, int upperOpNo, int userGroupNo, String dataType) {
		this.opNo = opNo;
		this.opName = opName;
		this.upperOpNo = upperOpNo;
		this.userGroupNo = userGroupNo;
		this.dataType = dataType;
	}

	public String getDataType() {
		return dataType;
	}

	public String getOpName() {
		return opName;
	}

	public int getOpNo() {
		return opNo;
	}

	public int getUpperOpNo() {
		return upperOpNo;
	}

	public int getUserGroupNo() {
		return userGroupNo;
	}

}

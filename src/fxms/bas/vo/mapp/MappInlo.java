package fxms.bas.vo.mapp;

public class MappInlo {

	private final String mappId;
	private final int inloNo;

	public MappInlo(String mappId, int inloNo) {
		this.mappId = mappId;
		this.inloNo = inloNo;
	}

	public String getMappId() {
		return mappId;
	}

	public int getInloNo() {
		return inloNo;	
	}

}

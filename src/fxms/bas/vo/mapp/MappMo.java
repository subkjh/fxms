package fxms.bas.vo.mapp;

public class MappMo {

	private final String mappId;
	private final long moNo;

	public MappMo(String mappId, long moNo) {
		this.mappId = mappId;
		this.moNo = moNo;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getMappId() {
		return mappId;
	}

}

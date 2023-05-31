package fxms.bas.vo.mapp;

/**
 * 관리대상 + 수집ID = 맵핑ID<br>
 * 
 * @author subkjh
 *
 */
public class MappMoPs {

	private final long moNo;
	private final String psId;
	private final String psName;
	private final String mappId;

	public MappMoPs(long moNo, String psId, String psName, String mappId) {
		this.moNo = moNo;
		this.psId = psId;
		this.psName = psName;
		this.mappId = mappId;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsId() {
		return psId;
	}

	public String getMappId() {
		return mappId;
	}

	public String getPsName() {
		return psName;
	}

}

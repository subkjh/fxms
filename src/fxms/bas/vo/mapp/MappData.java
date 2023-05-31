package fxms.bas.vo.mapp;

public class MappData {

	private String mngDiv;
	private String mappData;
	private String mappDescr;
	private Object mappId;

	public MappData(String mngDiv, String mappData, String mappDescr, Object mappId) {
		this.mngDiv = mngDiv;
		this.mappData = mappData;
		this.mappDescr = mappDescr;
		this.mappId = mappId;
	}

	public String getMappData() {
		return mappData;
	}

	public String getMappDescr() {
		return mappDescr;
	}

	public String getMngDiv() {
		return mngDiv;
	}

	public Object getMappId() {
		return mappId;
	}

}

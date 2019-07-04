package fxms.bas.co.vo;


public class FxVar {

	private String name;

	private String value;

	public FxVar() {

	}

	public FxVar(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

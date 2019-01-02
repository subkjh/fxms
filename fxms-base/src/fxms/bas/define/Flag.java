package fxms.bas.define;

public class Flag {

	private static final String HIDE = "H";
	private static final String READONLY = "R";

	private String flag;

	public Flag() {
		flag = "";
	}

	public Flag(String flag) {
		this.flag = flag == null ? "" : flag;
	}

	public boolean isReadOnly() {
		return flag.contains(READONLY);
	}

	public boolean isHide() {
		return flag.contains(HIDE);
	}
}

package fxms.bas.signal;

public class ReloadSignal extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1045357442507326176L;

	public static final String RELOAD_TYPE_ALL = "ALL";
	public static final String RELOAD_TYPE_ALARM = "ALARM";
	public static final String RELOAD_TYPE_MO = "MO";
	public static final String RELOAD_TYPE_CFG = "CFG";
	public static final String RELOAD_TYPE_VAR = "VAR";

	public ReloadSignal() {
		this(RELOAD_TYPE_ALL);
	}

	public ReloadSignal(String reloadType) {
		super(ReloadSignal.class.getSimpleName(), reloadType);
	}

	public boolean contains(String... types) {

		for (String type : types) {
			if (type.equals(getReloadType())) {
				return true;
			}
		}

		return false;
	}

	public String getReloadType() {
		return getTarget().toString();
	}
}

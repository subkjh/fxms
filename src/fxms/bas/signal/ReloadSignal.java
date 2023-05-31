package fxms.bas.signal;

public class ReloadSignal extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1045357442507326176L;

	public enum ReloadType {

		None, All, Alarm, Mo, AlarmCfg, AlarmCode, Var, Op, User, PsItem, Code, Inlo, Model, Adapter, MappData,
		CurValue;

		public static ReloadType getReloadType(String name) {
			for (ReloadType e : ReloadType.values()) {
				if (e.name().equals(name)) {
					return e;
				}
			}

			return None;
		}

	}

	private final Enum<?> reloadType;
	private final Object updatedObject;

	public ReloadSignal(Enum<?> reloadType) {
		super(ReloadSignal.class.getSimpleName(), reloadType.name());
		this.reloadType = reloadType;
		this.updatedObject = null;
	}

	public ReloadSignal(Enum<?> reloadType, Object updatedObject) {
		super(ReloadSignal.class.getSimpleName(), reloadType.name());
		this.reloadType = reloadType;
		this.updatedObject = updatedObject;
	}

	public Enum<?> getReloadType() {
		return reloadType;
	}

	public Object getUpdatedObject() {
		return updatedObject;
	}

}

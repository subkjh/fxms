package subkjh.bas.co.log;

/**
 * 
 * @author subkjh
 *
 */
public enum LOG_LEVEL {

	trace(1)

	, debug(2)

	, info(3);

	private int num;

	private LOG_LEVEL(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public boolean contains(LOG_LEVEL n) {
		return num >= n.num;
	}

	public boolean contains(int num) {
		return this.num >= num;
	}

	public static void main(String[] args) {
		System.out.println(LOG_LEVEL.trace.contains(LOG_LEVEL.trace));
		System.out.println(LOG_LEVEL.debug.contains(LOG_LEVEL.trace));
		System.out.println(LOG_LEVEL.info.contains(LOG_LEVEL.trace));
		System.out.println(LOG_LEVEL.trace.contains(LOG_LEVEL.info));

	}

	public static LOG_LEVEL getLevel(String name) {
		for (LOG_LEVEL e : LOG_LEVEL.values()) {
			if (e.name().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}

}

package subkjh.bas.dao.define;

public enum INDEX_TYPE {

	PK, UK, FK, KEY;

	public static INDEX_TYPE getType(String name) {
		for (INDEX_TYPE key : INDEX_TYPE.values()) {
			if (key.name().equalsIgnoreCase(name))
				return key;
		}
		return null;
	}

}

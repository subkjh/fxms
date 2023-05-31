package fxms.bas.impl.dpo;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.exp.AttrNotFoundException;
import subkjh.bas.co.lang.Lang;

public class FxFact extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5137858316601944423L;

	public FxFact(Map<String, Object> para) {
		super(para);
	}

	public FxFact(Object... objects) {

		for (int i = 1; i < objects.length; i += 2) {
			put(objects[i - 1].toString(), objects[i]);
		}

	}

	public FxFact(int userNo) {
		put("userNo", userNo);
	}

	public int getUserNo() throws Exception {
		Object val = get("userNo");
		Integer userNo = toInt(val);
		if (userNo == null) {
			return 0;
		}
		return userNo;
	}

	public long getMoNo() throws Exception {
		Object val = get("moNo");
		Long moNo = toLong(val);
		if (moNo == null) {
			throw new Exception(Lang.get("No parameter has been specified.", "moNo"));
		}
		return moNo;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> classOf, String name) throws Exception {
		Object val = get(name);
		if (val == null) {
			throw new Exception(Lang.get("No parameter has been specified.", name));
		}
		if (classOf.isInstance(val) == false) {
			throw new Exception(Lang.get("Not the requested object instance.", classOf.getName()));
		}

		return (T) val;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> classOf) throws Exception {
		for (Object val : this.values()) {
			if (classOf.isInstance(val)) {
				return (T) val;
			}
		}
		return null;
	}

	public String getString(String name) {
		Object val = get(name);
		return val == null ? null : val.toString();
	}

	public boolean isTrue(String name) {
		Object val = get(name);
		return toBoolean(val);
	}

	public long getLong(String name) throws AttrNotFoundException {
		Object val = get(name);
		Long ret = toLong(val);

		if (ret == null) {
			throw new AttrNotFoundException(name);
		}

		return ret.longValue();
	}

	public int getInt(String name) throws AttrNotFoundException {
		Object val = get(name);
		Integer ret = toInt(val);

		if (ret == null) {
			throw new AttrNotFoundException(name);
		}

		return ret.intValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> getMap(String name) {
		Object val = get(name);
		if (val instanceof Map) {
			return (Map) val;
		}
		return null;
	}

	private Integer toInt(Object val) {
		if (val != null) {
			if (val instanceof Number) {
				return ((Number) val).intValue();
			} else {
				try {
					return Integer.parseInt(val.toString());
				} catch (Exception e) {
				}
			}
		}

		return null;
	}

	private Long toLong(Object val) {
		if (val != null) {
			if (val instanceof Number) {
				return ((Number) val).longValue();
			} else {
				try {
					return Long.parseLong(val.toString());
				} catch (Exception e) {
				}
			}
		}

		return null;
	}

	private Boolean toBoolean(Object val) {
		if (val != null) {
			if (val instanceof Boolean) {
				return ((Boolean) val).booleanValue();
			} else {
				String s = val.toString();
				if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("true")) {
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}
}

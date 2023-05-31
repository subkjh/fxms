package fxms.bas.handler.vo;

import java.util.Map;

public class HandlerPDU {

	protected long getLong(Map<String, Object> map, String name, long defVal) {
		Object val = map.get(name);
		if (val == null)
			return defVal;

		if (val instanceof Number) {
			return ((Number) val).longValue();
		} else {
			return Double.valueOf(val.toString()).longValue();
		}
	}

	protected int getInt(Map<String, Object> map, String name, int defVal) {
		Object val = map.get(name);
		if (val == null)
			return defVal;

		if (val instanceof Number) {
			return ((Number) val).intValue();
		} else {
			return Double.valueOf(val.toString()).intValue();
		}
	}

	protected String getString(Map<String, Object> map, String name) {
		Object val = map.get(name);
		if (val == null)
			return null;
		return val.toString();
	}
}

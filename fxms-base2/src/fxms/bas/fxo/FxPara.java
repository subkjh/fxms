package fxms.bas.fxo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

public class FxPara {

	class COM {
		/** 필드명 */
		private String javaFieldName;
		/** 메소드명 */
		private String stringMethod;
		/** 메소드 결과가 일치할 경우 */
		private boolean result;
		/** 비교 값 */
		private String stringValue;

		/**
		 * 입력된 객체가 속성 조건이 맞는지 확인합니다.
		 * 
		 * @param target 속성을 비교할 객체
		 * @return 조건이 맞으면 true
		 */
		boolean match(Object obj) {

			Object value;
			try {
				value = ObjectUtil.get(obj, javaFieldName);
			} catch (Exception e) {
				return result == false ? true : false;
			}

			if (value == null)
				return result == false ? true : false;

			String s = value.toString();

			return matchStr(s);
		}

		boolean matchStr(String s) {

			if (stringMethod.equals("equals") && stringValue.equals(s) && result) {
				return true;
			} else if (stringMethod.equals("contains") && stringValue.contains(s) && result) {
				return true;
			} else if (stringMethod.equals("startsWith") && stringValue.startsWith(s) && result) {
				return true;
			} else if (stringMethod.equals("endsWith") && stringValue.endsWith(s) && result) {
				return true;
			} else if (stringMethod.equals("equalsIgnoreCase") && stringValue.equalsIgnoreCase(s) && result) {
				return true;
			} else if (stringMethod.equals("matches") && stringValue.matches(s) && result) {
				return true;
			}

			if (result)
				return false;
			else
				return true;
		}

	}

	private List<COM> comList;
	private boolean isAllMatch = false;
	private Map<String, Object> map;

	/**
	 * 
	 * @param field
	 * @param method
	 * @param value
	 * @param result
	 */
	public void add(String field, String method, String value, boolean result) {
		COM com = new COM();
		com.javaFieldName = field;
		com.stringMethod = method;
		com.stringValue = value;
		com.result = result;
		if (comList == null) {
			comList = new ArrayList<COM>();
		}

		comList.add(com);
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public Object get(String name) {
		if (map == null) {
			return null;
		}
		return map.get(name);
	}

	public int getInt(String name, int defVal) {

		Object s = get(name);

		if (s == null) {
			return defVal;
		}

		if (s instanceof Number) {
			return ((Number) s).intValue();
		} else {
			try {
				return Double.valueOf(s.toString()).intValue();
			} catch (Exception e) {
				return defVal;
			}
		}
	}

	public long getLong(String name, long defVal) {

		Object s = get(name);

		if (s == null) {
			return defVal;
		}

		if (s instanceof Number) {
			return ((Number) s).longValue();
		} else {
			try {
				return Double.valueOf(s.toString()).longValue();
			} catch (Exception e) {
				return defVal;
			}
		}
	}

	public boolean getBoolean(String name, boolean defVal) {
		Object s = get(name);
		if (s == null) {
			return defVal;
		}
		return "true".equalsIgnoreCase(s.toString());
	}

	public String getString(String name) {

		Object s = get(name);

		if (s == null) {
			return null;
		}
		return s.toString();
	}

	public boolean match(Object obj) {

		if (comList == null || comList.size() == 0)
			return true;

		if (isAllMatch) {
			for (COM c : comList) {
				if (c.match(obj) == false)
					return false;
			}

			return true;
		} else {
			for (COM c : comList) {
				if (c.match(obj)) {
					return true;
				}
			}

			return false;
		}
	}

	/**
	 * 속성으로 매칭되는지 확인한다.
	 * 
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	public boolean matchAttr(String attrName, String attrValue) {

		if (comList == null || comList.size() == 0)
			return false;

		if (isAllMatch) {
			for (COM c : comList) {
				if (c.javaFieldName.equals(attrName) != false) {
					return false;
				}
				if (c.matchStr(attrValue) == false) {
					return false;
				}
			}
			return true;
		} else {
			for (COM c : comList) {
				if (c.javaFieldName.equals(attrName) && c.matchStr(attrValue)) {
					return true;
				}
			}
		}

		return false;
	}

	public void setAllMatch(boolean isAllMatch) {
		this.isAllMatch = isAllMatch;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(String name, Object value) {

		if (map == null) {
			map = new HashMap<String, Object>();
		}

		Object v = map.get(name);
		if (v == null) {
			map.put(name, value);
		} else {
			if (v instanceof List) {
				((List) v).add(value);
			} else {
				List list = new ArrayList();
				list.add(v);
				list.add(value);
				map.put(name, list);
			}
		}
	}

	@Override
	public String toString() {
		if (map == null) {
			return "nothing";
		}

		StringBuffer sb = new StringBuffer();
		Object value;
		for (String key : map.keySet()) {
			value = map.get(key);
			sb.append(Logger.makeSubString(key, value));
		}
		return sb.toString();
	}

}

package fxms.bas.fxo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.utils.ObjectUtil;

/**
 * FxMS Filter, Adapter
 * 
 * @author subkjh
 *
 */
public class FxActorImpl implements FxActor {

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
		 * @param target
		 *            속성을 비교할 객체
		 * @return 조건이 맞으면 true
		 */
		public boolean match(Object obj) {

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

		public boolean matchStr(String s) {

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

	private String name;
	private List<COM> comList;
	private boolean isAllMatch = false;
	private Map<String, Object> parameters;

	/**
	 * 
	 * @param field
	 * @param method
	 * @param value
	 * @param result
	 */
	public void addCondition(String field, String method, String value, boolean result) {
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

	public String getName() {
		return name;
	}

	public Map<String, Object> getPara() {
		return parameters;
	}

	public Object getPara(String name) {
		if (parameters == null) {
			return null;
		}
		return parameters.get(name);
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

	/**
	 * 클래스가 생성되고 인수 및 조건이 모두 적용된 후에 호출된다.
	 */
	public void onCreated() {

	}

	public void setAllMatch(boolean isAllMatch) {
		this.isAllMatch = isAllMatch;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setPara(String name, Object value) {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		Object v = parameters.get(name);
		if (v == null) {
			parameters.put(name, value);
		} else {
			if (v instanceof List) {
				((List) v).add(value);
			} else {
				List list = new ArrayList();
				list.add(v);
				list.add(value);
				parameters.put(name, list);
			}
		}
	}

	protected int getParaInt(String name, int defVal) {

		Object s = getPara(name);

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

	protected long getParaLong(String name, long defVal) {

		Object s = getPara(name);

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

	protected String getParaStr(String name) {

		Object s = getPara(name);

		if (s == null) {
			return null;
		}
		return s.toString();
	}

}

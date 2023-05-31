package fxms.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoRaw;
import fxms.rule.condition.FxRuleCondition;

/**
 * 비즈니스 규칙 엔진의 요구사항은 크게 4가지로 나뉜다.<br>
 * <br>
 * 팩트 : 규칙이 확인할 수 있는 정보<br>
 * 액션 : 수행하려는 동작<br>
 * 조건 : 액션을 언제 발생시킬지 지정<br>
 * 규칙 : 실행하려는 비즈니스 규칙을 지정<br>
 * 보통 팩트/액션/조건을 한 그룹으로 묶어서 규칙으로 만든다.<br>
 * 
 * 팩트 클래스<br>
 * 
 * @author subkjh
 * @since 2023.02
 */
public class FxRuleFact extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4298330571696795996L;

	public static void main(String[] args) {
		FxRuleFact p = new FxRuleFact();
		p.addPayload("a.b.c.d", 1);
		p.addPayload("a.aa", 2);
		p.addPayload("a", 3);
		p.addPayload("a.abcd", 4);
		System.out.println(p);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(Map<String, Object> map, String name, Object value) {
		Object oldVal = map.get(name);
		if (oldVal == null) {
			map.put(name, value);
		} else if (oldVal instanceof List) {
			((List) oldVal).add(value);
		} else {
			List list = new ArrayList<>();
			list.add(oldVal);
			list.add(value);
			map.put(name, list);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addMap(Map<String, Object> objMap, String name, Object value) {
		String ss[] = name.split("\\.");
		if (ss.length == 1) {
			add(objMap, name, value);
		} else {
			Map<String, Object> map = objMap;
			for (int i = 0; i < ss.length - 1; i++) {
				Object val = map.get(ss[i]);
				if (val == null) {
					Map<String, Object> child = new HashMap<String, Object>();
					map.put(ss[i], child);
					map = child;
				} else if (val instanceof Map) {
					map = (Map) val;
				}
			}
			map.put(ss[ss.length - 1], value);
		}
	}

	@SuppressWarnings("rawtypes")
	private Object get(Map<String, Object> map, String name) {

		String ss[] = name.split("\\.");
		Object value = map.get(ss[0]);

		if (ss.length == 1)
			return value;
		for (int i = 1; i < ss.length; i++) {
			if (value instanceof Map) {
				value = ((Map) value).get(ss[i]);
				if (value != null && ss.length == i + 1) {
					return value;
				}
			} else {
				break;
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> getMap(String name) {
		Map<String, Object> map;

		Object value = get(name);
		if (value instanceof Map) {
			map = (Map) value;
		} else {
			map = new HashMap<String, Object>();
			put(name, map);
		}

		return map;
	}

	private boolean isOk(String str) {
		return "true".equalsIgnoreCase(str.split(",")[1]);
	}

	/**
	 * header에 변수와 값을 더한다.<br>
	 * 이미 존재하는 변수이면 값을 배열로 변경하여 데이터 넣는다.<br>
	 * 
	 * @param name
	 * @param value
	 */
	public void addHeader(String name, Object value) {
		addMap(getHeader(), name, value);
	}

	/**
	 * payload에 변수와 값을 더한다.<br>
	 * 이미 존재하는 변수이면 값을 배열로 변경하여 데이터 넣는다.<br>
	 * 
	 * @param name
	 * @param value
	 */
	public void addPayload(String name, Object value) {
		addMap(getPayload(), name, value);
	}

	/**
	 * 수집데이터를 payload의 psValue로 값을 추가한다.
	 * 
	 * @param value
	 */
	public void addPsValue(PsVoRaw value) {
		add(getPayload(), "psValue", value);
	}

	/**
	 * 룰 처리 결과를 추가한다.
	 * 
	 * @param condition
	 * @param action
	 */
	public void addRuleResult(FxRuleCondition<?> condition, FxRuleAction action) {
		String s = condition == null ? "null" : condition.getClass().getSimpleName();
		String a = action == null ? "null" : action.getClass().getSimpleName();

		this.addHeader("rule", s + "=" + a);
	}

	private Map<String, Object> getHeader() {
		return getMap("header");
	}

	/**
	 * header에 있는 변수값을 조회한다.
	 * 
	 * @param name
	 * @return
	 */
	public Object getHeader(String name) {
		return get(getHeader(), name);
	}

	/**
	 * payload 내용을 제공한다.
	 * 
	 * @return
	 */
	public Map<String, Object> getPayload() {
		return getMap("payload");
	}

	public Object getPayload(String name) {
		return get(getPayload(), name);
	}

	/**
	 * 수집된 원천데이터
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<PsVoRaw> getPsRawValues() {

		Object value = getPayload("psValue");
		if (value != null) {

			List<PsVoRaw> valueList = new ArrayList<PsVoRaw>();

			if (value instanceof PsVoRaw) {
				valueList.add((PsVoRaw) value);
			} else if (value instanceof List) {
				for (Object o : ((List) value)) {
					if (o instanceof PsVoRaw) {
						valueList.add((PsVoRaw) o);
					}
				}
			}

			return valueList.size() == 0 ? null : valueList;

		}
		return null;
	}

	/**
	 * 정재된 수집 데이터
	 * 
	 * @return
	 */
	public List<PsVo> getPsValues() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	public int getRunRuleCnt() {

		Object val = this.getHeader().get("rule");
		if (val instanceof String) {
			return isOk(val.toString()) ? 1 : 0;
		} else if (val instanceof List) {
			int cnt = 0;
			for (Object s : (List) val) {
				if (isOk(s.toString()))
					cnt++;
			}
			return cnt;
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public int getTotRuleCnt() {
		Object val = this.getHeader().get("rule");
		if (val instanceof String) {
			return 1;
		} else if (val instanceof List) {
			return ((List) val).size();
		}
		return 0;
	}
}

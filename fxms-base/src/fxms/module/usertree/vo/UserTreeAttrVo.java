package fxms.module.usertree.vo;

import java.util.Arrays;
import java.util.Map;

import fxms.module.usertree.dbo.FX_UR_TREE_ATTR;
import subkjh.bas.utils.ObjectUtil;

/**
 * 트리를 구성하는 속성
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserTreeAttrVo extends FX_UR_TREE_ATTR {

	/**
	 * 쿼리용 조건을 추가한다.
	 * 
	 * @param para
	 *            조건 맵
	 */
	public void addWhereParameter(Map<String, Object> para) {
		String columnName = getJavaFieldName();

		if ("contains".equals(getStringMethod())) {
			para.put(columnName + " like ", "%" + getStringValue() + "%");
		} else if ("equals".equals(getStringMethod())) {
			para.put(columnName, getStringValue());
		} else if ("equalsIgnoreCase".equals(getStringMethod())) {
			para.put(columnName, getStringValue());
		} else if ("startsWith".equals(getStringMethod())) {
			para.put(columnName + " like ", getStringValue() + "%");
		} else if ("matches".equals(getStringMethod())) {
			para.put(columnName, getStringValue());
		} else if ("empty".equals(getStringMethod())) {
			para.put(columnName, getStringValue());
		} else if ("in".equals(getStringMethod())) {
			para.put(columnName + " in ", Arrays.asList(getStringValue().split(",")));
		}

	}

	/**
	 * 데이터가 이 조건과 일치하는지 확인한다.
	 * 
	 * @param data
	 *            비교할 데이터
	 * @return 일치
	 */
	public boolean match(Object data) {
		String value;
		Object valueObj;
		boolean match = false;

		valueObj = ObjectUtil.get(data, getJavaFieldName());
		value = valueObj == null ? null : valueObj.toString();

		match = false;
		if (value == null) {
			if ("empty".equals(getStringMethod())) {
				match = true;
			}
		} else {
			if ("contains".equals(getStringMethod())) {
				match = value.contains(getStringValue());
			} else if ("equals".equals(getStringMethod())) {
				match = value.equals(getStringValue());
			} else if ("equalsIgnoreCase".equals(getStringMethod())) {
				match = value.equalsIgnoreCase(getStringValue());
			} else if ("startsWith".equals(getStringMethod())) {
				match = value.startsWith(getStringValue());
			} else if ("matches".equals(getStringMethod())) {
				match = value.matches(getStringValue());
			} else if ("empty".equals(getStringMethod())) {
				match = value.length() == 0;
			} else if ("in".equals(getStringMethod())) {
				String ss[] = getStringValue().split(",");
				match = false;
				for (String s : ss) {
					if (value.equals(s.trim())) {
						match = true;
						break;
					}
				}
			}
		}

		return match;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getJavaFieldName());
		sb.append(".");
		sb.append(getStringMethod());
		sb.append("(");
		sb.append(getStringValue());
		sb.append(")");

		return sb.toString();
	}

}

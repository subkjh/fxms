package fxms.module.chassis.beans;

import java.lang.reflect.Method;

import com.fxms.nms.mo.NeIfMo;

import fxms.bas.mo.Mo;

/**
 * 실장도 해당 MO 조건
 * 
 * @author subkjh
 * 
 */
public class MoAttr {

	public static void main(String[] args) {
		MoAttr attr = new MoAttr();
		attr.field = "ifName";
		attr.moClass = NeIfMo.MO_CLASS;
		attr.method = "equals";
		attr.value = "%ifName%";
		// attr.value = "test";

		NeIfMo mo = new NeIfMo();

		mo.setIfName("A");
		System.out.println(attr.match(null, mo));

		mo.setIfName("aaaaaaaaaaaaadfa341");
		System.out.println(attr.match(mo, mo));

		mo.setIfName("test");
		System.out.println(attr.match(null, mo));

	}

	private String moClass;
	private String field;
	private String method;
	private String value;
	private boolean result = true;

	public MoAttr() {

	}

	/**
	 * 
	 * @param moClass
	 *            MO분류
	 * @param field
	 *            MO필드명
	 * @param method
	 *            String메소드
	 * @param value
	 *            비교값
	 * @param result
	 *            결과가 true false인지
	 * @throws Exception
	 */

	public MoAttr(String moClass, String field, String method, String value, String result) throws Exception {

		if (isNull(moClass))
			throw new Exception("moClass is null");
		if (isNull(field))
			throw new Exception("field is null");
		if (isNull(method)) {
			method = "equals";
		}
		if (isNull(result)) {
			this.result = true;
		} else {
			this.result = result.equalsIgnoreCase("result");
		}

		this.moClass = moClass;
		this.field = field;
		this.method = method;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public String getMethod() {
		return method;
	}

	public String getMoClass() {
		return moClass;
	}

	public String getValue() {
		return value;
	}

	/**
	 * MO를 이용하여 이 속성의 값을 조회합니다.<br>
	 * 값이 '%'로 묶여 있으면 MO 속성에서 찾아 대체합니다.
	 * 
	 * @param mo
	 *            MO
	 * @return 이 속성의 값
	 */
	private String getValue(Mo mo) {

		if (mo == null)
			return value;

		char chs[] = value.toCharArray();
		if (chs[0] == '%' && chs[chs.length - 1] == '%') {
			try {
				return Mo.getValue(mo, value.substring(1, value.length() - 1)) + "";
			} catch (Exception e) {
			}
		}
		return value;
	}



	/**
	 * 관리대상에 맞는 속성인지 여부
	 * 
	 * @param parent
	 *            부모MO
	 * @param mo
	 *            MO
	 * @return 맞는지 여부
	 */
	public boolean match(Mo parent, Mo mo) {

		if (mo.getMoClass().startsWith(moClass) == false)
			return false;

		Object valObj;
		try {
			valObj = Mo.getValue(mo, field);
		} catch (Exception e) {
			return false;
		}

		if (valObj == null)
			return false;
		String valueMo = valObj.toString();

		String value = getValue(parent);

		Method method;
		try {
			method = valueMo.getClass().getMethod(this.method, String.class);
			if (method != null)
				return invoke(method, value, valueMo);
		} catch (Exception e1) {
			Method methodArray[] = valueMo.getClass().getMethods();
			for (Method m : methodArray) {
				if (m.getName().equals(this.method)) {
					if (m.getParameterTypes().length == 1 && m.getParameterTypes()[0].isInstance(value)) {
						return invoke(m, valueMo, value);
					}
				}
			}
		}

		return false;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return moClass + "|" + field + "|" + method + "|" + value;
	}

	private boolean invoke(Method method, String nodeValue, String valueCompare) {

		try {
			Object obj = method.invoke(nodeValue, valueCompare);
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue() == result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean isNull(String s) {
		return s == null || s.trim().length() == 0;
	}

}

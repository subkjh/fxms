package fxms.rule.condition;

import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import fxms.rule.RuleApi;

/**
 * 
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "값 조건 확인", descr = "payload안의 설정한 name의 값을 입력한 값과 비교하여 참, 거짓을 확인한다.")
public class FxConditionIf extends FxRuleConditionImpl {

	@FxAttr(text = "변수명", description = "payload에서 찾을 변수명을 지정한다.")
	private String var;

	@FxAttr(text = "비교할 값", description = "비교할 값을 입력한다.")
	private Object value;

	@FxAttr(text = "비교 조건", description = "비교 조건을 입력한다. ( ==, !=, >, >=, <, <= 중 하나)", required = false)
	private String condition;

	public static void main(String[] args) throws Exception {
		FxConditionIf c = new FxConditionIf(RuleApi.makePara("name", "aaa", "condition", "==", "value", "korea"));
		System.out.println(c.checkString("aaaa"));
		System.out.println(c.checkString("subkjh"));
		System.out.println(c.checkString("korea"));
	}

	public FxConditionIf(Map<String, Object> map) throws Exception {
		super(map);
		if (condition == null) {
			condition = "==";
		}
	}

	private boolean checkBoolean(Boolean value) {
		try {
			Boolean bol;
			if (this.value instanceof Boolean) {
				bol = (Boolean) this.value;
			} else {
				bol = "true".equalsIgnoreCase(String.valueOf(this.value));
			}
			return ((condition.equals("==")) && value == bol) || ((condition.equals("!=")) && value != bol);
		} catch (Exception e) {
			return checkString(value.toString());
		}
	}

	private boolean checkNumber(Number value) {
		try {
			Number num;
			if (this.value instanceof Number) {
				num = (Number) this.value;
			} else {
				num = Float.valueOf(this.value.toString());
			}

			return ((condition.equals(">")) && value.doubleValue() > num.doubleValue()) ||

					((condition.equals(">=")) && value.doubleValue() >= num.doubleValue()) ||

					((condition.equals("<")) && value.doubleValue() < num.doubleValue()) ||

					((condition.equals("<=")) && value.doubleValue() <= num.doubleValue()) ||

					((condition.equals("==")) && value.doubleValue() == num.doubleValue()) ||

					((condition.equals("!=")) && value.doubleValue() != num.doubleValue());

		} catch (Exception e) {
			return checkString(value.toString());
		}
	}

	private boolean checkString(String value) {

		String str;
		if (this.value instanceof String) {
			str = (String) this.value;
		} else {
			str = this.value.toString();
		}

		return ((condition.equals(">")) && value.compareTo(str) > 0) ||

				((condition.equals(">=")) && value.compareTo(str) >= 0) ||

				((condition.equals("<")) && value.compareTo(str) < 0) ||

				((condition.equals("<=")) && value.compareTo(str) <= 0) ||

				((condition.equals("==")) && value.compareTo(str) == 0) ||

				((condition.equals("!=")) && value.compareTo(str) != 0);
	}

	@Override
	public Boolean evaluate(FxRuleFact fact) {

		// payload에서 값 가져오기
		Object value = fact.getPayload(var);

		// 자료형에 맞게 비교하기
		if (value instanceof Number) {
			return checkNumber((Number) value);
		} else if (value instanceof Boolean) {
			return checkBoolean((Boolean) value);
		} else if (value instanceof String) {
			return checkString((String) value);
		}

		return false;
	}
}

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
@FxRuleActionInfo(name = "범위값 확인", descr = "payload안의 설정한 name의 값을 입력한 범위에 포함되는지 확인한다.")
public class FxConditionRange extends FxRuleConditionImpl {

	@FxAttr(text = "변수명", description = "payload에서 찾을 변수명을 지정한다.")
	private String name;

	@FxAttr(text = "시작범위", description = "데이터의 시작 범위를 지정한다.")
	private Object min;

	@FxAttr(text = "종료범위", description = "데이터의 종료 범위를 지정한다.")
	private Object max;

	public static void main(String[] args) throws Exception {
		FxConditionRange c = new FxConditionRange(RuleApi.makePara("name", "aaa", "min", "korea", "max", "subkjh"));
		System.out.println(c.checkString("aaaaa"));
		System.out.println(c.checkString("bbbbb"));
		System.out.println(c.checkString("zzzzz"));
		System.out.println(c.checkString("mamama"));
	}

	public FxConditionRange(Map<String, Object> map) throws Exception {
		super(map);
	}

	private boolean checkNumber(Number value) {
		try {
			Number min = RuleApi.getNumber(this.min);
			Number max = RuleApi.getNumber(this.max);

			return value.floatValue() >= min.floatValue() && value.floatValue() <= max.floatValue();

		} catch (Exception e) {
			return checkString(value.toString());
		}
	}

	private boolean checkString(String value) {

		String min = RuleApi.getString(this.min);
		String max = RuleApi.getString(this.max);

		return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
	}

	@Override
	public Boolean evaluate(FxRuleFact fact) {

		// 변수값 가져오기
		Object value = fact.getPayload(name);

		// 범위 비교
		if (value instanceof Number) {
			return checkNumber((Number) value);
		} else if (value instanceof String) {
			return checkString((String) value);
		}

		return false;
	}
}

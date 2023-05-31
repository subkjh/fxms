package fxms.rule.action;

import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * 
 * @author subkjh
 * @since 2023.02
 * 
 */
@FxRuleActionInfo(name = "랜덤값 추가", descr = "설정한 범위에서 랜덤하게 값을 구해 지정한 payload의 변수에 넣는다.")
public class TestAction extends FxRuleActionImpl {

	@FxAttr(text = "변수명", description = "임의로 생성된 값을 기록할 변수를 입력한다.")
	private String var;

	@FxAttr(text = "범위 시작값", description = "데이터의 시작 범위를 지정한다", required = false)
	private int min;

	// 초기값을 주면 클래스가 생성된 후에 다시 설정하게 되므로 초기값을 주기 않는다.
	@FxAttr(text = "범위 종료값", description = "데이터의 종료 범위를 지정한다.", required = false)
	private int max;

	public TestAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		int value = (int) (Math.random() * max);
		if (value < min) {
			value = min;
		}

		fact.addPayload(var, value);

	}
}

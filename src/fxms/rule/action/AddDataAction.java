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
 */
@FxRuleActionInfo(name = "데이터추가", descr = "설정한 변수와 값을 payload에 추가한다.")
public class AddDataAction extends FxRuleActionImpl {

	@FxAttr(text = "변수명", description = "값이 기록된 변수명을 입력한다.")
	private String var;

	@FxAttr(text = "추가할 값", description = "추가할 값을 입력한다.")
	private Object value;

	public AddDataAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		// 단순히 payload에 값을 추가한다.
		fact.addPayload(var, value);

	}
}

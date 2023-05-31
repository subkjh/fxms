package fxms.rule.action;

import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * payload 내용을 출력한다.
 * 
 * @author subkjh
 * @since 2023.02
 * 
 */
@FxRuleActionInfo(name = "payload JSON으로 출력", descr = "payload 내용을 JSON 형식으로 출력한다.")
public class PrintAction extends FxRuleActionImpl {

	public PrintAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		System.out.println(FxmsUtil.toJson(fact.getPayload()));

	}

}

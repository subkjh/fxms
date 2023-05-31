package fxms.rule.action.mo;

import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxAttr;
import fxms.bas.mo.Mo;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import fxms.rule.RuleApi;

/**
 * 관리대상을 조회하는 액션
 * 
 * @author subkjh
 *
 */
@FxRuleActionInfo(name = "관리대상조회", descr = "관리번호를 이용하여 관리대상을 조회한다.")
public class SelectMoAction extends FxRuleActionImpl {

	public SelectMoAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@FxAttr(text = "관리대상번호", description = "관리대상번호를 숫자로 입력한다.")
	private Long moNo;

	@FxAttr(text = "변수명", description = "관리대상를 가르키는 변수를 입력한다.")
	private String var;

	@Override
	public void execute(FxRuleFact inObj) throws Exception {

		// 사용중인 관리대상을 조회한다.
		List<Mo> list = MoApi.getApi().getMoList(RuleApi.makePara("moNo", moNo, "delYn", "N"));

		if (list.size() > 0) {
			inObj.addPayload(var, list.get(0));
		}

	}

}

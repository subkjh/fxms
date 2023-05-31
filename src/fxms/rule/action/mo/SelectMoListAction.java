package fxms.rule.action.mo;

import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.mo.Mo;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * 관리대상을 조회하는 액션
 * 
 * @author subkjh
 *
 */
@FxRuleActionInfo(name = "관리대상목록조회", descr = "조건을 이용하여 관리대상 목록을 조회한다.")
public class SelectMoListAction extends FxRuleActionImpl {

	public SelectMoListAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@FxAttr(text = "JSON형식 속성 데이터", description = "JSON 형식의 속성명과 값을 입력한다.")
	private String paraJson;

	@FxAttr(text = "변수명", description = "관리대상목록을 가르키는 변수를 입력한다.")
	private String var;

	@Override
	public void execute(FxRuleFact inObj) throws Exception {

		// 조건에 맞는 관리대상을 조회한다.
		Map<String, Object> para = FxmsUtil.toMapFromJson(paraJson);
		List<Mo> list = MoApi.getApi().getMoList(para);

		inObj.addPayload(var, list);
	}

}

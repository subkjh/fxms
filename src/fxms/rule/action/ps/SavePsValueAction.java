package fxms.rule.action.ps;

import java.util.List;
import java.util.Map;

import fxms.bas.api.ValueApi;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.PsVoRawList;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "수집내역 저장", descr = "수집한 데이터를 기록 요청한다.")
public class SavePsValueAction extends FxRuleActionImpl {

	public SavePsValueAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		List<PsVoRaw> valueList = fact.getPsRawValues();

		if (valueList != null) {
			// 값 기록하기
			ValueApi.getApi().addValue(new PsVoRawList("rule", System.currentTimeMillis(), valueList, null), true);
		}

	}
}

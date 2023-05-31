package fxms.rule.action.ps;

import java.util.List;
import java.util.Map;

import fxms.bas.vo.PsVo;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import subkjh.bas.co.log.Logger;

/**
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "수집내역 업데이트", descr = "수집한 내역을 지정한 컬럼에 업데이터한다.")
public class UpdatePsValueAction extends FxRuleActionImpl {

	public UpdatePsValueAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact inObj) throws Exception {

		List<PsVo> voList = inObj.getPsValues();
		if (voList != null) {

			// 현재값 업데이터 하기
			try {
				// ValueApi.getApi().updatePsValue( new PsVoList(getClass().getSimpleName(),
				// System.currentTimeMillis(), voList, null));
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

	}
}

package fxms.rule.action.ps;

import java.util.List;
import java.util.Map;

import fxms.bas.event.PsVoListEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import subkjh.bas.co.log.Logger;

/**
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "수집한 데이터 방송", descr = "수집한 성능 데이터를 요청한 곳으로 방송한다.")
public class PeekPsValueAction extends FxRuleActionImpl {

	public PeekPsValueAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		// 수집한 값 가져오기
		List<PsVo> valueList = fact.getPsValues();

		if (valueList != null) {

			// 방송 요청하기
			try {
				PsVoListEvent event = new PsVoListEvent(new PsVoList("rule", System.currentTimeMillis(), valueList));

				FxServiceImpl.fxService.sendEvent(event, true, true);

			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}

	}
}
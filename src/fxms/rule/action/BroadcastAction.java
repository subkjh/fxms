package fxms.rule.action;

import java.util.Map;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import fxms.rule.event.FxRuleEvent;
import subkjh.bas.co.log.Logger;

/**
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "payload 방송", descr = "payload 결과를 JSON형식으로 변환하여 보로드캐스팅한다.")
public class BroadcastAction extends FxRuleActionImpl {

	public BroadcastAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		// payload 내용을 각 서비스에 방송한다.
		try {
			if (FxServiceImpl.fxService != null) {
				FxRuleEvent event = new FxRuleEvent(fact.getPayload());
				FxServiceImpl.fxService.sendEvent(event, true, true);
			} else {
				Logger.logger.fail("service is not running");
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}
}

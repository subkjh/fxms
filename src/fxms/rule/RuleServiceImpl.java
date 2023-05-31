package fxms.rule;

import java.rmi.RemoteException;
import java.util.List;

import fxms.bas.event.NotiFilter;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.rule.event.FxRuleEvent;
import fxms.rule.triger.FxRuleTrigger;
import fxms.rule.vo.RuleVo;
import subkjh.bas.co.log.Logger;

/**
 * ReleService를 구현한 서비스
 * 
 * @author subkjh
 * @since 2023.02
 */
public class RuleServiceImpl extends FxServiceImpl implements RuleService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2188870307144066161L;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(RuleService.class.getSimpleName(), RuleServiceImpl.class, args);
	}

	public RuleServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}


	protected void onStarted() throws Exception {

		super.onStarted();

		// 항상 실행해야 할 룰을 가져와 실행시킨다.
		// TODO 룰 플로우가 변경되면 재 실행해야 함.
		List<RuleVo> ruleList = RuleApi.getApi().getRuleToRunAlways();
		for (RuleVo rule : ruleList) {
			try {
				RuleApi.getApi().runRule(rule);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		notiFilter.add(FxRuleEvent.class);
		return notiFilter;
	}

	@Override
	public long runRule(int brRuleNo) throws RemoteException, Exception {
		Logger.logger.info("brRuleNo={}", brRuleNo);

		try {
			return RuleApi.getApi().runRule(brRuleNo);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public String runRule(String json) throws RemoteException, Exception {

		Logger.logger.info("json={}", json);

		try {
			FxRuleTrigger trigger = RuleApi.getApi().makeTrigger(json);
			trigger.trigger(null);
			return FxmsUtil.toJson(trigger.getRuleEngine().getFact());
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

}

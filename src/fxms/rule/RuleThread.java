package fxms.rule;

import fxms.bas.event.FxEvent;
import fxms.bas.fxo.thread.FxThread;
import fxms.rule.dbo.all.FX_BR_RULE;
import fxms.rule.event.FxRuleStopEvent;
import fxms.rule.triger.FxRuleTrigger;
import fxms.rule.triger.TriggerListener;
import subkjh.bas.co.log.Logger;

/**
 * 룰을 실행하는 스레드
 * 
 * @author subkjh
 * @since 2023.02
 */
public class RuleThread extends FxThread {

	private final FxRuleTrigger trigger;
	private final long brRunNo;
	private long checkNextTime = System.currentTimeMillis();
	private boolean stopRule = false;

	/**
	 * 
	 * @param rule    실행할 룰
	 * @param trigger 트리거
	 */
	public RuleThread(FX_BR_RULE rule, FxRuleTrigger trigger) {

		this.trigger = trigger;

		// 실행 로그를 남긴다.
		this.brRunNo = RuleApi.getApi().logRuleStart(rule.getBrRuleNo(), rule.getRuleName(), trigger.getRuleEngine());
		this.setName("RuleThread-" + rule.getBrRuleNo());
	}

	/**
	 * 
	 * @return 비즈니스룰실행번호
	 */
	public long getBrRunNo() {
		return brRunNo;
	}

	@Override
	protected void doInit() throws Exception {
	}

	@Override
	protected void doWork() throws Exception {
		// 인터럽트 요청이 있는지 감시하는 스레드
		// 인터럽트가 존재하면 트리거를 종료한다.
		Thread th = new Thread() {
			public void run() {
				while (true) {
					if (isStop()) {
						trigger.stopTrigger();
						break;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		th.setName(this.getName() + "-sub");
		th.start();

		// 트리거 발생
		try {
			this.trigger.trigger(new TriggerListener() {
				@Override
				public void onFinish() {
					// 종료되면 로그를 남긴다.
					RuleApi.getApi().logRuleEnd(brRunNo, trigger.getRuleEngine().getFact());
				}
			});
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	private boolean isStop() {

		if (checkNextTime < System.currentTimeMillis()) {
			// 10초 간격으로 확인
			checkNextTime = System.currentTimeMillis() + (30000L);
			return RuleApi.getApi().isInterrupted(brRunNo);
		}

		return stopRule;
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		super.onEvent(noti);

		// TODO
		// 인터럽트 신호가 왔는지 확인한다.
		if (noti instanceof FxRuleStopEvent) {
			FxRuleStopEvent e = (FxRuleStopEvent) noti;
			if (e.getBrRunNo() == this.brRunNo) {
				stopRule = true;
			}
		}
	}
}

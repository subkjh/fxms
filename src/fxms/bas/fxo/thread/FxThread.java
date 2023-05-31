package fxms.bas.fxo.thread;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiReceiver;
import fxms.bas.fxo.FxActor;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxMatch;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.signal.PrintStatusSignal;
import fxms.bas.signal.ShutdownSignal;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.log.RunCntVo;

/**
 * FxMS에서 사용하는 기본 스레드
 * 
 * @author subkjh
 *
 */
public abstract class FxThread extends Thread implements NotiReceiver, Loggable, FxActor {

	private String stopReason = null;
	protected RunCntVo<FXTHREAD_STATUS> counter;
	private Map<String, Object> para;

	@FxAttr(name = "name")
	private String name = null;

	public FxThread() {
		counter = new RunCntVo<FXTHREAD_STATUS>();
		para = new HashMap<String, Object>();
	}

	@Override
	public FxMatch getMatch() {
		return null;
	}

	@Override
	public Map<String, Object> getPara() {
		return para;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return counter.toString();
	}

	/**
	 * 
	 * @return
	 */
	public FXTHREAD_STATUS getStatus() {
		return counter == null ? FXTHREAD_STATUS.Ready : counter.getStatus();
	}

	@Override
	public void onCreated() throws Exception {
		if (name != null && name.trim().length() > 0) {
			setName(name);
		}
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {
		if (noti instanceof ShutdownSignal) {
			stop(noti.toString());
		}
		if (noti instanceof PrintStatusSignal) {
			Logger.logger.info(getState(Logger.logger.getLevel()));
		}
	}

	@Override
	public void run() {

		Logger.logger.info(Logger.makeString(getName(), "RUNNING"));

		if (FxServiceImpl.fxService != null && isAddToService()) {
			FxServiceImpl.fxService.addFxActor(this);
		}

		counter.setStatus(FXTHREAD_STATUS.Ready);

		try {

			doInit();

			Logger.logger.info(getState(Logger.logger.getLevel()));

			counter.setStatus(FXTHREAD_STATUS.Running);

			doWork();

			stop("End");

		} catch (Exception e) {

			Logger.logger.error(e);

		} finally {

			if (FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.removeFxActor(this, stopReason);
			}

			counter.setStatus(FXTHREAD_STATUS.Finished);

			onStopped();

		}

	}

	/**
	 * 쓰레드를 종료한다.
	 * 
	 * @param msg
	 */
	public void stop(String msg) {

		Logger.logger.info("to finish with {}", msg);

		stopReason = msg;

		if (counter.getStatus() == FXTHREAD_STATUS.Waiting) {
			this.interrupt();
		}

	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * doWork를 호출하기 전에 호출된다.
	 * 
	 * @throws Exception
	 */
	protected abstract void doInit() throws Exception;

	/**
	 * 실제 처리하는 작업을 작성한다.
	 */
	protected abstract void doWork() throws Exception;

	protected RunCntVo<FXTHREAD_STATUS> getCounter() {
		return counter;
	}

	/**
	 * FxService의 FxActor로 추가할지 여부를 리턴한다.<br>
	 * FxService에 추가되면 자동으로 모든 이벤트를 onEvnet 함수를 통해 받는다.<br>
	 * 
	 * @return FxService의 FxActor로 추가할지 여부
	 */
	protected boolean isAddToService() {
		return true;
	}

	/**
	 * 외부에서 종료 신호가 있어 그만 종료해야하는지 아니면 계속 실행해도 되는지 확인한다.
	 * 
	 * @return
	 */
	protected boolean isContinue() {
		return stopReason == null;
	}

	/**
	 * 쓰레드가 종료되기 직전에 호출한다.
	 */
	protected void onStopped() {
		Logger.logger.info(Logger.makeString(getName(), "finished. " + stopReason));
	}

}
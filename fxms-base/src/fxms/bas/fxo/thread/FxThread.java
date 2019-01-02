package fxms.bas.fxo.thread;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.noti.FxEvent;
import fxms.bas.noti.NotiReceiver;
import fxms.bas.signal.PrintStatusSignal;
import fxms.bas.signal.ShutdownSignal;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Loggable;
import subkjh.bas.log.Logger;
import subkjh.bas.log.RunCounter;

public abstract class FxThread extends Thread implements NotiReceiver, Loggable {

	private String stopReason = null;
	protected RunCounter<FXTHREAD_STATUS> counter;

	public FxThread(String name) {
		counter = new RunCounter<FXTHREAD_STATUS>();
		setName(name != null ? name : getClass().getSimpleName());
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return counter.toString();
	}

	public FXTHREAD_STATUS getStatus() {
		return counter == null ? FXTHREAD_STATUS.Ready : counter.getStatus();
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {
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

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.addFxActor(this);
		}

		counter.setStatus(FXTHREAD_STATUS.Ready);

		doInit();

		Logger.logger.info(getState(Logger.logger.getLevel()));

		counter.setStatus(FXTHREAD_STATUS.Running);

		doWork();

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.removeFxActor(this, stopReason);
		}

		counter.setStatus(FXTHREAD_STATUS.Finished);

		onStopped();

	}

	public void stop(String msg) {

		Logger.logger.info("TO FINISH WITH '" + msg + "'");

		stopReason = msg;

		if (counter.getStatus() == FXTHREAD_STATUS.Waiting) {
			this.interrupt();
		}

	}

	@Override
	public String toString() {
		return getName();
	}

	protected abstract void doInit();

	protected abstract void doWork();

	protected RunCounter<FXTHREAD_STATUS> getCounter() {
		return counter;
	}

	protected boolean isContinue() {
		return stopReason == null;
	}

	protected void onStopped() {
		Logger.logger.info(Logger.makeString(getName(), "FINISHED(" + stopReason + ")"));
	}

}
package fxms.bas.fxo.thread;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.log.RunCntVo;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiReceiver;
import fxms.bas.co.signal.PrintStatusSignal;
import fxms.bas.co.signal.ShutdownSignal;
import fxms.bas.fxo.FxActor;
import fxms.bas.fxo.FxPara;
import fxms.bas.fxo.service.FxServiceImpl;

public abstract class FxThread extends Thread implements NotiReceiver, Loggable, FxActor {

	private String stopReason = null;
	protected RunCntVo<FXTHREAD_STATUS> counter;
	private FxPara data;

	public FxThread() {
		counter = new RunCntVo<FXTHREAD_STATUS>();
		data = new FxPara();
	}

	@Override
	public FxPara getFxPara() {
		return data;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return counter.toString();
	}

	public FXTHREAD_STATUS getStatus() {
		return counter == null ? FXTHREAD_STATUS.Ready : counter.getStatus();
	}

	@Override
	public void onCreated() {
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

		try {

			doInit();

			Logger.logger.info(getState(Logger.logger.getLevel()));

			counter.setStatus(FXTHREAD_STATUS.Running);

			doWork();

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

	@Override
	public void setPara(String name, String value) {
		getFxPara().set(name, value);

		if ("name".equals(name)) {
			setName(name);
		}
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

	protected abstract void doInit() throws Exception;

	protected abstract void doWork();

	protected RunCntVo<FXTHREAD_STATUS> getCounter() {
		return counter;
	}

	protected boolean isContinue() {
		return stopReason == null;
	}

	protected void onStopped() {
		Logger.logger.info(Logger.makeString(getName(), "FINISHED(" + stopReason + ")"));
	}

}
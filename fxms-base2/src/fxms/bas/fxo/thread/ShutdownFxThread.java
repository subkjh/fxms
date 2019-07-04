package fxms.bas.fxo.thread;

import fxms.bas.co.noti.FxEvent;
import fxms.bas.fxo.service.FxServiceImpl;

public class ShutdownFxThread extends CycleFxThread {
	private String shutdownReason = null;

	public ShutdownFxThread() throws Exception {
		super(ShutdownFxThread.class.getSimpleName(), "period 3");
	}

	@Override
	protected void doInit() {

	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {
		// nothing
	}

	public void shutdown(String reason) {
		shutdownReason = reason;
	}

	@Override
	protected void doCycle(long mstime) {
		
		FxServiceImpl.fxService.onCycle(mstime);

		if (shutdownReason != null) {

			for (int i = 0; i < 30; i++) {
				if (Thread.activeCount() == 2)
					break;

				Thread ths[] = new Thread[Thread.activeCount()];
				Thread.enumerate(ths);
				for (Thread th : ths) {
					if (th != this) {
						if (th instanceof FxThread) {
							((FxThread) th).stop(shutdownReason);
						}
						System.out.println(th.getName() + ";" + th.getClass().getName());
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			System.exit(0);

		}

	}
}

package fxms.bas.thread;

import fxms.bas.event.FxEvent;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.bas.fxo.thread.FxThread;

/**
 * 서비스의 종료를 인지하여 프로세스를 종료하는 스레드
 * 
 * @author subkjh
 *
 */
public class ShutdownCheckThread extends CycleFxThread {

	private String shutdownReason = null;

	public ShutdownCheckThread() throws Exception {
		super(ShutdownCheckThread.class.getSimpleName(), "period 3");
	}

	@Override
	protected void doInit() {

	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {
		// nothing
	}

	/**
	 * 종료 사유 설정
	 * 
	 * @param reason 종료 사유
	 */
	public void shutdown(String reason) {
		shutdownReason = reason;
	}

	@Override
	protected void doCycle(long mstime) {

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

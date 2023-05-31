package fxms.bas.fxo.thread;

import java.util.concurrent.LinkedBlockingQueue;

import fxms.bas.signal.ShutdownSignal;
import subkjh.bas.co.log.Logger;

public abstract class SubFxThread<E extends Object> extends Thread {

	private LinkedBlockingQueue<Object> queue;
	private FxThreadPool<E> pool;

	public SubFxThread(String name) {
		super(name);
	}

	@SuppressWarnings("unchecked")
	public void run() {
		Object obj;
		E e = null;
		long ptime;

		Logger.logger.trace("STARTED");

		while (true) {

			try {
				obj = queue.take();

				if (obj instanceof ShutdownSignal) {
					pool.onFinish(this);
					break;
				}

				ptime = System.currentTimeMillis();
				e = (E) obj;
				pool.onStart(e);
				doSub(e);
				pool.onFinish(e, System.currentTimeMillis() - ptime);

			} catch (Exception ex) {
				Logger.logger.error(ex);
				pool.onFinish(e, -1);
			}
		}

		Logger.logger.trace("FINISHED");

	}

	protected abstract void doSub(E e) throws Exception;

	void setPool(FxThreadPool<E> pool) {
		this.pool = pool;
	}

	void setQueue(LinkedBlockingQueue<Object> queue) {
		this.queue = queue;
	}

}
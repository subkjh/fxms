package fxms.bas.fxo.thread;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import fxms.bas.co.signal.ShutdownSignal;
import subkjh.bas.co.log.Logger;

public abstract class FxThreadPool<E> extends FxThread {

	private int size;
	private final LinkedBlockingQueue<Object> queue;
	private int sizeFinished = 0;

	public FxThreadPool(String name, int size) {

		setName(name);

		this.size = size;
		queue = new LinkedBlockingQueue<Object>();
	}

	@Override
	protected void doInit() {

		SubFxThread<E> sub;

		for (int i = 1; i <= size; i++) {
			sub = makeSub(getName() + "-#" + i);
			sub.setPool(this);
			sub.setQueue(queue);
			sub.start();
		}

	}

	@Override
	protected void doWork() {

		List<E> eList = getEList();
		if (eList != null) {
			for (E e : eList) {
				try {
					queue.put(e);
				} catch (InterruptedException e1) {
				}
			}
		}

		getCounter().setCntTotal(eList == null ? 0 : eList.size());

		for (int i = 0; i < size; i++) {
			try {
				queue.put(new ShutdownSignal("NO DATAS"));
			} catch (InterruptedException e) {
			}
		}

		while (sizeFinished != size) {
			Thread.yield();
		}

		stop("COMPLETED");
		Logger.logger.info("{}", getCounter());

	}

	protected abstract List<E> getEList();

	protected abstract SubFxThread<E> makeSub(String name);

	void onFinish(E e, long stime) {
		if (stime >= 0) {
			getCounter().addOk(stime);
		} else {
			getCounter().addFail();
		}
	}

	synchronized void onFinish(SubFxThread<E> sub) {
		sizeFinished++;
	}

	void onStart(E e) {

	}

}

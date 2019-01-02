package fxms.bas.fxo.service.mo;

import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.mo.Mo;

public class MoConfigThread extends QueueFxThread<Mo> {

	private MoConfiger configer;

	public MoConfigThread() {
		super("mo-config-thread");

		configer = new MoConfiger();
	}

	@Override
	protected void doWork(Mo mo) throws Exception {
		configer.doSync(mo);
	}

	@Override
	protected void onNoDatas(long index) {

	}

	@Override
	protected void doInit() {

	}

}

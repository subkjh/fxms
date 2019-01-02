package com.fxms.nms.cron;

import java.util.List;

import com.fxms.nms.mo.NeMo;

import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.cron.Crontab;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.fxo.thread.FxThreadPool;
import fxms.bas.fxo.thread.SubFxThread;
import fxms.bas.mo.Mo;

public class SyncCrontab extends Crontab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1528385253828101380L;

	@Override
	public void cron() throws Exception {

		FxThreadPool<NeMo> pool = new FxThreadPool<NeMo>("TROT-CONF-SYNC",
				FxCfg.getCfg().getInt(FxCfg.SIZE_THREAD_CONFIGF, 10)) {

			@Override
			protected List<NeMo> getEList() {
				// return PoDBM.getDBM().getMoListAll(NodeMo.class);
				return null;
			}

			@Override
			protected SubFxThread<NeMo> makeSub(final String name) {
				return new SubFxThread<NeMo>(name) {
					@Override
					protected void doSub(NeMo node) throws Exception {
						MoService service = ServiceApi.getApi().getService(MoService.class, node);
						Mo detectedMo = service.getConfig(node.getMoNo());
						MoApi.getApi().updateMo(detectedMo);

					}

				};
			}

		};

		pool.start();

	}

	@Override
	public int getOpcode() {
		return 0;
	}

	@Override
	public String getLog() {
		return null;
	}

	@Override
	public String getGroup() {
		return null;
	}

}

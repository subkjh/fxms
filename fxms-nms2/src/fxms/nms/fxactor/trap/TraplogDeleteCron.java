package fxms.nms.fxactor.trap;

import subkjh.bas.co.log.Logger;
import fxms.bas.co.cron.Crontab;
import fxms.nms.api.TrapApi;

public class TraplogDeleteCron extends Crontab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2906649089535330733L;

	@Override
	public void cron() throws Exception {
		try {
			TrapApi.getApi().deleteLogExpired();
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public String getGroup() {
		return "trap";
	}

	@Override
	public String getLog() {
		return null;
	}

	@Override
	public int getOpcode() {
		return 0;
	}

}

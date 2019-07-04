package fxms.nms.fxactor.syslog;

import subkjh.bas.co.log.Logger;
import fxms.bas.co.cron.Crontab;
import fxms.nms.api.SyslogApi;

public class SyslogDeleteCron extends Crontab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2757584614849967617L;

	@Override
	public void cron() throws Exception {
		try {
			SyslogApi.getApi().deleteLogExpired();
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;	
		}
	}

	@Override
	public String getGroup() {
		return "syslog";
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

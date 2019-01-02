package fxms.bas.fxo.cron;

import fxms.bas.cron.Crontab;

public class MakeStCron extends Crontab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7798844986293636226L;

	@Override
	public void cron() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getGroup() {
		return "Statistic";
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

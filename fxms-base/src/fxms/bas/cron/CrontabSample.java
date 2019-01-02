package fxms.bas.cron;

public class CrontabSample extends Crontab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1528385253828101380L;

	@Override
	public void cron() throws Exception {
		System.out.println("CrontabSample called");

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

	@Override
	public String getName() {
		return "Example";
	}

}

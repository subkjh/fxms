package fxms.bas.cron;

public class CrontabSample extends Crontab {


	@Override
	public void start() throws Exception {
		System.out.println("CrontabSample called");

	}

	@Override
	public String getGroup() {
		return null;
	}

	@Override
	public String getName() {
		return "Example";
	}

	@Override
	protected String getSchedule() {
		// TODO Auto-generated method stub
		return null;
	}

}

package fxms.bas.cron;

public interface CronListener {

	/**
	 * 크론 시작할 때 호출
	 * 
	 * @param e
	 */
	public void onStart(Crontab e);

	/**
	 * 크론 종료 후 오출
	 * 
	 * @param e
	 * @param isOk
	 * @param stime
	 */
	public void onFinished(Crontab e, boolean isOk, long stime);
}

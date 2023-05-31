package fxms.bas.cron;

public interface CronListener {

	/**
	 * 크론 시작할 때 호출
	 * 
	 * @param cron
	 */
	public long onStart(Crontab cron);

	/**
	 * 크론 종료되면 호출
	 * 
	 * @param cronRunNo
	 * @param cron
	 * @param ex
	 * @param spentTime
	 */
	public void onFinished(long cronRunNo, Crontab cron, Exception ex, int spentTime);
}

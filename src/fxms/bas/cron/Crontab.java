package fxms.bas.cron;

import fxms.bas.fxo.adapter.FxAdapterImpl;
import subkjh.bas.co.log.RunCntVo;

/**
 * 주기적인 작업
 * 
 * @author subkjh
 *
 */
public abstract class Crontab extends FxAdapterImpl {

	private Cron cron;
	private final RunCntVo<?> counter;

	public Crontab() {
		this.counter = new RunCntVo<>();
	}

	protected abstract String getSchedule();

	public RunCntVo<?> getCounter() {
		return counter;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Crontab) {
			Crontab o = (Crontab) obj;
			if (o.getClass().getName().equals(getClass().getName())) {
				return true;
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return 1;
	}

	public Cron getCron() {
		return cron;
	}

	/**
	 * 입력 일자
	 * 
	 * @return
	 */
	public Object getInPara() {
		return null;
	}

	/**
	 * 출력 내용
	 * 
	 * @return
	 */
	public Object getOutPara() {
		return null;
	}

	public String getThreadGroup() {
		return "single";
	}

	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void onCreated() throws Exception {

		super.onCreated();

		try {
			cron = new Cron();
			cron.setSchedule(getSchedule());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void start() throws Exception;

	@Override
	public String toString() {
		return "NAME(" + getName() + ")SCHEDULE(" + cron + ")" + counter;
	}

}

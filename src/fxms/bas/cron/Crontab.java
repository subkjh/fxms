package fxms.bas.cron;

import java.util.HashMap;
import java.util.Map;

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
	private final Map<String, Object> inPara;
	private final Map<String, Object> outPara;

	public Crontab() {
		this.counter = new RunCntVo<>();
		this.inPara = new HashMap<String, Object>();
		this.outPara = new HashMap<String, Object>();
	}

	protected abstract String getSchedule();

	public RunCntVo<?> getCounter() {
		return counter;
	}

	public Cron getCron() {
		return cron;
	}

	public Map<String, Object> getInPara() {
		return inPara;
	}

	public Map<String, Object> getOutPara() {
		return outPara;
	}

	public String getGroup() {
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

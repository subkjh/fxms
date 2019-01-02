package fxms.bas.cron;

import java.io.Serializable;

import fxms.bas.fxo.FxActorImpl;
import subkjh.bas.log.RunCounter;

public abstract class Crontab extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9035905682910071443L;

	private Cron cron;

	/** 서비스가 시작할 때 실행할지 여부 */
	private boolean runInit = false;

	private RunCounter<?> counter;

	public Crontab() {
		counter = new RunCounter<>();
	}

	public abstract void cron() throws Exception;

	public RunCounter<?> getCounter() {
		return counter;
	}

	public Cron getCron() {
		return cron;
	}

	public abstract String getGroup();

	public abstract String getLog();

	public abstract int getOpcode();

	/**
	 * 
	 * @return 서비스가 시작할 때 실행할지 여부
	 */
	public boolean isRunInit() {
		return runInit;
	}

	@Override
	public void setPara(String name, Object value) {

		super.setPara(name, value);

		if ("schedule".equals(name)) {
			try {
				setSchedule(String.valueOf(value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("run-init".equals(name)) {
			try {
				runInit = "true".equalsIgnoreCase(value + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setSchedule(String s) throws Exception {
		cron = new Cron();
		cron.setCron(s);
	}

	@Override
	public String toString() {
		return "NAME(" + getName() + ")SCHEDULE(" + cron + ")" + counter;
	}

}

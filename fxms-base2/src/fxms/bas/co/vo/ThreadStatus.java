package fxms.bas.co.vo;

/**
 * 
 * @author subkjh(김종훈)
 *
 */
public enum ThreadStatus {

	/** 초기화중 */
	Init(false),

	/** 준비 중 */
	Ready(false),

	/** 처리 중 */
	Running(false),

	/** Slept */
	Slept(false),

	/** 종료됨 */
	Stopped(true),

	/** 종료 중 */
	Stopping(true),

	/** 대기 중 */
	Waiting(false),

	/** queue 처리중 */
	Queueing(false);

	private boolean finished;

	ThreadStatus(boolean finished) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

}

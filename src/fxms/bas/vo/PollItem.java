package fxms.bas.vo;

/**
 * Polling Object 
 * 
 * 
 * @author 김종훈
 *
 */
public interface PollItem {
	
	public enum MoState {
		init, ok, error, timeout, notarget;
	}

	public enum Status {
		
		/** 대기 */
		Waiting,

		/** 큐 안에 있음 */
		InQueue,

		/** 작업 중임 */
		Processing;

	}

	public int getPollCycle();
}

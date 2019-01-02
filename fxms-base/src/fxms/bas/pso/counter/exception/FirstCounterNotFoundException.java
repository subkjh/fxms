package fxms.bas.pso.counter.exception;

/**
 * 카운터값이 처음임으로 값을 구할수 없음을 나타내는 Exception
 * 
 * @author subkjh
 * @since 2013.01.29
 */
public class FirstCounterNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8654922957196396886L;

	private String target;

	public FirstCounterNotFoundException(String target) {
		super("TARGET(" + target + ")");
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}

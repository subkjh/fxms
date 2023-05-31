package fxms.bas.exp;

/**
 * 카운터값이 처음임으로 값을 구할수 없음을 나타내는 Exception
 * 
 * @author subkjh
 * @since 2013.01.29
 */
public class FirstCounterNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8654922957196396886L;

	public FirstCounterNotFoundException(String target) {
		super("counter", target);
	}

}

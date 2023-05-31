package subkjh.dao.exp;

/**
 * 너무 많은 데이터 예외
 * 
 * @author subkjh
 *
 */
public class TooManyDatasException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4888457186678409383L;

	private final int size;

	public TooManyDatasException(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

}

package fxms.bas.exp;

/**
 * 관리대상을 찾지 못함
 * 
 * @author subkjh
 * @since 2017
 */
public class MoNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1016320254283658403L;

	public MoNotFoundException(long moNo) {
		super("mo", moNo);
	}

	public MoNotFoundException(String msg) {
		super("mo", null, msg);
	}
}

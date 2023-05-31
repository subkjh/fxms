package fxms.bas.exp;

/**
 * 속성을 찾지 못한 경우 보내지는 Exception
 * 
 * @author subkjh
 *
 */
public class AttrNotFoundException extends NotFoundException {

	/**
	 *
	 */
	private static final long serialVersionUID = 7293751392407250407L;

	public AttrNotFoundException(String attr) {
		super("attribute", attr);
	}

}

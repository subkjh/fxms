package fxms.bas.exp;

/**
 * 해당 클래스 인스턴스가 아닌 경우 발생
 * 
 * @author subkjh
 *
 */
public class NotInstanceException extends FxNotMatchException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3626330150277740723L;

	public NotInstanceException(Class<?> classOf, String name) {
		super(name + "is not instance of " + classOf.getName());
	}

}

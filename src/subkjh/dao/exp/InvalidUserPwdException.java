package subkjh.dao.exp;

/**
 * 계정정보 오류로 인한 로그인 실패
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class InvalidUserPwdException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -355215364857042754L;

	public InvalidUserPwdException(String msg) {
		super(msg);
	}
}

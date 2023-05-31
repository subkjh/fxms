package fxms.bas.exp;

/**
 * 동일 알람이 존재할 경우 발생<br>
 * 동일 알람이란 관리대상번호, 알람코드번호, 알람등급이 같은 경우에 해당된다. <br>
 * 
 * @author subkjh
 *
 */
public class AlarmExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -159666787859226884L;

	public AlarmExistException(String msg) {
		super(msg);
	}
}

package subkjh.bas.net.syslog.vo;

/**
 * 받은 SYSLOG를 처리하는 인터페이스
 * 
 * @author subkjh(김종훈)
 *
 */
public interface SyslogListener {

	/**
	 * 
	 * @param vo
	 *            받은 SYSLOG
	 */
	public void onRecv(SyslogVo vo);

}

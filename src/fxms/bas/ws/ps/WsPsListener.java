package fxms.bas.ws.ps;

/**
 * 
 * @author subkjh
 *
 */
public interface WsPsListener {

	/**
	 * 수신한 값을 받는다.
	 * 
	 * @param moNo  관리대상번호
	 * @param psId  성능항목
	 * @param date  일시
	 * @param value 값
	 */
	public void onValue(long moNo, String psId, String date, Number value);

}

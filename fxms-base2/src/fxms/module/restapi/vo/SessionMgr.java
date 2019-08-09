package fxms.module.restapi.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * 로그인된 세션을 관리한다.
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class SessionMgr {

	private Map<String, SessionVo> map;

	public SessionMgr() {
		map = new HashMap<String, SessionVo>();
	}

	/**
	 * 세션의 다음 일련번호를 제공한다.
	 * 
	 * @param sessionId
	 * @return
	 */
	public synchronized long getNextSeqno(String sessionId) {

		SessionVo data = map.get(sessionId);
		if (data == null) {
			return -1;
		}

		data.setSeqno(data.getSeqno() + 1);

		return data.getSeqno();
	}

	/**
	 * 세션ID와 호스트명이 일치한 세션을 제공한다. 이때, 일련번호가 100이내 범위여야 한다.
	 * 
	 * @param hostname
	 * @param sessionId
	 * @param seqno
	 * @return
	 */
	public SessionVo get(String hostname, String sessionId, long seqno) {

		SessionVo data = map.get(sessionId);

		long startSeqno = seqno - 100;
		long endSeqno = seqno + 100;

		if (data != null && data.getSeqno() >= startSeqno && data.getSeqno() <= endSeqno
				&& data.getHostname().equals(hostname)) {
			return data;
		}
		return null;
	}

	/**
	 * 세선 정보를 메모리에서 제거한다.
	 * 
	 * @param sessionId
	 */
	public void remove(String sessionId) {
		map.remove(sessionId);
	}

	/**
	 * 세션 정보와 접속한 호스트 정보를 보관한다.
	 * 
	 * @param data
	 * @return
	 */
	public synchronized long putNew(SessionVo data) {

		long seqno = (long) (Math.random() * System.currentTimeMillis());

		data.setSeqno(seqno);

		map.put(data.getSessionId(), data);

		return seqno;
	}

}

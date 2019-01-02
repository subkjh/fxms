package fxms.module.restapi.vo;

import java.util.HashMap;

public class SessionMap extends HashMap<String, SessionVo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6359267328595952953L;

	private static SessionMap map;

	public static SessionMap getSessionMap() {
		if (map == null) {
			map = new SessionMap();
		}

		return map;
	}

	private SessionMap() {

	}

	public synchronized long getSeqno(String sessionId) {

		long seqno = System.currentTimeMillis();

		SessionVo data = get(sessionId);
		if (data == null) {
			return -1;
		}

		data.setSeqno(seqno);

		return seqno;
	}

	public SessionVo getSession(String hostname, String sessionId, long seqno) {

		SessionVo data = get(sessionId);

		long startSeqno = seqno - 10000;
		long endSeqno = seqno + 10000;

		if (data != null && data.getSeqno() >= startSeqno && data.getSeqno() <= endSeqno
				&& data.getHostname().equals(hostname)) {
			return data;
		}
		return null;
	}

	public synchronized long setNewSession(SessionVo data, String hostname) {
		long seqno = System.currentTimeMillis();

		data.setHostname(hostname);
		data.setSeqno(seqno);

		put(data.getSessionId(), data);

		return seqno;
	}
}

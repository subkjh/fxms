package fxms.bas.ws.ps;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import fxms.bas.ws.socket.WsClient;
import subkjh.bas.co.log.Logger;
import test.bas.impl.handler.SystemHandlerTest;

/**
 * 수집된 성능을 실시간으로 받는 클라이언트용 클래스
 * 
 * @author SUBKJH-DEV
 *
 */
public class WsPsValueClient {

	class RecvPsVo {
		long moNo;
		String moInstance;
		String psId;
		Number value;
		String date;
	}

	public static void main(String[] args) throws Exception {

		SystemHandlerTest c = new SystemHandlerTest();
		List<Map<String, Object>> list = null ; //c.getVarList();
		String host = "10.0.1.11";
		int port = 63819;

		for (Map<String, Object> o : list) {
			if ("fxms-value-peeker-host".equals(String.valueOf(o.get("varName")))) {
				host = String.valueOf(o.get("varVal"));
			} else if ("fxms-value-peeker-port".equals(String.valueOf(o.get("varName")))) {
				port = Integer.parseInt(o.get("varVal") + "");
			}
		}

		// 가져오 정보로 접속하기
		WsPsValueClient r = new WsPsValueClient(host, port, "TS", "1111");

		WsPsListener listener = new WsPsListener() {
			@Override
			public void onValue(long moNo, String psId, String date, Number value) {
				System.out.println(date + "\t" + moNo + "." + psId + "=" + value);
			}
		};

		// 데이터 수집되면 엿보기 등록
		try {
			Thread.sleep(3000);

			r.add(100002, "STEAM", listener);
			r.add(100001, "INLPG", listener);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean connected;
	private String host;
	private int port;
	private Gson gson = new Gson();
	private Map<String, List<WsPsListener>> reqMap = new HashMap<String, List<WsPsListener>>();
	private WsClient client;
	private String userId;
	private String userPwd;

	public WsPsValueClient(String host, int port, String userId, String userPwd) {
		this.host = host;
		this.port = port;
		this.userId = userId;
		this.userPwd = userPwd;

		startConnectionChecker();
	}

	private void addToServer(long moNo, String psId) {
		if (client != null && connected) {
			Logger.logger.info("moNo={}, psId={}", moNo, psId);
			try {
				client.send(makeReqMsg("add", moNo, psId));
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	private void addToServerAll() {
		String keys[] = reqMap.keySet().toArray(new String[reqMap.size()]);

		String ss[];

		for (String key : keys) {
			ss = key.split("\\.");
			addToServer(Long.valueOf(ss[0]), ss[1]);
		}

	}

	private void connectServer() {

		try {
			URI uri = new URI("ws://" + host + ":" + port);
			Logger.logger.trace("connecting... {}", uri.toString());

			client = new WsClient(uri) {
				@Override
				public void onClose(int code, String reason, boolean remote) {
					Logger.logger.fail("closed with exit code " + code + " additional info: " + reason);
					connected = false;
				}

				@Override
				public void onMessage(String message) {

					Logger.logger.trace(message);

					RecvPsVo vo;
					try {
						vo = gson.fromJson(message, RecvPsVo.class);
						if (vo.moNo <= 0)
							return;
					} catch (Exception e) {
						return;
					}

					List<WsPsListener> entry = reqMap.get(vo.moNo + "." + vo.psId);
					if (entry == null || entry.size() == 0) {
						// 요청하지 않은 내용이면 서버에 삭제 요청한다.
						removeFromServer(vo.moNo, vo.psId);
					} else {

						// 필요한 곳에 분배한다.
						for (WsPsListener e : entry) {
							e.onValue(vo.moNo, vo.psId, vo.date, vo.value);
						}
					}

				}

				@Override
				public void onOpen(ServerHandshake handshakedata) {

					Logger.logger.info("new connection opened");

					connected = true;

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", userId);
					map.put("userPwd", userPwd);

					try {
						client.send(gson.toJson(map));
					} catch (Exception e) {
						Logger.logger.error(e);
					}

					addToServerAll();
				}
			};

			client.connect();

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	private String makeReqMsg(String action, long moNo, String psId) {
		ReqPsVo req = new ReqPsVo();
		req.setAction(action);
		req.setMoNo(moNo);
		req.setPsId(psId);
		req.setMoInstance(null);

		return gson.toJson(req);
	}

	private void removeFromServer(long moNo, String psId) {
		if (client != null && connected) {
			Logger.logger.info("moNo={}, psId={}", moNo, psId);
			try {
				client.send(makeReqMsg("remove", moNo, psId));
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	private void startConnectionChecker() {
		Thread th = new Thread() {
			public void run() {
				while (true) {

					if (connected == false) {
						connectServer();
					}

					try {
						Thread.sleep(10000);
					} catch (Exception e) {
					}

				}
			}
		};
		th.setName("ws-ps-receiver-connection-checker");
		th.start();
	}

	public boolean add(long moNo, String psId, WsPsListener listener) {
		String key = moNo + "." + psId;
		List<WsPsListener> entry = reqMap.get(key);
		if (entry == null) {
			entry = new ArrayList<WsPsListener>();
			entry.add(listener);
			reqMap.put(key, entry);
			addToServer(moNo, psId);
		} else {
			entry.add(listener);
		}

		return true;
	}

	public boolean remove(long moNo, String psId, WsPsListener listener) {
		String key = moNo + "." + psId;
		List<WsPsListener> entry = reqMap.get(key);
		if (entry != null) {

			entry.remove(listener);

			if (entry.size() == 1) {
				reqMap.remove(key);
				removeFromServer(moNo, psId);
			} else {
				entry.remove(listener);
			}
		}

		return true;
	}

}

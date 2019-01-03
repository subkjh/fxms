package com.fxms.ws.biz.ps;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.handshake.ServerHandshake;

import com.fxms.ws.socket.WsClient;
import com.google.gson.Gson;

import subkjh.bas.log.Logger;

/**
 * 수집된 성능을 실시간으로 받는 클라이언트용 클래스
 * @author SUBKJH-DEV
 *
 */
public class WsPsReceiver {

	public static void main(String[] args) {

		WsPsReceiver r = new WsPsReceiver("125.7.128.42", 63819);

		WsPsListener listener = new WsPsListener() {

			@Override
			public void onValue(long moNo, String psCode, long hstime, Number value) {

				System.out.println(moNo + "." + psCode + "(" + hstime + ")=" + value);

			}

		};

		try {
			Thread.sleep(3000);
			r.add(1040183, "IfInBytes", listener);
			
//			r.add(7, "cpu", listener);
//			Thread.sleep(3000);
//			r.add(7, "cpu", listener);
//			while (r.recvCnt < 3) {
//				Thread.sleep(3000);
//			}
//			r.remove(7, "cpu", listener);
//			Thread.sleep(8000);
//			r.remove(7, "cpu", listener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean connected;
	private String host;
	private int port;
	private Gson gson = new Gson();
	private Map<String, List<WsPsListener>> reqMap = new HashMap<String, List<WsPsListener>>();
	private WsClient client;
	private long recvCnt = 0;

	public WsPsReceiver(String host, int port) {
		this.host = host;
		this.port = port;

		startConnectionChecker();
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

	public boolean add(long moNo, String psCode, WsPsListener listener) {
		String key = moNo + "." + psCode;
		List<WsPsListener> entry = reqMap.get(key);
		if (entry == null) {
			entry = new ArrayList<WsPsListener>();
			entry.add(listener);
			reqMap.put(key, entry);
			addToServer(moNo, psCode);
		} else {
			entry.add(listener);
		}

		return true;
	}

	public boolean remove(long moNo, String psCode, WsPsListener listener) {
		String key = moNo + "." + psCode;
		List<WsPsListener> entry = reqMap.get(key);
		if (entry != null) {

			entry.remove(listener);

			if (entry.size() == 1) {
				reqMap.remove(key);
				removeFromServer(moNo, psCode);
			} else {
				entry.remove(listener);
			}
		}

		return true;
	}

	private void connectServer() {

		try {
			client = new WsClient(new URI("ws://" + host + ":" + port)) {
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
					} catch (Exception e) {
						return;
					}

					recvCnt++;

					
					List<WsPsListener> entry = reqMap.get(vo.getMoNo() + "." + vo.getPsCode());
					if (entry == null || entry.size() == 0) {
						// 요청하지 않은 내용이면 서버에 삭제 요청한다.
						removeFromServer(vo.getMoNo(), vo.getPsCode());
					} else {
						
						// 필요한 곳에 분배한다.
						for (WsPsListener e : entry) {
							e.onValue(vo.getMoNo(), vo.getPsCode(), vo.getMstime(), vo.getValue());
						}
					}

				}

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					Logger.logger.info("new connection opened");
					connected = true;

					addToServerAll();
				}
			};

			client.connect();

		} catch (URISyntaxException e) {
		}
	}

	private String makeReqMsg(String action, long moNo, String psCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("action", action);
		map.put("moNo", moNo);
		map.put("psCode", psCode);

		return gson.toJson(map);
	}

	private void addToServerAll() {
		String keys[] = reqMap.keySet().toArray(new String[reqMap.size()]);

		String ss[];

		for (String key : keys) {
			ss = key.split("\\.");
			addToServer(Long.valueOf(ss[0]), ss[1]);
		}

	}

	private void addToServer(long moNo, String psCode) {
		if (client != null && connected) {
			Logger.logger.info("moNo={}, psCode={}", moNo, psCode);
			try {
				client.send(makeReqMsg("add", moNo, psCode));
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	private void removeFromServer(long moNo, String psCode) {
		if (client != null && connected) {
			Logger.logger.info("moNo={}, psCode={}", moNo, psCode);
			try {
				client.send(makeReqMsg("remove", moNo, psCode));
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}
}

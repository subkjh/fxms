package com.fxms.ui.dx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.handshake.ServerHandshake;

import com.fxms.ui.VAR;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ws.biz.ps.RecvPsVo;
import com.fxms.ws.socket.WsClient;
import com.google.gson.Gson;

import fxms.client.log.Logger;

public class DxRtPsRequestor {

	public static interface PsListener {
		public void onValue(String psCode, long mstime, Number value);
	}

	private boolean connected;
	private String host;
	private int port;
	private Gson gson = new Gson();
	private WsClient client;
	private PsListener listener;
	private long moNo;
	private String psCodes[];
	private boolean toContinue = true;

	public DxRtPsRequestor(PsListener listener, long moNo, String... psCodes) throws Exception {

		this.host = CodeMap.getMap().getVar(VAR.PS_SERVER).getVarValue();
		this.port = CodeMap.getMap().getVar(VAR.PS_SERVER_PORT).getIntValue(-1);

		this.listener = listener;
		this.moNo = moNo;
		this.psCodes = psCodes;

		startConnectionChecker();
	}

	private void startConnectionChecker() {
		Thread th = new Thread() {
			public void run() {
				while (toContinue) {

					if (connected == false) {
						connectServer();
					}

					try {
						Thread.sleep(3000);
					} catch (Exception e) {
					}

				}
			}
		};
		th.setName("ws-ps-receiver-connection-checker");
		th.start();
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

					Logger.logger.info(message);

					RecvPsVo vo;
					try {
						vo = gson.fromJson(message, RecvPsVo.class);
					} catch (Exception e) {
						return;
					}

					listener.onValue(vo.getPsCode(), vo.getMstime(), vo.getValue());

				}

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					Logger.logger.info("new connection opened");
					connected = true;

					addToServer();
				}
			};

			client.connect();

		} catch (URISyntaxException e) {
		}
	}

	public void stop() {
		toContinue = false;
		if ( client != null ) {
			client.close();
		}
	}

	private String makeReqMsg(String action, long moNo, String psCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("action", action);
		map.put("moNo", moNo);
		map.put("psCode", psCode);

		return gson.toJson(map);
	}

	private void addToServer() {
		if (client != null && connected) {
			Logger.logger.info("moNo={}, psCode={}", moNo, Arrays.toString(psCodes));
			try {
				for (String psCode : psCodes) {
					client.send(makeReqMsg("add", moNo, psCode));
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

}

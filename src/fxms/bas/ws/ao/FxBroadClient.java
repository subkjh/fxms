package fxms.bas.ws.ao;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;

import fxms.bas.ws.socket.WsClient;
import subkjh.bas.co.log.Logger;

public class FxBroadClient {

	public static void main(String[] args) {

		new FxBroadClient("10.0.1.11", 63818, "SOIL", "1111");

	}

	private boolean connected;
	private String host;
	private int port;
	private Gson gson = new Gson();
	private WsClient client;
	private String userId;
	private String userPwd;

	public FxBroadClient(String host, int port, String userId, String userPwd) {
		this.host = host;
		this.port = port;
		this.userId = userId;
		this.userPwd = userPwd;

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

				}

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					Logger.logger.info("new connection opened");
					connected = true;

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", userId);
					map.put("userPwd", userPwd);
					map.put("eventType", "polling-completed");

					try {
						client.send(gson.toJson(map));
					} catch (Exception e) {
						Logger.logger.error(e);
					}

				}
			};

			client.connect();

		} catch (URISyntaxException e) {
		}
	}

}

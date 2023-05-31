package fxms.bas.ws.socket;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import subkjh.bas.co.log.Logger;

public class WsServer extends WebSocketServer {

	// public void setupSSL() throws Exception {
	// String STORETYPE = "JKS";
	// String KEYSTORE = ServiceCfg.getHomeDeployConf() + File.separator +
	// "skbroadband.com.jks";
	// String STOREPASSWORD = "direct106ssl";
	// String KEYPASSWORD = "direct106ssl";
	//
	// KeyStore ks = KeyStore.getInstance(STORETYPE);
	// File kf = new File(KEYSTORE);
	// ks.load(new FileInputStream(kf), STOREPASSWORD.toCharArray());
	//
	// KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
	// kmf.init(ks, KEYPASSWORD.toCharArray());
	// TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
	// tmf.init(ks);
	//
	// SSLContext sslContext = null;
	// sslContext = SSLContext.getInstance("TLS");
	// sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
	//
	// this.setWebSocketFactory(new
	// DefaultSSLWebSocketServerFactory(sslContext));
	// }

	private String welcomeMessage;

	public WsServer(String host, int port) throws UnknownHostException {
		super(host == null ? new InetSocketAddress(port) : new InetSocketAddress(host, port));
	}

	public void broadcast(String msg) {
		for (WebSocket conn : connections().toArray(new WebSocket[connections().size()])) {
			conn.send(msg);
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		Logger.logger.info(
				"closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		Logger.logger.fail("{}", conn);
		Logger.logger.error(ex);
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		Logger.logger.info("received ByteBuffer from " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		Logger.logger.info("received message from " + conn.getRemoteSocketAddress() + ": " + message);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {

		Logger.logger.info("{}", conn);

		if (welcomeMessage != null) {
			conn.send(welcomeMessage);
		}

	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

}

package fxms.bas.ws.ao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import fxms.bas.api.UserApi;
import fxms.bas.api.VarApi;
import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiReceiver;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.FxServiceMember;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.ws.socket.WsServer;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * WEB SOCKET으로 이벤트를 방송하는 스레드
 * 
 * @author subkjh
 *
 */
public class FxBroadServer extends FxActorImpl implements NotiReceiver, FxServiceMember, Runnable {

	public class ClientVo {

		WebSocket websocket;
		SessionVo session;

		public ClientVo(WebSocket websocket, SessionVo session) {
			this.websocket = websocket;
			this.session = session;
		}

		public WebSocket getWebsocket() {
			return websocket;
		}

		public SessionVo getSession() {
			return session;
		}

	}

	private WsServer server;

	@FxAttr(name = "host", required = false)
	private String host = null;
	@FxAttr(name = "port", required = false)
	private int port;
	@FxAttr(name = "host2db", required = false)
	private String host2db = null;
	@FxAttr(name = "port2db", required = false)
	private int port2db;
	@FxAttr(name = "var.name", required = false)
	private String varName;
	@FxAttr(name = "notify.class", required = false)
	private Object notifyClass;

	protected final List<Class<?>> classList;
	protected final Gson gson;
	private LinkedBlockingQueue<FxEvent> queue;
	private Thread thread;

	private final Map<WebSocket, ClientVo> clientMap = new HashMap<WebSocket, ClientVo>();

	public FxBroadServer() {
		gson = new Gson();
		classList = new ArrayList<Class<?>>();
		queue = new LinkedBlockingQueue<FxEvent>();
	}

	/**
	 * 
	 * @return 접속한 클라이언트
	 */
	protected List<ClientVo> getClientList() {
		synchronized (clientMap) {
			return new ArrayList<ClientVo>(clientMap.values());
		}
	}

	protected WsServer getServer() {
		return server;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append("port=");
		sb.append(port);

		return sb.toString();
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		FxServiceImpl.logger.debug("{}", noti);

		queue.put(noti);

	}

	@Override
	public void run() {

		FxEvent fxEvent;

		while (true) {

			try {
				fxEvent = queue.take();
			} catch (InterruptedException e) {
				continue;
			}

			// 이벤트 또는 서버가 없으면 무시
			if (fxEvent == null || server == null) {
				continue;
			}

			// 방송할 내용이 없으면 무시
			if (classList.size() == 0) {
				continue;
			}

			// 방송 조건에 해당되는 이벤트이면 서버에 방송 요청
			try {
				for (Class<?> classOfNotify : classList) {
					if (classOfNotify.isInstance(fxEvent)) {
						String msg = gson.toJson(fxEvent);
						server.broadcast(msg);
						break;
					}
				}
			} catch (Exception e) {
				FxServiceImpl.logger.error(e);
			}

		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onCreated() throws Exception {

		if (notifyClass instanceof List) {
			for (Object cls : (List) notifyClass) {
				try {
					Class<?> classOfNotify = Class.forName(String.valueOf(cls));
					classList.add(classOfNotify);
				} catch (ClassNotFoundException e) {
					Logger.logger.error(e);
				}
			}
		} else if (notifyClass instanceof String) {
			try {
				Class<?> classOfNotify = Class.forName(String.valueOf(notifyClass));
				classList.add(classOfNotify);
			} catch (ClassNotFoundException e) {
				Logger.logger.error(e);
			}
		}

	}

	@Override
	public void startMember() {
		try {
			server = new WsServer(null, port) {
				@Override
				public void onClose(WebSocket conn, int code, String reason, boolean remote) {

					super.onClose(conn, code, reason, remote);

					ClientVo client = null;
					synchronized (clientMap) {
						client = clientMap.remove(conn);

						if (client != null) {
							try {
								UserApi.getApi().logout(client.getSession().getSessionId(),
										ACCS_ST_CD.LOGOUT_SESSION_CLOSE);
							} catch (Exception e) {
								Logger.logger.error(e);
							}
						}
					}

				}

				@SuppressWarnings("rawtypes")
				@Override
				public void onMessage(WebSocket conn, String message) {

					super.onMessage(conn, message);

					// 클라이언트가 접속해서 자신의 정보를 보낸다.

					try {
						Map map = gson.fromJson(message, HashMap.class);
						String userId = String.valueOf(map.get("userId"));
						String userPwd = String.valueOf(map.get("userPwd"));
						Object jwt = map.get("jwt");
						String ipAddr = conn.getRemoteSocketAddress().getHostString();

						SessionVo session;

						if (jwt == null) {
							session = UserApi.getApi().login(userId, userPwd, ipAddr, "ao-socket");
						} else {
							session = UserApi.getApi().getSession(jwt.toString());
						}

						conn.send(gson.toJson(session));

						Logger.logger.info("{}={}", map, session.getUserId());
						synchronized (clientMap) {
							clientMap.put(conn, new ClientVo(conn, session));
						}
					} catch (Exception e) {
						Logger.logger.error(e);
						conn.send("로그인이 되지 않았습니다.");
					}
				}
			};

			Thread th = new Thread(server);
			th.setName(getName() + "-" + port);
			th.start();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("welcome", "For X Management System");
			String msg = gson.toJson(map);
			server.setWelcomeMessage(msg);

			StringBuffer sb = new StringBuffer();
			sb.append(Logger.makeSubString("host", host));
			sb.append(Logger.makeSubString("port", port));
			sb.append(Logger.makeSubString("notify.class", classList));
			Logger.logger.info(Logger.makeString(th.getName(), "started", sb.toString()));

			thread = new Thread(this);
			thread.setName(getName() + "-sender");
			thread.start();

			if (varName != null) {
				host2db = host2db == null ? (host == null ? FxCfg.getIpAddress() : host) : host2db;
				port2db = port2db <= 0 ? port : port2db;
				VarApi.getApi().setVarValue(varName + "-host", host2db, true);
				VarApi.getApi().setVarValue(varName + "-port", port2db, true);
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			server = null;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("port=").append(port);
		return sb.toString();
	}

}

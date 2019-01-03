package com.fxms.ws.fxactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.java_websocket.WebSocket;

import com.fxms.ws.socket.WsServer;
import com.fxms.ws.vo.ClientVo;
import com.google.gson.Gson;

import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.app.AppService;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.noti.FxEvent;
import fxms.bas.noti.NotiReceiver;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

public class FxBroadcaster extends FxActorImpl implements NotiReceiver, FxServiceMember, Runnable {

	private WsServer server;
	private final String NOTIFY_CLASS = "notify-class";
	private final String HOST = "host";
	private final String PORT = "port";
	private final String VAR = "var-name";
	private String host = null;
	private int port;
	protected final List<Class<?>> classList;
	protected final Gson gson;
	private LinkedBlockingQueue<FxEvent> queue;
	private Thread thread;
	private String varName;
	private final Map<WebSocket, ClientVo> clientMap = new HashMap<WebSocket, ClientVo>();

	public FxBroadcaster() {
		gson = new Gson();
		classList = new ArrayList<Class<?>>();
		queue = new LinkedBlockingQueue<FxEvent>();
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append("port=");
		sb.append(port);

		return sb.toString();
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {
		FxServiceImpl.logger.trace(noti.toString());
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

			if (fxEvent == null || server == null) {
				continue;
			}

			if (classList.size() == 0) {
				continue;
			}

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

	@Override
	public void setPara(String name, Object value) {

		super.setPara(name, value);

		if (name.equals(PORT)) {
			port = Integer.parseInt(value.toString());
		} else if (name.equals(HOST)) {
			host = String.valueOf(value);
		} else if (name.equals(VAR)) {
			varName = String.valueOf(value);
		} else if (name.equals(NOTIFY_CLASS)) {
			try {
				Class<?> classOfNotify = Class.forName(String.valueOf(value));
				classList.add(classOfNotify);
			} catch (ClassNotFoundException e) {
				Logger.logger.error(e);
			}
		}
	}

	@Override
	public void start() {
		try {
			server = new WsServer(host, port) {
				@SuppressWarnings("rawtypes")
				@Override
				public void onMessage(WebSocket conn, String message) {

					super.onMessage(conn, message);

					try {
						Map map = new Gson().fromJson(message, HashMap.class);
						AppService service = ServiceApi.getApi().getService(AppService.class);
						SessionVo user = service.getUser(map.get("session-id") + "");
						if (user != null) {
							Logger.logger.info("{}={}", map, user.getUserId());
							synchronized (clientMap) {
								clientMap.put(conn, new ClientVo(conn, user));
							}
						}
					} catch (Exception e) {
						Logger.logger.error(e);
					}
				}

				@Override
				public void onClose(WebSocket conn, int code, String reason, boolean remote) {

					super.onClose(conn, code, reason, remote);
					
					ClientVo client = null;
					synchronized (clientMap) {
						client = clientMap.remove(conn);
					}

					if (client != null) {
						try {
							AppService service = ServiceApi.getApi().getService(AppService.class);
							service.logout(client.getUser().getSessionId());
						} catch (Exception e) {
							Logger.logger.error(e);
						}
					}
				}
			};

			Thread th = new Thread(server);
			th.setName(getName() + "-" + port);
			th.start();

			Alarm event = new Alarm();
			String msg = gson.toJson(event);
			server.setWelcomeMessage(msg);

			StringBuffer sb = new StringBuffer();
			sb.append(Logger.makeSubString(HOST, host));
			sb.append(Logger.makeSubString(PORT, port));
			sb.append(Logger.makeSubString(NOTIFY_CLASS, classList));
			Logger.logger.info(Logger.makeString(th.getName(), "started", sb.toString()));

			FxServiceImpl.fxService.addFxActor(this);

			thread = new Thread(this);
			thread.setName(getName() + "-sender");
			thread.start();

			if (varName != null) {
				ServiceApi.getApi().setVarValue(varName, host != null ? host : FxCfg.getCfg().getIpAddress(), true);
				ServiceApi.getApi().setVarValue(varName + "-port", port, true);
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			server = null;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("port=");
		sb.append(port);

		return sb.toString();
	}

	protected List<ClientVo> getClientList() {
		synchronized (clientMap) {
			return new ArrayList<ClientVo>(clientMap.values());
		}
	}

	protected WsServer getServer() {
		return server;
	}

}

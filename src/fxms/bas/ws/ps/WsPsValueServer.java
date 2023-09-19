package fxms.bas.ws.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import com.google.gson.Gson;

import fxms.bas.api.LogApi;
import fxms.bas.api.UserApi;
import fxms.bas.api.VarApi;
import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.filter.PsValuePeeker;
import fxms.bas.fxo.service.FxServiceMember;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.thread.PsValueNotifyThread;
import fxms.bas.vo.PsVo;
import fxms.bas.ws.socket.WsServer;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 수집된 값을 보내주는 서버
 *
 * @author subkjh
 *
 */
public class WsPsValueServer extends FxActorImpl implements Runnable, FxServiceMember, PsValuePeeker {

	class Data {
		SessionVo session;
		List<ReqPsVo> reqList;
	}

	class ValueData {
		long hstime;
		ReqPsVo vo;
	}

	@FxAttr(name = "var.name", required = false)
	private String varName;
	@FxAttr(name = "host", required = false)
	private String host = null;
	@FxAttr(name = "port", required = false)
	private int port;
	@FxAttr(name = "host2db", required = false)
	private String host2db = null;
	@FxAttr(name = "port2db", required = false)
	private int port2db;

	private final Gson gson = new Gson();
	/** 클라이언트 기준 세션 정보 */
	private final Map<WebSocket, Data> sessionMap;
	private WsServer server;
	private LinkedBlockingQueue<ValueData> queue;

	public WsPsValueServer() {
		sessionMap = new HashMap<>();
		queue = new LinkedBlockingQueue<ValueData>();
	}

	/**
	 * 요청 내역을 맵에 넣는다.
	 *
	 * @param conn
	 * @param req
	 */
	private synchronized void process(WebSocket conn, Data data, ReqPsVo req) {

		if (req.isAdd()) {

			PsValueNotifyThread.getVoNotifier().setPeeker(req.getMoNo(), req.getPsId(), this, true);

			data.reqList.add(req);

			LogApi.getApi().logUserWorkHst(data.session.getUserNo(), data.session.getUserName(),
					data.session.getSessionId(), "register-ps-val-peek", String.valueOf(req.getMoNo()), "", 0, "",
					System.currentTimeMillis(), "MO", req.getMoNo(), null);

		} else {

			PsValueNotifyThread.getVoNotifier().setPeeker(req.getMoNo(), req.getPsId(), this, false);

			data.reqList.remove(req);

			LogApi.getApi().logUserWorkHst(data.session.getUserNo(), data.session.getUserName(),
					data.session.getSessionId(), "unregister-ps-val-peek", String.valueOf(req.getMoNo()), "", 0, "",
					System.currentTimeMillis(), "MO", req.getMoNo(), null);

		}

		Logger.logger.info("{} / data-size={}", data.session.getSessionId(), data.reqList.size());

	}

	/**
	 * 연결이 끊긴 내용을 제거한다.
	 *
	 * @param conn
	 */
	private void remove(WebSocket conn) {

		// 세션
		Data data = sessionMap.remove(conn);

		if (data == null) {
			return;
		}

		try {
			UserApi.getApi().logout(data.session.getSessionId(), ACCS_ST_CD.LOGOUT_SESSION_CLOSE);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		for (ReqPsVo req : data.reqList) {
			PsValueNotifyThread.getVoNotifier().setPeeker(req.getMoNo(), req.getPsId(), this, false);
		}

	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append("port=").append(port);
		sb.append(server.toString());
		return sb.toString();
	}

	@Override
	public void onValue(long mstime, PsVo vo) {

		Logger.logger.trace(vo.toString());

		ValueData value = new ValueData();
		value.hstime = DateUtil.getDtm(mstime);
		value.vo = new ReqPsVo(vo);

		try {
			queue.put(value);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@Override
	public void run() {

		ValueData value;

		while (true) {

			try {
				value = queue.take();
			} catch (InterruptedException e) {
				continue;
			}

			// 이벤트 또는 서버가 없으면 무시
			if (value == null || server == null) {
				continue;
			}

			// 방송 조건에 해당되는 이벤트이면 서버에 방송 요청
			Map<String, Object> para = ObjectUtil.toMap(value.vo);
			para.put("date", value.hstime);
			String msg = gson.toJson(para);

			for (WebSocket conn : sessionMap.keySet()) {
				Data data = sessionMap.get(conn);
				for (ReqPsVo vo : data.reqList) {
					if (vo.getKey().equals(value.vo.getKey())) {
						try {
							conn.send(msg);
						} catch (Exception e) {
							Logger.logger.error(e);
						}
					}
				}
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

					Logger.logger.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code
							+ " additional info: " + reason);

					remove(conn);

				}

				@Override
				public void onError(WebSocket conn, Exception ex) {
					super.onError(conn, ex);

					Logger.logger.fail("{}", conn);
					Logger.logger.error(ex);
					remove(conn);

				}

				@SuppressWarnings("rawtypes")
				@Override
				public void onMessage(WebSocket conn, String message) {

					super.onMessage(conn, message);

					// 클라이언트가 접속해서 자신의 정보를 보낸다.

					Logger.logger.debug("received message from " + conn.getRemoteSocketAddress() + ": " + message);

					Data data = sessionMap.get(conn);

					if (data == null) {

						// 1. 로그인이 되어야 함.

						try {
							Map map = gson.fromJson(message, HashMap.class);
							String userId = String.valueOf(map.get("userId"));
							String userPwd = String.valueOf(map.get("userPwd"));
							Object jwt = map.get("jwt");
							String ipAddr = conn.getRemoteSocketAddress().getHostString();
							SessionVo session;

							if (jwt == null) {
								session = UserApi.getApi().login(userId, userPwd, ipAddr, "ps-socket");
							} else {
								session = UserApi.getApi().getSession(jwt.toString());
							}

							conn.send(gson.toJson(session));

							Logger.logger.info("{}={}", map, session.getUserId());
							synchronized (sessionMap) {
								data = new Data();
								data.session = session;
								data.reqList = new ArrayList<>();
								sessionMap.put(conn, data);
							}
						} catch (Exception e) {
							Logger.logger.error(e);
							conn.send("로그인이 되지 않았습니다.");
							return;
						}
					} else {

						// 2. 관심 관리대상과 성능ID가 옴.

						ReqPsVo req = gson.fromJson(message, ReqPsVo.class);

						process(conn, data, req);

					}
				}

				@Override
				public void onOpen(WebSocket conn, ClientHandshake handshake) {

					super.onOpen(conn, handshake);

					Logger.logger.info("{}", conn);

				}
			};

			Thread th = new Thread(server);
			th.setName(getName() + "-" + port);
			th.start();

			// Welcome 메시지
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("welcome", "For X management System");
			String msg = gson.toJson(map);
			server.setWelcomeMessage(msg);

			// 로깅
			StringBuffer sb = new StringBuffer();
			sb.append(Logger.makeSubString("host", host));
			sb.append(Logger.makeSubString("port", port));
			Logger.logger.info(Logger.makeString(th.getName(), "started", sb.toString()));

			// 변수 등록
			host2db = host2db == null ? (host == null ? FxCfg.getIpAddress() : host) : host2db;
			port2db = port2db <= 0 ? port : port2db;
			VarApi.getApi().setVarValue(varName + "-host", host2db, true);
			VarApi.getApi().setVarValue(varName + "-port", port2db, true);

			// 큐에서 빼내 보내는 스레드
			Thread sender = new Thread(this);
			sender.setName(getName() + "-sender");
			sender.start();

		} catch (Exception e) {
			Logger.logger.error(e);
			server = null;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Data data : sessionMap.values()) {
			sb.append(Logger.makeSubString(data.session.getHostname(), data.reqList.size()));
		}
		return Logger.makeString("", sessionMap.size(), sb.toString());
	}
}

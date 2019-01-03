package com.fxms.ws.biz.ps;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

import fxms.bas.api.FxApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class WsPsServer extends WebSocketServer {

	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 8888;
		WsPsServer server = new WsPsServer(null, port);
		new Thread(server).start();

		new Thread() {
			public void run() {

				while (true) {

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					PsVo vo = new PsVo();
					vo.setPsCode("cpu");
					vo.setMoNo((int) (Math.random() * 10));
					vo.setValue(Math.random() * 10);

					server.onValue(FxApi.getDate(0), vo);
				}
			}
		}.start();

	}

	private final Gson gson = new Gson();
	private final Map<ReqPsVo, List<WebSocket>> reqMap;
	private ValuePeekerWsPsServer peeker;

	public String getState() {
		StringBuffer sb = new StringBuffer();

		for (ReqPsVo vo : reqMap.keySet()) {
			sb.append("\n\t");
			sb.append(vo.getMoNo());
			sb.append("/");
			if (vo.getMoInstance() != null) {
				sb.append(vo.getMoInstance());
				sb.append("/");
			}
			sb.append(vo.getPsCode());
		}

		return sb.toString();
	}

	public WsPsServer(String host, int port) throws UnknownHostException {
		super(host == null ? new InetSocketAddress(port) : new InetSocketAddress("localhost", port));

		Logger.logger.info("host={}, port={}", host, port);

		reqMap = Collections.synchronizedMap(new HashMap<ReqPsVo, List<WebSocket>>());
		try {
			peeker = new ValuePeekerWsPsServer();
			peeker.setServer(this);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println(
				"closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);

		remove(conn);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		Logger.logger.fail("{}", conn);
		Logger.logger.error(ex);

		remove(conn);

	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		System.out.println("received ByteBuffer from " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {

		System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);

		ReqPsVo req = gson.fromJson(message, ReqPsVo.class);

		process(conn, req);

	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		Logger.logger.info("{}", conn);
	}

	public void onValue(long mstime, PsVo vo) {

		Logger.logger.trace(vo.toString());

		List<WebSocket> entry = reqMap.get(new ReqPsVo(vo));

		if (entry != null) {
			Map<String, Object> para = ObjectUtil.toMap(vo);
			para.put("mstime", mstime);
			String msg = gson.toJson(para);
			for (WebSocket conn : entry) {
				try {
					conn.send(msg);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}
	}

	private synchronized void process(WebSocket conn, ReqPsVo req) {

		List<WebSocket> entry;

		if (req.isAdd()) {

			setReqToPeeker(req.getMoNo(), req.getMoInstance(), req.getPsCode(), true);

			entry = reqMap.get(req);
			if (entry == null) {
				entry = new ArrayList<WebSocket>();
				reqMap.put(req, entry);
			}
			if (entry.contains(conn) == false) {
				entry.add(conn);
				Logger.logger.info("added {}", conn);
			}

		} else {

			setReqToPeeker(req.getMoNo(), req.getMoInstance(), req.getPsCode(), false);

			entry = reqMap.get(req);
			if (entry != null) {
				entry.remove(conn);
				Logger.logger.info("removed {}", conn);
				if (entry.size() == 0) {
					reqMap.remove(req);
				}
			}
		}
	}

	private void setReqToPeeker(long moNo, String moInstance, String psCode, boolean add) {

		Logger.logger.debug("moNo={}, moInstance={}, psCode={}, add={}", moNo, moInstance, psCode, add);

		List<MoService> serviceList = ServiceApi.getApi().getServiceList(MoService.class, null);
		for (MoService service : serviceList) {
			try {
				service.setPeekValue(moNo, moInstance, psCode, peeker, add);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	private void remove(WebSocket conn) {

		ReqPsVo keys[] = reqMap.keySet().toArray(new ReqPsVo[reqMap.size()]);
		List<WebSocket> entry;

		for (ReqPsVo key : keys) {
			entry = reqMap.get(key);
			if (entry != null) {
				if (entry.remove(conn)) {
					Logger.logger.info("removed {}", conn);
					setReqToPeeker(key.getMoNo(), key.getMoInstance(), key.getPsCode(), false);
				}

				if (entry.size() == 0) {
					reqMap.remove(key);
				}
			}
		}

	}

}

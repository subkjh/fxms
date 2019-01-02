package com.fxms.ui.dx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.handshake.ServerHandshake;

import com.fxms.ui.UiCode;
import com.fxms.ui.UiCode.Action;
import com.fxms.ui.VAR;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.event.FX_EVENT_TYPE;
import com.fxms.ui.bas.event.FxEvent;
import com.fxms.ui.bas.event.FxEventDispatcher;
import com.fxms.ui.dx.item.DxItemInformation;
import com.fxms.ui.node.alarm.AlarmReceiverStatusDxNode;
import com.fxms.ws.socket.WsClient;
import com.google.gson.Gson;

import fxms.client.log.Logger;
import javafx.application.Platform;

public class DxEventReceiver {

	public interface EventFilter {
		public boolean isOk(FxEvent event);
	}

	private static DxEventReceiver receiver;

	public static int SHOW_CLEAR_ALARM_MINUTES = 60;

	public static DxEventReceiver getReceiver() {

		if (receiver == null) {

			try {
				receiver = new DxEventReceiver();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return receiver;
	}

	public static void main(String[] args) {
		DxEventReceiver.getReceiver();
	}

	private List<DxListener<FxEvent>> listenerList;
	private boolean connected = false;
	private AlarmReceiverStatusDxNode linkStatus;
	private String host;
	private int port;

	private DxEventReceiver() throws Exception {

		listenerList = new ArrayList<DxListener<FxEvent>>();
		this.host = CodeMap.getMap().getVar(VAR.FX_BROADCASTER).getVarValue();
		this.port = CodeMap.getMap().getVar(VAR.FX_BROADCASTER_PORT).getIntValue(-1);

		Thread th = new Thread() {
			public void run() {
				while (true) {

					if (linkStatus != null) {
						linkStatus.setStatus(connected);
					}

					if (connected == false) {
						connectServer();
					}

					try {
						Thread.sleep(3000);
					} catch (Exception e) {
					}

					for (DxListener<FxEvent> listener : listenerList) {
						try {
							listener.onData(Action.loop, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		th.setName("DxEventReceiver");
		th.start();
	}

	public void add(DxListener<FxEvent> listener) {
		Logger.logger.info(listener + " added");
		listenerList.add(listener);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void broadcast(FxEvent event) {

		if (listenerList.size() == 0) {
			return;
		}

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				doEvent(event);

				for (DxListener listener : listenerList) {
					try {
						listener.onData(UiCode.Action.add, event);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void doEvent(FxEvent event) {
		if (event.getEventType().equals("ReloadSignal")) {
			new Thread() {
				public void run() {
					try {
						UiCode.reload(DxItemInformation.getInformation());
					} catch (Exception e1) {
						DxItemInformation.getInformation().showMsg(0, e1.getMessage());
					}
				}
			}.start();
		} else if (event.getEventType().equals("mo")) {
			CodeMap.getMap().reloadMoCount();
			FxEventDispatcher.getDispatcher().dispatcher(FX_EVENT_TYPE.Mo, null);
		}
	}

	public void remove(DxListener<FxEvent> listener) {
		Logger.logger.info(listener + " removed");
		listenerList.remove(listener);
	}

	private void connectServer() {
		WsClient client;

		try {
			client = new WsClient(new URI("ws://" + host + ":" + port)) {
				
				private Gson gson = new Gson();

				@Override
				public void onClose(int code, String reason, boolean remote) {
					Logger.logger.fail("closed with exit code " + code + " additional info: " + reason);
					connected = false;
				}

				@Override
				public void onMessage(String message) {

					FxEvent event;
					try {
						event = gson.fromJson(message, FxEvent.class);
						Logger.logger.trace("event={} <-- {} ", event.getEventType(), message);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}

					broadcast(event);
				}

				@Override
				public void onOpen(ServerHandshake handshakedata) {
					Logger.logger.info("new connection opened");
					connected = true;
				}
			};

			client.connect();

		} catch (URISyntaxException e) {
		}
	}
}

package com.fxms.ui.dx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.handshake.ServerHandshake;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.biz.pane.ShowAlarmDetailPane;
import com.fxms.ui.node.alarm.AlarmReceiverStatusDxNode;
import com.fxms.ws.socket.WsClient;
import com.google.gson.Gson;

import fxms.client.log.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class DxAlarmReceiver {

	public interface AlarmFilter {
		public boolean isOk(UiAlarm alarm);
	}

	private static DxAlarmReceiver broader;

	public static int SHOW_CLEAR_ALARM_MINUTES = 60;

	public static DxAlarmReceiver getBorader() {

		if (broader == null) {

			try {
				broader = new DxAlarmReceiver();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			broader.reload();

		}

		return broader;
	}

	public static void main(String[] args) throws Exception {
		// DxAsyncSelector.getSelector().setServer("125.7.128.42", 10005);
		// DxAsyncSelector.getSelector().login("SYSTEM", "SYSTEM");
		DxAlarmReceiver.getBorader();
	}

	// private Map<Long, Alarm> alarmMap3 = new HashMap<Long, Alarm>();
	/** key : mo-no */
	private Map<Long, List<UiAlarm>> alarmMap = new HashMap<Long, List<UiAlarm>>();
	private List<DxListener<UiAlarm>> listenerList;
	private CONNECT_STATUS connected = CONNECT_STATUS.Disconnected;
	private AlarmReceiverStatusDxNode linkStatus;
	private String host;
	private int port;

	enum CONNECT_STATUS {
		Disconnected, Connected, Connecting;
	}

	private DxAlarmReceiver() throws Exception {

		// this.host = CodeMap.getMap().getVar(VAR.FX_ALARM_BROADCASTER).getVarValue();
		// this.port =
		// CodeMap.getMap().getVar(VAR.FX_ALARM_BROADCASTER_PORT).getIntValue(-1);

		this.host = "125.7.128.42";
		this.port = 63818;

		alarmMap = Collections.synchronizedMap(new HashMap<Long, List<UiAlarm>>());

		listenerList = new ArrayList<DxListener<UiAlarm>>();

		Thread th = new Thread() {
			public void run() {
				while (true) {

					if (linkStatus != null) {
						linkStatus.setStatus(connected == CONNECT_STATUS.Connected);
					}

					if (connected == CONNECT_STATUS.Disconnected) {
						connectServer();
					}

					try {
						Thread.sleep(3000);
					} catch (Exception e) {
					}

					for (DxListener<UiAlarm> listener : listenerList) {
						try {
							listener.onData(Action.loop, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		th.setName("RRR");
		th.start();
	}

	public void add(DxListener<UiAlarm> listener) {
		Logger.logger.info(listener + " added");
		listenerList.add(listener);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void broadcast(UiAlarm alarm) {

		Action action = null;

		Logger.logger.info(alarm.toString());

		if (alarm.getStatus().equals("acked")) {
			action = Action.acked;
		} else if (alarm.isClearYn()) {
			action = Action.remove;
		} else if (alarm.getAckDate() > 0) {
			action = Action.update;
		} else if ("deleted".equals(alarm.getStatus())) {
			action = Action.remove;
			alarm.setClearYn(true);
		} else if ("changed".equals(alarm.getStatus())) {
			action = Action.update;
		} else if (alarm.isClearYn() == false) {
			action = Action.add;
		}

		if (action == Action.add) {
			addAlarmToMap(alarm);
		} else if (action == Action.remove) {
			removeAlarmToMap(alarm);
		} else if (action == Action.update) {
			addAlarmToMap(alarm);
		}

		final Action act = action;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for (DxListener listener : listenerList) {
					try {
						listener.onData(act, alarm);
					} catch (Exception e) {
						System.err.println(listener.getClass().getName());
						e.printStackTrace();
					}
				}
			}
		});
	}

	public List<UiAlarm> getAlarmList(DxAlarmReceiver.AlarmFilter filter) {
		List<UiAlarm> alarmList = new ArrayList<UiAlarm>();
		for (List<UiAlarm> list : alarmMap.values()) {
			for (UiAlarm alarm : list) {
				if (filter.isOk(alarm)) {
					if (alarmList.contains(alarm) == false) {
						alarmList.add(alarm);
					}
				}
			}
		}
		return alarmList;
	}

	public AlarmReceiverStatusDxNode getLinkStatus() {
		return linkStatus;
	}

	/**
	 * 입력된 관리대상의 최상위 등급의 경보를 조회한다.
	 * 
	 * @param moNo
	 *            관리대상번호
	 * @return 최상위 등급의 경보. 없으면 null
	 */
	public UiAlarm getTopAlarm(long moNo) {

		UiAlarm alarm = null;
		int curAlarmCnt = 0;

		List<UiAlarm> alarmList = alarmMap.get(moNo);
		if (alarmList != null) {
			for (UiAlarm e : alarmList) {
				if (e.isClearYn() == false) {
					if (alarm == null || e.getAlarmLevel() < alarm.getAlarmLevel()) {
						alarm = e;
					}
					curAlarmCnt++;
				}
			}
		}

		if (alarm != null) {
			alarm.setAlarmCount(curAlarmCnt);
		}

		return alarm;
	}

	/**
	 * 입력된 MO들의 경보 중에서 최상위 등급를 조회한다.
	 * 
	 * @param moNoList
	 *            찾은 MO 목록
	 * @return 최상위 경보등급. 없으면 unknown 등급
	 */
	public int getTopAlarmLevel(List<Long> moNoList) {

		int level = UiAlarm.AlarmLevel.unknown.getLevel();

		for (Long moNo : moNoList) {
			List<UiAlarm> alarmList = alarmMap.get(moNo);
			if (alarmList != null) {
				for (UiAlarm e : alarmList) {
					if (e.isClearYn() == false) {
						if (e.getAlarmLevel() < level) {
							level = e.getAlarmLevel();
						}
					}
				}
			}
		}

		return level;
	}

	public void reload() {

		alarmMap.clear();

		try {
			List<UiAlarm> alarmList = DxAsyncSelector.getSelector().getAlarmCur();

			Logger.logger.info("alarm-size ={}", alarmList.size());

			for (UiAlarm alarm : alarmList) {
				addAlarmToMap(alarm);
			}

			alarmList = DxAsyncSelector.getSelector().getAlarmHst(SHOW_CLEAR_ALARM_MINUTES);
			for (UiAlarm alarm : alarmList) {
				addAlarmToMap(alarm);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remove(DxListener<UiAlarm> listener) {
		Logger.logger.info(listener + " removed");
		listenerList.remove(listener);
	}

	public void setAlarmNode(Region node, long moNo, Group group) {

		String id = moNo + "-alarm";
		Button alarmNode = null;

		for (Node e : group.getChildren()) {
			if (e.getId() != null && e.getId().equals(id)) {
				alarmNode = (Button) e;
				break;
			}
		}

		UiAlarm alarm = getTopAlarm(moNo);

		if (alarm != null) {
			String text = "(" + alarm.getAlarmCount() + ") " + alarm.getMoName() + "." + alarm.getAlcdName();

			if (alarmNode == null) {
				alarmNode = makeAlarmNode(node, node);
				alarmNode.setId(id);
				group.getChildren().add(alarmNode);
				alarmNode.toFront();

				alarmNode.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {

						UiOpCodeVo opcode = null;
						Map<String, Object> initData = new HashMap<String, Object>();

						if (alarm.getAlarmCount() > 1) {
							opcode = CodeMap.getMap().getOpCode(OP_NAME.AlarmList);
							initData.put("moNo", alarm.getMoNo());
							initData.put("clearYn", "N");
						} else {
							opcode = CodeMap.getMap().getOpCode(OP_NAME.AlarmDetailShow);
							initData.put("alarmNo", alarm.getAlarmNo());
						}

						opcode.showScreen(node, initData, null, null);
					}
				});

			}

			alarmNode.setText(text);
			alarmNode.getStyleClass().clear();
			alarmNode.getStyleClass().add("alarm-level-" + alarm.getAlarmLevel());

		} else if (alarmNode != null) {
			group.getChildren().remove(alarmNode);
		}
	}

	public void setLinkStatus(AlarmReceiverStatusDxNode linkStatus) {
		this.linkStatus = linkStatus;
	}

	private void addAlarmToMap(UiAlarm alarm) {
		addAlarmToMap(alarm.getMoNo(), alarm);
		if (alarm.getUpperMoNo() > 0) {
			addAlarmToMap(alarm.getUpperMoNo(), alarm);
		}
	}

	private void addAlarmToMap(long moNo, UiAlarm alarm) {
		List<UiAlarm> alarmList;

		alarmList = alarmMap.get(moNo);

		if (alarmList == null) {
			alarmList = new ArrayList<UiAlarm>();
			alarmMap.put(moNo, alarmList);
		}

		if (alarmList.contains(alarm) == false) {
			alarmList.add(alarm);
		} else {
			// 기존 경보를 대처한다. alarmNo로 비교하여 처리됨.
			alarmList.remove(alarm);
			alarmList.add(alarm);
		}
	}

	private void connectServer() {
		WsClient client;

		Logger.logger.debug("ws://{}:{} connecting...", host, port);

		try {
			client = new WsClient(new URI("ws://" + host + ":" + port)) {

				private Gson gson = new Gson();

				@Override
				public void onClose(int code, String reason, boolean remote) {
					Logger.logger.fail("host={}:{}, code={}, reason={}, remote={}", host, port, code, reason, remote);
					connected = CONNECT_STATUS.Disconnected;
				}

				@Override
				public void onMessage(String message) {

					Logger.logger.trace(message);

					UiAlarm alarm;
					try {
						alarm = gson.fromJson(message, UiAlarm.class);
						if (alarm.getAlarmNo() > 0) {
							broadcast(alarm);
						}
					} catch (Exception e) {
						return;
					}

				}

				@Override
				public void onOpen(ServerHandshake handshakedata) {

					Logger.logger.info("new connection opened");
					connected = CONNECT_STATUS.Connected;

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("session-id", DxAsyncSelector.getSelector().getSessionId());

					String msg = gson.toJson(map);

					Logger.logger.info("send={}", msg);

					this.send(msg);
				}
			};

			connected = CONNECT_STATUS.Connecting;
			client.connect();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private Button makeAlarmNode(Node target, Region box) {
		Button node = new Button();
		node.setOpacity(.9);
		node.setLayoutX(box.getLayoutX() + box.getWidth() / 2);
		node.setLayoutY(box.getLayoutY() + box.getHeight() / 2);

		node.setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

		node.setMaxWidth(180);
		node.setMinSize(50, 16);
		node.setPrefWidth(box.getWidth());

		return node;
	}

	private void removeAlarmToMap(UiAlarm alarm) {
		removeAlarmToMap(alarm.getMoNo(), alarm);
		if (alarm.getUpperMoNo() > 0) {
			removeAlarmToMap(alarm.getUpperMoNo(), alarm);
		}
	}

	private void removeAlarmToMap(long moNo, UiAlarm alarm) {
		List<UiAlarm> alarmList;

		alarmList = alarmMap.get(moNo);

		if (alarmList != null) {
			alarmList.remove(alarm);
		}

	}
}

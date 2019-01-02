package com.fxms.ui.node.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.dx.DxAsyncSelector;

import fxms.client.ObjectUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;

/**
 * 네트워크 그리기
 * 
 * @author SUBKJH-DEV
 *
 */
public class NetworkDrawPane extends ScrollPane implements FxUi {

	public enum TOPOLOGY_TYPE {
		RING, P2P, P2MP, LINEAR, START;

		public static TOPOLOGY_TYPE getType(String name) {
			for (TOPOLOGY_TYPE e : TOPOLOGY_TYPE.values()) {
				if (e.name().equalsIgnoreCase(name)) {
					return e;
				}
			}

			return RING;
		}
	}

	public final int RING_DIRECTION_West2East = 1;
	public final int RING_DIRECTION_East2West = 0;

	protected final Group box = new Group();
	private List<NetworkNode> nodeList;
	private TOPOLOGY_TYPE topologyType = TOPOLOGY_TYPE.P2P;
	private NetworkEvent networkEvent;
	private UiNetVo netVo = new UiNetVo();
	private boolean editable = false;
	/** 링크가 다시 그려지기 때문에 연결 정보를 별도 보관한다. */
	private Map<String, NetworkLinkVo> linkMap = new HashMap<String, NetworkLinkVo>();
	private double ringRotationAngle = 90;
	private int ringDirection = RING_DIRECTION_East2West;

	public NetworkDrawPane() {
		this(null);
	}

	public NetworkDrawPane(Label tooltip) {

		if (tooltip != null) {
			networkEvent = new NetworkEvent(this, tooltip);
		}

		nodeList = new ArrayList<NetworkNode>();

		BorderPane pane = new BorderPane();
		pane.setId("canvas");

		if (networkEvent != null) {
			networkEvent.addEvent(pane);
			setContextMenu(networkEvent.getContextMenu());
		}

		pane.setCenter(box);
		setFitToHeight(true);
		setFitToWidth(true);
		setContent(pane);

		// setCenter(group);
		setStyle("-fx-background-color: transparent;");

		widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				redraw();
			}
		});

		heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				redraw();
			}
		});

	}

	public void addNetItem(UiNetItemVo item) {
		NetworkLinkVo vo = new NetworkLinkVo(item.getStartNeMoNo(), item.getEndNeMoNo());
		vo.setEastIfMoNo(item.getEndIfMoNo());
		vo.setWestIfMoNo(item.getStartIfMoNo());

		String id = item.getStartNeMoNo() + "-" + item.getEndNeMoNo();
		linkMap.put(id, vo);

	}

	/**
	 * 노드를 추가한다.
	 * 
	 * @param node
	 *            추가할 노드
	 */
	public void addNode(NetworkNode node) {

		nodeList.add(node);

		if (networkEvent != null) {
			networkEvent.addEvent(node);
		}

		redraw();

	}

	public UiNetVo getNet() {

		netVo.setTopologyType(topologyType.name());

		netVo.getItemList().clear();
		NetworkLinkVo vo;
		UiNetItemVo item;
		int itemSeqno = 1;

		// link는 제일 마지막에 추가된 내용이 제일 앞에 존재하므로 뒤에서 부터 읽는다.
		Node node;
		for (int i = box.getChildren().size() - 1; i >= 0; i--) {
			node = box.getChildren().get(i);

			if (node.getUserData() instanceof NetworkLinkVo) {
				vo = (NetworkLinkVo) node.getUserData();
				item = new UiNetItemVo();
				item.setItemSeqno(itemSeqno);
				item.setEndIfMoNo(vo.getEastIfMoNo());
				item.setEndNeMoNo(vo.getEastNeMoNo());
				item.setStartIfMoNo(vo.getWestIfMoNo());
				item.setStartNeMoNo(vo.getWestNeMoNo());

				netVo.getItemList().add(item);

				itemSeqno++;
			}
		}

		return netVo;
	}

	/**
	 * 
	 * @return 노드 목록
	 */
	public List<NetworkNode> getNetworkNodeList() {
		return nodeList;
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return null;
	}

	@Override
	public void init(UiOpCodeVo opcode) {
	}

	@Override
	public void initData(Map<String, Object> data) {

		ObjectUtil.toObject(data, netVo);
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("netNo", netVo.getNetNo());
		para.put("netName", netVo.getNetName());
		List<UiNetItemVo> itemList = DxAsyncSelector.getSelector().selectList("nms", "get-network-item", para,
				UiNetItemVo.class);

		List<Long> moNoList = new ArrayList<Long>();

		for (UiNetItemVo item : itemList) {

			addNetItem(item);

			if (moNoList.contains(item.getStartNeMoNo()) == false) {
				moNoList.add(item.getStartNeMoNo());
			}

			if (moNoList.contains(item.getEndNeMoNo()) == false) {
				moNoList.add(item.getEndNeMoNo());
			}
		}

		for (Long moNo : moNoList) {

			if (editable) {
				addNode(new NetworkNode(moNo));
			} else {
				addNode(new NetworkNodeM(moNo));
			}

		}

		TOPOLOGY_TYPE type = TOPOLOGY_TYPE.getType(netVo.getTopologyType());
		setTopologyType(type);

	}

	/**
	 * 네트워크를 다시 그린다.
	 */
	public void redraw() {

		box.getChildren().clear();

		if (nodeList.size() == 0) {
			return;
		}

		if (topologyType == TOPOLOGY_TYPE.RING) {
			RING_DATA data = new RING_DATA();
			data.centerX = getCenterX();
			data.centerY = getCenterY();
			data.radiusX = getRadiusX();
			data.radiusY = getRadiusY();
			drawRing(data);

			data.radiusX = data.radiusX - 8;
			data.radiusY = data.radiusY - 8;
			drawRing(data);

		} else if (topologyType == TOPOLOGY_TYPE.P2MP) {
			drawP2MP();
		} else if (topologyType == TOPOLOGY_TYPE.LINEAR) {
			drawLinear();
		} else if (topologyType == TOPOLOGY_TYPE.START) {
			drawStart();
		} else if (topologyType == TOPOLOGY_TYPE.P2P) {
			drawP2P();
		}

		this.requestLayout();

	}

	/**
	 * 노드 삭제
	 * 
	 * @param node
	 *            삭제할 노드
	 */
	public void removeNode(Node node) {

		if (nodeList.remove(node)) {
			redraw();
		} else {
			box.getChildren().remove(node);
		}

	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setNetVo(UiNetVo netVo) {
		this.netVo = netVo;
	}

	public void setRingDirection(int ringDirection) {
		this.ringDirection = ringDirection;
		redraw();
	}

	public void setRingRotationAngle(double ringRotationAngle) {
		this.ringRotationAngle = ringRotationAngle;
		redraw();
	}

	/**
	 * 
	 * @param topologyType
	 *            네트워크 종류
	 */
	public void setTopologyType(TOPOLOGY_TYPE topologyType) {
		this.topologyType = topologyType;
		redraw();
	}

	private Line drawLine(Node node1, Node node2) {

		Line line = new Line();

		NetworkLinkVo vo = getLinkVo(node1, node2);
		line.setId(vo.toString());
		line.setUserData(vo);

		line.setStartX(getCenterX(node1));
		line.setStartY(getCenterY(node1));

		line.setEndX(getCenterX(node2));
		line.setEndY(getCenterY(node2));

		line.setStrokeType(StrokeType.CENTERED);
		line.setStrokeWidth(3);
		line.setStroke(Color.FORESTGREEN);

		if (networkEvent != null) {
			networkEvent.addEvent(line);
		}

		return line;
	}

	private void drawLinear() {
		if (nodeList.size() == 0) {
			return;
		}

		final int nodeSizeRow = 4;

		double hGap = getWidth() / (nodeSizeRow + 1f);
		int rowCnt = nodeList.size() / nodeSizeRow;
		if (nodeList.size() % nodeSizeRow != 0) {
			rowCnt++;
		}
		double vGap = getHeight() / (rowCnt + 1f);

		Node node, nodePrev = null;
		double x, y;
		int row;

		for (int i = 0; i < nodeList.size(); i++) {

			row = i / nodeSizeRow;

			if (row % 2 == 0) {
				// 짝수열
				x = hGap * ((i % nodeSizeRow) + 1);
			} else {
				// 홀수열. 오른쪽에서 왼쪽으로 배치
				x = hGap * ((row + 1) * nodeSizeRow - i);
			}

			y = vGap * (row + 1);

			node = nodeList.get(i);
			node.setLayoutX(x);
			node.setLayoutY(y);
			box.getChildren().add(node);

			if (nodePrev != null) {
				box.getChildren().add(0, drawLine(nodePrev, node));
			}

			nodePrev = node;

		}
	}

	private void drawP2MP() {
		if (nodeList.size() == 0) {
			return;
		}

		Node node2;
		Node node = nodeList.get(0);
		node.setLayoutX(getCenterX());
		node.setLayoutY(getCenterY() - (getCenterY() / 2));

		box.getChildren().add(node);

		double y = getCenterY() + (getCenterY() / 2);
		double gap = getWidth() / nodeList.size();
		double x = gap;

		for (int i = 1; i < nodeList.size(); i++) {
			node2 = nodeList.get(i);
			node2.setLayoutX(x);
			node2.setLayoutY(y);
			box.getChildren().add(node2);

			box.getChildren().add(0, drawLine(node, node2));

			x += gap;
		}
	}

	private void drawP2P() {
		if (nodeList.size() == 0) {
			return;
		}

		double y = getCenterY();
		double gap = getWidth() / nodeList.size();
		double x = gap;
		Node node, nodePrev = null;

		for (int i = 0; i < nodeList.size(); i++) {

			x = gap * i;

			node = nodeList.get(i);
			node.setLayoutX(x);
			node.setLayoutY(y);
			box.getChildren().add(node);

			if (nodePrev != null) {
				box.getChildren().add(0, drawLine(nodePrev, node));
			}

			nodePrev = node;
		}
	}

	private void drawRing(RING_DATA data) {

		int size = nodeList.size();
		double d2 = 2f * Math.PI / 360f;
		double x;
		double y;
		double angle = 360f / size;
		double startAngle;
		NetworkNode node;

		for (int i = 0; i < nodeList.size(); i++) {

			if (ringDirection == RING_DIRECTION_West2East) {
				startAngle = angle * (size - i - 1);
			} else {
				startAngle = angle * i;
			}

			startAngle = ringRotationAngle + startAngle;
			if (startAngle > 360f) {
				startAngle -= 360f;
			}

			node = nodeList.get(i);
			if (box.getChildren().contains(node) == false) {
				node.setText("(" + i + ") " + node.getMoNo());
				node.setOpacity(.75);

				if (ringDirection == RING_DIRECTION_West2East) {
					x = getCenterX() + Math.cos(d2 * (startAngle + angle)) * data.radiusX;
					y = getCenterY() - Math.sin(d2 * (startAngle + angle)) * data.radiusY;
				} else {
					x = getCenterX() + Math.cos(d2 * startAngle) * data.radiusX;
					y = getCenterY() - Math.sin(d2 * startAngle) * data.radiusY;
				}

				node.setLayoutX(x - 5);
				node.setLayoutY(y - 5);
				box.getChildren().add(node);
			}

			if (i < nodeList.size() - 1) {
				makeArc(node, nodeList.get(i + 1), startAngle, angle, data);
			} else {
				makeArc(node, nodeList.get(0), startAngle, angle, data);
			}
		}
	}

	private void drawStart() {
		if (nodeList.size() == 0) {
			return;
		}

		Node node2;
		Node node = nodeList.get(0);
		node.setLayoutX(getCenterX());
		node.setLayoutY(getCenterY());

		box.getChildren().add(node);

		int size = nodeList.size() - 1;

		for (int index = 0; index < size; index++) {

			double d = 2 * Math.PI / size;
			double x = getCenterX() + Math.cos(d * index) * getRadiusX();
			double y = getCenterY() - Math.sin(d * index) * getRadiusY();

			node2 = nodeList.get(index + 1);
			node2.setLayoutX(x);
			node2.setLayoutY(y);
			box.getChildren().add(node2);

			box.getChildren().add(0, drawLine(node, node2));

		}

	}

	private double getCenterX() {
		return getWidth() / 2;
	}

	private double getCenterX(Node node) {
		if (node instanceof Region) {
			Region r = (Region) node;
			return node.getLayoutX() + (r.getWidth() / 2);
		} else {
			return node.getLayoutX();
		}
	}

	private double getCenterY() {
		return getHeight() / 2;
	}

	private double getCenterY(Node node) {
		if (node instanceof Region) {
			Region r = (Region) node;
			return node.getLayoutY() + (r.getHeight() / 2);
		} else {
			return node.getLayoutY();
		}
	}

	private NetworkLinkVo getLinkVo(Node node1, Node node2) {

		String id = node1.getId() + "-" + node2.getId();

		NetworkLinkVo vo = linkMap.get(id);

		if (vo == null) {
			vo = new NetworkLinkVo(node1, node2);
			linkMap.put(id, vo);
		}

		return vo;
	}

	private double getRadiusX() {
		double size = Math.min(getWidth(), getHeight());

		return size / 2f - 48f;
	}

	private double getRadiusY() {
		return getRadiusX();
	}

	class RING_DATA {
		double centerX;
		double centerY;
		double radiusX;
		double radiusY;
		int direction = RING_DIRECTION_West2East;
	}

	private void makeArc(Node nodePrev, Node node, double startAngle, double angle, RING_DATA data) {

		NetworkLinkVo vo;

		if (ringDirection == data.direction) {
			vo = getLinkVo(nodePrev, node);
		} else {
			vo = getLinkVo(node, nodePrev);
		}

		Arc arc = new Arc();
		arc.setId(vo.toString());
		arc.setUserData(vo);
		arc.setCenterX(data.centerX);
		arc.setCenterY(data.centerY);
		arc.setRadiusX(data.radiusX);
		arc.setRadiusY(data.radiusY);
		arc.setStartAngle(startAngle);
		arc.setLength(angle);
		// arc.setType(ArcType.ROUND);
		// arc.setType(ArcType.CHORD);
		arc.setType(ArcType.OPEN);
		arc.setFill(Color.TRANSPARENT);
		arc.setStrokeWidth(3);
		arc.setStrokeType(StrokeType.INSIDE);
		arc.setStroke(Color.FORESTGREEN);
		// arc.setOpacity(.6);

		box.getChildren().add(0, arc);

		if (networkEvent != null) {
			networkEvent.addEvent(arc);
		}

	}

}

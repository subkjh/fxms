package com.fxms.ui.node.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.node.diagram.node.DiagNode;
import com.fxms.ui.node.diagram.node.DiagNodeDiagram;
import com.fxms.ui.node.diagram.node.DiagNodeLine;
import com.fxms.ui.node.diagram.node.DiagNodeMo;
import com.fxms.ui.node.diagram.vo.DiagLineVo;
import com.fxms.ui.node.diagram.vo.DiagMainVo;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;
import com.fxms.ui.node.diagram.vo.DiagPropertyVo;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class FxDiagram extends AnchorPane {

	private DiagramCfg cfg = new DiagramCfg();
	private List<DiagNodeLine> lineList = new ArrayList<DiagNodeLine>();
	private DiagMainVo mainVo;

	public FxDiagram() {
		mainVo = new DiagMainVo();
	}

	public DiagNode addDiagNode(DiagNodeVo vo) {

		if (vo.isRemoved()) {
			// 삭제된 내용을 추가하지 않는다.
			return null;
		}

		DiagNode node = (DiagNode) DiagNode.makeDiagNode(vo);
		if (node != null) {
			getChildren().add((Node) node);
			return node;
		}
		return null;
	}

	public DiagNodeLine addLine(DiagNode startNode, DiagNode endNode) {

		DiagLineVo vo = new DiagLineVo();
		vo.setLinkDiagNodeNo1(startNode.getDiagNodeVo().getDiagNodeNo());
		vo.setLinkDiagNodeNo2(endNode.getDiagNodeVo().getDiagNodeNo());

		DiagNodeLine line = new DiagNodeLine(vo, startNode, endNode);
		lineList.add(line);
		getChildren().add(line);
		line.toBack();

		return line;
	}

	public DiagramCfg getCfg() {
		return cfg;
	}

	public DiagMainVo getDiagMainVo() {

		DiagLineVo lineVo;
		DiagNodeVo nodeVo;

		int diagNodeNo = 1;

		mainVo.getLineList().clear();
		mainVo.getNodeList().clear();
		mainVo.getPropertyList().clear();

		for (DiagNodeLine line : lineList) {
			lineVo = line.getDiagLineVo();
			lineVo.setDiagNodeNo(diagNodeNo++);
			mainVo.getLineList().add(lineVo);

			for (String key : lineVo.getProperties().keySet()) {
				mainVo.getPropertyList()
						.add(new DiagPropertyVo(lineVo.getDiagNodeNo(), key, lineVo.getProperties().get(key)));
			}
		}

		for (Node node : getChildren()) {
			if (node instanceof DiagNode) {
				nodeVo = ((DiagNode) node).getDiagNodeVo();
				nodeVo.setDiagNodeNo(diagNodeNo++);
				mainVo.getNodeList().add(nodeVo);

				for (String key : nodeVo.getProperties().keySet()) {
					mainVo.getPropertyList()
							.add(new DiagPropertyVo(nodeVo.getDiagNodeNo(), key, nodeVo.getProperties().get(key)));
				}
			}
		}

		return mainVo;
	}

	public DiagNode getDiagNode(String id) {
		for (Node node : getChildren()) {
			if (node.getId() != null && node.getId().equals(id) && node instanceof DiagNode) {
				return (DiagNode) node;
			}
		}
		return null;
	}

	public DiagNodeDiagram getDiagNodeDiagram(int diagNo) {
		for (Node node : getChildren()) {
			if (node instanceof DiagNodeDiagram) {
				if (((DiagNodeDiagram) node).getDiagNo() == diagNo) {
					return (DiagNodeDiagram) node;
				}
			}
		}
		return null;
	}

	public DiagNodeMo getDiagNodeMo(long moNo) {
		for (Node node : getChildren()) {
			if (node instanceof DiagNodeMo) {
				if (((DiagNodeMo) node).getMoNo() == moNo) {
					return (DiagNodeMo) node;
				}
			}
		}
		return null;
	}

	public double getDiagramHeight() {
		DiagNodeVo nodeVo;
		double minY = Double.MAX_VALUE;
		double maxHeight = Double.MIN_VALUE;

		for (Node node : getChildren()) {
			if (node instanceof DiagNode) {
				nodeVo = ((DiagNode) node).getDiagNodeVo();
				minY = Math.min(minY, nodeVo.getDiagNodeY());
				maxHeight = Math.max(maxHeight, nodeVo.getDiagNodeHeight());
			}
		}

		return maxHeight + minY;

	}

	public double getDiagramWidth() {
		DiagNodeVo nodeVo;
		double minX = Double.MAX_VALUE;
		double maxWidth = Double.MIN_VALUE;

		for (Node node : getChildren()) {
			if (node instanceof DiagNode) {
				nodeVo = ((DiagNode) node).getDiagNodeVo();
				minX = Math.min(minX, nodeVo.getDiagNodeX());
				maxWidth = Math.max(maxWidth, nodeVo.getDiagNodeWidth());
			}
		}

		return maxWidth + minX;
	}

	public Node getNode(String id) {
		for (Node node : getChildren()) {
			if (node.getId() != null && node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getNodeList(Class<T> classOfT) {
		List<T> list = new ArrayList<T>();

		for (Node node : getChildren()) {
			if (classOfT.isInstance(node)) {
				list.add((T) node);
			}
		}
		return list;
	}

	public void redrawLines(DiagNode node) {
		for (DiagNodeLine line : lineList) {
			if (line.containsNode(node)) {
				line.redraw();
			}
		}
	}

	public void setDiagNo(int diagNo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("diagNo", diagNo);
		loadDiagram(parameters);
	}

	protected void loadDiagram(Map<String, Object> parameters) {

		UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.DiagramGet);
		Map<String, Object> retMap = DxAsyncSelector.getSelector().callMethod(FxDiagram.this, opCode, parameters);

		if (retMap != null) {

			DiagMainVo mainVo = new DiagMainVo();
			mainVo.setMap(retMap);

			checkMo(mainVo);

			setDiagram(mainVo);

		} 
	}

	/**
	 * 삭제된 MO를 정리한다.
	 * 
	 * @param main
	 */
	private void checkMo(DiagMainVo main) {

		List<Long> moNoList = new ArrayList<Long>();
		long moNo;

		for (DiagNodeVo node : main.getNodeList()) {
			moNo = node.getMoNo();
			if (moNo >= 0) {
				moNoList.add(moNo);
			}
		}

		if (moNoList.size() > 0) {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("moNo", moNoList);
			List<Mo> moList = DxAsyncSelector.getSelector().getMoList(para, null);
			NODE: for (DiagNodeVo node : main.getNodeList()) {
				moNo = node.getMoNo();
				if (moNo >= 0) {
					for (Mo mo : moList) {
						if (mo.getMoNo() == moNo) {
							continue NODE;
						}
					}
					node.setRemoved(true);
				}
			}

		}

	}

	public void setDiagram(DiagMainVo diagram) {

		getChildren().clear();
		lineList.clear();

		if (diagram == null) {
			this.mainVo = new DiagMainVo();
		} else {
			this.mainVo = diagram;
		}

		for (DiagNodeVo nodeVo : mainVo.getNodeList()) {
			addDiagNode(nodeVo);
		}

		DiagNode startNode, endNode;

		for (DiagLineVo vo : mainVo.getLineList()) {
			startNode = getDiagNode(vo.getLinkDiagNodeNo1() + "");
			endNode = getDiagNode(vo.getLinkDiagNodeNo2() + "");

			if (startNode != null && endNode != null) {
				this.addLine(startNode, endNode);
			}
		}

		setPrefSize(mainVo.getWidth(), mainVo.getHeight());
	}
}

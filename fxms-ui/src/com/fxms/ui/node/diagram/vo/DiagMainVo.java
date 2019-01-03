package com.fxms.ui.node.diagram.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.utils.ObjectUtil;

public class DiagMainVo {

	private int diagNo;

	private String diagTitle;

	private int opNo;

	private List<DiagNodeVo> nodeList;

	private List<DiagLineVo> lineList;

	private List<DiagPropertyVo> propertyList;

	public DiagMainVo() {

	}

	public int getDiagNo() {
		return diagNo;
	}

	public String getDiagTitle() {
		return diagTitle;
	}

	public List<DiagLineVo> getLineList() {
		if (lineList == null) {
			lineList = new ArrayList<DiagLineVo>();
		}
		return lineList;
	}

	public List<DiagNodeVo> getNodeList() {
		if (nodeList == null) {
			nodeList = new ArrayList<DiagNodeVo>();
		}
		return nodeList;
	}

	public int getOpNo() {
		return opNo;
	}

	public List<DiagPropertyVo> getPropertyList() {
		if (propertyList == null) {
			propertyList = new ArrayList<DiagPropertyVo>();
		}
		return propertyList;
	}

	public Map<String, Object> getSaveMap() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("main", this);
		parameters.put("node", getNodeList());
		parameters.put("line", getLineList());
		parameters.put("property", getPropertyList());
		return parameters;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setMap(Map<String, Object> properties) {

		ObjectUtil.toObject((Map) properties.get("main"), this);

		DiagNodeVo nodeVo;
		List<Map<String, Object>> nodeMapList = (List<Map<String, Object>>) properties.get("node");
		for (Map<String, Object> nodeMap : nodeMapList) {
			nodeVo = new DiagNodeVo();
			ObjectUtil.toObject(nodeMap, nodeVo);
			getNodeList().add(nodeVo);
		}

		DiagLineVo lineVo;
		List<Map<String, Object>> lineMapList = (List<Map<String, Object>>) properties.get("line");
		for (Map<String, Object> lineMap : lineMapList) {
			lineVo = new DiagLineVo();
			ObjectUtil.toObject(lineMap, lineVo);
			getLineList().add(lineVo);
		}

		DiagPropertyVo propertyVo;
		List<Map<String, Object>> propertyMapList = (List<Map<String, Object>>) properties.get("property");
		PRO: for (Map<String, Object> propertyMap : propertyMapList) {

			propertyVo = new DiagPropertyVo();
			ObjectUtil.toObject(propertyMap, propertyVo);

			for (DiagNodeVo e : getNodeList()) {
				if (e.getDiagNodeNo() == propertyVo.getDiagNodeNo()) {
					e.getProperties().put(propertyVo.getPropertyName(), propertyVo.getPropertyValue());
					continue PRO;
				}
			}

			for (DiagLineVo e : getLineList()) {
				if (e.getDiagNodeNo() == propertyVo.getDiagNodeNo()) {
					e.getProperties().put(propertyVo.getPropertyName(), propertyVo.getPropertyValue());
					continue PRO;
				}
			}
		}
	}

	public void setDiagNo(int diagNo) {
		this.diagNo = diagNo;
	}

	public void setDiagTitle(String diagTitle) {
		this.diagTitle = diagTitle;
	}

	public void setOpNo(int opNo) {
		this.opNo = opNo;
	}

	public String toString() {
		return String.valueOf(ObjectUtil.toMap(this));
	}

	public double getHeight() {
		double maxHeight = Double.MIN_VALUE;
		for (DiagNodeVo node : getNodeList()) {
			maxHeight = Math.max(maxHeight, node.getDiagNodeY() + node.getDiagNodeHeight());
		}
		return maxHeight + getY();
	}

	public double getY() {
		double minY = Double.MAX_VALUE;

		for (DiagNodeVo node : getNodeList()) {
			minY = Math.min(minY, node.getDiagNodeY());
		}

		return minY;
	}

	public double getWidth() {
		double maxWidth = Double.MIN_VALUE;
		for (DiagNodeVo node : getNodeList()) {
			maxWidth = Math.max(maxWidth, node.getDiagNodeX() + node.getDiagNodeWidth());
		}

		return maxWidth + getX();
	}

	public double getX() {
		double minX = Double.MAX_VALUE;

		for (DiagNodeVo node : getNodeList()) {
			minX = Math.min(minX, node.getDiagNodeX());
		}
		return minX;
	}

}

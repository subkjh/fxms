package com.fxms.ui.bas.vo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.node.diagram.event.FxBounds;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class UiBasicVo {

	private int uiGroupNo;

	private int uiNo;

	private String uiTitle;

	private String uiStyle;

	private int uiX;

	private int uiY;

	private int uiWidth;

	private int uiHeight;

	private int opNo;

	private boolean visibleYn = true;

	private int seqBy;

	private List<UiPropertyVo> children;

	public UiBasicVo() {

	}

	public UiBasicVo(FxBounds bounds) {
		uiX = (int) bounds.getX();
		uiY = (int) bounds.getY();
		uiWidth = (int) bounds.getWidth();
		uiHeight = (int) bounds.getHeight();
	}

	public List<UiPropertyVo> getChildren() {
		if (children == null) {
			children = new ArrayList<UiPropertyVo>();
		}
		return children;
	}

	public int getOpNo() {
		return opNo;
	}

	public Map<String, Object> getProperties() {

		Map<String, Object> map = new HashMap<String, Object>();

		for (UiPropertyVo vo : getChildren()) {
			map.put(vo.getPropertyName(), vo.getPropertyValue());
		}

		return map;
	}

	public String getProperty(String name) {

		for (UiPropertyVo vo : getChildren()) {
			if (vo.getPropertyName().equals(name)) {
				return vo.getPropertyValue();
			}
		}

		return null;
	}

	public Number getPropertyNumber(String name) {

		String value = getProperty(name);
		if (value != null) {
			try {
				return Double.valueOf(value);
			} catch (Exception e) {
			}
		}

		return null;
	}

	public Boolean getPropertyBoolean(String name) {

		String value = getProperty(name);
		if (value != null) {
			String s = value.toString().toLowerCase();
			return "y".equals(s) || "1".equals(s) || "true".equals(s);
		}

		return false;
	}

	public int getSeqBy() {
		return seqBy;
	}

	public int getUiGroupNo() {
		return uiGroupNo;
	}

	public int getUiHeight() {
		return uiHeight;
	}

	public int getUiNo() {
		return uiNo;
	}

	public String getUiStyle() {
		return uiStyle;
	}

	public String getUiTitle() {
		return uiTitle;
	}

	public int getUiWidth() {
		return uiWidth;
	}

	public int getUiX() {
		return uiX;
	}

	public int getUiY() {
		return uiY;
	}

	public boolean isVisibleYn() {
		return visibleYn;
	}

	public Node makeNode() throws Exception {

		UiOpCodeVo opcode = CodeMap.getMap().getOpCode(getOpNo());
		if (opcode == null || opcode.getUiJavaClass() == null) {
			return null;
		}

		Node node = (Node) Class.forName(opcode.getUiJavaClass()).newInstance();

		if (node instanceof DxNode) {
			if (((DxNode) node).initDxNode(this) == false) {
				node = new Label("ERROR " + this.toString());
				((Label) node).setWrapText(true);
			}
		} else {
			node = new Label("Not DxNode " + this.toString());
			((Label) node).setWrapText(true);
		}

		node.setUserData(this);
		node.setLayoutX(this.getUiX());
		node.setLayoutY(this.getUiY());

		if (node instanceof Region) {
			Region region = (Region) node;
			region.setPrefWidth(this.getUiWidth());
			region.setPrefHeight(this.getUiHeight());

			if (showBorder()) {
				region.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(2))));
			}
		}

		return node;
	}

	public void setOpNo(int opNo) {
		this.opNo = opNo;
	}

	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

	public void setUiGroupNo(int uiGroupNo) {
		this.uiGroupNo = uiGroupNo;
	}

	public void setUiHeight(int uiHeight) {
		this.uiHeight = uiHeight;
	}

	public void setUiNo(int uiNo) {
		this.uiNo = uiNo;
	}

	public void setUiStyle(String uiStyle) {
		this.uiStyle = uiStyle;
	}

	public void setUiTitle(String uiTitle) {
		this.uiTitle = uiTitle;
	}

	public void setUiWidth(int uiWidth) {
		this.uiWidth = uiWidth;
	}

	public void setUiX(int uiX) {
		this.uiX = uiX;
	}

	public void setUiY(int uiY) {
		this.uiY = uiY;
	}

	public void setVisibleYn(boolean visibleYn) {
		this.visibleYn = visibleYn;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("title=" + uiTitle);
		sb.append(",width=" + uiWidth);
		sb.append(",height=" + uiHeight);
		sb.append(",properties=" + getChildren());
		return sb.toString();
	}

	public boolean showBorder() {
		Object value = getProperties().get("border");
		if (value != null) {
			String s = value.toString().toLowerCase();
			return "y".equals(s) || "1".equals(s) || "true".equals(s);
		} else {
			return true;
		}
	}
}

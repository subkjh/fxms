package com.fxms.ui.node.diagram.vo;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.bas.utils.ObjectUtil;

public class DiagLineVo {

	private int diagNo;

	private int diagNodeNo;

	private int linkDiagNodeNo1;

	private int linkDiagNodeNo2;

	private Map<String, Object> properties;

	public int getDiagNo() {
		return diagNo;
	}

	public int getDiagNodeNo() {
		return diagNodeNo;
	}

	public int getLinkDiagNodeNo1() {
		return linkDiagNodeNo1;
	}

	public int getLinkDiagNodeNo2() {
		return linkDiagNodeNo2;
	}

	public Map<String, Object> getProperties() {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public void setDiagNo(int diagNo) {
		this.diagNo = diagNo;
	}

	public void setDiagNodeNo(int diagNodeNo) {
		this.diagNodeNo = diagNodeNo;
	}

	public void setLinkDiagNodeNo1(int linkDiagNodeNo1) {
		this.linkDiagNodeNo1 = linkDiagNodeNo1;
	}

	public void setLinkDiagNodeNo2(int linkDiagNodeNo2) {
		this.linkDiagNodeNo2 = linkDiagNodeNo2;
	}

	public String toString() {
		return String.valueOf(ObjectUtil.toMap(this));
	}

}

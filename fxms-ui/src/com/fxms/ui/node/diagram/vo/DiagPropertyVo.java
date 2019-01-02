package com.fxms.ui.node.diagram.vo;

import com.fxms.ui.bas.utils.ObjectUtil;

public class DiagPropertyVo {

	private int diagNo;

	private int diagNodeNo;

	private String propertyName;

	private Object propertyValue;

	public DiagPropertyVo() {

	}

	public DiagPropertyVo(int diagNodeNo, String propertyName, Object propertyValue) {
		this.diagNodeNo = diagNodeNo;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public int getDiagNo() {
		return diagNo;
	}

	public int getDiagNodeNo() {
		return diagNodeNo;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	public void setDiagNo(int diagNo) {
		this.diagNo = diagNo;
	}

	public void setDiagNodeNo(int diagNodeNo) {
		this.diagNodeNo = diagNodeNo;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String toString() {
		return String.valueOf(ObjectUtil.toMap(this));
	}
}

package com.fxms.ui.bas.vo.ui;

public class UiPropertyVo {

	private int uiGroupNo;

	private int uiNo;

	private String propertyName;

	private String propertyValue;

	public UiPropertyVo() {

	}

	@Override
	public String toString() {
		return propertyName + "=" + propertyValue;
	}

	public UiPropertyVo(int uiGroupNo, int uiNo, String propertyName, String propertyValue) {
		this.uiGroupNo = uiGroupNo;
		this.uiNo = uiNo;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public int getUiGroupNo() {
		return uiGroupNo;
	}

	public void setUiGroupNo(int uiGroupNo) {
		this.uiGroupNo = uiGroupNo;
	}

	public int getUiNo() {
		return uiNo;
	}

	public void setUiNo(int uiNo) {
		this.uiNo = uiNo;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}

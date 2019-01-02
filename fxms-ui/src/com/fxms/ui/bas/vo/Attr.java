package com.fxms.ui.bas.vo;

public class Attr {

	private String attrId;

	private String attrText;

	private Object userData;

	public Attr() {

	}

	public Attr(String attrId, String attrText, Object userData) {
		this.attrId = attrId;
		this.attrText = attrText;
		this.userData = userData;
	}

	public String getAttrId() {
		return attrId;
	}

	public String getAttrText() {
		return attrText;
	}

	public Object getUserData() {
		return userData;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public void setAttrText(String attrText) {
		this.attrText = attrText;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

	@Override
	public String toString() {
		return String.valueOf(attrText);
	}

}

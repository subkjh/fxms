package com.fxms.ui.bas.vo.folder;

public class UiUserTreeVo {

	private String etcMoTreeName;

	private String iconName;

	private String inloType;

	private int orderBy = 0;

	private int shareUgrpNo = 0;

	private String targetMoClass;

	private String treeName;

	private String treeDescr;

	private int treeNo;

	private int upperTreeNo;

	private int userNo = 0;

	public String getEtcMoTreeName() {
		return etcMoTreeName;
	}

	public String getIconName() {
		return iconName;
	}

	public String getInloType() {
		return inloType;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public int getShareUgrpNo() {
		return shareUgrpNo;
	}

	public String getTargetMoClass() {
		return targetMoClass;
	}

	public String getTreeDescr() {
		return treeDescr;
	}

	public String getTreeName() {
		return treeName;
	}

	public int getTreeNo() {
		return treeNo;
	}

	public int getUpperTreeNo() {
		return upperTreeNo;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setEtcMoTreeName(String etcMoTreeName) {
		this.etcMoTreeName = etcMoTreeName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public void setInloType(String inloType) {
		this.inloType = inloType;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public void setShareUgrpNo(int shareUgrpNo) {
		this.shareUgrpNo = shareUgrpNo;
	}

	public void setTargetMoClass(String targetMoClass) {
		this.targetMoClass = targetMoClass;
	}

	public void setTreeDescr(String treeDescr) {
		this.treeDescr = treeDescr;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public void setTreeNo(int treeNo) {
		this.treeNo = treeNo;
	}

	public void setUpperTreeNo(int upperTreeNo) {
		this.upperTreeNo = upperTreeNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(treeName);
		return sb.toString();
	}

}

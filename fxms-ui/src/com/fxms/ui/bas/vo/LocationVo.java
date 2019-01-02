package com.fxms.ui.bas.vo;

public class LocationVo {

	private int inloNo;
	private int upperInloNo = 0;
	private String inloName;
	private String inloFname;
	private String inloType;

	public LocationVo(String inloName) {
		this.inloName = inloName;
	}

	public LocationVo() {

	}

	public String getInloFname() {
		return inloFname;
	}

	public String getInloName() {
		return inloName;
	}

	public int getInloNo() {
		return inloNo;
	}

	public String getInloType() {
		return inloType;
	}

	public int getUpperInloNo() {
		return upperInloNo;
	}

	public void setInloFname(String inloFname) {
		this.inloFname = inloFname;
	}

	public void setInloName(String inloName) {
		this.inloName = inloName;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public void setInloType(String inloType) {
		this.inloType = inloType;
	}

	public void setUpperInloNo(int upperInloNo) {
		this.upperInloNo = upperInloNo;
	}

	@Override
	public String toString() {
		return inloName;
	}
}

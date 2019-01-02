package com.fxms.ui.bas.vo;

public class UiTimeVo {

	private String dataType;

	private long doneDate;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public long getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(long doneDate) {
		this.doneDate = doneDate;
	}

	public String toString() {
		return dataType + "=" + doneDate;
	}

}

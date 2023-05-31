package fxms.bas.impl.vo;

import subkjh.dao.def.FxColumn;

public class CodeVo {

	@FxColumn(name = "CD_CLASS", size = 30, comment = "코드분류")
	private String cdClass;

	@FxColumn(name = "CD_CODE", size = 30, comment = "코드")
	private String cdCode;

	@FxColumn(name = "CD_NAME", size = 50, nullable = true, comment = "코드명")
	private String cdName;

	@FxColumn(name = "VAL1", size = 1000, nullable = true, comment = "값1")
	private String val1;

	@FxColumn(name = "VAL2", size = 1000, nullable = true, comment = "값2")
	private String val2;

	@FxColumn(name = "VAL3", size = 1000, nullable = true, comment = "값3")
	private String val3;

	@FxColumn(name = "VAL4", size = 1000, nullable = true, comment = "값4")
	private String val4;

	@FxColumn(name = "VAL5", size = 1000, nullable = true, comment = "값5")
	private String val5;

	@FxColumn(name = "VAL6", size = 1000, nullable = true, comment = "값6")
	private String val6;

	public String getCdClass() {
		return cdClass;
	}

	public void setCdClass(String cdClass) {
		this.cdClass = cdClass;
	}

	public String getCdCode() {
		return cdCode;
	}

	public void setCdCode(String cdCode) {
		this.cdCode = cdCode;
	}

	public String getCdName() {
		return cdName;
	}

	public void setCdName(String cdName) {
		this.cdName = cdName;
	}

	public String getVal1() {
		return val1;
	}

	public void setVal1(String val1) {
		this.val1 = val1;
	}

	public String getVal2() {
		return val2;
	}

	public void setVal2(String val2) {
		this.val2 = val2;
	}

	public String getVal3() {
		return val3;
	}

	public void setVal3(String val3) {
		this.val3 = val3;
	}

	public String getVal4() {
		return val4;
	}

	public void setVal4(String val4) {
		this.val4 = val4;
	}

	public String getVal5() {
		return val5;
	}

	public void setVal5(String val5) {
		this.val5 = val5;
	}

	public String getVal6() {
		return val6;
	}

	public void setVal6(String val6) {
		this.val6 = val6;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(cdClass).append(",").append(cdCode).append(",").append(cdName);
		return sb.toString();
	}
}

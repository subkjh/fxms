package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class ValueAddDto {

	@FxAttr(description = "관리대상번호", example = "123456")
	private long moNo;

	@FxAttr(description = "성능ID", example = "MoStatus")
	private String psId;

	@FxAttr(description = "수집값", example = "123")
	private Number value;

	public long getMoNo() {
		return moNo;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public String getPsId() {
		return psId;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

}

package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class AlCfgSelectDto {

	@FxAttr(description = "MO클래스")
	private String moClass;

	public String getMoClass() {
		return moClass;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}
}

package fxms.bas.impl.vo;

import fxms.bas.fxo.FxAttr;

public class AlarmCodeVo {

	@FxAttr(description = "경보코드번호")
	private int alcdNo;

	@FxAttr(description = "경보코드명")
	private String alcdName;

	@FxAttr(description = "경보코드표시명")
	private String alcdDispName;

	@FxAttr(description = "MO클래스")
	private String moClass;

}

package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class CdCodeAddDto extends CdCodeDto {

	@FxAttr(description = "코드명")
	private String cdName;

	@FxAttr(description = "설명")
	private String cdDesc;

	@FxAttr(description = "값1", required = false)
	private String val1;

	@FxAttr(description = "값2", required = false)
	private String val2;

	@FxAttr(description = "값3", required = false)
	private String val3;

	@FxAttr(description = "값4", required = false)
	private String val4;

	@FxAttr(description = "값5", required = false)
	private String val5;

	@FxAttr(description = "값6", required = false)
	private String val6;
}

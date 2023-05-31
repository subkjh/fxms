package fxms.bas.impl.handler.dto.mo;

import fxms.bas.fxo.FxAttr;

public class MoSetupDto extends MoDto {

	@FxAttr(description = "명령어", example = "reset")
	private String method;

	public String getMethod() {
		return method;
	}

}

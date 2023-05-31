package fxms.bas.impl.handler.dto.mo;

import fxms.bas.fxo.FxAttr;

public class MoTestDto extends MoDto {

	@FxAttr(description = "실시간처리여부", example = "true")
	private Boolean realtime;

	public Boolean getRealtime() {
		return realtime;
	}

}

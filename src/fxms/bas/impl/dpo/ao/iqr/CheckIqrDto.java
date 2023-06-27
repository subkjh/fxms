package fxms.bas.impl.dpo.ao.iqr;

import fxms.bas.fxo.FxAttr;
import fxms.bas.vo.PsKind;

public class CheckIqrDto {

	@FxAttr(description = "수집일시", example = "20200101000000")
	private Long psDtm;

	@FxAttr(description = "수집항목", example = "MoStatus")
	private String psId;

	@FxAttr(description = "데이터종류", example = "15M")
	private String psKindName = PsKind.PSKIND_15M;

	public String getDate() {
		return String.valueOf(psDtm).substring(0, 8);
	}

	public Long getPsDtm() {
		return psDtm;
	}

	public void setPsDtm(Long psDtm) {
		this.psDtm = psDtm;
	}

	public String getPsId() {
		return psId;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public String getPsKindName() {
		return psKindName;
	}

	public void setPsKindName(String psKindName) {
		this.psKindName = psKindName;
	}

}

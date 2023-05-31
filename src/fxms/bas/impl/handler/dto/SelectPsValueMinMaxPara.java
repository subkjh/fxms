package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;
import subkjh.bas.co.utils.DateUtil;

public class SelectPsValueMinMaxPara {

	@FxAttr(description = "관리대상번호", example = "123456")
	private long moNo;

	@FxAttr(description = "성능ID", example = "MoStatus")
	private String psId;

	@FxAttr(description = "성능종류명", required = false, example = "RAW")
	private String psKindName;

	@FxAttr(description = "수집일자", required = false, example = "20230101")
	private String psDate;

	@FxAttr(description = "성능종류명. psKindName으로 대신함", required = false, example = "RAW")
	private String psDataName = "RAW";

	public SelectPsValueMinMaxPara() {
		this.psDate = String.valueOf(DateUtil.getYmd());
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsId() {
		return psId;
	}

	public String getPsDate() {
		return psDate;
	}

	public String getPsKindName() {
		return psKindName;
	}

}

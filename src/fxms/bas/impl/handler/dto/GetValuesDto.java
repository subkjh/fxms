package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;
import subkjh.bas.co.utils.DateUtil;

public class GetValuesDto {

	@FxAttr(description = "관리대상번호", example = "123456")
	private long moNo;

	@FxAttr(description = "성능ID", example = "MoStatus")
	private String psId;

	@FxAttr(description = "성능종류명", required = false, example = "RAW")
	private String psKindName;

	@FxAttr(description = "조회시작성능데이터", required = false, example = "20230101100000")
	private long startDate;

	@FxAttr(description = "조회종료성능데이터", required = false, example = "20230101235959")
	private long endDate;

	public GetValuesDto() {
		this.startDate = DateUtil.getDtm(System.currentTimeMillis() - 86400000L);
		this.endDate = DateUtil.getDtm();
		this.psKindName = "RAW";
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsId() {
		return psId;
	}

	public String getPsKindName() {
		return psKindName;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndDate() {
		return endDate;
	}

}

package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;
import subkjh.bas.co.utils.DateUtil;

public class GetOperatingRateDto {

	@FxAttr(text = "관리대상번호", example = "123456", required = false)
	private long moNo;

	@FxAttr(text="주기명", description = "FX_PS_STAT_KIND 값 중에 하나이다.", example = "1D")
	private String psKindName;

	@FxAttr(text = "조회시작성능데이터", required = false, example = "20230101100000", description = "조회시작시간으로 년월일시분초 형식으로 입력한다. 생략하면 오늘 00:00:00값으로 설정된다.")
	private long startDate;

	@FxAttr(text = "조회종료성능데이터", required = false, example = "20230101235959", description = "조회시작시간으로 년월일시분초 형식으로 입력한다. 생략하면 오늘 23:59:59값으로 설정된다.")
	private long endDate;

	@FxAttr(text = "MO인스턴스", required = false)
	private String moInstance;

	public GetOperatingRateDto() {
		this.startDate = Long.parseLong(DateUtil.getYmdStr() + "000000");
		this.endDate = Long.parseLong(DateUtil.getYmdStr() + "235959");		
		this.psKindName = "1D";
	}

	public long getMoNo() {
		return moNo;
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

	public String getMoInstance() {
		return moInstance;
	}

}

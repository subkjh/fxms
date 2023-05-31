package fxms.rule.action.ps;

import java.util.List;
import java.util.Map;

import fxms.bas.api.ValueApi;
import fxms.bas.fxo.FxAttr;
import fxms.bas.vo.PsValueSeries;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import subkjh.bas.co.utils.DateUtil;

/**
 * 
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "수집데이터 조회", descr = "수집된 데이터를 조회한다.")
public class GetPsValueAction extends FxRuleActionImpl {

	@FxAttr(text = "관리대상", description = "관리대상의 번호를 입력한다.")
	private long moNo;
	@FxAttr(text = "수집항목", description = "조회할 항목을 입력한다.")
	private String psId;
	@FxAttr(text = "데이터 통계 종류", description = "데이터 통계 종류를 입력한다.", required = false)
	private String psDataKind;
	@FxAttr(text = "데이터범위시작", description = "데이터 범위 시작 일시를 입력한다.", required = false)
	private String startDate;
	@FxAttr(text = "데이터범위끝", description = "데이터 범위 종류 일시를 입력한다.", required = false)
	private String endDate;
	@FxAttr(text = "변수명", description = "값이 기록된 변수명을 입력한다.")
	private String var;

	public GetPsValueAction(Map<String, Object> map) throws Exception {
		super(map);

		if (psDataKind == null) {
			psDataKind = "RAW";
		}
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		// 일시 확인
		long hstimeStart = (startDate != null ? DateUtil.getHstime(startDate)
				: DateUtil.getDtm(System.currentTimeMillis() - 3600000L));
		long hstimeEnd = (endDate != null ? DateUtil.getHstime(endDate) : DateUtil.getDtm());

		// 가져오기
		List<PsValueSeries> list = ValueApi.getApi().getValues(moNo, psId, psDataKind, hstimeStart, hstimeEnd);

		// payload에 넣긱
		fact.addPayload(var, list);
	}
}

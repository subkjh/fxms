package fxms.rule.action.alarm;

import java.util.List;
import java.util.Map;

import fxms.bas.api.EventApi;
import fxms.bas.impl.dpo.vo.ValuePrevMap;
import fxms.bas.vo.PsVo;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * @author subkjh
 * @since 2023.02
 * 
 */
@FxRuleActionInfo(name = "수집내역 알람 확인", descr = "수집한 데이터를 알람조건에 설정된 내용으로 비교 요청한다.")
public class CheckAlarmPsValueAction extends FxRuleActionImpl {

	public CheckAlarmPsValueAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact inObj) throws Exception {

		// 수집 데이터
		List<PsVo> valueList = inObj.getPsValues();
		ValuePrevMap prevMap = ValuePrevMap.getInstance(this.getClass().getName());

		if (valueList != null) {
			long mstime = System.currentTimeMillis();
			for (PsVo e : valueList) {

				// 현재값 가져오기
				Number prevValue = prevMap.getValue(e);
				prevMap.setValue(e);

				// 현재값과 새로 수집한 데이터를 비교하여 이벤트를 발생한다.
				EventApi.getApi().checkValue(e.getMo() //
						, e.getPsItem() //
						, prevValue //
						, e.getValue()//
						, mstime //
						, null);

			}
		}
	}
}

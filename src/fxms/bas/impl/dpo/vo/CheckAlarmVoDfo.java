package fxms.bas.impl.dpo.vo;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.EventApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;

/**
 * 수집 데이터 알람 확인
 * 
 * @author subkjh
 *
 */
public class CheckAlarmVoDfo implements FxDfo<PsVoList, Boolean> {

	@Override
	public Boolean call(FxFact fact, PsVoList voList) throws Exception {
		return checkAlarm(voList);
	}

	/**
	 * 
	 * @param voList
	 * @throws Exception
	 */
	public boolean checkAlarm(PsVoList voList) {

		ValuePrevMap prevMap = ValuePrevMap.getInstance(this.getClass().getName());
		AlarmApi api = AlarmApi.getApi();

		for (PsVo e : voList) {

			Number prevValue = prevMap.getValue(e);
			prevMap.setValue(e);

			// 수집된 데이터가 성능항목에 설정된 범위인지 확인한다.
			if (e.getPsItem().isAcceptable(e.getValue()) == false) {
				try {
					api.fireAlarm(e.getMo(), e.getPsItem().getPsId(), ALARM_CODE.value_not_acceptable.getAlcdNo(), null,
							String.valueOf(e.getValue()), null);
				} catch (Exception ex) {
					Logger.logger.error(ex);
				}
				continue;
			}

			EventApi.getApi().checkValue(e.getMo(), e.getPsItem(), prevValue, e.getValue(), voList.getMstime(), null);

		}

		return true;
	}

}
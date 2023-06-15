package fxms.bas.impl.dpo.vo;

import java.util.Map;

import fxms.bas.api.ValueApi;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsItem.PS_VAL_TYPE;
import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsVoRaw;

/**
 * 관리대상의 관제항목에 대한 테스트용 데이터를 생성한다.
 * 
 * @author subkjh
 *
 */
public class MakeTestPsVoDfo implements FxDfo<Map<String, Object>, PsVoRaw> {

	@Override
	public PsVoRaw call(FxFact fact, Map<String, Object> data) throws Exception {

		Mo mo = (Mo) data.get("mo");
		PsItem psItem = (PsItem) data.get("psItem");
		Object moInstance = data.get("moInstance");

		return makeTestValue(mo, moInstance == null ? null : moInstance.toString(), psItem);
	}

	/**
	 * 
	 * @param mo
	 * @param psItem
	 * @return
	 */
	public PsVoRaw makeTestValue(Mo mo, String moInstance, PsItem psItem) {
		int value;
		int min, max;

		// offline으로 테스트한다.
		if (Math.random() <= 0.05d) {
			return null;
		}

		PsValueComp vo = null;
		try {
			vo = ValueApi.getApi().getCurValue(mo.getMoNo(), moInstance, psItem.getPsId());
		} catch (Exception e) {
		}

		// 누적값이면 이전보다 크게 한다.
		if (psItem.getPsValType() == PS_VAL_TYPE.AV) {
			float value2 = 100;
			if (vo != null && vo.getCurValue() != null) {
				value2 = vo.getCurValue().floatValue();
				value2 += (Math.random() * 20);
			} else {
				value2 = 100;
			}
			return new PsVoRaw(mo.getMoNo(), psItem.getPsId(), value2, moInstance);
		}

		min = psItem.getMinVal() == null ? 0 : psItem.getMinVal().intValue();
		max = psItem.getMaxVal() == null ? 100 : psItem.getMaxVal().intValue();

		if (vo != null) {

			value = vo.getCurValue().intValue();

			if (Math.random() >= 0.5d) {
				value += (Math.random() * -100);
			} else {
				value += (Math.random() * 100);
			}

			if (value < min) {
				value = min;
			} else if (value > max) {
				value = max;
			}

		} else {
			value = (int) (Math.random() * max);
			if (value < min) {
				value = min;
			}
		}

		return new PsVoRaw(mo.getMoNo(), psItem.getPsId(), value, moInstance);

	}
}
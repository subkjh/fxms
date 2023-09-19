package fxms.bas.impl.dpo.vo;

import java.util.ArrayList;
import java.util.Map;

import fxms.bas.api.PsApi;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;

/**
 * 수집한 데이터를 기록하는 프로세스
 * 
 * @author subkjh
 *
 */
public class ValueAddDpo implements FxDpo<PsVoList, Integer> {

	@Override
	public Integer run(FxFact fact, PsVoList voList) throws Exception {
		return add(voList);
	}

	/**
	 * 
	 * @param voList
	 * @return
	 * @throws Exception
	 */
	public int add(PsVoList voList) throws Exception {

		Map<String, Integer> ret;

		try {
			// 2. 데이터 기록
			ret = new ValueWriteDfo().write(voList);

			// 3. 통계 요청
			new ReqMakeStatValueDfo().requestMakeStat(voList.getMstime(), new ArrayList<String>(ret.keySet()),
					PsApi.getApi().getPsKindRaw());

		} catch (Exception e) {
			Logger.logger.error(e);
			return -1;
		}

		// 1분 이내 수집된 데이터인 경우에 한하여 업데이트한다.
		if (voList.getMstime() < System.currentTimeMillis() - PsApi.UPDATE_VALID_TIME) {

			Logger.logger.debug("{}, psDate={}",
					Lang.get("Data collected a long time ago does not update current values."), voList.getHstime());

		} else {

			// 5. 수정데이터 기록
			new UpdateDiffValueDfo().update(voList);

			// 7. 수집 데이터 공유
			new NotifyVoDfo().notify(voList);

		}

		int count = 0;
		for (Integer v : ret.values()) {
			if (v != null) {
				count += v.intValue();
			}
		}
		return count;
	}

}

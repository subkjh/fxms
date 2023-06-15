package fxms.bas.impl.dpo.vo;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.exp.PsItemNotFoundException;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsVoList;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.log.Logger;

/**
 * 수집한 데이터 중에서 유효한 내역만 추출한다.<br>
 * 수집일시가 없거나 성능항목이 없으면 무의미한 데이터이므로 제거한다.
 * 
 * @author subkjh
 *
 */
public class ValueExtractValidDfo implements FxDfo<PsVoRawList, PsVoList> {

	@Override
	public PsVoList call(FxFact fact, PsVoRawList data) throws Exception {
		return extractValidData(data);
	}

	/**
	 * 
	 * @param voList
	 * @return
	 */
	public PsVoList extractValidData(PsVoRawList voList) {

		if (voList == null || voList.size() == 0)
			return null;

		if (voList.getMstime() == 0) {
			Logger.logger.fail("Collection date unknown {}", voList.getOwner());
			return null;
		}

		PsVoList ret = new PsVoList(voList);
		int notValueCount = 0;
		int notPsItemCount = 0;
		int notMoCount = 0;
		PsItem psItem;
		Mo mo;

		for (PsVoRaw raw : voList) {

			// 수집 데이터가 없으면 버림
			if (raw == null) {
				notValueCount++;
				continue;
			}

			try {
				mo = MoApi.getApi().getMo(raw.getMoNo());
			} catch (Exception e) {
				notMoCount++;
				continue;
			}

			// 수집 항목 ID가 없으면 버림.
			try {
				psItem = PsApi.getApi().getPsItem(raw.getPsId());
			} catch (PsItemNotFoundException ex) {
				try {
					AlarmApi.getApi().fireAlarm(null, raw.getPsId(), ALARM_CODE.fxms_not_found_psitem.getAlcdNo(), null,
							raw.getPsId(), null);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
				notPsItemCount++;
				continue;
			}

			ret.add(psItem.format(raw.getValue()), mo, psItem, raw.getMoInstance());

		}

		Logger.logger.debug("owner={}, date={}, size={}, ignore=v:{},ps:{},mo:{}", ret.getOwner(), ret.getHstime(),
				ret.size(), notValueCount, notPsItemCount, notMoCount);

		return ret;
	}
}
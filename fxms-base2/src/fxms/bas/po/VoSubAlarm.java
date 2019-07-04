package fxms.bas.po;

import fxms.bas.api.EventApi;
import fxms.bas.api.MoApi;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;

/**
 * 수집된 성능에 대한 임계 확인
 * 
 * @author subkjh
 * 
 */
public class VoSubAlarm extends VoSub {

	public VoSubAlarm() {
		super(VoSubAlarm.class.getSimpleName());
	}

	@Override
	protected void doWork(VoList voList) {

		if (voList == null)
			return;

		Logger.logger.trace("{}", voList);

		Mo mo;

		for (PsVo e : voList) {

			mo = MoApi.getApi().getMo(e.getMoNo());

			// MO가 없으면 경보를 처리하지 않습니다.
			if (mo != null) {

				EventApi.getApi().checkPsValue(mo //
						, e.getMoInstance()//
						, e.getPsCode() //
						, getPrevValue(e.getMoNo(), e.getMoInstance(), e.getPsCode()) //
						, e.getValue()//
						, voList.getMstime() //
						, null);
			}

			setPrevValue(e);

		}

	}

	@Override
	protected void onNoDatas(long index) {
	}

	@Override
	protected void doInit() {

	}
}

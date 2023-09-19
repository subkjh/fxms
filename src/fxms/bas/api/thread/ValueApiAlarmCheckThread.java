package fxms.bas.api.thread;

import fxms.bas.api.EventApi;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;

/**
 * 수집된 성능에 대한 임계 확인
 * 
 * @author subkjh
 * 
 */
public class ValueApiAlarmCheckThread extends ValueApiBasThread {

	public ValueApiAlarmCheckThread() {
		super(ValueApiAlarmCheckThread.class.getSimpleName());
	}

	@Override
	protected void doWork(PsVoList voList) {

		if (voList == null)
			return;

		Logger.logger.trace("alarm-check : {}", voList);

		for (PsVo e : voList) {

			EventApi.getApi().checkValue(e.getMo() //
					, e.getPsItem() //
					, getPrevValue(e.getMo().getMoNo(), e.getPsItem().getPsId()) //
					, e.getValue()//
					, voList.getMstime() //
					, null);

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

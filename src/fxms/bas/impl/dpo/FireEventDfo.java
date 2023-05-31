package fxms.bas.impl.dpo;

import fxms.bas.api.AlarmApi;
import fxms.bas.mo.Mo;
import fxms.bas.mo.Moable;
import fxms.bas.vo.Alarm;
import subkjh.bas.co.log.Logger;

public class FireEventDfo implements FxDfo<Mo, Alarm> {

	@Override
	public Alarm call(FxFact fact, Mo data) throws Exception {

		int alcdNo = fact.getInt("alcdNo");

		return fireEvent(data, alcdNo);
	}

	public Alarm fireEvent(Moable mo, int alcdNo) {

		try {
			return AlarmApi.getApi().fireAlarm(mo.getMoNo(), alcdNo, null);
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

}
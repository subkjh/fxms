package fxms.bas.impl.dpo.mo;

import java.util.Map;

import fxms.bas.co.ALARM_CODE;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.FireEventDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.log.Logger;

public class AddMoDpo implements FxDpo<Map<String, Object>, Mo> {

	@Override
	public Mo run(FxFact fact, Map<String, Object> data) throws Exception {

		int userNo = fact.getUserNo();
		String moClass = fact.getString("moClass");
		boolean broadcast = fact.isTrue("broadcast");

		try {
			return addMo(userNo, moClass, data, "", broadcast);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

	public Mo addMo(int userNo, String moClass, Map<String, Object> datas, String reason, boolean broadcast)
			throws Exception {

		try {

			long moNo = new AddMoDfo().addMo(userNo, moClass, datas, reason);
			Mo mo = new GetMoDfo().selectMo(moNo);

			if (broadcast) {
				new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Mo));
				new FireEventDfo().fireEvent(mo, ALARM_CODE.mo_added.getAlcdNo());
			}

			return mo;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

}

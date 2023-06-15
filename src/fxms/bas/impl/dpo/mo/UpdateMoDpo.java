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

public class UpdateMoDpo implements FxDpo<Map<String, Object>, Mo> {

	@Override
	public Mo run(FxFact fact, Map<String, Object> data) throws Exception {

		int userNo = fact.getUserNo();

		return updateMo(userNo, fact.getMoNo(), data, fact.isTrue("broadcast"));

	}

	public Mo updateMo(int userNo, long moNo, Map<String, Object> para, boolean broadcast) throws Exception {
		try {

			new UpdateMoDfo().updateMo(userNo, moNo, para);

			Mo updatedMo = new GetMoDfo().selectMo(moNo);

			if (broadcast) {
				new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Mo));
				new FireEventDfo().fireEvent(updatedMo, ALARM_CODE.mo_updated.getAlcdNo());
			}

			return updatedMo;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

}

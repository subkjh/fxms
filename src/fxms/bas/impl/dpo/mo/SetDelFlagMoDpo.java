package fxms.bas.impl.dpo.mo;

import fxms.bas.co.ALARM_CODE;
import fxms.bas.event.FxEvent.STATUS;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.FireEventDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;

public class SetDelFlagMoDpo implements FxDpo<Void, Mo> {

	@Override
	public Mo run(FxFact fact, Void data) throws Exception {

		try {

			long moNo = fact.getMoNo();
			boolean broadcast = fact.isTrue("broadcast");
			return deleteMo(fact.getUserNo(), moNo, "", broadcast);

		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
			throw e;
		}
	}

	public Mo deleteMo(int userNo, long moNo, String reason, boolean broadcast) throws MoNotFoundException, Exception {

		Mo mo = new GetMoDfo().selectMo(moNo);

		new SetDelFlagMoDfo().deleteMo(mo, 0);

		mo.setStatus(STATUS.deleted);

		if (broadcast) {
			new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Mo));
			new FireEventDfo().fireEvent(mo, ALARM_CODE.mo_deleted.getAlcdNo());
		}

		return mo;

	}

}

package fxms.bas.impl.dpo.vo;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.thread.PsValueNotifyThread;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;

/**
 * 수집 데이터를 실시간으로 공유한다.
 * 
 * @author subkjh
 *
 */
public class NotifyVoDfo implements FxDfo<PsVoList, Boolean> {

	@Override
	public Boolean call(FxFact fact, PsVoList voList) throws Exception {
		return notify(voList);
	}

	/**
	 * 
	 * @param voList
	 * @throws Exception
	 */
	public boolean notify(PsVoList voList) {

		try {

			PsValueNotifyThread.getVoNotifier().put(voList);
			return true;

		} catch (Exception e) {

			Logger.logger.error(e);
			return false;
		}

	}

}
package fxms.nms.fxactor.trap;

import subkjh.bas.co.log.Logger;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.nms.api.TrapApi;

/**
 * 유효기간이 지난 트랩 로그를 삭제한다.
 * 
 * @author subkjh(김종훈)
 *
 */
public class TrapLogDeleteThread extends CycleFxThread {

	/**
	 * 보관 기간이 지난 로그 삭제
	 */
	public TrapLogDeleteThread() {
		super("TrapLogDeleteThread", 12 * 60 * 60, true);
	}

	@Override
	protected void doCycle(long mstime) {
		try {
			TrapApi.getApi().deleteLogExpired();
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

}

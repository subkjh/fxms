package com.fxms.nms.fxactor;

import java.util.List;

import fxms.bas.mo.Mo;
import fxms.bas.poller.exception.PollingNoTargetException;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;

public interface NeValueActor {

	/**
	 * 
	 * @param pollMsdate
	 *            폴링일시
	 * @param mo
	 *            수집 대상
	 * @param psCodes
	 *            수집할 성능 항목
	 * @return
	 * @throws PollingTimeoutException
	 * @throws Exception
	 */
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes)
			throws PollingTimeoutException, PollingNoTargetException, Exception;

	
	public boolean match(Object obj);

}

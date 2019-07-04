package fxms.nms.fxactor;

import java.util.List;

import fxms.bas.mo.Mo;
import fxms.bas.po.PsVo;
import fxms.bas.poller.exp.PollingNoTargetException;
import fxms.bas.poller.exp.PollingTimeoutException;

/**
 * 장비로부터 값을 가져오는 아답터
 * 
 * @author subkjh(김종훈)
 *
 */
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
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException,
			PollingNoTargetException, Exception;

	/**
	 * 장비가 이 아답터에 해당되는지 여부
	 * 
	 * @param obj
	 * @return
	 */
	public boolean match(Object obj);

}

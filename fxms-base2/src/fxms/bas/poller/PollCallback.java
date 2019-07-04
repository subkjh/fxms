package fxms.bas.poller;

import java.util.List;

import fxms.bas.co.noti.FxEvent;
import fxms.bas.po.PsVo;

/**
 * 
 * @author SUBKJH-DEV
 *
 */
public interface PollCallback {

	/**
	 * 수집된 내용을 넘긴다.
	 * 
	 * @param value
	 */
	public void onValue(String owner, long pollMsdate, List<PsVo> valueList, FxEvent sign);
}

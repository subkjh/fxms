package fxms.bas.event;

import fxms.bas.fxo.FxActor;

/**
 * Signal Receiver
 * 
 * @author subkjh
 *
 */
public interface NotiReceiver extends FxActor {

	/**
	 * 이벤트를 받는다.
	 * 
	 * @param noti
	 * @throws Exception
	 */
	public void onEvent(FxEvent noti) throws Exception;

}

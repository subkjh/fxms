package fxms.bas.co.noti;

import fxms.bas.fxo.FxActor;

/**
 * Signal Receiver
 * 
 * @author subkjh
 *
 */
public interface NotiReceiver extends FxActor {

	public void onNotify(FxEvent noti) throws Exception;

}

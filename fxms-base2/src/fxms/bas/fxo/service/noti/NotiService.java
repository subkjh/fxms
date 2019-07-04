package fxms.bas.fxo.service.noti;

import java.rmi.RemoteException;
import java.util.List;

import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiFilter;
import fxms.bas.fxo.service.FxService;

public interface NotiService extends FxService {

	/**
	 * 
	 * @param peerName
	 * @param noti
	 * @return NotiService 상대에게 보내지 못한 내용을 보낸다.
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<FxEvent> onBroadcast(String peerName, FxEvent noti) throws RemoteException, Exception;

	/**
	 * 
	 * @param peerName
	 * @param service
	 * @param filter
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void register(String peerName, FxService service, NotiFilter filter) throws RemoteException, Exception;

}

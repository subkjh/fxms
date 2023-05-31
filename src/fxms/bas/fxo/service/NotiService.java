package fxms.bas.fxo.service;

import java.rmi.RemoteException;

import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiFilter;

public interface NotiService extends FxService {

	/**
	 * 
	 * @param peerName
	 * @param noti
	 * @return NotiService 상대에게 보내지 못한 내용을 보낸다.
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void broadcast(String peerName, FxEvent noti) throws RemoteException, Exception;

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

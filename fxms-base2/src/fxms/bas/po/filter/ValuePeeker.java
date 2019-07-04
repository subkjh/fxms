package fxms.bas.po.filter;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fxms.bas.po.PsVo;

/**
 * Receive value collectd
 * 
 * @author subkjh
 *
 */
public interface ValuePeeker extends Remote {

	public void onValue(long mstime, PsVo vo) throws RemoteException, Exception;

}
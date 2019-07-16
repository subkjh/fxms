package fxms.bas.fxo.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiFilter;

/**
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public interface FxService extends Remote {

	/**
	 * 노트 받는 메소스
	 * 
	 * @param noti
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void onNotify(FxEvent noti) throws RemoteException, Exception;

	public String getFxServiceId() throws RemoteException, Exception;

	/**
	 * 노티 받는 조건을 제공한다.
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public NotiFilter getNotiFilter() throws RemoteException, Exception;

	public String getStatus(String level) throws RemoteException, Exception;

	public String getStatusThread(String threadName) throws RemoteException, Exception;

	public void runCron(String name) throws RemoteException, Exception;

	public void runClass(String runnableClassName) throws RemoteException, Exception;

	/**
	 * 
	 * @param classNameOfNoti
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void sendNoti(String classNameOfNoti) throws RemoteException, Exception;

	public String setLogLevel(String threadName, String level) throws RemoteException, Exception;

	public void stop(String reason) throws RemoteException, Exception;
}

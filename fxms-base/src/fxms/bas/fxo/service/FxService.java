package fxms.bas.fxo.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fxms.bas.noti.FxEvent;
import fxms.bas.noti.NotiFilter;

public interface FxService extends Remote {
	
	public void onNotify(FxEvent noti) throws RemoteException, Exception;

	public String getFxServiceId() throws RemoteException, Exception;

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

package fxms.bas.fxo.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiFilter;

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
	public void sendEvent(String classNameOfNoti) throws RemoteException, Exception;

	public String setLogLevel(String threadName, String level) throws RemoteException, Exception;

	/**
	 * 확인용
	 * 
	 * @param who 누가확인하는지
	 * @return 현재 시간
	 * @throws RemoteException
	 * @throws Exception
	 */
	public long ping(String who) throws RemoteException, Exception;

	public void stop(String reason) throws RemoteException, Exception;
}

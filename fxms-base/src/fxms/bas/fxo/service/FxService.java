package fxms.bas.fxo.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fxms.bas.noti.FxEvent;
import fxms.bas.noti.NotiFilter;

public interface FxService extends Remote {

	/**
	 * 
	 * @return 서비스의 ID
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String getFxServiceId() throws RemoteException, Exception;

	/**
	 * 이벤트 필터링 조건
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public NotiFilter getNotiFilter() throws RemoteException, Exception;

	/**
	 * 상태 조회
	 * 
	 * @param level
	 *            상태 조회 등급
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String getStatus(String level) throws RemoteException, Exception;

	/**
	 * 스레드의 상태 조회
	 * 
	 * @param threadName
	 *            스레드명
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String getStatusThread(String threadName) throws RemoteException, Exception;

	/**
	 * 받은 이벤트 처리 메소드
	 * 
	 * @param noti
	 *            이벤트
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void onNotify(FxEvent noti) throws RemoteException, Exception;

	/**
	 * Runnable 클래스를 실행한다.
	 * 
	 * @param runnableClassName
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void runClass(String runnableClassName) throws RemoteException, Exception;

	/**
	 * 크론 작업을 실행한다.
	 * 
	 * @param name
	 *            크론명
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void runCron(String name) throws RemoteException, Exception;

	/**
	 * FxEvent를 보낸다.
	 * 
	 * @param classNameOfNoti
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void sendNoti(String classNameOfNoti) throws RemoteException, Exception;

	/**
	 * 스레드의 로그 레벨을 설정한다.
	 * 
	 * @param threadName
	 *            스레드명. null이거나 "all"이면 서비스 전체의 로그 레벨을 설정한다.
	 * @param level
	 *            로그 레벨
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String setLogLevel(String threadName, String level) throws RemoteException, Exception;

	/**
	 * 서비스를 종료한다.
	 * 
	 * @param reason
	 *            종료 설명
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void stop(String reason) throws RemoteException, Exception;
}

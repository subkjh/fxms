package fxms.bas.fxo.service.app;

import java.rmi.RemoteException;

import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.fxo.service.FxService;
import fxms.bas.po.VoList;
import fxms.module.restapi.vo.SessionVo;

public interface AppService extends FxService {

	/**
	 * 
	 * @param userId
	 * @param password
	 * @param ipAddr
	 * @return 세션ID
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SessionVo login(String userId, String password, String ipAddr) throws RemoteException, Exception;

	/**
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void logout(String sessionId) throws RemoteException, Exception;

	/**
	 * 
	 * @param event
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Alarm onEvent(AlarmEvent event) throws RemoteException, Exception;

	/**
	 * 
	 * @param voList
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void addVo(VoList voList) throws RemoteException, Exception;

	/**
	 * 로그인된 세션 정보를 조회한다.
	 * 
	 * @param hostname  접속 호스트명
	 * @param sessionId 세션ID
	 * @param seqno     일련번호
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SessionVo getUser(String hostname, String sessionId, long seqno) throws RemoteException, Exception;

}

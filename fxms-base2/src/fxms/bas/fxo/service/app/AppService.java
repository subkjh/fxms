package fxms.bas.fxo.service.app;

import java.rmi.RemoteException;

import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.co.def.reflect.FxMethod;
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

	public void logout(String sessionId) throws RemoteException, Exception;

	public Alarm onEvent(AlarmEvent event) throws RemoteException, Exception;

	public void reqMakeStat(String psTable, String psType, long psDate) throws RemoteException, Exception;

	public void addVo(VoList voList) throws RemoteException, Exception;

	public Object invokeFx(FxMethod fxMethod) throws RemoteException, Exception;

	public SessionVo getUser(String sessionId) throws RemoteException, Exception;

}

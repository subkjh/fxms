package fxms.bas.fxo.service.app;

import java.rmi.RemoteException;

import fxms.bas.alarm.AlarmEvent;
import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.define.reflect.FxMethod;
import fxms.bas.fxo.service.FxService;
import fxms.bas.pso.VoList;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.user.UserProc;

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

	public <T> T process(String sessionId, Class<? extends UserProc<T>> classOfP, Object... parameters)
			throws RemoteException, Exception;

//	public Object process(String sessionId, String className, String jsonParameter) throws RemoteException, Exception;

	public SessionVo getUser(String sessionId) throws RemoteException, Exception;

}

package fxms.bas.fxo.service.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fxms.bas.alarm.AlarmCleaner;
import fxms.bas.alarm.AlarmEvent;
import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.dao.FxConfDao;
import fxms.bas.define.reflect.FxMethod;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.app.mgr.RemakeStatReq;
import fxms.bas.fxo.service.app.mgr.StatMakeFxThread;
import fxms.bas.noti.FxEvent;
import fxms.bas.noti.NotiFilter;
import fxms.bas.pso.ValueApi;
import fxms.bas.pso.VoList;
import fxms.bas.signal.AliveSignal;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.log.Logger;
import subkjh.bas.user.UserAlog;
import subkjh.bas.user.UserProc;
import subkjh.bas.user.dao.UserDao;
import subkjh.bas.user.dbo.LogoutDbo;
import subkjh.bas.user.exception.ConstructorNotFoundException;
import subkjh.bas.user.exception.ExpireLoginException;
import subkjh.bas.user.exception.NotLoginException;
import subkjh.bas.user.process.UserProcMgr;

public class AppServiceImpl extends FxServiceImpl implements AppService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2429276239016556946L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(AppService.class.getSimpleName(), AppServiceImpl.class, args);
	}

	private StatMakeFxThread statMaker;

	private long noActionAllowTime = 2 * 60 * 1000L;

	private Map<String, SessionVo> sessionMap = new HashMap<String, SessionVo>();

	private AlarmCleaner alarmCleaner;

	public AppServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public void addVo(VoList voList) throws RemoteException, Exception {

		logger.info("value-size={}", voList.size());

		ValueApi.getApi().addValue(voList, false);

	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		return new NotiFilter();
	}

	@Override
	public SessionVo getUser(String sessionId) throws RemoteException, Exception {
		return sessionMap.get(sessionId);
	}

	@Override
	public Object invokeFx(FxMethod fxMethod) throws RemoteException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionVo login(String userId, String password, String ipaddr) throws RemoteException, Exception {

		logger.info("{} {} {}", userId, password, ipaddr);
		SessionVo user = null;

		try {
			user = new UserDao().login(userId, password, ipaddr);
			logger.info("{} {} {} {}", userId, password, ipaddr, user.getSessionId());
			user.setLastOpTime(System.currentTimeMillis());
			sessionMap.put(user.getSessionId(), user);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

		return user;
	}

	@Override
	public void logout(String sessionId) throws RemoteException, Exception {

		LogoutDbo dbo = new LogoutDbo();
		dbo.setLogoutDate(FxApi.getDate());
		dbo.setLogStatusCode(UserAlog.TYPE_LOGOUT);
		dbo.setSessionId(sessionId);

		try {
			FxConfDao.getDao().update(dbo, null);
		} catch (Exception e) {
			logger.error(e);
		}

		sessionMap.remove(sessionId);

	}

	@Override
	public Alarm onEvent(AlarmEvent event) throws RemoteException, Exception {

		Thread.currentThread().setName(event.getAlarmKey() + "-" + event.getAlarmLevel());

		try {
			Alarm alarm = AlarmApi.getApi().analyze(event);
			logger.info("{} --> {}", event, alarm);
			return alarm;
		} catch (Exception e) {
			logger.fail("{}", event);
			Logger.logger.error(e);
			throw e;
		}

	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		super.onNotify(noti);

		if (noti instanceof AliveSignal) {
			AliveSignal signal = (AliveSignal) noti;
			ServiceApi.getApi().updateServiceStatus(signal.getMsIpaddr(), signal.getServiceName(),
					signal.getStartTime(), "ON");
		}

	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> T process(String sessionId, Class<? extends UserProc<T>> classOfP, Object... parameters)
			throws RemoteException, Exception {

		Thread.currentThread().setName(getClientHost() + "-" + classOfP.getSimpleName());

		logger.debug("{} {} {} {}", sessionId, classOfP.getSimpleName(), Logger.getParaString(parameters),
				Arrays.toString(parameters));

		checkSession(sessionId);

		try {
			Constructor<?> c = UserProcMgr.getConstructor(classOfP, parameters);
			if (c == null) {
				c = UserProcMgr.getConstructor(classOfP, parameters.length);
				if (c == null) {
					throw new ConstructorNotFoundException(classOfP.getName() + "(" + getTypeMsg(parameters) + ")");
				}
			}

			Object para[] = UserProcMgr.makeParameterObject(c.getParameterTypes(), parameters);

			logger.debug(Arrays.toString(c.getParameterTypes()), Arrays.toString(para));

			UserProc<T> proc = (UserProc<T>) c.newInstance(para);
			return proc.process(this, sessionId);
		} catch (ConstructorNotFoundException e) {
			logger.fail(e.getClass().getSimpleName() + " : " + e.getMessage());
			throw new Exception(e.getClass().getSimpleName() + " : " + e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error(e);
			throw new Exception(e.getTargetException());
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public Object process(String sessionId, String className, String jsonParameter) throws RemoteException, Exception {
//
//		Class<?> classOfP = null;
//		try {
//			classOfP = Class.forName(className);
//
//		} catch (ClassNotFoundException e) {
//			logger.error(e);
//			throw new Exception("Class(" + className + ") NOT FOUND");
//		}
//
//		Thread.currentThread().setName(getClientHost() + "-" + classOfP.getSimpleName());
//
//		Gson son = new Gson();
//		Map<String, Object> paraMap = son.fromJson(jsonParameter, HashMap.class);
//
//		logger.debug("{} {} {}", sessionId, classOfP.getSimpleName(), jsonParameter);
//
//		checkSession(sessionId);
//
//		try {
//
//			Constructor<?> c = UserProcMgr.getConstructor(classOfP, paraMap.size());
//			if (c == null) {
//				throw new ConstructorNotFoundException(classOfP.getName() + "(" + paraMap + ")");
//			}
//
//			Object paraArray[] = new Object[paraMap.size()];
//			for (int i = 0; i < paraMap.size(); i++) {
//				paraArray[i] = paraMap.get("p" + i);
//			}
//
//			Object para[] = UserProcMgr.makeParameterObject(c.getParameterTypes(), paraArray);
//
//			logger.debug(Arrays.toString(c.getParameterTypes()), Arrays.toString(para));
//
//			UserProc<T> proc = (UserProc<T>) c.newInstance(para);
//			return proc.process(this, sessionId);
//		} catch (ConstructorNotFoundException e) {
//			logger.fail(e.getClass().getSimpleName() + " : " + e.getMessage());
//			throw new Exception(e.getClass().getSimpleName() + " : " + e.getMessage());
//		} catch (InvocationTargetException e) {
//			logger.error(e);
//			throw new Exception(e.getTargetException());
//		} catch (Exception e) {
//			logger.error(e);
//			throw e;
//		}
//	}

	@Override
	public void reqMakeStat(String psTable, String psType, long psDate) throws RemoteException, Exception {

		RemakeStatReq req = new RemakeStatReq(psTable, psType, psDate);

		int ret = statMaker.add(req);

		if (ret != 0) {
			logger.info("{} {} {} {}", psTable, psType, psDate, (ret == 1 ? "ok" : "error"));
		} else {
			logger.trace("{} {} {} exist", psTable, psType, psDate);
		}
	}

	private void checkSession(String sessionId) throws NotLoginException, ExpireLoginException, Exception {

		Exception exp = null;

		SessionVo user = sessionMap.get(sessionId);
		if (user == null) {
			exp = new NotLoginException(sessionId);
		} else if (System.currentTimeMillis() - user.getLastOpTime() > noActionAllowTime) {
			sessionMap.remove(sessionId);
			exp = new ExpireLoginException("session-id=" + sessionId);
		} else {
			user.setLastOpTime(System.currentTimeMillis());
		}

		logger.info("{} {}", sessionId, exp);

		if (exp != null) {
			throw exp;
		}
	}

	private String getTypeMsg(Object para[]) {

		StringBuffer sb = new StringBuffer();
		for (Object o : para) {
			if (sb.length() > 0) {
				sb.append(", ");
			}

			sb.append(o == null ? "<null>" : o.getClass().getSimpleName());
		}

		return sb.toString();
	}

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();

		try {
			ServiceApi.getApi().doSetServiceStatus("INIT");

			ServiceApi.getApi().updateServiceStatus(FxCfg.getCfg().getIpAddress(), FxCfg.getFxServiceName(),
					FxCfg.getCfg().getLong(FxCfg.START_TIME, 0), "ON");

		} catch (Exception e) {
			logger.error(e);
		}

		statMaker = new StatMakeFxThread();
		statMaker.start();

		alarmCleaner = new AlarmCleaner();
		alarmCleaner.start();
	}
}

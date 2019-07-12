package fxms.bas.fxo.service.app;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.CoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.def.reflect.FxMethod;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiFilter;
import fxms.bas.co.signal.AliveSignal;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.po.ValueApi;
import fxms.bas.po.VoList;
import fxms.module.restapi.vo.SessionVo;

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

	private Map<String, SessionVo> sessionMap = new HashMap<String, SessionVo>();

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

		SessionVo user = CoApi.getApi().login(userId, password, ipaddr);

		sessionMap.put(user.getSessionId(), user);

		return user;
	}

	@Override
	public void logout(String sessionId) throws RemoteException, Exception {

		CoApi.getApi().logout(sessionId);

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
			ServiceApi.getApi().updateServiceStatus(signal.getMsIpaddr(), signal.getServiceName(), signal.getStartTime(), "ON");
		}

	}

	@Override
	public void reqMakeStat(String psTable, String psType, long psDate) throws RemoteException, Exception {
		logger.trace("{} {} {}", psTable, psType, psDate);
		PsApi.getApi().makeStatReq(psTable, psType, psDate);
	}

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();

		try {
			ServiceApi.getApi().setAllServiceStatus("INIT");

			ServiceApi.getApi().updateServiceStatus(FxCfg.getCfg().getIpAddress(), FxCfg.getFxServiceName(),
					FxCfg.getCfg().getLong(FxCfg.START_TIME, 0), "ON");

		} catch (Exception e) {
			logger.error(e);
		}

	}
}

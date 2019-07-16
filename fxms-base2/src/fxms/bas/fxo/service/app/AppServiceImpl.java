package fxms.bas.fxo.service.app;

import java.rmi.RemoteException;

import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.CoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiFilter;
import fxms.bas.co.signal.AliveSignal;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.po.ValueApi;
import fxms.bas.po.VoList;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.co.log.Logger;

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
	public SessionVo getUser(String hostname, String sessionId, long seqno) throws RemoteException, Exception {
		return CoApi.getApi().getSessionMgr().get(hostname, sessionId, seqno);
	}

	@Override
	public SessionVo login(String userId, String password, String ipaddr) throws RemoteException, Exception {

		logger.info("{} {} {}", userId, password, ipaddr);

		SessionVo user = CoApi.getApi().login(userId, password, ipaddr);

		return user;
	}

	@Override
	public void logout(String sessionId) throws RemoteException, Exception {
		CoApi.getApi().logout(sessionId);
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

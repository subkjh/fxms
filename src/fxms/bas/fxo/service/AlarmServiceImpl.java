package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.event.NotiFilter;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmClearEvent;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.MoStateEvt;
import subkjh.bas.co.log.Logger;

/**
 * 알람서비스
 * 
 * @author subkjh
 *
 */
public class AlarmServiceImpl extends FxServiceImpl implements AlarmService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2188870307144066161L;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(AlarmService.class.getSimpleName(), AlarmServiceImpl.class, args);
	}

	public AlarmServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public Alarm clearAlarm(AlarmClearEvent event) throws RemoteException, Exception {

		Alarm clearAlarm = null;
		try {
			clearAlarm = AlarmApi.getApi().clearAlarm(event);
			return clearAlarm;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info("{} --> {}", event.getAlarmNo(), clearAlarm == null ? "(error or notfound)" : clearAlarm);
		}

	}

	@Override
	public Alarm fireAlarm(AlarmOccurEvent event) throws RemoteException, Exception {

		Alarm alarm = null;
		try {
			alarm = AlarmApi.getApi().fireAlarm(event);
			return alarm;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info("alarmKey={}, {} --> {}", event.getAlarmKey(),
					event.getAlarm() == null ? "" : event.getAlarm().getAlarmKey(), alarm == null ? "(error)" : alarm);
		}

	}

	@Override
	public Alarm fireAlarm(long moNo, int alcdNo, Object moInstance) throws RemoteException, Exception {
		Alarm alarm = null;
		try {
			alarm = AlarmApi.getApi().fireAlarm(moNo, alcdNo, moInstance);
			return alarm;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info("moNo={}, moInstance={}, alcdNo={} --> {}", moNo, moInstance, alcdNo,
					alarm == null ? "(error)" : alarm);
		}
	}

	@Override
	public Alarm getAlarm(long alarmNo) throws RemoteException, Exception {
		Alarm alarm = null;
		try {
			alarm = AlarmApi.getApi().getHstAlarm(alarmNo);
			return alarm;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info("alarmNo={} --> {}", alarmNo, alarm == null ? "(error)" : alarm.getAlarmKey());
		}
	}

	@Override
	public List<AlarmCfg> getAlarmCfgs(Map<String, Object> para) throws RemoteException, Exception {
		List<AlarmCfg> list = null;
		try {
			list = AlarmApi.getApi().getAlCfgs(para);
			return list;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info("para={} --> {}", para, list == null ? "(error)" : list.size());
		}
	}

	@Override
	public List<AlarmCode> getAlarmCodes() throws RemoteException, Exception {
		List<AlarmCode> list = null;
		try {
			list = AlarmApi.getApi().getAlCds();
			return list;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info(" --> {}", list == null ? "(error)" : list.size());
		}
	}

	@Override
	public List<Alarm> getAlarms(long startDate, long endDate, Map<String, Object> para)
			throws RemoteException, Exception {
		List<Alarm> list = null;
		try {
			list = AlarmApi.getApi().getHstAlarms(startDate, endDate, para);
			return list;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info("date={}~{}, para={} --> {}", startDate, endDate, para, list == null ? "(error)" : list.size());
		}
	}

	@Override
	public List<Alarm> getCurAlarms(Map<String, Object> para) throws RemoteException, Exception {
		List<Alarm> list = null;
		try {
			list = AlarmApi.getApi().getCurAlarms(para);
			return list;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			logger.info("para={} --> {}", para, list == null ? "(error)" : list.size());
		}
	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		notiFilter.add(Mo.class);
		notiFilter.add(MoStateEvt.class);
		notiFilter.add(Alarm.class);
		return notiFilter;
	}

	@Override
	protected void onInit(StringBuffer sb) throws Exception {

		super.onInit(sb);

		FxApi.initServiceApi(AlarmApi.class);
	}

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();
	}

}

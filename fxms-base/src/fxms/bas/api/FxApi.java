package fxms.bas.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fxms.bas.alarm.AlarmEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.noti.FxEvent;
import fxms.bas.noti.NotiReceiver;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ShutdownSignal;
import subkjh.bas.lang.Lang;
import subkjh.bas.log.Loggable;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public abstract class FxApi implements Loggable, NotiReceiver {

	protected static Object lockObj = new Object();

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HHmmss");
	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 눈으로 확인할 수 있는 시간 형식으로 넘긴다.
	 * 
	 * @return yyyyMMddHHmmss의 값
	 */
	public static synchronized long getDate(long... mstime) {
		if (mstime.length == 0 || mstime[0] <= 0) {
			return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
		} else {
			return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(mstime[0])));
		}
	}
	public static synchronized String getTime(long... mstime) {
		if (mstime.length == 0 || mstime[0] <= 0) {
			return HHMMSS.format(new Date(System.currentTimeMillis()));
		} else {
			return HHMMSS.format(new Date(mstime[0]));
		}
	}

	public static synchronized int getYmd(long... mstime) {
		if (mstime.length == 0 || mstime[0] <= 0) {
			return Integer.parseInt(YYYYMMDD.format(new Date(System.currentTimeMillis())));
		} else {
			return Integer.parseInt(YYYYMMDD.format(new Date(mstime[0])));
		}
	}

	protected Map<String, Object> makeMap(Object... parameters) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters.length > i + 1) {
				map.put(String.valueOf(parameters[i]), parameters[i + 1]);
			}
		}
		return map;
	}

	@SuppressWarnings({ "unchecked" })
	protected static <T> T makeApi(Class<T> classOfT) {

		synchronized (lockObj) {
			try {
				FxApi api = (FxApi) ObjectUtil.makeObject4Use(classOfT);
				if (FxServiceImpl.fxService != null) {
					FxServiceImpl.fxService.addFxActor(api);
				}
				api.initApi();
				return (T) api;
			} catch (Exception e) {
				Logger.logger.error(e);
				if (FxServiceImpl.fxService != null) {
					try {
						FxServiceImpl.fxService.onNotify(new ShutdownSignal(
								Lang.get("** API({}) INIT FAIL - " + e.getMessage(), classOfT.getName())));
					} catch (Exception e1) {
						Logger.logger.error(e);
					}
				}
			}
		}

		return null;
	}

	public FxApi() {

	}

	private String name;

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void addEvent(Mo mo, int alcdNo) {

		AlarmEvent event = EventApi.getApi().makeEvent(mo, null, alcdNo);

		if (event != null) {
			EventApi.getApi().sendEvent(event);
		}
	}

	@Override
	public String getName() {
		return name == null ? getClass().getSimpleName() : name;
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		if (noti instanceof ReloadSignal) {
			ReloadSignal reload = (ReloadSignal) noti;
			if (ReloadSignal.RELOAD_TYPE_ALL.equals(reload.getReloadType())) {
				reload();
			}
		}
	}

	/**
	 * 클래스가 생성될 때 한 번 호출된다.
	 * 
	 * @throws Exception
	 */
	protected abstract void initApi() throws Exception;

	/**
	 * 데이터를 다시 읽어올 필요가 있을 때 호출된다.
	 * 
	 * @throws Exception
	 */
	protected abstract void reload() throws Exception;

}
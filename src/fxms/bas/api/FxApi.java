package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiReceiver;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxMatch;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ShutdownSignal;
import fxms.bas.utils.ClassUtil;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.FileUtil;

/**
 * FxMS 기본 API
 * 
 * @author subkjh
 *
 */
public abstract class FxApi implements Loggable, NotiReceiver {

	public interface KeyMaker<KEY, DATA> {
		public KEY getKey(DATA data);
	}

	public static <KEY, DATA> Map<KEY, DATA> toMap(List<DATA> datas, KeyMaker<KEY, DATA> maker) {
		Map<KEY, DATA> ret = new HashMap<>();
		for (DATA data : datas) {
			ret.put(maker.getKey(data), data);
		}
		return ret;
	}

	protected static Object lockObj = new Object();
	private static ClassUtil classUtil = null;

	@SuppressWarnings({ "unchecked" })
	protected static <T> T makeApi(Class<T> classOfT) {

		synchronized (lockObj) {

			if (classUtil == null) {
				classUtil = new ClassUtil(FxServiceImpl.serviceName);
			}

			try {

				FxApi api = (FxApi) classUtil.makeObject4Use(classOfT);

				if (FxServiceImpl.fxService != null) {
					FxServiceImpl.fxService.addFxActor(api);
				}

				api.onCreated();

				Logger.logger.info(Logger.makeString(api.getClass().getName(), api.getPara().toString()));

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

	public static void loadApiClass(String fxService) {
		classUtil = new ClassUtil(fxService);
	}

	/**
	 * 입력된 인자를 이용하여 Class Map을 생성한다.
	 * 
	 * @param parameters
	 * @return
	 */
	public static Map<String, Object> makePara(Object... parameters) {

		Map<String, Object> para = new HashMap<String, Object>();

		for (int i = 0; i < parameters.length; i += 2) {
			para.put(String.valueOf(parameters[i]), parameters[i + 1]);
		}

		return para;
	}

	private Map<String, Object> para;

	private String name;

	public FxApi() {
		para = new HashMap<String, Object>();
	}

	/**
	 * 데이터를 다시 읽어올 필요가 있을 때 호출된다.
	 * 
	 * @throws Exception
	 */
	public abstract void reload(Enum<?> type) throws Exception;

	/**
	 * 
	 * @param filename
	 * @param data
	 */
	public static void save2file(String filename, Object data) {

		int day = DateUtil.getDayOfWeek();

		String file = FxCfg.getFile(FxCfg.getHomeDatas(), "fxms",
				filename + "." + FxServiceImpl.serviceName.toLowerCase() + "." + day + ".json");

		FileUtil.writeToFile(file, FxmsUtil.toJson(data) + "\n", false);
	}

	/**
	 * 이벤트를 NotiService에 통보한다.<br>
	 * ReloadSignal이면 변경 시간을 기록한다.
	 * 
	 * @param event
	 */
	public void broadcast(FxEvent event) {

		// 다시 읽기 이면 저장소에 기록한다.
		if (event instanceof ReloadSignal) {
			try {
				VarApi.getApi().setTimeUpdated(((ReloadSignal) event).getReloadType(), DateUtil.getDtm());
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		// 통보한다.
		try {
			FxServiceImpl.fxService.sendEvent(event, true, true);
		} catch (Exception e) {
			Logger.logger.fail("Not broadcast event={}", event);
		}
	}

	@Override
	public FxMatch getMatch() {
		return null;
	}

	@Override
	public String getName() {
		return name == null ? getClass().getSimpleName() : name;
	}

	@Override
	public Map<String, Object> getPara() {
		return para;
	}

	/**
	 * 입력된 문자열이 공백이 아닌지 여부
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotEmpty(String s) {
		return s != null && s.trim().length() > 0;
	}

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 두 문자열이 같은지 여부
	 * 
	 * @param s
	 * @param s1
	 * @return
	 */
	public static boolean isSame(String s, String s1) {

		if (s != null && s1 != null) {
			return s.equals(s1);
		}

		if (s == null && s1 == null) {
			return true;
		}

		return false;
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		if (noti instanceof ReloadSignal) {
			ReloadSignal reload = (ReloadSignal) noti;
			reload(reload.getReloadType());
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public static void initServiceApi(Class<?> classOf) throws Exception {

		Object api = FxApi.makeApi(classOf);

		Logger.logger.info("{} --> {}", classOf.getName(), api.getClass().getName());

		if (api instanceof FxApiServiceTag) {
			if (classOf.isInstance(api)) {
				throw new Exception(Lang.get("The ServiceTag API is not available for this service.") + "(API="
						+ api.getClass().getName() + ")");
			}
		}

	}

}
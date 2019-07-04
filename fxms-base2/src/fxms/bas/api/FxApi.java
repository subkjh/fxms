package fxms.bas.api;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import subkjh.bas.BasCfg;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import fxms.bas.ao.AlarmEvent;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiReceiver;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.co.signal.ShutdownSignal;
import fxms.bas.fxo.FxPara;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;

public abstract class FxApi implements Loggable, NotiReceiver {

	protected static Object lockObj = new Object();
	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HHmmss");
	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private static Map<Class<?>, FxPara> apiParaMap = null;

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

	private synchronized static FxPara getApiFxPara(Class<?> classOf) {
		if (apiParaMap == null) {
			apiParaMap = loadParameters();
		}
		return apiParaMap.get(classOf);
	}

	private static Map<Class<?>, FxPara> loadParameters() {
		Map<Class<?>, FxPara> map = new HashMap<Class<?>, FxPara>();

		File file = new File(BasCfg.getHomeDeployConf("api"));
		if (file.exists() == false) {
			Logger.logger.fail("Not Found File : {}", file.getName());
			return map;
		}

		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				loadParameters(f, map);
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private static void loadParameters(File file, Map<Class<?>, FxPara> paraMap) {

		Logger.logger.info("{}", file.getPath());

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(file));
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		Element root = document.getRootElement();
		List<Element> nodeList = root.getChildren();
		String name, javaClass, value;
		Class<?> classOf = null;
		FxPara para;

		for (Element node : nodeList) {

			if (node.getName().equals("api") == false)
				continue;

			javaClass = node.getAttributeValue("class").trim();

			try {
				classOf = Class.forName(javaClass);
			} catch (ClassNotFoundException e) {
				continue;
			}

			para = new FxPara();

			List<Element> children = node.getChildren();
			for (Element child : children) {
				if (child.getName().equals("para")) {
					name = child.getAttributeValue("name");
					value = child.getAttributeValue("value");
					para.set(name, value);
				}
			}

			paraMap.put(classOf, para);
		}
	}

	@SuppressWarnings({ "unchecked" })
	protected static <T> T makeApi(Class<T> classOfT) {

		synchronized (lockObj) {
			try {
				FxApi api = (FxApi) ObjectUtil.makeObject4Use(classOfT);
				if (FxServiceImpl.fxService != null) {
					FxServiceImpl.fxService.addFxActor(api);
				}

				FxPara para = getApiFxPara(classOfT);
				if (para != null) {
					api.para = para;
				}

				api.initApi();

				Logger.logger.info(Logger.makeString(api.getClass().getName()) + api.getFxPara().toString());

				return (T) api;

			} catch (Exception e) {
				Logger.logger.error(e);
				if (FxServiceImpl.fxService != null) {
					try {
						FxServiceImpl.fxService.onNotify(new ShutdownSignal(Lang.get("** API({}) INIT FAIL - " + e.getMessage(),
								classOfT.getName())));
					} catch (Exception e1) {
						Logger.logger.error(e);
					}
				}
			}
		}

		return null;
	}

	private FxPara para;

	private String name;

	public FxApi() {
		para = new FxPara();
	}

	public void addEvent(Mo mo, int alcdNo) {

		AlarmEvent event = EventApi.getApi().makeEvent(mo, null, alcdNo);

		if (event != null) {
			EventApi.getApi().sendEvent(event);
		}
	}

	@Override
	public FxPara getFxPara() {
		return para;
	}

	@Override
	public String getName() {
		return name == null ? getClass().getSimpleName() : name;
	}

	@Override
	public void onCreated() {

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

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setPara(String name, String value) {
		para.set(name, value);
	}

	/**
	 * 클래스가 생성될 때 한 번 호출된다.
	 * 
	 * @throws Exception
	 */
	protected abstract void initApi() throws Exception;

	protected Map<String, Object> makeMap(Object... parameters) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters.length > i + 1) {
				map.put(String.valueOf(parameters[i]), parameters[i + 1]);
			}
		}
		return map;
	}

	/**
	 * 데이터를 다시 읽어올 필요가 있을 때 호출된다.
	 * 
	 * @throws Exception
	 */
	protected abstract void reload() throws Exception;

}
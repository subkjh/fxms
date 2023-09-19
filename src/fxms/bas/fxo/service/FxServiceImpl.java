package fxms.bas.fxo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.VarApi;
import fxms.bas.cron.Cron;
import fxms.bas.cron.CronFxThread;
import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiReceiver;
import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxActor;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.adapter.FxGetValueAdapter;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.bas.handler.FxHttpHandler;
import fxms.bas.handler.vo.HandlerVo;
import fxms.bas.impl.dpo.mo.MoClassInitDfo;
import fxms.bas.impl.dpo.mo.MoDpo;
import fxms.bas.impl.dvo.MoDefDvo;
import fxms.bas.poller.AdapterPoller;
import fxms.bas.signal.AliveSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.signal.ShutdownSignal;
import fxms.bas.thread.NotiSender;
import fxms.bas.thread.ShutdownCheckThread;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.FileUtil;
import subkjh.bas.net.NioClient;
import subkjh.bas.net.soproth.AliveClientJSonSoproth;

/**
 * FxMS 기본 서비스
 * 
 * @author subkjh
 *
 */
public abstract class FxServiceImpl extends UnicastRemoteObject implements FxService {

	public static FxServiceImpl fxService;
	public static String serviceName = "FxMS";
	public static Logger logger = Logger.logger;
	private static Map<String, String> errMap = new HashMap<String, String>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 5906962447101102100L;

	public synchronized static boolean setError(String name, String msg) {

		boolean log = true;
		if (msg == null) {
			if (errMap.remove(name) == null) {
				log = false;

			}
		} else {
			String msgOld = errMap.get(name);
			if (msgOld != null && msgOld.equals(msg)) {
				log = false;
			} else {
				errMap.put(name, msg);
			}
		}

		if (log) {

			StringBuffer sb = new StringBuffer();
			for (String key : errMap.keySet()) {
				sb.append("\n" + key + " : " + errMap.get(key));
			}
			FxServiceImpl.logger.fail("\n** CHECK-LIST" + sb.toString() + "\n***");
		}

		return log;
	}

	public static void start(String name, Class<?> classOfService, String[] args) {

		// try {
		// RMISocketFactory.setSocketFactory(new RMISocketFactory() {
		// public Socket createSocket(String host, int port) throws IOException
		// {
		// Socket socket = new Socket();
		// socket.setSoTimeout(30000);
		// socket.setSoLinger(false, 0);
		// socket.connect(new InetSocketAddress(host, port), 30000);
		// return socket;
		// }
		//
		// public ServerSocket createServerSocket(int port) throws IOException {
		// return new ServerSocket(port);
		// }
		// });
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		FxServiceImpl.serviceName = name;

		try {
			start0(classOfService, args);
		} catch (Exception e) {
			e.printStackTrace();
			if (logger != null)
				logger.error(e);
			System.exit(0);
		}
	}

	private static void start0(Class<?> classOfService, String[] args) throws Exception {

		int port = 0;

		FxCfg cfg = FxCfg.getCfg(serviceName);

		try {
			StringBuffer sb = new StringBuffer();

			if (args != null && args.length > 0) {
				FxCfg.setHome(args[0]);
				sb.append(Logger.makeSubString("home", args[0]));
			} else {
				sb.append(Logger.makeSubString("home", FxCfg.getHome()));
			}

			FxServiceImpl.logger = Logger.createLogger(FxCfg.getHomeLogs(), serviceName);
			Logger.logger = logger;

			cfg.putAll(args);

			port = cfg.getParaInt(FX_PARA.fxservicePort, 0);

			if (port == 0) {
				cfg.setAlone(true);
				logger.info(Logger.makeString("PORT-SERVICE", "NOT DEFINED. START ALONE"));
				sb.append(Logger.makeSubString("service-run-type", "alone"));
			} else {
				cfg.setAlone(false);
				sb.append(Logger.makeSubString("service-run-type", "ms"));
			}

			long startTime = DateUtil.getDtm();

			cfg.put(FX_PARA.fxmsStartTime.getKey(), startTime);

			sb.append(Logger.makeSubString(FX_PARA.fxmsStartTime.getKey(), startTime));
			sb.append(Logger.makeSubString("service-name", serviceName));
			sb.append(Logger.makeSubString("service-port", port));
			sb.append(Logger.makeSubString("service-class", classOfService.getName()));
			sb.append(Logger.makeSubString("timezone", cfg.getString("timezone", null)));

			Constructor<?> c = classOfService.getConstructor(String.class, int.class);

			logger.info(Logger.makeString(serviceName, "starting", sb.toString()));

			fxService = (FxServiceImpl) c.newInstance(serviceName, port);
			fxService.cfg = cfg;
			fxService.start();

		} catch (FileNotFoundException e) {
			logger.error(e);
			throw new Exception(serviceName + " can not execute because some file is not exist.");
		} catch (InvocationTargetException e) {
			logger.error(e);
			logger.error(e.getTargetException());
			if (e.getTargetException() instanceof java.rmi.server.ExportException) {
				throw new Exception("PORT-TROS(" + port + ") IS USED");
			}
			if (e.getTargetException() != null)
				throw new Exception("running fail - " + e.getTargetException().getMessage());
			throw new Exception("running fail - " + e.getMessage());
		} catch (java.rmi.server.ExportException e) {
			logger.error(e);
			throw new Exception(serviceName + " is already running or " + port + " is using by others");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	private final long CHECK_POLLER_CYCLE = 60 * 1000L; // 폴러 데이터 확인 주기
	private final long LOG_API_STATE_CYCLE = 300 * 1000L; // api 상태 로그 남기는 주기
	private long nextLogApiState = System.currentTimeMillis() + LOG_API_STATE_CYCLE;
	private long nextCheckPollerTime = System.currentTimeMillis() + 3000L;

	private final Map<String, FxActor> actors;
	private CronFxThread cronFxThread = null;
	private NotiSender notiSender;
	private boolean registryMine = false;
	private ShutdownCheckThread shutdownThread;
	/** 이벤트의 마지막 시간을 보관한다. */
	private final Map<String, Long> eventRecvMap = new HashMap<String, Long>();
	private FxCfg cfg;

	/**
	 * 
	 * @param name 서비스명
	 * @param port 사용할 포트
	 * @throws RemoteException
	 */
	public FxServiceImpl(String name, int port) throws RemoteException, Exception {

		super(port);

		// super(port, new RMIClientSocketFactoryImpl(), new
		// RMISSLServerSocketFactory());
		fxService = this;
		FxCfg.getCfg().setFxServiceName(name);
		FxCfg.getCfg().setFxServicePort(port);
		actors = new HashMap<>();
	}

	/**
	 * 
	 * @param actor
	 * @return
	 */
	public boolean addFxActor(FxActor actor) {

		if (actor == null) {
			return false;
		}

		synchronized (this.actors) {
			if (this.actors.get(actor.getName()) != null) {
				return false;
			}
			this.actors.put(actor.getName(), actor);
		}

		logger.info("{} added", actor.getName());

		return true;
	}

	/**
	 * 이름이 같은 Actor를 제공한다.
	 * 
	 * @param name
	 * @return
	 */
	public FxActor getFxActor(String name) {
		synchronized (this.actors) {
			return this.actors.get(name);
		}
	}

	@Override
	public String getFxServiceId() throws RemoteException, Exception {
		return FxCfg.getCfg().getFxServiceId();
	}

	@Override
	public String getStatus(String name) throws RemoteException, Exception {

		LOG_LEVEL level = LOG_LEVEL.getLevel(name);
		if (level == null) {
			level = LOG_LEVEL.info;
		}
		logger.info("{}", level);

		Map<String, Object> map = FxCfg.getCfg().getPara();

		synchronized (this.actors) {
			for (FxActor e : this.actors.values()) {
				if (e.getName() != null && e.getName().length() > 0) {
					try {
						if (e instanceof Loggable) {
							map.put(e.getName(), ((Loggable) e).getState(level));
						} else {
							map.put(e.getName(), e.toString());
						}
					} catch (Exception ex) {
						logger.error(ex);
					}
				}
			}
		}

		map.put("system-time", DateUtil.getDtm());
		map.put("event-time", eventRecvMap.toString());

		return FxCfg.makeLog(map);
	}

	@Override
	public String getStatusThread(String threadName) throws RemoteException, Exception {
		String ret = "";
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();

		Thread keys[] = map.keySet().toArray(new Thread[map.size()]);
		List<Thread> listThread = new ArrayList<Thread>();
		for (Thread key : keys) {
			listThread.add(key);
		}

		Collections.sort(listThread, new Comparator<Thread>() {
			@Override
			public int compare(Thread o1, Thread o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		for (Thread key : listThread) {
			StackTraceElement[] list = map.get(key);
			if (key.getName().startsWith(threadName) || threadName.equals("all")) {
				ret += "\n\n" + key + "\n";
				for (StackTraceElement e : list) {
					ret += "\t" + e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + ":"
							+ e.getLineNumber() + ")\n";
				}
			}
		}

		return ret;
	}

	@Override
	public void onNotify(FxEvent event) throws Exception {

		if (event == null) {
			return;
		}

		// 이벤트 종류별 마지막 수신한 시간을 보관한다.
		eventRecvMap.put(event.getClass().getName(), System.currentTimeMillis());

		if (event instanceof AliveSignal) {
			logger.trace("receive {}", event);
			return;
		} else {
			logger.debug("receive {}", event);
		}

		// 그외는 API를 포함하여 모두에게 보낸다.
		notifyChildren(event);

		// 종료이벤트이면 종료한다.
		if (event instanceof ShutdownSignal) {
			stop(event.toString());
		}
	}

	@Override
	public long ping(String who) throws RemoteException, Exception {

		logger.info("{}", who);

		return System.currentTimeMillis();
	}

	public void rebind() throws Exception {

		String url = makeUrl();

		if (url != null) {

			try {
				Remote remote = Naming.lookup(url);

				if (remote != null) {
					if (remote instanceof FxService) {
						FxService service = (FxService) remote;
						if (service.equals(this) == false) {
							try {
								service.stop("THE SAME TROS IS RUNNING");
								logger.info(Logger.makeString("THE SAME NAME SERVICE FOUND", "FINISH"));
							} catch (java.rmi.ConnectException ce) {
								// 등록만 되어있고 서비스는 종료된 상태
							} catch (java.rmi.NoSuchObjectException ce) {
							} catch (Exception e) {
								logger.error(e);
							}
						}

						logger.info(Logger.makeString("UNBOUND REGISTRY", url));
						Naming.unbind(url);
					}
				}
			} catch (java.rmi.NotBoundException e1) {
			} catch (Exception e1) {
				logger.error(e1);
			}

			try {
				Naming.rebind(url, this);
				logger.info(Logger.makeString("BOUND REGISTRY", url));
			} catch (Exception e) {
				throw e;
			}

		} else {
			logger.fail(Logger.makeString("BIND REGISTRY", "URL NOT DEFINED"));
		}

	}

	public boolean removeFxActor(FxActor actor, String reason) {
		if (actor == null)
			return false;

		boolean ret = false;

		synchronized (this.actors) {
			ret = this.actors.remove(actor.getName()) != null;
		}

		logger.info(Logger.makeString(actor.getName(), (ret ? "REMOVED" : "NOT FOUND") + "(" + reason + ")"));

		return ret;
	}

	/**
	 * 이벤트를 보낸다.
	 * 
	 * @param event
	 * @param toNotiService NotiService에게 보낼지 여부
	 * @param inside        내부에 통보할지 여부
	 */
	public void sendEvent(FxEvent event, boolean toNotiService, boolean inside) {

		if (event == null) {
			return;
		}

		if (toNotiService) {
			if (notiSender != null) {
				notiSender.put(event);
			} else {
				logger.fail("NOTI-SENDER NOT RUNNING : " + event);
			}
		}

		if (inside) {
			try {
				onNotify(event);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	@Override
	public String setLogLevel(String threadName, String level) throws RemoteException, Exception {

		logger.info(threadName, LOG_LEVEL.getLevel(level));

		LOG_LEVEL levelStr = LOG_LEVEL.getLevel(level);

		if (threadName == null || "all".equalsIgnoreCase(threadName)) {
			logger.setLevel(levelStr);
		} else {
			logger.setLevel4Th(threadName, levelStr.getNum());
		}

		return levelStr.name();
	}

	/**
	 * 
	 * @param para
	 * @throws Exception
	 */
	public void start() throws Exception {

		logger.setLevel(cfg.getLogLevel());
		logger.setPrintOutConsole(cfg.isLogPrintConsole());
		logger.setMaxBackupFileCount(cfg.getLogMaxFile());

		logger.info("log level={}, print-console={}, file-size={}", logger.getLevel(), logger.isPrintOutConsole(),
				logger.getMaxBackupFileCount());

		try {
			start0();
		} catch (Exception e) {
			logger.error(e);
			stop("START EXCEPTION");
			throw e;
		}

	}

	@Override
	public void stop(String reason) throws RemoteException, Exception {

		logger.info(Logger.makeString("STOPPING", reason));

		List<Method> toStop = new ArrayList<Method>();

		for (Method m : FxService.class.getDeclaredMethods()) {
			if (m.getName().startsWith("stop") && m.getParameterTypes().length == 0) {
				toStop.add(m);
			}
		}

		String name;
		for (int i = 99; i >= 0; i--) {
			name = "start" + String.format("%02d", i);
			for (Method m : toStop) {
				if (m.getName().startsWith(name)) {
					try {
						m.invoke(this);
						logger.info(m.getName() + " OK!!");
					} catch (InvocationTargetException e) {
						logger.error(e);
						logger.error(e.getTargetException());
						throw new Exception(e.getTargetException());
					} catch (Exception e2) {
						logger.error(e2);
						throw e2;
					}
				}
			}
		}

		logger.info(Logger.makeString("STOPPED", "OK"));

		if (shutdownThread != null) {
			shutdownThread.shutdown(reason);
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			System.exit(0);
		}

	}

	@SuppressWarnings("unchecked")
	protected <T> Map<String, T> getFxActors(Class<T> classOf) {

		Map<String, T> retMap = new HashMap<>();

		synchronized (this.actors) {
			for (FxActor f : this.actors.values()) {
				if (classOf.isInstance(f)) {
					retMap.put(f.getName(), (T) f);
				}
			}
		}

		return retMap;
	}

	/**
	 * 서비스에서 관리하는 리시버에게 이벤트를 전달한다.
	 * 
	 * @param event
	 */
	protected void notifyChildren(FxEvent event) {

		for (NotiReceiver r : this.getFxActors(NotiReceiver.class).values()) {
			try {
				r.onEvent(event);
				logger.trace("receiver={}, noti={}", r.getName(), event);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
	}

	/**
	 * Reload신호를 받아 후속 작업을 하는 곳
	 * 
	 * @param type
	 */
	protected void onChanged(ReloadType type) throws Exception {

		logger.info("changed {}", type.name());

	}

	/**
	 * ShutdownCheckThread가 주기적으로 호출한다.
	 * 
	 * @param mstime
	 */
	protected void onCycle(long mstime) {

		checkPollers(mstime);

		logApiState(mstime);

	}

	/**
	 * 
	 * @param sb
	 * @throws Exception
	 */
	protected void onInit(StringBuffer sb) throws Exception {

		FxApi.loadApiClass(FxServiceImpl.serviceName);

		sb.append(Logger.makeSubString("class-list", "loaded"));

		FxActorParser.getParser();

		sb.append(Logger.makeSubString("actor-list", "loaded"));

	}

	/**
	 * 모든 시작을 마친 후 호출됨
	 * 
	 * @throws Exception
	 */
	protected void onStarted() throws Exception {

		initCron();

		initMemberThread();

		initHttp();

		initMo();

		CycleFxThread thread = new CycleFxThread("OnCycleCaller", Cron.CYCLE_1_SECOND) {
			@Override
			protected void doCycle(long mstime) {
				onCycle(mstime);
			}

		};
		thread.setName("OnCycleCaller");
		thread.start();

	}

	protected void start09SignSedner(StringBuffer sb) throws Exception {

		logger.trace("called");

		if (cfg.isAlone()) {
			sb.append(Logger.makeSubString(NotiSender.NAME, "inactive"));
			return;
		}

		notiSender = new NotiSender(FxCfg.getCfg().getFxServiceId());
		notiSender.start();

		sb.append(Logger.makeSubString(notiSender.getName(), "running"));
	}

	/**
	 * Adapter 중에서 폴링이 필요한 부분을 확인하여 처리한다.
	 * 
	 * @throws Exception
	 */
	private void checkPollers(long mstime) {

		if (nextCheckPollerTime > mstime) {
			return;
		}

		nextCheckPollerTime += CHECK_POLLER_CYCLE;

		StringBuffer sb = new StringBuffer();

		AdapterPoller poller;
		List<FxGetValueAdapter> list = AdapterApi.getApi().getAdapters(FxGetValueAdapter.class);
		int count = 0;

		Map<String, AdapterPoller> pollers = getFxActors(AdapterPoller.class); // 현재 실행중인 폴러

		for (FxGetValueAdapter a : list) {

			// 폴링 주기가 있는 아탑터를 실행한다.
			if (a.getPollCycle() > 0) {
				poller = new AdapterPoller(a);
				if (pollers.get(poller.getName()) == null) {
					poller.start();
					addFxActor(poller);
					sb.append(Logger.makeSubString(a.getClass().getSimpleName(),
							a.getPollCycle() + " seconds, starting"));
				} else {
					// 중지 대상에서 제외
					pollers.remove(poller.getName());
					sb.append(Logger.makeSubString(a.getClass().getSimpleName(),
							a.getPollCycle() + " seconds, keep running"));
				}
				count++;
			}
		}

		// 비활성화된 폴러는 중지한다.
		for (AdapterPoller o : pollers.values()) {
			sb.append(Logger.makeSubString(o.getAdapter().getClass().getSimpleName(), "to finish"));
			o.stop("removed");
		}

		if (sb.length() > 0) {
			logger.info(Logger.makeString("AdapterPoller", count, sb.toString()));
		} else {
			logger.info("AdapterPoller : {}", Lang.get("Nothing has changed."));
		}
	}

	/**
	 * Cron 작업을 활성화한다.
	 * 
	 * @throws Exception
	 */
	private void initCron() throws Exception {

		cronFxThread = new CronFxThread();
		cronFxThread.start();

		logger.info(Logger.makeString("cron", "", ""));
	}

	/**
	 * RestAPI를 초기화 한다.
	 * 
	 * @throws Exception
	 */
	private void initHttp() throws Exception {

		List<HandlerVo> handlerList = FxActorParser.getParser().getActorList(HandlerVo.class);

		if (handlerList != null) {

			HttpServer server;
			StringBuffer sb;
			FxHttpHandler handler;
			Map<Integer, HandlerVo> handMap = new HashMap<Integer, HandlerVo>();
			HandlerVo vo;
			for (HandlerVo hand : handlerList) {
				if (hand.getPort() > 0) {
					vo = handMap.get(hand.getPort());

					if (vo != null) {
						// 같은 포트이면 내용을 합친다.
						vo.merge(hand);
					} else {
						handMap.put(hand.getPort(), hand);
					}
				} else {
					logger.fail(Logger.makeString("RESTA " + hand.getName(), "PORT IS NOT DEFINED"));
				}
			}

			for (HandlerVo hand : handMap.values()) {

				sb = new StringBuffer();

				try {
					server = HttpServer.create(new InetSocketAddress(hand.getPort()), 0);
					for (String context : hand.getHandler().keySet()) {
						handler = hand.getHandler().get(context);
						server.createContext(context, handler);
						sb.append("\n  ");
						sb.append(Logger.fill(context, 40, '-'));
						sb.append(handler.getClass().getName());
					}
					server.setExecutor(null);
					server.start();

					logger.info(Logger.makeString("HANDLE [" + hand.getName() + "] PORT=" + hand.getPort(), "started",
							sb.toString()));

					addFxActor(hand);

					setHandlerInfo(hand, true);

				} catch (Exception e) {

					// 정상적으로 처리되지 않은 경우 접근을 못하도록 변경한다.
					setHandlerInfo(hand, false);

					logger.fail(Logger.makeString("RESTA [" + hand.getName() + "] PORT=" + hand.getPort(),
							e.getClass().getSimpleName()));
					Logger.logger.error(e);
					throw e;
				}
			}
		}
	}

	private void initMemberThread() throws Exception {
		List<FxServiceMember> memList = FxActorParser.getParser().getActorList(FxServiceMember.class);
		StringBuffer sb = new StringBuffer();

		for (FxServiceMember mem : memList) {
			try {
				mem.startMember();
				sb.append(Logger.makeSubString(mem.getName(), "starting"));
				addFxActor(mem);
			} catch (Exception e) {
				logger.error(e);
				sb.append(Logger.makeSubString(mem.getName(), "error"));
			}

		}

		logger.info(Logger.makeString("FxServiceMember", memList.size(), sb.toString()));
	}

	private void initMo() throws Exception {

		StringBuffer sb = new StringBuffer();

		List<MoDefDvo> list = new MoClassInitDfo().call(null, null);
		for (MoDefDvo def : list) {
			MoDpo.add(def);
			sb.append(Logger.makeSubString(def.getMoClass(),
					def.getJavaClass().getSimpleName() + " > " + def.getDboJavaClass().getSimpleName()));
		}

		logger.info(Logger.makeString("Set MoClass to use", list.size(), sb.toString()));

	}

	/**
	 * FxApi 상태를 로깅한다.
	 */
	private void logApiState(long mstime) {

		if (nextLogApiState > mstime) {
			return;
		}

		nextLogApiState += LOG_API_STATE_CYCLE;

		for (FxApi api : getFxActors(FxApi.class).values()) {
			try {
				logger.debug(api.getState(LOG_LEVEL.debug));
			} catch (Exception ex) {
				logger.error(ex);
			}
		}

	}

	private String makeUrl() {
		int portRmi = FxCfg.getCfg().getRmiPort();
		if (portRmi > 0) {
			return "rmi://" + FxCfg.getIpAddress() + ":" + portRmi + "/" + FxServiceImpl.serviceName;
		} else {
			return null;
		}
	}

	private void setHandlerInfo(HandlerVo hand, boolean isOk) throws Exception {

		if (hand.getPort2db() <= 0) {
			// 0이면 저장소에 기록하지 않는다.
			return;
		}

		int port = isOk ? hand.getPort2db() : 0;
		String host = isOk ? hand.getHost2db() : "null";

		VarApi.getApi().setVarValue(hand.getName() + "-host", host, true);
		VarApi.getApi().setVarValue(hand.getName() + "-port", port, true);
		for (String name : hand.getSamePortList()) {
			VarApi.getApi().setVarValue(name + "-host", host, true);
			VarApi.getApi().setVarValue(name + "-port", port, true);
		}
	}

	private void start0() throws Exception {

		StringBuffer msg = new StringBuffer();

		start01WritePid(msg);

		start02SetUncaughtException(msg);

		start03ReadConfig(msg);

		onInit(msg);

		String name;
		List<Method> toStart = new ArrayList<Method>();

		for (Method m : FxServiceImpl.class.getDeclaredMethods()) {
			if (m.getName().startsWith("start") && m.getParameterTypes().length == 1) {
				toStart.add(m);
			}
		}

		for (int i = 4; i <= 99; i++) {
			name = "start" + String.format("%02d", i);
			for (Method m : toStart) {
				if (m.getName().startsWith(name)) {
					try {
						m.invoke(this, new Object[] { msg });
						logger.info(m.getName() + " OK!!");
					} catch (InvocationTargetException e) {
						logger.error(e);
						logger.error(e.getTargetException());
						throw new Exception(e.getTargetException());
					} catch (Exception e2) {
						logger.error(e2);
						throw e2;
					}
				}
			}
		}

		onStarted();

		logger.info(Logger.makeString(FxCfg.getCfg().getFxServiceId(), "started", msg.toString()));

	}

	private void start01WritePid(StringBuffer sb) {

		File filePid = new File(FxCfg.getFile(FxCfg.getHomeDatas(), "pids", FxServiceImpl.serviceName + ".pid"));

		// PID 설정
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		String jvmName = runtimeBean.getName();
		long pid = Long.valueOf(jvmName.split("@")[0]);
		FileUtil.writeToFile(filePid.getPath(), pid + "", false);

		FxCfg.getCfg().put("PID", pid);

		sb.append(Logger.makeSubString("pid", pid));
		logger.trace("pid={}", pid);
	}

	/**
	 * 정상처리되지 않은 예외에 대한 처리 설정
	 */
	private void start02SetUncaughtException(StringBuffer sb) {

		UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.fail("UncaughtException: thread={}", t.getName());
				logger.error(e);
				FxCfg.getCfg().addCount("COUNT_UNCAUGHT_EXCEPTION", 1);
			}

		};

		Thread.setDefaultUncaughtExceptionHandler(handler);

		sb.append(Logger.makeSubString("set-uncaught-exception", "ok"));

		logger.trace("ok");
	}

	/**
	 * 환경화일 읽어 오기
	 * 
	 * @param para
	 * @throws Exception
	 */
	private void start03ReadConfig(StringBuffer sb) throws Exception {

		if (FxCfg.getProjectName() == null) {
			throw new Exception("FILE(fxms.xml)PARAMETER(project) NOT DEFINED");
		}

		sb.append(Logger.makeSubString("project-name", FxCfg.getProjectName()));

		if ("localhost".equals(FxCfg.getIpAddress()) == false) {
			if (FxCfg.getCfg().hasNIC(FxCfg.getIpAddress()) == false) {
				throw new Exception("NETWORK-INTERFACE(" + FxCfg.getIpAddress() + ") NOT HAVE");
			}
		}

		sb.append(Logger.makeSubString("ip-address", FxCfg.getIpAddress()));
		sb.append(Logger.makeSubString("service-port", cfg.getFxServicePort()));

		logger.trace("ok");

	}

	@SuppressWarnings("unused")
	private void start04Alive(StringBuffer sb) throws Exception {

		if (cfg.isAlone()) {
			sb.append(Logger.makeSubString("alive-thread", "alone"));
			return;
		}

		int portAlive = FxCfg.getCfg().getAlivePort();

		if (portAlive > 0) {

			NioClient client = new NioClient();
			try {
				AliveClientJSonSoproth soproth = new AliveClientJSonSoproth(FxCfg.getIpAddress(),
						FxServiceImpl.serviceName) {
					public void stopSoproth(String msg) {
						super.stopSoproth(msg);
						logger.info(Logger.makeString("Aliver disconnected", null));
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}
						System.exit(0);
					}
				};

				client.startClient(NioClient.class.getSimpleName(), FxCfg.getIpAddress(), portAlive, soproth);

				sb.append(Logger.makeSubString("alive-thread", "port-" + portAlive));

			} catch (Exception e) {
				String msg = Logger.makeString("FX.MS", "NOT RUNNING on " + portAlive);
				logger.fail(msg);
				throw new Exception(msg + ":" + e.getMessage());
			}
		}
	}

	@SuppressWarnings("unused")
	private void start05CreateRegistry(StringBuffer sb) throws Exception {

		int portRmi = FxCfg.getCfg().getRmiPort();

		if (portRmi <= 0) {
			sb.append(Logger.makeSubString("create-registry", "RMI-PORT NOT DEFINED"));
			return;
		}

		try {
			LocateRegistry.createRegistry(portRmi);
			sb.append(Logger.makeSubString("create-registry", portRmi));
			registryMine = true;
		} catch (RemoteException e) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
			}

			String url = "//" + FxCfg.getIpAddress() + ":" + portRmi;
			try {
				String list[] = Naming.list(url);
				sb.append(Logger.makeSubString("create-registry", "other"));
				sb.append(Logger.makeSubString("rmi-url", url));
				sb.append(Logger.makeSubString("service-list", Arrays.toString(list)));
			} catch (Exception e1) {
				throw e1;
			}
		}
	}

	@SuppressWarnings("unused")
	private void start08Bind(StringBuffer sb) throws Exception {

		if (cfg.isAlone() && registryMine == false) {
			return;
		}

		rebind();
	}

	@SuppressWarnings("unused")
	private void start10ShutdownThread(StringBuffer sb) throws Exception {

		if (cfg.isAlone()) {
			sb.append(Logger.makeSubString("shutdown-thread", "alone"));
			return;
		}

		shutdownThread = new ShutdownCheckThread();
		shutdownThread.start();

		sb.append(Logger.makeSubString("shutdown-thread", "running"));

		logger.trace("ok");
	}

	/**
	 * unbind
	 */
	@SuppressWarnings("unused")
	private void stop98Unbind() {

		if (cfg.isAlone()) {
			return;
		}

		String url = makeUrl();

		if (url != null) {
			try {
				Naming.unbind(url);
				logger.info(Logger.makeString("UNBIND REGISTRY", "OK"));
			} catch (Exception e) {
				logger.fail(Logger.makeString("UNBIND REGISTRY", "FAIL"));
				logger.fail(e.getMessage());
			}
		}
	}

}

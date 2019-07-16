package fxms.bas.fxo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.net.NioClient;
import subkjh.bas.net.soproth.AliveClientJSonSoproth;
import fxms.bas.api.FxApi;
import fxms.bas.co.cron.CronFxThread;
import fxms.bas.co.noti.ExAliveNotiFilter;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiFilter;
import fxms.bas.co.noti.NotiReceiver;
import fxms.bas.co.noti.NotiSender;
import fxms.bas.co.signal.AliveSignal;
import fxms.bas.co.signal.ShutdownSignal;
import fxms.bas.fxo.FxActor;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.noti.thread.NotiPeerFxThread;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.fxo.thread.ShutdownFxThread;

public class FxServiceImpl extends UnicastRemoteObject implements FxService {

	public static FxServiceImpl fxService;

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

		try {
			start0(name, classOfService, args);
		} catch (Exception e) {
			e.printStackTrace();
			if (logger != null)
				logger.error(e);
			System.exit(0);
		}
	}

	private static void start0(String name, Class<?> classOfService, String[] args) throws Exception {

		int port = 0;

		FxCfg.setFxServiceName(name);

		try {
			StringBuffer sb = new StringBuffer();

			if (args != null && args.length > 0) {
				FxCfg.setHome(args[0]);
				sb.append(Logger.makeSubString("home", args[0]));
			} else {
				sb.append(Logger.makeSubString("home", FxCfg.getHome()));
			}

			FxServiceImpl.logger = Logger.createLogger(FxCfg.getHomeLogs(), name);
			Logger.logger = logger;

			Map<String, Object> para = FxCfg.parse(args);

			port = FxCfg.getInt(para, FxCfg.PARA_PORT_SERVICE, 0);

			if (port == 0) {
				FxCfg.setAlone(true);
				logger.info(Logger.makeString("PORT-SERVICE", "NOT DEFINED. START ALONE"));
				sb.append(Logger.makeSubString("service-run-type", "alone"));
			} else {
				sb.append(Logger.makeSubString("service-run-type", "ms"));
			}

			long startTime = FxApi.getDate(0);

			para.put(FxCfg.START_TIME, startTime);

			sb.append(Logger.makeSubString("start-time", startTime));
			sb.append(Logger.makeSubString("service-name", name));
			sb.append(Logger.makeSubString("service-port", port));
			sb.append(Logger.makeSubString("service-class", classOfService.getName()));

			Constructor<?> c = classOfService.getConstructor(String.class, int.class);

			logger.info(Logger.makeString(name, "starting", sb.toString()));

			fxService = (FxServiceImpl) c.newInstance(name, port);
			fxService.start(para);

		} catch (FileNotFoundException e) {
			logger.error(e);
			throw new Exception(name + " can not execute because some file is not exist.");
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
			throw new Exception(name + " is already running or " + port + " is using by others");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	private List<FxActor> actorList;
	private CronFxThread cronFxThread = null;
	private NotiSender notiSender;
	private boolean registryMine = false;
	private ShutdownFxThread shutdownThread;
	/** 이벤트의 마지막 시간을 보관한다. */
	private Map<String, Long> eventRecvMap = new HashMap<String, Long>();

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
		FxCfg.setFxServiceName(name);
		FxCfg.setFxServicePort(port);

		actorList = Collections.synchronizedList(new ArrayList<FxActor>());
	}

	/**
	 * 
	 * @param actor
	 * @return
	 */
	public boolean addFxActor(FxActor actor) {
		if (actor == null)
			return false;

		for (FxActor e : getFxActorArray()) {
			if (e.getName().equals(actor.getName())) {
				logger.fail(actor.getName());
				return false;
			}
		}

		actorList.add(actor);
		logger.info(actor.getName());

		return true;
	}

	/**
	 * 이벤트별 마지막 수신 시간을 갖는다.
	 * 
	 * @return
	 */
	public Map<String, Long> getEventRecvMap() {
		return eventRecvMap;
	}

//	public <T> T getFxActor(Class<T> classOfActor) {
//		for (FxActor e : getFxActorArray()) {
//			if (classOfActor.isInstance(e)) {
//				return (T) e;
//			}
//		}
//		return null;
//	}

	public FxActor getFxActor(String name) {
		for (FxActor e : getFxActorArray()) {
			if (e.getName().equals(name))
				return e;
		}
		return null;
	}

	@Override
	public String getFxServiceId() throws RemoteException, Exception {
		return FxCfg.getCfg().getFxServiceId();
	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		return new ExAliveNotiFilter();
	}

	@Override
	public String getStatus(String name) throws RemoteException, Exception {

		LOG_LEVEL level = LOG_LEVEL.getLevel(name);
		if (level == null) {
			level = LOG_LEVEL.info;
		}
		logger.info("{}", level);

		Map<String, Object> map = FxCfg.getCfg().getPara();

		for (FxActor e : getFxActorArray()) {
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

		map.put("system-time", FxApi.getDate());
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

	public void onCycle(long mstime) {
		logger.trace("date={}", FxApi.getDate());
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		// 이벤트 종류별 마지막 수신한 시간을 보관한다.
		if (noti != null && noti.getEventType() != null) {
			eventRecvMap.put(noti.getEventType(), System.currentTimeMillis());
		}

		if (noti instanceof AliveSignal) {
			logger.trace("receive {}", noti);
		} else {
			logger.debug("receive {}", noti);
		}

		NotiReceiver notiReceiver;
		for (FxActor e : getFxActorArray()) {
			try {

				if (e instanceof NotiPeerFxThread) {
					continue;
				}

				if (e instanceof NotiReceiver) {
					notiReceiver = (NotiReceiver) e;
					// logger.debug("receiver={}, noti={}",
					// notiReceiver.getName(), noti);
					notiReceiver.onNotify(noti);
					// logger.trace("receiver={}, noti={} ok",
					// notiReceiver.getName(), noti);
				}

			} catch (Exception ex) {
				logger.error(ex);
			}
		}

		if (noti instanceof ShutdownSignal) {
			stop(noti.toString());
		}
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

		for (FxActor e : getFxActorArray()) {
			if (e.getName().equals(actor.getName())) {
				ret = actorList.remove(e);
				break;
			}
		}

		logger.info(Logger.makeString(actor.getName(), (ret ? "REMOVED" : "NOT FOUND") + "(" + reason + ")"));

		return ret;
	}

	@Override
	public void runClass(String runnableClassName) throws RemoteException, Exception {

		try {
			Object obj = Class.forName(runnableClassName).newInstance();
			if (obj instanceof Runnable) {
				Thread t = new Thread((Runnable) obj);
				t.setName(obj.getClass().getSimpleName());
				t.start();
				logger.info(Logger.makeString(t.getName(), "RUNNING"));
			} else {
				throw new Exception("NOT Runnable Class");
			}

		} catch (Exception e) {
			logger.error(e);
			logger.fail(Logger.makeString(runnableClassName, "FAIL"));
			throw e;
		}

	}

	@Override
	public void runCron(String name) throws RemoteException, Exception {

		logger.info(name);

		try {
			cronFxThread.runCron(name);
			if (cronFxThread == null) {
				throw new Exception("크론 서비스가 실행중이 아닙니다.");
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}

	}

	/**
	 * N TROS에게 s를 방송해 달라고 요청합니다.
	 * 
	 * @param s
	 */
	public void send(FxEvent noti) {
		if (notiSender != null) {
			notiSender.put(noti);
		} else {
			logger.fail("NOTI-SENDER NOT RUNNING : " + noti);
		}
	}

	@Override
	public void sendNoti(String classNameOfNoti) throws RemoteException, Exception {

		logger.info(classNameOfNoti);

		try {
			Object obj = Class.forName(classNameOfNoti).newInstance();
			if (obj instanceof FxEvent) {
				send((FxEvent) obj);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
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
	public void start(Map<String, Object> para) throws Exception {

		FxCfg.getCfg().setPara(para);

		logger.setLevel(FxCfg.getCfg().getLogLevel());
		logger.setPrintOutConsole(FxCfg.getCfg().isLogPrintConsole());
		logger.setMaxBackupFileCount(FxCfg.getCfg().getLogMaxFile());

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

	protected FxActor[] getFxActorArray() {
		return actorList.toArray(new FxActor[actorList.size()]);
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> getFxActorList(Class<T> classOf) {

		List<T> tmpList = new ArrayList<T>();
		FxActor[] actors = getFxActorArray();

		for (FxActor f : actors) {
			if (classOf.isInstance(f)) {
				tmpList.add((T) f);
			}
		}

		return tmpList;
	}

	protected void onInit(StringBuffer sb) throws Exception {
		ObjectUtil.loadClassDef();
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

	protected void start09SignSedner(StringBuffer sb) throws Exception {

		logger.trace("called");

		if (FxCfg.isAlone()) {
			sb.append(Logger.makeSubString(NotiSender.NAME, "inactive"));
			return;
		}

		notiSender = new NotiSender(FxCfg.getCfg().getFxServiceId());
		notiSender.start();

		sb.append(Logger.makeSubString(notiSender.getName(), "running"));
	}

	private String makeUrl() {
		int portRmi = FxCfg.getCfg().getRmiPort();
		if (portRmi > 0) {
			return "rmi://" + FxCfg.getCfg().getIpAddress() + ":" + portRmi + "/" + FxCfg.getFxServiceName();
		} else {
			return null;
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

		File filePid = new File(FxCfg.getFile(FxCfg.getHomeDatas(), "pids", FxCfg.getFxServiceName() + ".pid"));

		// PID 설정
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		String jvmName = runtimeBean.getName();
		long pid = Long.valueOf(jvmName.split("@")[0]);
		FileUtil.writeToFile(filePid.getPath(), pid + "", false);

		FxCfg.getCfg().setPara("PID", pid);

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

		if (FxCfg.getCfg().getProject() == null) {
			throw new Exception("FILE(fxms.xml)PARAMETER(project) NOT DEFINED");
		}

		sb.append(Logger.makeSubString("project-name", FxCfg.getCfg().getProject()));

		if ("localhost".equals(FxCfg.getCfg().getIpAddress()) == false) {
			if (FxCfg.getCfg().hasNIC(FxCfg.getCfg().getIpAddress()) == false) {
				throw new Exception("NETWORK-INTERFACE(" + FxCfg.getCfg().getIpAddress() + ") NOT HAVE");
			}
		}

		sb.append(Logger.makeSubString("ip-address", FxCfg.getCfg().getIpAddress()));
		sb.append(Logger.makeSubString("service-port", FxCfg.getFxServicePort()));

		logger.trace("ok");

	}

	@SuppressWarnings("unused")
	private void start04Alive(StringBuffer sb) throws Exception {

		if (FxCfg.isAlone()) {
			sb.append(Logger.makeSubString("alive-thread", "alone"));
			return;
		}

		int portAlive = FxCfg.getCfg().getAlivePort();

		if (portAlive > 0) {

			NioClient client = new NioClient();
			try {
				AliveClientJSonSoproth soproth = new AliveClientJSonSoproth(FxCfg.getCfg().getIpAddress(),
						FxCfg.getFxServiceName()) {
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

				client.startClient(NioClient.class.getSimpleName(), FxCfg.getCfg().getIpAddress(), portAlive, soproth);

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

			String url = "//" + FxCfg.getCfg().getIpAddress() + ":" + portRmi;
			try {
				String list[] = Naming.list(url);
				sb.append(Logger.makeSubString("create-registry", "other"));
			} catch (Exception e1) {
				throw e1;
			}
		}
	}

	@SuppressWarnings("unused")
	private void start08Bind(StringBuffer sb) throws Exception {

		if (FxCfg.isAlone() && registryMine == false) {
			return;
		}

		rebind();
	}

	@SuppressWarnings("unused")
	private void start10ShutdownThread(StringBuffer sb) throws Exception {

		if (FxCfg.isAlone()) {
			sb.append(Logger.makeSubString("shutdown-thread", "alone"));
			return;
		}

		shutdownThread = new ShutdownFxThread();
		shutdownThread.start();

		sb.append(Logger.makeSubString("shutdown-thread", "running"));

		logger.trace("ok");
	}

	@SuppressWarnings("unused")
	private void start11Cron(StringBuffer sb) throws Exception {

		cronFxThread = new CronFxThread();
		cronFxThread.start();

		sb.append(Logger.makeSubString("cron", "running"));

		logger.trace("ok");

	}

	/**
	 * unbind
	 */
	@SuppressWarnings("unused")
	private void stop98Unbind() {

		if (FxCfg.isAlone()) {
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

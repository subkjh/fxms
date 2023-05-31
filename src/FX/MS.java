package FX;

import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import fxms.bas.api.ServiceApi;
//import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.FxServiceMo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.NioServer;
import subkjh.bas.net.soproth.AliveServerJSonSoproth;

public class MS extends UnicastRemoteObject implements FxMS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3949467601904892254L;

	public static final String name = "FxMS";

	public static void main(String[] args) {

		if (args.length > 1) {

			// 서비스명, 서비스대응자바클래스가 인자로 넘어온다.
			String serviceName = args[0];
			String javaClass = args[1];

			try {
				FxServiceImpl.start(serviceName, Class.forName(javaClass), new String[0]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
		} else {

			FxCfg cfg = null;
			try {
				cfg = FxCfg.getCfg("FX.MS");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			Logger.logger = Logger.createLogger(FxCfg.getHomeLogs(), name);

			int port = cfg.getParaInt(FX_PARA.fxmsPort, 8801);

			try {
				MS ms = new MS(port);
				ms.startAliveServer();
				ms.bind();
				ms.loop();
			} catch (Exception e) {
				Logger.logger.info(Logger.makeString(e.getClass().getSimpleName() + ":" + e.getMessage(), "exit"));
				System.exit(0);
			}
		}
	}

	private Logger logger;

	private NioServer<AliveServerJSonSoproth> aliveServer;

	public MS(int port) throws RemoteException {
		super(port);
		logger = Logger.logger;

		logger.info(Logger.fill("home", 50, '.') + FxCfg.getHome());

		String ip = FxCfg.getIpAddress();
		logger.info(Logger.fill("ip-address", 50, '.') + ip);

		List<String> ipList = FxCfg.getCfg().getNicIpList();
		for (String s : ipList) {
			logger.info(Logger.fill(s, 50, '.') + (s.equals(ip) ? "MINE" : "OTHER"));
		}
	}

	private void bind() throws Exception {

		int portRmi = FxCfg.get(FX_PARA.fxmsRmiPort, 8802);

		Logger.logger.info("** CREATE REGISTRY...");

		LocateRegistry.createRegistry(portRmi);
		Logger.logger.info("... OK");

		String url = "rmi://" + FxCfg.getIpAddress() + ":" + portRmi + "/" + name;

		try {
			Naming.rebind(url, this);

			Logger.logger.info(url);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 입력된 서비스 실행
	 * 
	 * @param serviceName
	 * @param javaClass
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	private String executeService(String serviceName, String javaClass) throws RemoteException, Exception {

		if (FxCfg.isTest) {

			new Thread() {
				public void run() {
					FX.MS.main(new String[] { serviceName, javaClass });
				}

			}.start();
			return "ok";
		}

		String cmd = FxCfg.getHomeBin("fxms.sh") + " " + serviceName + " " + javaClass;

		String msg;
		try {
			Runtime.getRuntime().exec(cmd);
			msg = Logger.fill(cmd, 50, '.') + " ok";
		} catch (Exception e) {
			msg = Logger.fill(cmd, 50, '.') + " error";
			logger.error(e);
		}

		logger.info(msg);

		return msg;
	}

	private void loop() {

		String fxsvrIpAddr = FxCfg.getIpAddress();

		logger.info(Logger.makeString(FX_PARA.fxsvrIpAddr.getKey(), fxsvrIpAddr));

		List<FxServiceMo> serviceList = null;
		AliveServerJSonSoproth soproth;

		while (true) {

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {

			}

			try {
				serviceList = ServiceApi.getApi().getServiceList();
			} catch (Exception e1) {
				Logger.logger.error(e1);
			}

			if (serviceList != null) {

				for (FxServiceMo service : serviceList) {

					if (fxsvrIpAddr.equals(service.getFxsvrIpAddr()) == false) {
						continue;
					}

					soproth = aliveServer.getSoproth(service.getFxsvrIpAddr() + "/" + service.getFxsvcName());

					if (soproth == null) {
						try {
							executeService(service.getFxsvcName(), service.getFxsvcJavaClass());
						} catch (Exception e) {
							Logger.logger.error(e);
						}
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {

						}
					}
				}
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {

			}
		}
	}

	private void startAliveServer() throws Exception {

		int portAlive = FxCfg.getCfg().getAlivePort();

		if (portAlive <= 0) {
			throw new Exception("Alive Port is not defined");
		}

		aliveServer = new NioServer<AliveServerJSonSoproth>();
		aliveServer.startServer("AliveServer", null, portAlive, AliveServerJSonSoproth.class);
		Thread.sleep(1000);
	}

	@Override
	public String getVersion() throws RemoteException, Exception {
		return "fxms";
	}

}

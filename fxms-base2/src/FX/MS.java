package FX;

import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.ServiceApi;
import fxms.bas.co.exp.NotFoundException;
import fxms.bas.co.vo.FxServiceVo;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
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
			String serviceName = args[0];

			String javaClass = args[1];
			try {
				FxServiceImpl.start(serviceName, Class.forName(javaClass), new String[0]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
		} else {

			Map<String, Object> para = null;
			try {
				para = FxCfg.parse(new String[0]);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			FxCfg.getCfg().setPara(para);
			Logger.logger = Logger.createLogger(FxCfg.getHomeLogs(), name);

			int port = FxCfg.getInt(para, FxCfg.PARA_PORT_FXMS, 0);

			if (port == 0) {
				port = 8801;
			}

			try {
				MS ms = new MS(port);
				ms.startAliveServer();
				ms.bind();
				ms.loop();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	private Logger logger;

	public MS(int port) throws RemoteException {
		super(port);
		logger = Logger.logger;

		logger.info(Logger.fill("home", 50, '.') + FxCfg.getHome());

		String ip = FxCfg.getCfg().getIpAddress();
		logger.info(Logger.fill("ip-address", 50, '.') + ip);

		List<String> ipList = FxCfg.getCfg().getNicIpList();
		for (String s : ipList) {
			logger.info(Logger.fill(s, 50, '.') + (s.equals(ip) ? "MINE" : "OTHER"));
		}
	}

	@Override
	public String addService(String serviceName, String javaClass) throws RemoteException, Exception {
		ServiceApi.getApi().addService(FxCfg.getCfg().getIpAddress(), serviceName, javaClass);
		return "OK";
	}

	@Override
	public String getVersion() throws RemoteException, Exception {
		return null;
	}

	@Override
	public String removeService(String serviceName) throws RemoteException, Exception {
		ServiceApi.getApi().removeService(FxCfg.getCfg().getIpAddress(), serviceName);
		return "OK";
	}

	@Override
	public String startService(String serviceName) throws RemoteException, Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("msIpaddr", FxCfg.getCfg().getIpAddress());
		para.put("serviceName", serviceName);
		try {
			List<FxServiceVo> serviceList = ServiceApi.getApi().getServiceList();
			if (serviceList.size() == 0) {
				throw new NotFoundException("SERVICE", serviceName);
			}
			FxServiceVo service = serviceList.get(0);
			return executeService(service.getServiceName(), service.getServiceJavaClass());
		} catch (Exception e1) {
			Logger.logger.error(e1);
			throw e1;
		}
	}

	private String executeService(String serviceName, String javaClass) throws RemoteException, Exception {

		String cmd = FxCfg.getHomeBin("fxms.sh") + " " + serviceName + " " + javaClass;

		String msg;
		try {
			Runtime.getRuntime().exec(cmd);
			msg = Logger.fill(cmd, 50, '.') + "ok";
		} catch (Exception e) {
			msg = Logger.fill(cmd, 50, '.') + "error";
			logger.error(e);
		}

		logger.info(msg);

		return msg;
	}

	@Override
	public String stopService(String serviceName) throws RemoteException, Exception {
		return null;
	}

	private void bind() throws Exception {

		int portRmi = FxCfg.getCfg().getInt(FxCfg.PARA_PORT_FXMS_REGSTRY, 8802);

		Logger.logger.info("** CREATE REGISTRY...");

		LocateRegistry.createRegistry(portRmi);
		Logger.logger.info("... OK");

		String url = "rmi://" + FxCfg.getCfg().getIpAddress() + ":" + portRmi + "/" + name;

		try {
			Naming.rebind(url, this);

			Logger.logger.info(url);

		} catch (Exception e) {
			throw e;
		}
	}

	private void loop() {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("msIpaddr", FxCfg.getCfg().getIpAddress());

		List<FxServiceVo> serviceList = null;

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

				for (FxServiceVo service : serviceList) {
					soproth = aliveServer.getSoproth(service.getMsIpaddr() + "/" + service.getServiceName());

					if (soproth == null) {
						try {
							executeService(service.getServiceName(), service.getServiceJavaClass());
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

	private NioServer<AliveServerJSonSoproth> aliveServer;

	private void startAliveServer() throws Exception {

		int portAlive = FxCfg.getCfg().getAlivePort();

		if (portAlive <= 0) {
			throw new Exception("Alive Port is not defined");
		}

		aliveServer = new NioServer<AliveServerJSonSoproth>();
		aliveServer.startServer("AliveServer", null, portAlive, AliveServerJSonSoproth.class);
	}

}

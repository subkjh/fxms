package fxms.bas.fxo.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fxms.bas.event.FxEvent;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 서비스를 시작, 종료, 상태 조회를 관리하는 클래스
 * 
 * @author subkjh
 * @since 2007-01-01
 * 
 */
public class FxServiceUtil {

	public static final int OP_START = 1;
	public static final int OP_STOP = 2;
	public static final int OP_STATUS = 3;
	public static final int OP_DUMP = 4;
	public static final int OP_STATUS_SIMPLE = 5;
	public static final int OP_LIST = 6;
	public static final int OP_HELP = 7;
	public static final int OP_LEVEL = 8;
	public static final int OP_NOTI = 9;
	public static final int OP_CALL = 10;
	public static final int OP_DELETE = 11;
	public static final int OP_ADD = 12;
	public static final int OP_RESTART = 13;

	public static void main(String[] args) {

		String host = "localhost";
		int port = 1099;
		String serviceName = null;
		String javaClass = null;
		int op = 5;
		String threadName = "*";
		String levelName = "info";
		List<String> newArgs = new ArrayList<String>();

		FxServiceUtil util = new FxServiceUtil();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-service") && args.length > i + 1) {
				i++;
				serviceName = args[i];
			} else if (args[i].equals("-host") && args.length > i + 1) {
				i++;
				host = args[i];
			} else if (args[i].equals("-port") && args.length > i + 1) {
				i++;
				port = Integer.parseInt(args[i]);
			} else if (args[i].equals("-javaClass") && args.length > i + 1) {
				i++;
				javaClass = args[i];
			} else if (args[i].equals("-thread") && args.length > i + 1) {
				i++;
				threadName = args[i];
			} else if (args[i].equals("-level") && args.length > i + 1) {
				i++;
				levelName = args[i];
			} else if (args[i].equals("--start")) {
				op = OP_START;
			} else if (args[i].equals("--stop")) {
				op = OP_STOP;
			} else if (args[i].equals("--restart")) {
				op = OP_RESTART;
			} else if (args[i].equals("--status")) {
				op = OP_STATUS;
			} else if (args[i].equals("--delete")) {
				op = OP_DELETE;
			} else if (args[i].equals("--add")) {
				op = OP_ADD;
			} else if (args[i].equals("--call")) {
				op = OP_CALL;
			} else if (args[i].equals("--statusSimple")) {
				op = OP_STATUS_SIMPLE;
			} else if (args[i].equals("--dump")) {
				op = OP_DUMP;
			} else if (args[i].equals("--list")) {
				op = OP_LIST;
			} else if (args[i].equals("--help")) {
				op = OP_HELP;
			} else if (args[i].equals("--level")) {
				op = OP_LEVEL;
			} else if (args[i].equals("--noti")) {
				op = OP_NOTI;
			} else {
				newArgs.add(args[i]);
			}
		}

		switch (op) {
		case OP_STATUS:
			if (serviceName == null) {
				util.status(host, port);
			} else {
				util.status(host, port, serviceName, levelName);
			}
			break;
		case OP_LEVEL:
			if (serviceName != null)
				util.setLogLevel(host, port, serviceName, threadName, levelName);
			break;
		case OP_NOTI:
			util.noti(host, port, javaClass);
			break;
		case OP_CALL:
			util.call(host, port, serviceName, newArgs.toArray(new String[newArgs.size()]));
			break;
		default:
			util.printList(host, port);
		}
	}

	private final String TAG = " ### ";

	public boolean call(String host, int port, String serviceName, String args[]) {

		if (host == null || serviceName == null || args.length == 0)
			return false;

		String method = args[0];
		String paraStr[] = Arrays.copyOfRange(args, 1, args.length);
		System.out.println(Logger.fill("METHOD", 30, '.') + method);
		System.out.println(Logger.fill("PARAMETERS", 30, '.') + Arrays.toString(paraStr));
		System.out.println("\n");

		FxService service;
		try {
			service = getFxService(host, port, serviceName);

			if (call(service, method, paraStr, false) == false) {
				if (call(service, method, paraStr, true) == false) {
					System.out.println("Method Not Found");
				}
			}
			return true;

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return false;
	}

	private boolean call(FxService service, String method, String paraStr[], boolean ignoreCase) throws Exception {
		boolean isMethod;
		for (Method m : service.getClass().getMethods()) {

			isMethod = ignoreCase ? m.getName().equalsIgnoreCase(method) : m.getName().equals(method);

			if (isMethod && m.getParameterTypes().length == (paraStr.length)) {

				Object para[] = new Object[paraStr.length];
				for (int i = 0; i < para.length; i++) {
					para[i] = ObjectUtil.convert(m.getParameterTypes()[i], paraStr[i]);
				}
				try {

					System.out.println("--- R E S U L T ---");
					System.out.println("\n");

					Object obj = m.invoke(service, para);

					if (obj != null) {
						System.out.println(obj);
					}

					return true;
				} catch (InvocationTargetException e) {
					e.getTargetException().printStackTrace();
					return true;
				} catch (IllegalArgumentException e) {
					continue;
				} catch (IllegalAccessException e) {
					continue;
				}

			}
		}

		return false;
	}

	/**
	 * 서비스를 찾아 제공합니다.
	 * 
	 * @param host
	 *            IP주소
	 * @param port
	 *            RMI포트
	 * @param serviceName
	 *            서비스명
	 * @return 찾은 서비스
	 * @throws NotBoundException
	 *             , Exception
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public FxService getFxService(String host, int port, String serviceName)
			throws MalformedURLException, RemoteException, NotBoundException, Exception {

		Remote ret = Naming.lookup("rmi://" + host + ":" + port + "/" + serviceName);
		if (ret instanceof FxService) {
			return (FxService) ret;
		} else {
			throw new Exception(serviceName + " is not a FxService.");
		}
	}

	public boolean noti(String host, int port, String javaClass) {
		if (host == null)
			return false;

		NotiService service;
		FxEvent noti = null;
		try {
			Object obj = Class.forName(javaClass).newInstance();
			if (obj instanceof FxEvent) {
				noti = (FxEvent) obj;
			}
		} catch (Exception e) {
		}

		if (noti == null) {
			System.out.println(javaClass + " is not a NotiBean");
			return false;
		}

		try {
			service = (NotiService) getFxService(host, port, NotiService.class.getSimpleName());
			service.broadcast("Console", noti);
		} catch (NotBoundException e) {
			System.out.println(
					String.format("%-50s\t", "rmi://" + host + ":" + port + "/" + NotiService.class.getSimpleName())
							+ "Not Bound");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	public boolean setLogLevel(String host, int port, String serviceName, String threadName, String levelName) {
		if (host == null || serviceName == null)
			return false;

		FxService service;
		try {
			service = getFxService(host, port, serviceName);

			LOG_LEVEL level = LOG_LEVEL.getLevel(levelName);
			if (level == null) {
				System.out.println("LOG LEVEL IS NOT VALID");
				return true;
			}

			String msg = service.setLogLevel(levelName, threadName);

			if (threadName == null) {
				System.out.println("HOST(" + host + ")SERVICE-NAME(" + serviceName + ") CHANGED TO " + msg);
			} else {
				System.out.println("HOST(" + host + ")SERVICE-NAME(" + serviceName + ")THREAD-NAME(" + threadName
						+ ") CHANGED TO " + msg);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return true;

	}

	/**
	 * 서버에 있는 모든 서비스의 상태를 조회합니다.
	 * 
	 * @param host
	 *            IP 주소
	 * @param port
	 *            RMI 포트
	 * @return 처리 결과
	 */
	public boolean status(String host, int port) {
		if (host == null)
			return false;

		String serviceNames[] = null;
		try {
			serviceNames = Naming.list("rmi://" + host + ":" + port);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		if (serviceNames != null) {

			List<String> urls = new ArrayList<String>();
			for (String url : serviceNames) {
				urls.add(url);
			}
			Collections.sort(urls);

			for (String url : urls) {

				Remote ret = null;
				try {
					ret = Naming.lookup(url);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					e1.printStackTrace();
				}

				if (ret instanceof FxService) {
					FxService service = (FxService) ret;
					try {
						System.out.println(TAG + url + TAG);
						System.out.println(service.getStatus(LOG_LEVEL.debug.name()));
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return true;
	}

	/**
	 * 서비스 상태를 조회합니다.
	 * 
	 * @param host
	 *            IP 주소
	 * @param port
	 *            RMI 포트
	 * @param serviceName
	 *            서비스명
	 * @return 처리 결과
	 */
	public boolean status(String host, int port, String serviceName, String level) {
		if (host == null || serviceName == null)
			return false;

		FxService service;
		try {
			service = getFxService(host, port, serviceName);
			System.out.println(TAG + host + ":" + serviceName + TAG);
			System.out.println(service.getStatus(level));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return true;
	}

	private void printList(String host, int port) {
		String serviceNames[] = null;
		try {
			serviceNames = Naming.list("rmi://" + host + ":" + port);
			for (String s : serviceNames)
				System.out.println(s);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	void printUsage() {
		System.out.println("Usage : ");
		System.out.println("java " + getClass().getName() + " host port service <method> <args[]> ");
		System.out.println();
		System.out.println("--start -javaClass <javaClass>");
		System.out.println("--stop -host <host> -port <port> -service <serviceName>");
		System.out.println("--status -host <host> -port <port> -service <serviceName>");
		System.out.println("--dump -host <host> -port <port> -service <serviceName> [-thread <threadName>]");
		System.out.println("--help -host <host> -port <port> -service <serviceName>");
		System.out.println("--call -host <host> -port <port> -service <serviceName> <method> <args[]>");
		System.out.println("\n(default : host=localhost, port=1099)");
	}

}

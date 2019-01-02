package fxms.bas.fxo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.BasCfg;
import subkjh.bas.log.Logger;

/**
 * Time Richer Object Properties
 *
 */
public class FxCfg extends BasCfg {

	public static Charset charset = Charset.forName("utf-8");
	public static String DB_CONFIG = "FXMSDB";
	public static String DB_PSVALUE = "FXMSDB";
	public static final String PARA_IPADDRESS = "IP-ADDRESS";
	public static final String PARA_PORT_FXMS = "PORT-FXMS";
	public static final String PARA_PORT_FXMS_REGSTRY = "PORT-FXMS-RMI";
	public static final String PARA_PORT_SERVICE = "PORT-SERVICE";
	public static final String PARA_PS_MS_IPADDR = "PS-MS-IPADDR";
	public static final String PARA_PS_TYPE = "PS-TYPE";
	public static final String PARA_SERVICE_NAME = "SERVICE-NAME";
	public static final String PATH_IF_OCTETS = "PATH_IF_OCTETS";
	public static final String PATH_IF_OCTETS_LAST = "PATH_IF_OCTETS_LAST";
	public static final String SIZE_THREAD_CONFIGF = "SIZE_THREAD_CONFIGF";
	public static final String START_TIME = "start-time";
	public static final int ALIVE_SIGNAL_CYCLE = 30;

	static String fxServiceName = "FxService";
	static int fxServicePort = 0;
	private static FxCfg fxCfg;

	/**
	 * 단독으로 운용되는지 여부
	 */
	private static boolean isAlone = false;

	public static boolean isAlone() {
		return isAlone;
	}

	public static void setAlone(boolean isAlone) {
		FxCfg.isAlone = isAlone;
	}

	public static FxCfg getCfg() {
		if (fxCfg == null) {
			fxCfg = new FxCfg();
		}

		return fxCfg;
	}

	public static String getFxServiceName() {
		return fxServiceName;
	}

	public static int getFxServicePort() {
		return fxServicePort;
	}

	public static Map<String, Object> parse(String args[]) throws FileNotFoundException, Exception {

		Map<String, Object> argsPara = FxCfg.getMap(args);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(PARA_SERVICE_NAME, fxServiceName);

		StringBuffer sb = new StringBuffer();

		String xmlFile = getHomeDeployConf("fxms.xml");
		String xmlFileSite = null;
		String xmlFileIp = null;

		parseFile("basic", new File(xmlFile), map, sb);

		String proj = (String) map.get(PARA_PROJECT);
		xmlFileSite = getHomeDeployConf(proj + ".xml");

		try {
			parseFile("project", new File(xmlFileSite), map, sb);
		} catch (Exception e) {
			map.put(PARA_PROJECT, "UNKNOWN");
		}

		try {
			parseFile("service", new File(getHomeDeployConf(fxServiceName + ".xml")), map, sb);
		} catch (Exception e) {
		}

		Object obj = map.get(PARA_IPADDRESS);
		if (obj != null) {
			xmlFileIp = getHomeDeployConf(obj + ".xml");
			try {
				parseFile("ipaddress", new File(xmlFileIp), map, sb);
			} catch (Exception e) {
			}

			xmlFileIp = getHomeDeployConf(obj + "-" + fxServiceName + ".xml");
			try {
				parseFile("ip&service", new File(xmlFileIp), map, sb);
			} catch (Exception e) {
			}
		}

		FxServiceImpl.logger.info(Logger.makeString("config-file", "", sb.toString()));

		map.putAll(argsPara);

		map.put("home.java", System.getProperty("java.home"));
		map.put("home", FxCfg.getHome());

		return map;
	}

	@SuppressWarnings("unchecked")
	private static void parseFile(String tag, File f, Map<String, Object> map, StringBuffer sb)
			throws FileNotFoundException, Exception {

		if (f.exists() == false) {
			sb.append(Logger.makeSubString(0, tag + ":" + f.getName(), "not founnd"));
			throw new FileNotFoundException(f.getPath());
		}

		sb.append(Logger.makeSubString(0, tag + ":" + f.getName(), "parsing"));

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (document == null)
			return;

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();

		for (Element child : children) {
			if (child.getName().equals("project")) {
				map.put(PARA_PROJECT, child.getTextTrim());
				break;
			}
		}

		parsePara(children, map);

	}

	private static void parsePara(List<Element> children, Map<String, Object> props) {

		String name;
		String value;
		String os;
		String service;

		String osName = System.getProperty("os.name", "");
		osName = osName.toLowerCase();

		if (children == null)
			return;

		for (Element child : children) {

			if (child.getName().equals("para")) {
				name = child.getAttributeValue("name");
				value = child.getAttributeValue("value");
				value = getValue(props, value, 0);
				os = child.getAttributeValue("os");
				service = child.getAttributeValue("service");

				if (service != null && service.equals(fxServiceName) == false) {
					continue;
				}
				if (os != null && osName.indexOf(os.toLowerCase()) < 0) {
					continue;
				}

				props.put(name, value);

			}
		}
	}

	public static void setFxServiceName(String fxServiceName) {
		FxCfg.fxServiceName = fxServiceName;
	}

	public static void setFxServicePort(int fxServicePort) {
		FxCfg.fxServicePort = fxServicePort;
	}

	public String getFxServiceId() {
		return getIpAddress() + "-" + getFxServiceName();
	}

	public String getIpAddress() {
		String ip = getString(PARA_IPADDRESS, null);
		if (ip == null) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				Logger.logger.error(e);
			}
		}

		if (ip == null) {
			ip = "localhost";
		}

		if (getPara() != null) {
			getPara().put(PARA_IPADDRESS, ip);
		}
		return ip;
	}

	public String getIpAddress(String serviceName) {
		String ip = getString("IP-ADDRESS-" + serviceName, null);
		return ip;
	}

	public List<String> getNicIpList() {

		List<String> list = new ArrayList<String>();

		Enumeration<NetworkInterface> en;
		try {
			en = NetworkInterface.getNetworkInterfaces();

			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();
				if (ni.isLoopback())
					continue;

				Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress ia = inetAddresses.nextElement();
					list.add(ia.getHostAddress());
				}
			}
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
		}

		return list;
	}

	public void setRmiPort(int port) {
		getPara().put("PORT-RMI", port);
	}

	public int getRmiPort() {
		return getInt("PORT-RMI", 1099);
	}

	public int getAlivePort() {
		return getInt("PORT-ALIVE", 0);
	}

	public String getUrl4Service(String serviceName) {
		return getString("URL-" + serviceName, null);
	}

	private long fxServiceStartTime;

	public long getFxServiceStartTime() {
		return fxServiceStartTime;
	}

	public void setFxServiceStartTime(long fxServiceStartTime) {
		this.fxServiceStartTime = fxServiceStartTime;
	}

}

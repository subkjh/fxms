package fxms.bas.fxo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;

/**
 * Fxms 환경 변수
 *
 */
public class FxCfg extends BasCfg {
	/**
	 * 테스트 모두 여부
	 */
	public static boolean isTest = false;
	public static Charset charset = Charset.forName("utf-8");
	public static String DB_CONFIG = "FXMSDB";
	public static String DB_PSVALUE = "FXMSDB";
	public static final int ALIVE_SIGNAL_CYCLE = 30;

	private static FxCfg fxCfg;
	private static Object lockObj = new Object();

	public static boolean get(FX_PARA name, boolean defValue) {
		return FxCfg.getCfg().isTrue(name.getKey(), defValue);
	}

	public static int get(FX_PARA name, int defaultValue) {
		return FxCfg.getCfg().getInt(name.getKey(), defaultValue);
	}

	public static long get(FX_PARA name, long defaultValue) {
		return FxCfg.getCfg().getLong(name.getKey(), defaultValue);
	}

	public static String get(FX_PARA name, String defValue) {
		return FxCfg.getCfg().getString(name.getKey(), defValue);
	}

	public static FxCfg getCfg() {

		synchronized (lockObj) {

			if (fxCfg == null) {
				try {
					fxCfg = new FxCfg(null);
				} catch (Exception e) {
				}
			}
			return fxCfg;

		}

	}

	public static FxCfg getCfg(String serviceName) throws Exception {
		synchronized (lockObj) {

			if (fxCfg == null) {
				fxCfg = new FxCfg(serviceName);
			}

			return fxCfg;
		}
	}

	private String serviceName;

	private FxCfg(String serviceName) throws Exception {

		super();

		put(FX_PARA.fxmsServiceName.getKey(), serviceName);
		this.serviceName = serviceName;

		loadConfig();
	}

	/**
	 * 1. fxms.xml<br>
	 * 2. {projectName}.xml<br>
	 * 3. {serviceName}.xml<br>
	 * 4. {ip}.xml<br>
	 * 5. {ip}_{serviceName}.xml<br>
	 * 위 순서대로 환경 내역을 분석한다. 파일이 없으면 무시된다.
	 * 
	 * @param args
	 * @return
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void loadConfig() throws FileNotFoundException, Exception {

		Map<String, Object> map = new HashMap<>();

		StringBuffer sb = new StringBuffer();

		String xmlFile = getHomeDeployConf("fxms.xml");
		String xmlFileSite = null;
		String xmlFileIp = null;

		parseFile("basic", new File(xmlFile), map, sb);

		String proj = getProjectName();
		xmlFileSite = getHomeDeployConf(proj + ".xml");

		try {
			parseFile("project", new File(xmlFileSite), map, sb);
		} catch (Exception e) {
		}

		try {
			parseFile("service", new File(getHomeDeployConf(serviceName + ".xml")), map, sb);
		} catch (Exception e) {
		}

		String ip = getIpAddress();
		if (ip != null) {
			xmlFileIp = getHomeDeployConf(ip + ".xml");
			try {
				parseFile("ipaddress", new File(xmlFileIp), map, sb);
			} catch (Exception e) {
			}

			xmlFileIp = getHomeDeployConf(ip + "-" + serviceName + ".xml");
			try {
				parseFile("ip&service", new File(xmlFileIp), map, sb);
			} catch (Exception e) {
			}
		}

		FxServiceImpl.logger.info(Logger.makeString("config-file", "", sb.toString()));

		map.put("home.java", System.getProperty("java.home"));
		map.put("home", BasCfg.getHome());

		Object timezone = map.get(FX_PARA.fxmsTimezone.getKey());
		if (timezone != null) {
			try {
				TimeZone.setDefault(TimeZone.getTimeZone(timezone.toString()));
			} catch (Exception e) {
				FxServiceImpl.logger.error(e);

			}
		} else {
			map.put(FX_PARA.fxmsTimezone.getKey(), TimeZone.getDefault().getID());
		}

		putAll(map);
	}

	@SuppressWarnings("unchecked")
	private void parseFile(String tag, File f, Map<String, Object> map, StringBuffer sb)
			throws FileNotFoundException, Exception {

		if (!f.exists()) {
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

		parsePara(children, map);

	}

	private void parsePara(List<Element> children, Map<String, Object> props) {

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

				if ((service != null && !service.equals(serviceName))
						|| (os != null && osName.indexOf(os.toLowerCase()) < 0)) {
					continue;
				}

				props.put(name, value);

			}
		}

		Object val = props.get(FX_PARA.project.getKey());
		if (val != null) {
			setProjectName(val.toString());
		}
		val = props.get(FX_PARA.fxsvrIpAddr.getKey());
		if (val != null) {
			setIpAddress(val.toString());
		}

	}

	public int getAlivePort() {
		return getParaInt(FX_PARA.fxmsAlivePort, 0);
	}

	public String getFxServiceId() {
		return getIpAddress() + "-" + getFxServiceName();
	}

	/**
	 * AppServie, MoService 등등 인지 서비스명을 제공한다.
	 *
	 * @return 서비스명
	 */
	public String getFxServiceName() {
		return this.getParaString(FX_PARA.fxmsServiceName, "FxService");
	}

	public int getFxServicePort() {
		return this.getParaInt(FX_PARA.fxservicePort, 0);
	}

	public int getRmiPort() {
		return this.getParaInt(FX_PARA.fxserviceRmiPort, 1099);
	}

	public boolean isAlone() {
		return this.isTrue("is.alone", false);
	}

	/**
	 * 실행인자로 넘어온 내용을 환경변수에 넣는다.
	 * 
	 * @param args
	 */
	public void putAll(String args[]) {
		Map<String, Object> argsPara = BasCfg.getMap(args);
		para.putAll(argsPara);
	}

	public void setAlone(boolean isAlone) {
		put("is.alone", isAlone);
	}

	public void setFxServiceName(String fxServiceName) {
		put(FX_PARA.fxmsServiceName.getKey(), fxServiceName);
	}

	public void setFxServicePort(int fxServicePort) {
		put(FX_PARA.fxservicePort.getKey(), fxServicePort);
	}

}

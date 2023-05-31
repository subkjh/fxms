package subkjh.bas;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

public class BasCfg {

	public static final int DOT_SIZE = 40;

	private static String projectName = null;
	private static String ipAddress = null;
	protected static String home = null;

	public static String getFile(String... paths) {
		String folder = paths[0];
		for (int index = 1; index < paths.length - 1; index++) {
			folder = folder + File.separator + paths[index];
		}

		File f = new File(folder);
		if (f.exists() == false)
			f.mkdirs();

		return folder + File.separator + paths[paths.length - 1];
	}

	public static String getFolder(String... folders) {
		String folder = folders[0];
		for (int index = 1; index < folders.length; index++) {
			folder = folder + File.separator + folders[index];
		}

		File f = new File(folder);
		if (f.exists() == false)
			f.mkdirs();

		return folder;
	}

	/**
	 * 서비스의 홈 디렉토리를 제공합니다.<br>
	 * java 실행 시 -DRUN_HOME=<홈 디렉토리>로 정의합니다.
	 * 
	 * @return 홈 디렉토리
	 */
	public static String getHome() {
		if (home == null)
			home = System.getProperty(FX_PARA.envFxmsHome.getKey());
		if (home == null)
			home = System.getenv(FX_PARA.envFxmsHome.getKey());
		home = home == null ? "." : home;
		return home;
	}

	public static String getHome(String filename) {
		return getHome() + File.separator + filename;
	}

	/**
	 * 명령어 폴더를 제공합니다.<br>
	 * HOME/bin<br>
	 * 
	 * @return 배포 폴더
	 */
	public static String getHomeBin() {
		return getHome() + File.separator + "bin";
	}

	/**
	 * 명령어 폴더 아래의 화일을 제공합니다.<br>
	 * HOME/bin/filename<br>
	 * 
	 * @return 배포 폴더 아래의 화일
	 */
	public static String getHomeBin(String filename) {
		return getHomeBin() + File.separator + filename;
	}

	/**
	 * 데이터 폴더를 제공합니다.<br>
	 * HOME/datas
	 * 
	 * @return 데이터폴더
	 */
	public static String getHomeDatas() {
		return getFolder(getHome(), "datas");
	}

	/**
	 * 데이터 폴더의 화일을 제공합니다.<br>
	 * HOME/datas/filename<br>
	 * 
	 * @param filename
	 * @return 데이터 폴더의 화일
	 */
	public static String getHomeDatas(String filename) {
		return getHomeDatas() + File.separator + filename;
	}

	/**
	 * 배포 폴더를 제공합니다.<br>
	 * HOME/deploy<br>
	 * 
	 * @return 배포 폴더
	 */
	public static String getHomeDeploy() {
		return getFolder(getHome(), "deploy");
	}

	/**
	 * 배포 폴더 아래의 화일을 제공합니다.<br>
	 * HOME/deploy/filename<br>
	 * 
	 * @return 배포 폴더 아래의 화일
	 */
	public static String getHomeDeploy(String filename) {
		return getHomeDeploy() + File.separator + filename;
	}

	/**
	 * 환경 화일이 존재하는 폴더는 제공합니다.<br>
	 * HOME/deploy/conf<br>
	 * 
	 * @return 환경 화일의 폴더
	 */
	public static String getHomeDeployConf() {
		return getHomeDeploy() + File.separator + "conf";
	}

	/**
	 * 환경화일 폴더 밑의 화일을 제공합니다.<br>
	 * HOME/deploy/conf/filename<br>
	 * 
	 * @param filename
	 * @return 화일명
	 */
	public static String getHomeDeployConf(String filename) {
		return getHomeDeployConf() + File.separator + filename;
	}

	/**
	 * 쿼리 홈 디렉토리를 제공합니다.<br>
	 * HOME/deploy/conf/sql<br>
	 * 
	 * @return 쿼리 홈 폴더
	 */
	public static String getHomeDeployConfSql(String... files) {
		String path = getHomeDeployConf() + File.separator + "sql";

		for (String file : files) {
			path += File.separator + file;
		}

		return path;
	}

	/**
	 * 로그를 남길 폴더를 제공합니다.<br>
	 * HOME/logs<br>
	 * 
	 * @return 로그 폴더
	 */
	public static String getHomeLogs() {
		return getHome() + File.separator + "logs";
	}

	/**
	 * 로그 폴더 아래의 화일을 제공합니다.<br>
	 * HOME/logs/filename<br>
	 * 
	 * @param filename
	 * @return 로그 폴더 아래의 화일명
	 */
	public static String getHomeLogs(String filename) {
		return getHomeLogs() + File.separator + filename;
	}

	public static String getIpAddress() {

		if (ipAddress == null) {
			try {
				ipAddress = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				Logger.logger.error(e);
			}
		}

		return ipAddress;
	}

	/**
	 * 문자배열을 파싱하여 맵으로 전달합니다.<br>
	 * --name 또는 name=value 또는 -name value 형식을 가져야 합니다.<br>
	 * 
	 * @param args
	 * @return 맵
	 */
	public static Map<String, Object> getMap(String args[]) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (args != null) {
			String a;
			for (int i = 0; i < args.length; i++) {
				a = args[i];
				if (a.startsWith("--")) {
					map.put(a.substring(2), true);
				} else if (a.startsWith("-") && (i + 1) < args.length) {
					i++;
					map.put(a.substring(1), true);
					try {
						map.put(a.substring(1), Integer.parseInt(args[i]));
					} catch (Exception e) {
						map.put(a.substring(1), args[i]);
					}
				} else {
					String ss[] = a.split("=");
					if (ss.length == 2) {
						try {
							map.put(ss[0], Integer.parseInt(ss[1]));
						} catch (Exception e) {
							map.put(ss[0], ss[1]);
						}
					}
				}
			}
		}

		return map;
	}

	/**
	 * 
	 * @return deploy/filter-list.xml
	 */
	public static String getPathFilter() {
		return getFolder(getHomeDeploy(), "filter");
	}

	public static String getPathFull(String str) {

		if (str == null)
			return getHome();

		String s = str.replace('/', File.separatorChar);

		if (s.charAt(0) == '.') {
			if (s.length() == 1)
				return getHome();
			if (s.charAt(1) == File.separatorChar) {
				return getHome() + File.separator + s.substring(2);
			} else {
				return getHome() + File.separator + s.substring(1);
			}
		}

		return s;
	}

	public static String getProjectName() {
		return BasCfg.projectName;
	}

	public static String makeLog(Map<String, Object> para) {
		List<String> lines = new ArrayList<String>();
		Object obj;
		synchronized (para) {
			for (String key : para.keySet()) {
				obj = para.get(key);
				if (obj != null)
					lines.add(Logger.fill(key, DOT_SIZE, '.') + " " + obj);
			}
		}

		java.util.Collections.sort(lines);

		StringBuffer sb = new StringBuffer();
		for (String s : lines) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}

	public static Map<String, Object> parseArgs(String args[]) {
		Map<String, Object> props = new HashMap<String, Object>();

		if (args != null) {
			String a;
			for (int i = 0; i < args.length; i++) {
				a = args[i];
				if (a.startsWith("--")) {
					props.put(a.substring(2), true);
				} else {
					String ss[] = a.split("=");
					if (ss.length == 2) {
						try {
							props.put(ss[0], Integer.parseInt(ss[1]));
						} catch (Exception e) {
							props.put(ss[0], ss[1]);
						}
					} else if (ss.length == 1) {
						props.put(ss[0], null);
					}
				}
			}
		}
		return props;
	}

	/**
	 * 홈 디렉토리를 강제로 정의합니다.
	 * 
	 * @param home
	 */
	public static void setHome(String home) {
		BasCfg.home = home;
	}

	public static void setIpAddress(String ipAddress) {
		BasCfg.ipAddress = ipAddress;
	}

	public static void setProjectName(String projectName) {
		BasCfg.projectName = projectName;
	}

	protected static String getValue(Map<String, Object> props, String valueOrg, int fromIndex) {
		if (valueOrg == null)
			return null;

		int beginIndex, endIndex = valueOrg.length();
		String name;
		Object value;
		String valueNew = null;

		beginIndex = valueOrg.indexOf('$', fromIndex);
		if (beginIndex < 0)
			return valueOrg;
		if (valueOrg.indexOf('{', beginIndex) != beginIndex + 1)
			return valueOrg;

		beginIndex += 2; // $, {

		char chs[] = valueOrg.toCharArray();
		for (int i = beginIndex; i < chs.length; i++) {
			if (chs[i] != '}')
				continue;
			endIndex = i;
			break;
		}

		name = valueOrg.substring(beginIndex, endIndex);

		value = props.get(name);
		if (value != null) {
			valueNew = valueOrg.substring(0, beginIndex - 2) + value.toString();
			fromIndex = valueNew.length();
			valueNew += valueOrg.substring(endIndex + 1);

		} else {
			return valueOrg;
		}

		return getValue(props, valueNew, fromIndex);
	}

	protected final Map<String, Object> para;

	protected BasCfg() {
		para = new HashMap<String, Object>();
	}

	public void addCount(String name, int value) {
		Object v = para.get(name);
		if (v instanceof Number) {
			para.put(name, ((Number) v).intValue() + value);
		} else {
			para.put(name, value);
		}

	}

	public int getInt(String name, int defaultValue) {
		if (para == null)
			return defaultValue;

		Object value = para.get(name);
		if (value == null)
			return defaultValue;

		if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public LOG_LEVEL getLogLevel() {
		return LOG_LEVEL.getLevel(getParaString(FX_PARA.logLevel, "trace"));
	}

	public int getLogMaxFile() {
		return getParaInt(FX_PARA.logMaxFileSize, 10);
	}

	/**
	 * 
	 * @param para
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public long getLong(String name, long defaultValue) {
		if (para == null)
			return defaultValue;

		Object value = para.get(name);
		if (value == null)
			return defaultValue;

		if (value instanceof Number) {
			return ((Number) value).longValue();
		}

		try {
			return Long.parseLong(value.toString());
		} catch (Exception e) {
			return defaultValue;
		}
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

	public Map<String, Object> getPara() {
		return para;
	}

	public int getParaInt(FX_PARA name, int defaultValue) {
		return getInt(name.getKey(), defaultValue);
	}

	public long getParaLong(FX_PARA name, long defaultValue) {
		return getLong(name.getKey(), defaultValue);
	}

	public String getParaString(FX_PARA name, String defValue) {
		return getString(name.getKey(), defValue);
	}

	public String getPrintPara() {
		return makeLog(this.para);
	}

	public String getString(String name, String defValue) {
		Object ret = para == null ? null : para.get(name);
		return ret == null ? defValue : ret.toString();
	}

	public boolean hasNIC(String ip) {
		return getNicIpList().contains(ip);
	}

	public boolean isLogPrintConsole() {
		return isTrue(FX_PARA.logPrintConsole.getKey(), false);
	}

	public boolean isTrue(String name, boolean defaultValue) {
		if (para == null)
			return defaultValue;

		Object value = para.get(name);
		if (value == null)
			return defaultValue;

		return value.toString().equalsIgnoreCase("true") //
				|| value.toString().equalsIgnoreCase("yes") //
				|| value.toString().equalsIgnoreCase("y");

	}

	public void put(String name, Object value) {
		para.put(name, value);
	}

	/**
	 * 환경변수에 모두 적용한다.
	 * 
	 * @param para
	 */
	protected void putAll(Map<String, Object> para) {
		this.para.putAll(para);
	}

}

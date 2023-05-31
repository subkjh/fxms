package subkjh.bas.co.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Lang {

	public static void throwException(String name, Object... msg) throws Exception {
		throw new Exception(get(name, msg));
	}

	private static final Map<String, String> map = new HashMap<String, String>();

	/**
	 * 언어를 제공합니다.<br>
	 * 없을 경우 입력된 문자열을 그래도 제공합니다.
	 * 
	 * @param key 찾는 문자열
	 * @param msg 문자열에 추가할 내용 "[]"로 묶어 표시합니다.
	 * @return 대체 문자열
	 */
	public static String get(String name, Object... msg) {

		String ret = map.get(name);
		if (ret == null)
			ret = name;

		if (msg.length > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append(ret);
			for (Object o : msg) {
				sb.append(" (").append(o).append(")");
			}
			return sb.toString();
		}

		return ret;
	}

	public static String get2(Object... msg) {

		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		String ret = map.get(className + "." + msg[0]);
		if (ret == null) {
			ret = map.get(msg[0]);
			if (ret == null)
				ret = String.valueOf(msg[0]);
		}

		for (int i = 1; i < msg.length; i++) {
			ret = ret.replaceFirst("\\{\\}", String.valueOf(msg[i]));
		}

		return ret;
	}

	public static void setFile(File f) throws Exception {

		if (f == null || f.exists() == false) {
			throw new Exception("not found language properties " + (f != null ? f.getPath() : ""));
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.loadFromXML(fis);
			for (Object key : p.keySet()) {
				map.put((String) key, (String) (p.getProperty((String) key)));
			}

		} catch (Exception e) {
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
				}
		}

	}

	/**
	 * 언어를 제공합니다.<br>
	 * 없을 경우 입력된 문자열을 그래도 제공합니다.<br>
	 * 그룹과 문자열을 "."으로 구분합니다.
	 * 
	 * @param key      찾는 문자열
	 * @param keyClass 찾을 문자열 그룹
	 * @return 대체 문자열
	 */
	public static String get4Class(String key, String keyClass) {
		String ret = map.get(keyClass + "." + key);
		if (ret != null)
			return ret;

		ret = map.get(key);

		return ret == null ? key : ret;
	}

	/**
	 * 사용할 언어를 설정합니다.
	 * 
	 * @param map 언어가 정의된 맵
	 */
	public static void init(Map<String, String> map) {
		if (map != null) {
			Lang.map.putAll(map);
		}
	}

	public static void put(String name, String value) {
		Lang.map.put(name, value);
	}

	public static void main(String[] args) throws Exception {

		System.out.println(Lang.get("우리는 {} 이땅에 태어나 {}", "1", "2", "3"));

		Lang c = new Lang();

		// c.findKorean(new
		// File("C:\\01.Projects(A)\\workspace\\nPrism3.0\\flex_src"));
		// c.findKorean(new
		// File("C:\\01.Projects(A)\\workspace\\Dfc_Base_Library\\src"));
		// c.findKorean(new
		// File("C:\\01.Projects(A)\\workspace\\Dfc_Layout_Library\\src"));
		// c.findKorean(new
		// File("C:\\01.Projects(A)\\workspace\\Dfc_Socket_Library\\src"));

		// c.findKorean(new File("C:\\01.Projects(A)\\workspace\\ds1.0\\src"));

		Map<String, String> langMap = new HashMap<String, String>();
		c.findKorean(new
		// File("C:\\01.Projects(A)\\workspace\\nPrism3.0\\flex_src"), langMap);
		File("src"), langMap);

		System.out.println("Table Name\tDF_CD_LANG");
		System.out.println("LANG_KEY\tLANG_VALUE");
		for (String s : langMap.keySet()) {
			System.out.println(s + "\t" + "[" + s + "]");
		}
	}

	private int countKorean;

	public void findKorean(File file, Map<String, String> map) throws Exception {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				findKorean(f, map);
			}

		} else {

			if (file.getName().endsWith(".java") == false)
				return;

			System.out.println("--------------------------------------------------------------");
			System.out.println(file.getPath());
			System.out.println("--------------------------------------------------------------");

			List<String> lineList = getLines(file);

			int beginIndex, endIndex, fromIndex = 0;
			String str;
			String con;
			String line;
			String lineTrim;

			for (String s : lineList) {
				con = s;
				beginIndex = 0;
				endIndex = 0;
				fromIndex = 0;
				line = s;
				lineTrim = line.trim();

				if (lineTrim.startsWith("//"))
					continue;

				while (true) {
					beginIndex = con.indexOf("\"", fromIndex);
					if (beginIndex >= 0) {
						endIndex = con.indexOf("\"", beginIndex + 1);
						if (endIndex < 0)
							break;
						fromIndex = endIndex + 1;
						str = con.substring(beginIndex, endIndex + 1);
						if (isKorean(str)) {

							System.out.println(countKorean + ". [" + str + "] in [" + line + "]");

							countKorean++;
							str = str.replaceAll("\\\"", "");
							str = str.replace("{Lang.get('", "");
							str = str.replace("')}", "");
							map.put(str, str);
						}
					} else {
						break;
					}
				}

			}
		}
	}

	private List<String> getLines(File file) {
		List<String> ret = new ArrayList<String>();

		BufferedReader in = null;
		String val;

		try {
			in = new BufferedReader(new FileReader(file));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}

					ret.add(val);

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret;

	}

	private boolean isKorean(String s) {
		char chs[] = s.toCharArray();
		for (char ch : chs) {
			if (ch > 0x7f)
				return true;
		}

		return false;
	}
}

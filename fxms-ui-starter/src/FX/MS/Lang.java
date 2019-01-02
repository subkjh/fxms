package FX.MS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Lang {

	private static Map<String, String> map = new HashMap<String, String>();

	public enum Type {
		button, msg, datetime, column, menu;
	}

	public static String getText(Type type, String name) {

		if (map.size() == 0) {
			loadLang();
		}

		String ret = map.get(type.name() + "." + name);
		if (ret != null) {
			return ret;
		}

		ret = map.get(name);
		return ret == null ? name : ret;
	}

	public static void loadLang() {
		BufferedReader in = null;
		String val = "";
		String ss[];
		try {
			in = new BufferedReader(new InputStreamReader(Lang.class.getResourceAsStream("lang.text")));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					ss = val.split("=");
					if (ss.length > 0) {
						map.put(ss[0].trim(), ss[1].trim());
					}
				} catch (IOException e) {
					break;
				}
			}
		} catch (Exception e) {

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

	}

}

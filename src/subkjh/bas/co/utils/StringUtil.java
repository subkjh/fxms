package subkjh.bas.co.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static List<String> split(String s, char c) {
		List<String> list = new ArrayList<String>();
		char chs[] = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char ch : chs) {
			if (ch == c) {
				list.add(sb.toString());
				sb = new StringBuffer();
			} else {
				sb.append(ch);
			}
		}

		list.add(sb.toString());

		return list;
	}

	public static boolean isNotEmpty(String s) {
		return s != null && s.trim().length() > 0;
	}

	public static int toInt(String s, int defVal) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return defVal;
		}
	}
}

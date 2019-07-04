package fxms.bas.mo.node;

import java.util.HashMap;
import java.util.Map;

public class PollPass {

	public static final String ICMP_PING = "icmp";
	public static final String ICMP_SNMP = "snmp";
	public static final String ICMP_TRAFFIC = "traffic";

	private Map<String, Integer> map;

	public static void main(String[] args) {
		PollPass p = new PollPass();

		p.set("aa", 1);
		p.set("bb", 2);

		System.out.println(p.getPollString());
	}

	public void setPollString(String s) {
		if (s == null || s.length() == 0) {
			map = null;
		} else {
			String item[] = s.split(",");
			for (String i : item) {
				String ss[] = i.split("=");
				try {
					set(ss[0], Integer.getInteger(ss[1]));
				} catch (Exception e) {
				}
			}
		}
	}

	public void set(String name, Integer sec) {
		if (map == null) {
			map = new HashMap<String, Integer>();
		}

		if (sec == null) {
			map.remove(name);
		} else {
			map.put(name, sec);
		}
	}

	public String getPollString() {
		if (map == null)
			return "";

		Integer val;
		StringBuffer sb = new StringBuffer();
		for (String key : map.keySet()) {
			val = map.get(key);
			if (val != null) {
				if (sb.toString().length() > 0)
					sb.append(",");
				sb.append(key + "=" + val.toString());
			}
		}

		return sb.toString();
	}
}

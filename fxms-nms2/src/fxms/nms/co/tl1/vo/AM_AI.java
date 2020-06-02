package fxms.nms.co.tl1.vo;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.net.co.vo.NetPdu;

/**
 * Autonomous Message<br>
 * auto id
 * 
 * @author subkjh
 * 
 */
public class AM_AI {

	private String almcode;
	private String atag;
	private List<String> verb;

	public static final String TL1_ALARM_CRITICAL = "cri";
	public static final String TL1_ALARM_MAJOR = "maj";
	public static final String TL1_ALARM_MINOR = "min";
	public static final String TL1_ALARM_WARNING = "war";
	public static final String TL1_ALARM_CLEAR = "clr";

	public static void main(String[] args) {
		String str = "A  32 REPT ALM EQPT";
		List<String> list = NetPdu.split(str);
		for (String s : list) {
			System.out.println(s);
		}
	}

	public AM_AI(String s) throws Exception {
		String line = s.trim();
		List<String> list = NetPdu.split(line);

		String ac = list.get(0);
		if ("*C".equals(ac)) {
			almcode = TL1_ALARM_CRITICAL;
		} else if ("**".equals(ac)) {
			almcode = TL1_ALARM_MAJOR;
		} else if ("*".equals(ac)) {
			almcode = TL1_ALARM_MINOR;
		} else if ("WN".equals(ac)) {
			almcode = TL1_ALARM_WARNING;
		} else if ("A".equals(ac)) {
			almcode = TL1_ALARM_CLEAR;
		}

		atag = list.get(1);

		verb = new ArrayList<String>();
		for (int i = 2; i < list.size(); i++) {
			verb.add(list.get(i));
		}

	}

	@Override
	public String toString() {
		return "AM_AI : almcode=" + almcode + ", atag=" + atag + ", verb=" + verb;
	}

	public String getAlmcode() {
		return almcode;
	}

	public String getAtag() {
		return atag;
	}

	public List<String> getVerb() {
		return verb;
	}
}

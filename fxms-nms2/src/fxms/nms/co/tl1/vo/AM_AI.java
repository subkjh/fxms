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
		if ("*C".equals(ac))
			almcode = "cri";
		else if ("**".equals(ac))
			almcode = "maj";
		else if ("*".equals(ac))
			almcode = "min";
		else if ("A".equals(ac))
			almcode = "non";

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

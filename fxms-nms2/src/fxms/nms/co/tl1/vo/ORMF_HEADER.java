package fxms.nms.co.tl1.vo;


/**
 * Output Response Message Format<br>
 * Header<br>
 * 
 * <cr><lf><lf>^^^<sid>^<year>-<month>-<day>^<hour>:<minute>:<second><br>
 * 
 * @author subkjh
 *
 */
public class ORMF_HEADER {

	private String sid;
	private long hstime;

	public ORMF_HEADER(String s) throws Exception {
		String line = s.trim();

		String ss[] = line.split(" +");
		if (ss.length != 3)
			throw new Exception("Output Response Message Format error [" + s + "]");
		sid = ss[0];

		String dt = ss[1] + ss[2];
		dt = dt.replaceAll("-", "");
		dt = dt.replaceAll(":", "");

		hstime = Long.parseLong(dt);
	}

	public long getHstime() {
		return hstime;
	}

	public String getSid() {
		return sid;
	}

	@Override
	public String toString() {
		return "ORMF_HEADER : sid=" + sid + ", hstime=" + hstime;
	}

}

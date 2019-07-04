package fxms.nms.co.tl1.vo;

/**
 * Output Response Message Format<br>
 * Response Identification<br>
 * 
 * format : <cr><lf>M^^<ctag>^<completion code><br>
 * 
 * @author subkjh
 * 
 */
public class ORMF_RI {

	private String ctag;
	private String cc;

	/** 성공적으로 수행함 */
	public static final String CC_COMPLD = "COMPLD";
	/** 문제 발생 */
	public static final String CC_DENY = "DENY";
	/** 일부만 성공함 */
	public static final String CC_PRTL = "PRTL";
	/** 성공적으로 수행되었으나, Delay Activation 상태 */
	public static final String CC_DELAY = "DELAY";
	/** 자료가 더 있다는 의미 */
	public static final String CC_RTRV = "RTRV";

	public ORMF_RI(String s) throws Exception {
		String line = s.trim();

		if (line.toCharArray()[0] != 'M') throw new Exception("ORMF_RI Format Error [" + line + "]");

		String ss[] = line.substring(2).trim().split(" ");

		if (ss.length == 1) {
			ctag = "";
			cc = ss[0];
		} else {
			ctag = ss[0];
			cc = ss[1];
		}

	}

	public String getCc() {
		return cc;
	}

	public String getCtag() {
		return ctag;
	}

	@Override
	public String toString() {
		return "ORMF_RI : ctag=" + ctag + ", cc=" + cc;
	}
}

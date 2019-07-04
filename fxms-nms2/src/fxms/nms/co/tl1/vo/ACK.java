package fxms.nms.co.tl1.vo;

import fxms.nms.co.tl1.NetPduTL1;

/**
 * Acknowledgment<br>
 * <acknowledgment code>^<ctag><cr><lf> <
 * 
 * @author subkjh
 * 
 */
public class ACK extends NetPduTL1 {

	/** In Progress */
	public static final String AC_IP = "IP";
	/** Printout Follows */
	public static final String AC_PF = "PF";
	/** All Right */
	public static final String AC_OK = "OK";
	/** No Acknowledgment */
	public static final String AC_NA = "NA";
	/** No Good */
	public static final String AC_NG = "NG";
	/** Repeat Later */
	public static final String AC_RL = "RL";

	private String ackCode;

	private String ctag;

	/**
	 * 
	 * @param str
	 *            acknowledgment code ^ ctag cr lf <
	 */
	public ACK(String str) throws Exception {
		String line = str.trim();
		char chs[] = line.toCharArray();
		if (chs[chs.length - 1] != '<')
			throw new Exception("Acknowledgment error [" + str + "]");
		String ss[] = line.split(" ");

		ackCode = ss[0];
		ctag = ss[1].trim();
	}

	public String getAckCode() {
		return ackCode;
	}

	public String getCtag() {
		return ctag;
	}

	public String toString() {
		return ackCode + ", " + ctag;
	}

}

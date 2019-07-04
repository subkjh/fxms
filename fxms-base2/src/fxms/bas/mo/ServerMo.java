package fxms.bas.mo;

import java.io.Serializable;

/**
 * @since 2018.01.17 16:04
 * @author subkjh autometic create by subkjh.dao
 *
 */

public class ServerMo extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4937769751394580926L;

	public static final String MO_CLASS = "FXSERVER";

	public static String getMoName(String msIpaddr, String serviceName) {
		return msIpaddr + "/" + serviceName;
	}

	public static void set(ServerMo mo, String msName, String msIpaddr, String msDescr) {

		mo.setMngYn(true);
		mo.setMoName(msName);
		mo.setMsIpaddr(msIpaddr);

		mo.setMsDescr(msDescr);
		mo.setMsIpaddr(msIpaddr);
		mo.setMsName(msName);
	}

	public ServerMo() {
		setMoClass(MO_CLASS);
	}

	/** 관리서버명 */
	private String msName;

	/** 관리서버주소 */
	private String msIpaddr;

	/** 관리서버설명 */
	private String msDescr;

	/** 최대관할MO수 */
	private Number maxMoCount = 100000;

	/** 현재관할MO수 */
	private Number curMoCount = 0;

	/**
	 * 관리서버명
	 * 
	 * @return 관리서버명
	 */
	public String getMsName() {
		return msName;
	}

	/**
	 * 관리서버명
	 * 
	 * @param msName
	 *            관리서버명
	 */
	public void setMsName(String msName) {
		this.msName = msName;
	}

	/**
	 * 관리서버주소
	 * 
	 * @return 관리서버주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
	}

	/**
	 * 관리서버주소
	 * 
	 * @param msIpaddr
	 *            관리서버주소
	 */
	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	/**
	 * 관리서버설명
	 * 
	 * @return 관리서버설명
	 */
	public String getMsDescr() {
		return msDescr;
	}

	/**
	 * 관리서버설명
	 * 
	 * @param msDescr
	 *            관리서버설명
	 */
	public void setMsDescr(String msDescr) {
		this.msDescr = msDescr;
	}

	/**
	 * 최대관할MO수
	 * 
	 * @return 최대관할MO수
	 */
	public Number getMaxMoCount() {
		return maxMoCount;
	}

	/**
	 * 최대관할MO수
	 * 
	 * @param maxMoCount
	 *            최대관할MO수
	 */
	public void setMaxMoCount(Number maxMoCount) {
		this.maxMoCount = maxMoCount;
	}

	/**
	 * 현재관할MO수
	 * 
	 * @return 현재관할MO수
	 */
	public Number getCurMoCount() {
		return curMoCount;
	}

	/**
	 * 현재관할MO수
	 * 
	 * @param curMoCount
	 *            현재관할MO수
	 */
	public void setCurMoCount(Number curMoCount) {
		this.curMoCount = curMoCount;
	}
}

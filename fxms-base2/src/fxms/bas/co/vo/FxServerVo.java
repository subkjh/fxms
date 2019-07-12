package fxms.bas.co.vo;

/**
 * @since 2018.01.17 16:04
 * @author subkjh autometic create by subkjh.dao
 *
 */

public class FxServerVo {

	/** 관리서버명 */
	private String msName;

	/** 관리서버주소 */
	private String msIpaddr;

	/** 관리서버설명 */
	private String msDescr;

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
	 * @param msName 관리서버명
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
	 * @param msIpaddr 관리서버주소
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
	 * @param msDescr 관리서버설명
	 */
	public void setMsDescr(String msDescr) {
		this.msDescr = msDescr;
	}

}

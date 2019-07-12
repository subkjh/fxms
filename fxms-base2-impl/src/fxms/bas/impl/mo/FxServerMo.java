package fxms.bas.impl.mo;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.HasAlarmCfg;

public class FxServerMo extends Mo implements HasAlarmCfg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7522141530589295056L;

	public static final String MO_CLASS = "FXSERVER";

	/** MO표시명 */
	private String moAname;

	/** MO종류 */
	private String moType;

	/** MO메모 */
	private String moMemo;

	/** 경보조건번호 */
	private int alarmCfgNo = -1;

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

	public FxServerMo() {
		setMoClass(MO_CLASS);
	}

	public int getAlarmCfgNo() {
		return alarmCfgNo;
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
	 * 최대관할MO수
	 * 
	 * @return 최대관할MO수
	 */
	public Number getMaxMoCount() {
		return maxMoCount;
	}

	public String getMoAname() {
		return moAname == null ? getMoName() : moAname;
	}

	public String getMoMemo() {
		return moMemo;
	}

	public String getMoType() {
		return moType;
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
	 * 관리서버주소
	 * 
	 * @return 관리서버주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
	}

	/**
	 * 관리서버명
	 * 
	 * @return 관리서버명
	 */
	public String getMsName() {
		return msName;
	}

	public void set(String msName, String msIpaddr, String msDescr) {

		setMngYn(true);
		setMoName(msName);
		setMsIpaddr(msIpaddr);

		setMsDescr(msDescr);
		setMsIpaddr(msIpaddr);
		setMsName(msName);
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 현재관할MO수
	 * 
	 * @param curMoCount 현재관할MO수
	 */
	public void setCurMoCount(Number curMoCount) {
		this.curMoCount = curMoCount;
	}

	/**
	 * 최대관할MO수
	 * 
	 * @param maxMoCount 최대관할MO수
	 */
	public void setMaxMoCount(Number maxMoCount) {
		this.maxMoCount = maxMoCount;
	}

	public void setMoAname(String moAname) {
		this.moAname = moAname;
	}

	public void setMoMemo(String moMemo) {
		this.moMemo = moMemo;
	}

	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 관리서버설명
	 * 
	 * @param msDescr 관리서버설명
	 */
	public void setMsDescr(String msDescr) {
		this.msDescr = msDescr;
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
	 * 관리서버명
	 * 
	 * @param msName 관리서버명
	 */
	public void setMsName(String msName) {
		this.msName = msName;
	}

}

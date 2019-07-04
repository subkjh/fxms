package fxms.bas.impl.mo;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.HasAlarmCfg;

public class FxMo extends Mo implements HasAlarmCfg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6337383924839710613L;

	public static final String MO_CLASS = "MO";

	/** MO표시명 */
	private String moAname;

	/** MO종류 */
	private String moType;

	/** MO메모 */
	private String moMemo;

	/** 경보조건번호 */
	private int alarmCfgNo = -1;

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public String getMoAname() {
		return moAname;
	}

	public String getMoMemo() {
		return moMemo;
	}

	public String getMoType() {
		return moType;
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
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

}

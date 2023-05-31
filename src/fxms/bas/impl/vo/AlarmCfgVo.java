package fxms.bas.impl.vo;

import fxms.bas.fxo.FxAttr;

public class AlarmCfgVo {

	@FxAttr(description = "경보조건번호")
	private int alarmCfgNo;

	@FxAttr(description = "경보조건명")
	private String alarmCfgName;

	@FxAttr(description = "MO클래스")
	private String moClass = "MO";

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(alarmCfgNo).append(",").append(alarmCfgName);
		
		return sb.toString();
	}
	
}

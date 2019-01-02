package com.fxms.ui.bas.vo;

public class FxVarVo {

	public FxVarVo() {
	}

	private String varName;

	private String varValue;

	/**
	 * 변수명
	 * 
	 * @return 변수명
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * 변수명
	 * 
	 * @param varName
	 *            변수명
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}

	/**
	 * 변수설정값
	 * 
	 * @return 변수설정값
	 */
	public String getVarValue() {
		return varValue;
	}

	/**
	 * 변수설정값
	 * 
	 * @param varValue
	 *            변수설정값
	 */
	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

	public int getIntValue(int defVal) {
		try {
			return Integer.valueOf(varValue).intValue();
		} catch (Exception e) {
		}

		return defVal;
	}

}

package com.fxms.ui.bas.code;

public class UiPsItemVo {

	private static final String[] UNIT = new String[] { "", "K", "M", "G", "T", "P" };

	public static String makeAutoUnit(Number num, int decimalPoint) {

		double value = num.doubleValue();

		int index = 0;
		while (value > 1000) {
			if (index >= UNIT.length) {
				break;
			}

			value /= 1000;
			index++;
		}

		
		String fmt = "%." + decimalPoint + "f%s";
		
		return String.format(fmt, value, UNIT[index]);
	}
	
	private String psCode;
	private String psName;
	private String psUnit;
	private String psGroup;
	private String computeFormula;
	private Double valMax;
	private Double valMin;
	private String moClass;
	private String moType;

	public UiPsItemVo() {
	}

	/**
	 * 계산식
	 * 
	 * @return 계산식
	 */
	public String getComputeFormula() {
		return computeFormula;
	}

	public String getMoClass() {
		return moClass;
	}

	public String getMoType() {
		return moType;
	}

	/**
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCode() {
		return psCode;
	}

	public String getPsGroup() {
		return psGroup;
	}

	/**
	 * 상태값명
	 * 
	 * @return 상태값명
	 */
	public String getPsName() {
		return psName;
	}

	/**
	 * 성능단위
	 * 
	 * @return 성능단위
	 */
	public String getPsUnit() {
		return psUnit;
	}

	public Double getValMax() {
		return valMax;
	}

	public Double getValMin() {
		return valMin;
	}

	/**
	 * 계산식
	 * 
	 * @param computeFormula
	 *            계산식
	 */
	public void setComputeFormula(String computeFormula) {
		this.computeFormula = computeFormula;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 상태값번호
	 * 
	 * @param psCode
	 *            상태값번호
	 */
	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	public void setPsGroup(String psGroup) {
		this.psGroup = psGroup;
	}

	/**
	 * 상태값명
	 * 
	 * @param psName
	 *            상태값명
	 */
	public void setPsName(String psName) {
		this.psName = psName;
	}

	/**
	 * 성능단위
	 * 
	 * @param psUnit
	 *            성능단위
	 */
	public void setPsUnit(String psUnit) {
		this.psUnit = psUnit;
	}

	public void setValMax(Double valMax) {
		this.valMax = valMax;
	}

	public void setValMin(Double valMin) {
		this.valMin = valMin;
	}

}

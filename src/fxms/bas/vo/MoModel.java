package fxms.bas.vo;

import java.io.Serializable;

public class MoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6743569049553840806L;

	/** 모델번호 */
	private final int modelNo;

	/** 모델명 */
	private final String modelName;

	/** 제조사명 */
	private final String vendorName;

	private final String moClass;

	public MoModel(int modelNo, String moClass, String modelName, String vendorName) {
		this.modelNo = modelNo;
		this.moClass = moClass;
		this.modelName = modelName;
		this.vendorName = vendorName;
	}

	public String getModelName() {
		return modelName;
	}

	public int getModelNo() {
		return modelNo;
	}

	public String getVendorName() {
		return vendorName;
	}

	public String getMoClass() {
		return moClass;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("MODEL(NO(");
		sb.append(getModelNo());
		sb.append(")NAME(");
		sb.append(getModelName());
		sb.append(")");
		return sb.toString();
	}
}
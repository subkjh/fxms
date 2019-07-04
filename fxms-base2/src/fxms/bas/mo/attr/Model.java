package fxms.bas.mo.attr;

import java.io.Serializable;

public class Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6743569049553840806L;

	/** 모델번호 */
	private int modelNo;

	/** 모델명 */
	private String modelName;

	/** 장비종류 */
	private String devType;

	/** 제조사명 */
	private String vendorName;

	public Model(int modelNo, String modelName, String devType, String vendorName) {
		this.modelNo = modelNo;
		this.modelName = modelName;
		this.devType = devType;
		this.vendorName = vendorName;
	}

	public String getDevType() {
		return devType;
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

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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
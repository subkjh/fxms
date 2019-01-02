package com.fxms.ui.bas.mo;

public class PbrMo extends Mo {

	public static final String MO_CLASS = "PBR";

	private String pbrType;

	private String containerId;

	public PbrMo() {
		setMoClass(MO_CLASS);
	}

	/**
	 * 컨테이너 ID
	 * 
	 * @return 컨테이너 ID
	 */
	public String getContainerId() {
		return containerId;
	}

	/**
	 * PBR 유형
	 * 
	 * @return PBR 유형
	 */
	public String getPbrType() {
		return pbrType;
	}

	/**
	 * 컨테이너 ID
	 * 
	 * @param containerId
	 *            컨테이너 ID
	 */
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	/**
	 * PBR 유형
	 * 
	 * @param pbrType
	 *            PBR 유형
	 */
	public void setPbrType(String pbrType) {
		this.pbrType = pbrType;
	}
}

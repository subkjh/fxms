package com.fxms.ui.bas.mo;

import com.fxms.ui.bas.property.MoLocatable;

public class ContainerMo extends Mo implements MoLocatable {

	public static final String MO_CLASS = "CONTAINER";

	private String containerId;

	private String containerType;

	private int inloNo;

	private String inloMemo;
	
	public ContainerMo() {
		setMoClass(MO_CLASS);
	}

	public String getContainerId() {
		return containerId;
	}

	/**
	 * 컨테이너 종류
	 * 
	 * @return 컨테이너 종류
	 */
	public String getContainerType() {
		return containerType;
	}

	/**
	 * 설치위치메모
	 * 
	 * @return 설치위치메모
	 */
	public String getInloMemo() {
		return inloMemo;
	}

	/**
	 * 컨테이너 설치위치번호
	 * 
	 * @return 컨테이너 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	public boolean isLeaf()
	{
		return false;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	/**
	 * 컨테이너 종류
	 * 
	 * @param containerType
	 *            컨테이너 종류
	 */
	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	/**
	 * 설치위치메모
	 * 
	 * @param inloMemo
	 *            설치위치메모
	 */
	public void setInloMemo(String inloMemo) {
		this.inloMemo = inloMemo;
	}

	/**
	 * 컨테이너 설치위치번호
	 * 
	 * @param inloNo
	 *            컨테이너 설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}
}

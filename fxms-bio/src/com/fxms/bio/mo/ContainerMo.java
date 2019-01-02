package com.fxms.bio.mo;

import java.io.Serializable;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoOwnership;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.09.28 17:03
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_CONTAINER", comment = "CONTAINER테이블")
@FxIndex(name = "FX_MO_CONTAINER__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_CONTAINER__UK", type = INDEX_TYPE.UK, columns = { "CONTAINER_ID" })
@FxIndex(name = "FX_MO_CONTAINER__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class ContainerMo extends Mo implements Serializable, MoOwnership {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5154445411921169234L;

	public static final String MO_CLASS = "CONTAINER";

	@FxColumn(name = "CONTAINER_ID", size = 50, comment = "컨테이너 ID")
	private String containerId;

	@FxColumn(name = "CONTAINER_TYPE", size = 30, nullable = true, comment = "컨테이너 종류")
	private String containerType;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "컨테이너 설치위치번호")
	private int inloNo;

	@FxColumn(name = "INLO_MEMO", size = 200, nullable = true, comment = "설치위치메모")
	private String inloMemo;

	@FxColumn(name = "GW_MO_NO", size = 19, nullable = true, comment = "GW관리번호")
	private long gwMoNo;

	@FxColumn(name = "INSTALL_YMD", size = 8, nullable = true, comment = "설치일자")
	private String installYmd;

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

	public long getGwMoNo() {
		return gwMoNo;
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

	public String getInstallYmd() {
		return installYmd;
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

	public void setGwMoNo(long gwMoNo) {
		this.gwMoNo = gwMoNo;
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

	public void setInstallYmd(String installYmd) {
		this.installYmd = installYmd;
	}

}

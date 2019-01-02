package com.fxms.bio.mo;

import java.io.Serializable;

import fxms.bas.mo.Mo;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FB_MO_PBR", comment = "PBR관리 테이블")
@FxIndex(name = "FB_MO_PBR__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FB_MO_PBR__FK_MO", type = INDEX_TYPE.FK, columns = { "MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class PbrMo extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1025285202101771540L;

	public static final String MO_CLASS = "PBR";

	public PbrMo() {
		setMoClass(MO_CLASS);	
	}

	@FxColumn(name = "PBR_TYPE", size = 10, comment = "PBR 유형")
	private String pbrType;

	@FxColumn(name = "CONTAINER_ID", size = 50, comment = "컨테이너 ID")
	private String containerId;

	/**
	 * PBR 유형
	 * 
	 * @return PBR 유형
	 */
	public String getPbrType() {
		return pbrType;
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

	/**
	 * 컨테이너 ID
	 * 
	 * @return 컨테이너 ID
	 */
	public String getContainerId() {
		return containerId;
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
}

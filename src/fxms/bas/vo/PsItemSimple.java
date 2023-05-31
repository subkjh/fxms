package fxms.bas.vo;

import java.io.Serializable;

public class PsItemSimple implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6922232959844399635L;

	private final String psUnit;
	private final String psId;
	private final String psName;
	private final String psColumn;

	public PsItemSimple(PsItem item) {
		this.psId = item.getPsId();
		this.psName = item.getPsName();
		this.psUnit = item.getPsUnit();
		this.psColumn = item.getPsColumn();
	}

	public String getPsUnit() {
		return psUnit;
	}

	public String getPsId() {
		return psId;
	}

	public String getPsName() {
		return psName;
	}

	public String getPsColumn() {
		return psColumn;
	}
	
	

}

package fxms.bas.mo.attr;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_CF_MODEL", comment = "모델테이블")
@FxIndex(name = "FX_CF_MODEL__PK", type = INDEX_TYPE.PK, columns = { "MODEL_NO" })
public class Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6743569049553840806L;

	@FxColumn(name = "MODEL_NO", size = 9, comment = "모델번호")
	private int modelNo;

	@FxColumn(name = "MODEL_NAME", size = 100, nullable = true, comment = "모델명")
	private String modelName;

	@FxColumn(name = "DEV_TYPE", size = 30, nullable = true, comment = "장비종류")
	private String devType;

	@FxColumn(name = "VENDOR_NAME", size = 30, nullable = true, comment = "제조사명")
	private String vendorName;

	public Model() {
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
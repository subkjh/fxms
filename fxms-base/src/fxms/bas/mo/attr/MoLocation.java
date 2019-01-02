package fxms.bas.mo.attr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_CF_INLO", comment = "설치위치테이블")
@FxIndex(name = "FX_CF_INLO__PK", type = INDEX_TYPE.PK, columns = { "INLO_NO" })
public class MoLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1422788650778102260L;

	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호")
	private int inloNo;

	@FxColumn(name = "UPPER_INLO_NO", size = 9, comment = "상위설치위치번호", defValue = "0")
	private int upperInloNo = 0;

	@FxColumn(name = "INLO_NAME", size = 100, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "INLO_FNAME", size = 200, comment = "설치위치전체명")
	private String inloFname;
	
	@FxColumn(name = "INLO_TYPE", size = 30, comment = "설치위치종류(코드집)")
	private String inloType;
	
	private MoLocation parent;

	private List<MoLocation> children;

	public List<MoLocation> getChildren() {
		if (children == null) {
			children = new ArrayList<MoLocation>();
		}
		return children;
	}

	public String getInloFname() {
		return inloFname;
	}

	public String getInloName() {
		return inloName;
	}

	public int getInloNo() {
		return inloNo;
	}

	public String getInloType() {
		return inloType;
	}

	public MoLocation getParent() {
		return parent;
	}

	public int getUpperInloNo() {
		return upperInloNo;
	}

	public void setInloFname(String inloFname) {
		this.inloFname = inloFname;
	}

	public void setInloName(String inloName) {
		this.inloName = inloName;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public void setInloType(String inloType) {
		this.inloType = inloType;
	}

	public void setParent(MoLocation parent) {
		this.parent = parent;
	}

	public void setUpperInloNo(int upperInloNo) {
		this.upperInloNo = upperInloNo;
	}

	public String toString() {
		return "INLO(" + inloNo + ":" + inloFname + ")";
	}
}

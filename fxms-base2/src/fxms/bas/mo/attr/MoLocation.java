package fxms.bas.mo.attr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MoLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1422788650778102260L;

	/** 설치위치번호 */
	private int inloNo;

	/** 상위설치위치번호 */
	private int upperInloNo = 0;

	/** 설치위치명 */
	private String inloName;

	/** 설치위치전체명 */
	private String inloFname;

	/** 설치위치종류(코드집) */
	private String inloType;

	private MoLocation parent;

	private List<MoLocation> children;

	public MoLocation(int inloNo, String inloName, String inloType, int upperInloNo, String inloFname) {
		this.inloNo = inloNo;
		this.inloName = inloName;
		this.inloType = inloType;
		this.upperInloNo = upperInloNo;
		this.inloFname = inloFname;
	}

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

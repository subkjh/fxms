package fxms.module.usertree.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLocation;

/**
 * 트리용 설치위치 VO
 * 
 * @author SUBKJH-DEV
 *
 */

public class UserTreeLocationVo {

	private int inloNo;

	private String inloName;

	private String inloFname;

	private String inloType;

	private List<Mo> moList;

	public UserTreeLocationVo() {

	}

	public UserTreeLocationVo(MoLocation src) {
		inloNo = src.getInloNo();
		inloName = src.getInloName();
		inloFname = src.getInloFname();
		inloType = src.getInloType();
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

	public List<Mo> getMoList() {

		if (moList == null) {
			moList = new ArrayList<Mo>();
		}

		return moList;
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

}

package fxms.bas.co;

public class AmUserVo {

	private int userNo;

	private String amName;

	private String amMail;

	private String amTelno;

	public AmUserVo() {

	}

	public AmUserVo(int userNo, String amName, String amMail, String amTelno) {
		this.userNo = userNo;
		this.amName = amName;
		this.amMail = amMail;
		this.amTelno = amTelno;
	}

	public String getAmMail() {
		return amMail;
	}

	public String getAmName() {
		return amName;
	}

	public String getAmTelno() {
		return amTelno;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setAmMail(String amMail) {
		this.amMail = amMail;
	}

	public void setAmName(String amName) {
		this.amName = amName;
	}

	public void setAmTelno(String amTelno) {
		this.amTelno = amTelno;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

}

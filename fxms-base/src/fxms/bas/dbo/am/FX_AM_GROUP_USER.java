package fxms.bas.dbo.am;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.08.10 09:55
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AM_GROUP_USER", comment = "관리그룹사용자내역테이블")
@FxIndex(name = "FX_AM_GROUP_USER__PK", type = INDEX_TYPE.PK, columns = { "AM_GROUP_NO", "USER_NO" })
public class FX_AM_GROUP_USER  {

	public FX_AM_GROUP_USER() {
	}

	@FxColumn(name = "AM_GROUP_NO", size = 9, comment = "관리그룹번호")
	private int amGroupNo;

	@FxColumn(name = "AM_USER_TYPE", size = 10, comment = "사용자구분")
	private String amUserType;

	@FxColumn(name = "USER_NO", size = 19, comment = "운용자번호")
	private long userNo;

	@FxColumn(name = "AM_NAME", size = 50, nullable = true, comment = "사용자명")
	private String amName;

	@FxColumn(name = "AM_MAIL", size = 100, nullable = true, comment = "사용자이메일")
	private String amMail;

	@FxColumn(name = "AM_TELNO", size = 50, nullable = true, comment = "사용자전화번호")
	private String amTelno;

	@FxColumn(name = "AM_DESCR", size = 200, nullable = true, comment = "사용자설명")
	private String amDescr;

	@FxColumn(name = "ENABLE_YN", size = 1, nullable = true, comment = "활성화여부", defValue = "'Y'")
	private boolean enableYn = true;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	/**
	 * 관리그룹번호
	 * 
	 * @return 관리그룹번호
	 */
	public int getAmGroupNo() {
		return amGroupNo;
	}

	/**
	 * 관리그룹번호
	 * 
	 * @param amGroupNo
	 *            관리그룹번호
	 */
	public void setAmGroupNo(int amGroupNo) {
		this.amGroupNo = amGroupNo;
	}

	/**
	 * 사용자구분
	 * 
	 * @return 사용자구분
	 */
	public String getAmUserType() {
		return amUserType;
	}

	/**
	 * 사용자구분
	 * 
	 * @param amUserType
	 *            사용자구분
	 */
	public void setAmUserType(String amUserType) {
		this.amUserType = amUserType;
	}

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public long getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(long userNo) {
		this.userNo = userNo;
	}

	/**
	 * 사용자명
	 * 
	 * @return 사용자명
	 */
	public String getAmName() {
		return amName;
	}

	/**
	 * 사용자명
	 * 
	 * @param amName
	 *            사용자명
	 */
	public void setAmName(String amName) {
		this.amName = amName;
	}

	/**
	 * 사용자이메일
	 * 
	 * @return 사용자이메일
	 */
	public String getAmMail() {
		return amMail;
	}

	/**
	 * 사용자이메일
	 * 
	 * @param amMail
	 *            사용자이메일
	 */
	public void setAmMail(String amMail) {
		this.amMail = amMail;
	}

	/**
	 * 사용자전화번호
	 * 
	 * @return 사용자전화번호
	 */
	public String getAmTelno() {
		return amTelno;
	}

	/**
	 * 사용자전화번호
	 * 
	 * @param amTelno
	 *            사용자전화번호
	 */
	public void setAmTelno(String amTelno) {
		this.amTelno = amTelno;
	}

	/**
	 * 사용자설명
	 * 
	 * @return 사용자설명
	 */
	public String getAmDescr() {
		return amDescr;
	}

	/**
	 * 사용자설명
	 * 
	 * @param amDescr
	 *            사용자설명
	 */
	public void setAmDescr(String amDescr) {
		this.amDescr = amDescr;
	}

	/**
	 * 활성화여부
	 * 
	 * @return 활성화여부
	 */
	public boolean isEnableYn() {
		return enableYn;
	}

	/**
	 * 활성화여부
	 * 
	 * @param enableYn
	 *            활성화여부
	 */
	public void setEnableYn(boolean enableYn) {
		this.enableYn = enableYn;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}
}

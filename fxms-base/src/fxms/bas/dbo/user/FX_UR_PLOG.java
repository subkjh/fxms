package fxms.bas.dbo.user;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_PLOG", comment = "운영자암호이력테이블")
@FxIndex(name = "FX_UR_PLOG__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "USER_PASSWD" })
public class FX_UR_PLOG {

	public FX_UR_PLOG() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "USER_PASSWD", size = 255, comment = "운용자암호")
	private String userPasswd;

	@FxColumn(name = "USE_SRT_DATE", size = 14, comment = "사용시작일시")
	private long useSrtDate;

	@FxColumn(name = "USE_END_DATE", size = 14, nullable = true, comment = "사용종료일시")
	private long useEndDate;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시")
	private long regDate;

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	/**
	 * 운용자암호
	 * 
	 * @return 운용자암호
	 */
	public String getUserPasswd() {
		return userPasswd;
	}

	/**
	 * 운용자암호
	 * 
	 * @param userPasswd
	 *            운용자암호
	 */
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	/**
	 * 사용시작일시
	 * 
	 * @return 사용시작일시
	 */
	public long getUseSrtDate() {
		return useSrtDate;
	}

	/**
	 * 사용시작일시
	 * 
	 * @param useSrtDate
	 *            사용시작일시
	 */
	public void setUseSrtDate(long useSrtDate) {
		this.useSrtDate = useSrtDate;
	}

	/**
	 * 사용종료일시
	 * 
	 * @return 사용종료일시
	 */
	public long getUseEndDate() {
		return useEndDate;
	}

	/**
	 * 사용종료일시
	 * 
	 * @param useEndDate
	 *            사용종료일시
	 */
	public void setUseEndDate(long useEndDate) {
		this.useEndDate = useEndDate;
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

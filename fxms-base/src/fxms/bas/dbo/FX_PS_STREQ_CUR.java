package fxms.bas.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.20 17:09
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_PS_STREQ_CUR", comment = "통계생성요청테이블")
@FxIndex(name = "FX_PS_STAT_REQ__PK", type = INDEX_TYPE.PK, columns = { "MAKE_REQ_NO" })
@FxIndex(name = "FX_PS_STREQ_CUR__UK", type = INDEX_TYPE.UK, columns = { "PS_TABLE", "PS_TYPE", "PS_DATE" })
public class FX_PS_STREQ_CUR implements Serializable {

	public FX_PS_STREQ_CUR() {
	}

	public static final String FX_SEQ_MAKEREQNO = "FX_SEQ_MAKEREQNO";
	@FxColumn(name = "MAKE_REQ_NO", size = 19, comment = "생성요청번호", sequence = "FX_SEQ_MAKEREQNO")
	private long makeReqNo;

	@FxColumn(name = "PS_TABLE", size = 15, comment = "테이블명")
	private String psTable;

	@FxColumn(name = "PS_TYPE", size = 10, comment = "자료종류")
	private String psType;

	@FxColumn(name = "PS_DATE", size = 14, comment = "생성할 일시")
	private long psDate;

	@FxColumn(name = "MAKE_POSSIBLE_DATE", size = 14, comment = "생성가능일시")
	private long makePossibleDate;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	/**
	 * 생성요청번호
	 * 
	 * @return 생성요청번호
	 */
	public long getMakeReqNo() {
		return makeReqNo;
	}

	/**
	 * 생성요청번호
	 * 
	 * @param makeReqNo
	 *            생성요청번호
	 */
	public void setMakeReqNo(long makeReqNo) {
		this.makeReqNo = makeReqNo;
	}

	/**
	 * 테이블명
	 * 
	 * @return 테이블명
	 */
	public String getPsTable() {
		return psTable;
	}

	/**
	 * 테이블명
	 * 
	 * @param psTable
	 *            테이블명
	 */
	public void setPsTable(String psTable) {
		this.psTable = psTable;
	}

	/**
	 * 자료종류
	 * 
	 * @return 자료종류
	 */
	public String getPsType() {
		return psType;
	}

	/**
	 * 자료종류
	 * 
	 * @param psType
	 *            자료종류
	 */
	public void setPsType(String psType) {
		this.psType = psType;
	}

	/**
	 * 생성할 일시
	 * 
	 * @return 생성할 일시
	 */
	public long getPsDate() {
		return psDate;
	}

	/**
	 * 생성할 일시
	 * 
	 * @param psDate
	 *            생성할 일시
	 */
	public void setPsDate(long psDate) {
		this.psDate = psDate;
	}

	/**
	 * 생성가능일시
	 * 
	 * @return 생성가능일시
	 */
	public long getMakePossibleDate() {
		return makePossibleDate;
	}

	/**
	 * 생성가능일시
	 * 
	 * @param makePossibleDate
	 *            생성가능일시
	 */
	public void setMakePossibleDate(long makePossibleDate) {
		this.makePossibleDate = makePossibleDate;
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

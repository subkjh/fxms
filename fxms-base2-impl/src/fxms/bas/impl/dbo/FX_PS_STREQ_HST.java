package fxms.bas.impl.dbo;

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

@FxTable(name = "FX_PS_STREQ_HST", comment = "통계생성이력테이블")
@FxIndex(name = "FX_PS_STREQ_HST__PK", type = INDEX_TYPE.PK, columns = { "MAKE_REQ_NO" })
public class FX_PS_STREQ_HST {

	public FX_PS_STREQ_HST() {
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

	@FxColumn(name = "START_DATE", size = 14, nullable = true, comment = "생성시작일시")
	private long startDate;

	@FxColumn(name = "END_DATE", size = 14, nullable = true, comment = "생성종료일시")
	private long endDate;

	@FxColumn(name = "MAKE_STATE", size = 1, nullable = true, comment = "생성상태", defValue = "‘R’")
	private String makeState = "R";

	@FxColumn(name = "ROW_SIZE", size = 19, nullable = true, comment = "생성건수")
	private long rowSize;

	@FxColumn(name = "RET_MSG", size = 1000, nullable = true, comment = "결과메시지")
	private String retMsg;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

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
	 * 생성시작일시
	 * 
	 * @return 생성시작일시
	 */
	public long getStartDate() {
		return startDate;
	}

	/**
	 * 생성시작일시
	 * 
	 * @param startDate
	 *            생성시작일시
	 */
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	/**
	 * 생성종료일시
	 * 
	 * @return 생성종료일시
	 */
	public long getEndDate() {
		return endDate;
	}

	/**
	 * 생성종료일시
	 * 
	 * @param endDate
	 *            생성종료일시
	 */
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	/**
	 * 생성상태
	 * 
	 * @return 생성상태
	 */
	public String getMakeState() {
		return makeState;
	}

	/**
	 * 생성상태
	 * 
	 * @param makeState
	 *            생성상태
	 */
	public void setMakeState(String makeState) {
		this.makeState = makeState;
	}

	/**
	 * 생성건수
	 * 
	 * @return 생성건수
	 */
	public long getRowSize() {
		return rowSize;
	}

	/**
	 * 생성건수
	 * 
	 * @param rowSize
	 *            생성건수
	 */
	public void setRowSize(long rowSize) {
		this.rowSize = rowSize;
	}

	/**
	 * 결과메시지
	 * 
	 * @return 결과메시지
	 */
	public String getRetMsg() {
		return retMsg;
	}

	/**
	 * 결과메시지
	 * 
	 * @param retMsg
	 *            결과메시지
	 */
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
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

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}
}

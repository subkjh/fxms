package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.03.03 14:38
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_PS_STAT_CRE", comment = "성능통계생성테이블")
@FxIndex(name = "FX_PS_STAT_CRE__PK", type = INDEX_TYPE.PK, columns = { "PS_CRE_REQ_NO" })
@FxIndex(name = "FX_PS_STAT_CRE__KEY1", type = INDEX_TYPE.KEY, columns = { "PS_TBL", "PS_DATA_CD", "PS_DTM" })
@FxIndex(name = "FX_PS_STAT_CRE__FK1", type = INDEX_TYPE.FK, columns = {
		"PS_TBL" }, fkTable = "FX_PS_ITEM", fkColumn = "PS_TBL")
public class FX_PS_STAT_CRE {

	public FX_PS_STAT_CRE() {
	}

	public static final String FX_SEQ_PSCREREQNO = "FX_SEQ_PSCREREQNO";
	@FxColumn(name = "PS_CRE_REQ_NO", size = 19, comment = "성능생성요청번호", sequence = "FX_SEQ_PSCREREQNO")
	private long psCreReqNo;

	@FxColumn(name = "PS_TBL", size = 15, comment = "성능테이블")
	private String psTbl;

	@FxColumn(name = "PS_DATA_CD", size = 10, comment = "성능데이터코드")
	private String psDataCd;

	@FxColumn(name = "PS_CRE_REQ_DTM", size = 14, comment = "성능생성요청일시")
	private long psCreReqDtm;

	@FxColumn(name = "CREBL_DTM", size = 14, comment = "생성가능일시")
	private long creblDtm;

	@FxColumn(name = "CRE_ST_CD", size = 1, comment = "생성상태코드", defValue = "W")
	private String creStCd = "W";

	@FxColumn(name = "PS_DTM", size = 14, comment = "성능일시")
	private long psDtm;

	@FxColumn(name = "CRE_STRT_DTM", size = 14, nullable = true, comment = "생성시작일시")
	private long creStrtDtm;

	@FxColumn(name = "CRE_END_DTM", size = 14, nullable = true, comment = "생성종료일시")
	private long creEndDtm;

	@FxColumn(name = "OK_YN", size = 1, nullable = true, comment = "성공여부")
	private String okYn;

	@FxColumn(name = "ROW_SIZE", size = 9, nullable = true, comment = "생성건수")
	private int rowSize;

	@FxColumn(name = "REQ_CNT", size = 9, nullable = true, comment = "요구횟수", defValue = "0")
	private int reqCnt = 0;

	@FxColumn(name = "RET_MSG", size = 1000, nullable = true, comment = "결과메시지")
	private String retMsg;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, comment = "수정일시")
	private long chgDtm;

	/**
	 * 성능생성요청번호
	 * 
	 * @return 성능생성요청번호
	 */
	public long getPsCreReqNo() {
		return psCreReqNo;
	}

	/**
	 * 성능생성요청번호
	 * 
	 * @param psCreReqNo 성능생성요청번호
	 */
	public void setPsCreReqNo(long psCreReqNo) {
		this.psCreReqNo = psCreReqNo;
	}

	/**
	 * 성능테이블
	 * 
	 * @return 성능테이블
	 */
	public String getPsTbl() {
		return psTbl;
	}

	/**
	 * 성능테이블
	 * 
	 * @param psTbl 성능테이블
	 */
	public void setPsTbl(String psTbl) {
		this.psTbl = psTbl;
	}

	/**
	 * 성능데이터코드
	 * 
	 * @return 성능데이터코드
	 */
	public String getPsDataCd() {
		return psDataCd;
	}

	/**
	 * 성능데이터코드
	 * 
	 * @param psDataCd 성능데이터코드
	 */
	public void setPsDataCd(String psDataCd) {
		this.psDataCd = psDataCd;
	}

	/**
	 * 성능생성요청일시
	 * 
	 * @return 성능생성요청일시
	 */
	public long getPsCreReqDtm() {
		return psCreReqDtm;
	}

	/**
	 * 성능생성요청일시
	 * 
	 * @param psCreReqDtm 성능생성요청일시
	 */
	public void setPsCreReqDtm(long psCreReqDtm) {
		this.psCreReqDtm = psCreReqDtm;
	}

	/**
	 * 생성가능일시
	 * 
	 * @return 생성가능일시
	 */
	public long getCreblDtm() {
		return creblDtm;
	}

	/**
	 * 생성가능일시
	 * 
	 * @param creblDtm 생성가능일시
	 */
	public void setCreblDtm(long creblDtm) {
		this.creblDtm = creblDtm;
	}

	/**
	 * 생성상태코드
	 * 
	 * @return 생성상태코드
	 */
	public String getCreStCd() {
		return creStCd;
	}

	/**
	 * 생성상태코드
	 * 
	 * @param creStCd 생성상태코드
	 */
	public void setCreStCd(String creStCd) {
		this.creStCd = creStCd;
	}

	/**
	 * 성능일시
	 * 
	 * @return 성능일시
	 */
	public long getPsDtm() {
		return psDtm;
	}

	/**
	 * 성능일시
	 * 
	 * @param psDtm 성능일시
	 */
	public void setPsDtm(long psDtm) {
		this.psDtm = psDtm;
	}

	/**
	 * 생성시작일시
	 * 
	 * @return 생성시작일시
	 */
	public long getCreStrtDtm() {
		return creStrtDtm;
	}

	/**
	 * 생성시작일시
	 * 
	 * @param creStrtDtm 생성시작일시
	 */
	public void setCreStrtDtm(long creStrtDtm) {
		this.creStrtDtm = creStrtDtm;
	}

	/**
	 * 생성종료일시
	 * 
	 * @return 생성종료일시
	 */
	public long getCreEndDtm() {
		return creEndDtm;
	}

	/**
	 * 생성종료일시
	 * 
	 * @param creEndDtm 생성종료일시
	 */
	public void setCreEndDtm(long creEndDtm) {
		this.creEndDtm = creEndDtm;
	}

	/**
	 * 성공여부
	 * 
	 * @return 성공여부
	 */
	public String isOkYn() {
		return okYn;
	}

	/**
	 * 성공여부
	 * 
	 * @param okYn 성공여부
	 */
	public void setOkYn(String okYn) {
		this.okYn = okYn;
	}

	/**
	 * 생성건수
	 * 
	 * @return 생성건수
	 */
	public int getRowSize() {
		return rowSize;
	}

	/**
	 * 생성건수
	 * 
	 * @param rowSize 생성건수
	 */
	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}

	/**
	 * 요구횟수
	 * 
	 * @return 요구횟수
	 */
	public int getReqCnt() {
		return reqCnt;
	}

	/**
	 * 요구횟수
	 * 
	 * @param reqCnt 요구횟수
	 */
	public void setReqCnt(int reqCnt) {
		this.reqCnt = reqCnt;
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
	 * @param retMsg 결과메시지
	 */
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}
}

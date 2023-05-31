package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * 통계생성 처리 결과를 기록하기 위한 DBO
 * 
 * @since 2022.05.02 18:01
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_PS_STAT_CRE", comment = "성능통계생성테이블")
@FxIndex(name = "FX_PS_STAT_CRE__PK", type = INDEX_TYPE.PK, columns = { "PS_CRE_REQ_NO" })
public class StatMakeUpdateDbo {

	public StatMakeUpdateDbo() {
	}

	@FxColumn(name = "PS_CRE_REQ_NO", size = 19, comment = "성능생성요청번호", sequence = "FX_SEQ_PSCREREQNO")
	private long psCreReqNo;

	@FxColumn(name = "CRE_ST_CD", size = 1, comment = "생성상태코드", defValue = "W")
	private String creStCd = "W";

	@FxColumn(name = "CRE_STRT_DTM", size = 14, nullable = true, comment = "생성시작일시")
	private long creStrtDtm;

	@FxColumn(name = "CRE_END_DTM", size = 14, nullable = true, comment = "생성종료일시")
	private long creEndDtm;

	@FxColumn(name = "OK_YN", size = 1, nullable = true, comment = "성공여부")
	private boolean okYn;

	@FxColumn(name = "ROW_SIZE", size = 9, nullable = true, comment = "생성건수")
	private int rowSize;

	@FxColumn(name = "RET_MSG", size = 1000, nullable = true, comment = "결과메시지")
	private String retMsg;

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
	public boolean isOkYn() {
		return okYn;
	}

	/**
	 * 성공여부
	 * 
	 * @param okYn 성공여부
	 */
	public void setOkYn(boolean okYn) {
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

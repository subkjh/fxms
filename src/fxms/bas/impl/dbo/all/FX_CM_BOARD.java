package fxms.bas.impl.dbo.all;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.24 15:47
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CM_BOARD", comment = "소통게시판테이블")
@FxIndex(name = "FX_CM_BOARD__PK", type = INDEX_TYPE.PK, columns = { "BOARD_NO" })
public class FX_CM_BOARD {

	public FX_CM_BOARD() {
	}

	public static final String FX_SEQ_BOARD_NO = "FX_SEQ_BOARDNO";
	@FxColumn(name = "BOARD_NO", size = 9, comment = "게시판번호", sequence = "FX_SEQ_BOARDNO")
	private int boardNo;

	@FxColumn(name = "BOARD_TITLE", size = 50, comment = "게시판제목")
	private String boardTitle;

	@FxColumn(name = "BOARD_CL_CD", size = 2, comment = "게시판구분코드")
	private String boardClCd;

	@FxColumn(name = "BOARD_CNTS", size = 4000, comment = "게시내용")
	private String boardCnts;

	@FxColumn(name = "TOP_DISP_YN", size = 1, comment = "TOP표시여부", defValue = "N")
	private boolean topDispYn = false;

	@FxColumn(name = "POST_STRT_DATE", size = 8, comment = "게시시작일자")
	private String postStrtDate;

	@FxColumn(name = "POST_FNSH_DATE", size = 8, comment = "게시종료일자")
	private String postFnshDate;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 게시판번호
	 * 
	 * @return 게시판번호
	 */
	public int getBoardNo() {
		return boardNo;
	}

	/**
	 * 게시판번호
	 * 
	 * @param boardNo 게시판번호
	 */
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	/**
	 * 게시판제목
	 * 
	 * @return 게시판제목
	 */
	public String getBoardTitle() {
		return boardTitle;
	}

	/**
	 * 게시판제목
	 * 
	 * @param boardTitle 게시판제목
	 */
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}

	/**
	 * 게시판구분코드
	 * 
	 * @return 게시판구분코드
	 */
	public String getBoardClCd() {
		return boardClCd;
	}

	/**
	 * 게시판구분코드
	 * 
	 * @param boardClCd 게시판구분코드
	 */
	public void setBoardClCd(String boardClCd) {
		this.boardClCd = boardClCd;
	}

	/**
	 * 게시내용
	 * 
	 * @return 게시내용
	 */
	public String getBoardCnts() {
		return boardCnts;
	}

	/**
	 * 게시내용
	 * 
	 * @param boardCnts 게시내용
	 */
	public void setBoardCnts(String boardCnts) {
		this.boardCnts = boardCnts;
	}

	/**
	 * TOP표시여부
	 * 
	 * @return TOP표시여부
	 */
	public boolean isTopDispYn() {
		return topDispYn;
	}

	/**
	 * TOP표시여부
	 * 
	 * @param topDispYn TOP표시여부
	 */
	public void setTopDispYn(boolean topDispYn) {
		this.topDispYn = topDispYn;
	}

	/**
	 * 게시시작일자
	 * 
	 * @return 게시시작일자
	 */
	public String getPostStrtDate() {
		return postStrtDate;
	}

	/**
	 * 게시시작일자
	 * 
	 * @param postStrtDate 게시시작일자
	 */
	public void setPostStrtDate(String postStrtDate) {
		this.postStrtDate = postStrtDate;
	}

	/**
	 * 게시종료일자
	 * 
	 * @return 게시종료일자
	 */
	public String getPostFnshDate() {
		return postFnshDate;
	}

	/**
	 * 게시종료일자
	 * 
	 * @param postFnshDate 게시종료일자
	 */
	public void setPostFnshDate(String postFnshDate) {
		this.postFnshDate = postFnshDate;
	}

	/**
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
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

package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_CM_BOARD", comment = "소통게시판테이블")
@FxIndex(name = "FX_CM_BOARD__PK", type = INDEX_TYPE.PK, columns = { "BOARD_NO" })
public class UpdateBoardCloseDbo {

	@FxColumn(name = "BOARD_NO", size = 9, comment = "게시판번호", sequence = "FX_SEQ_BOARDNO")
	private int boardNo;

	@FxColumn(name = "POST_FNSH_DATE", size = 8, nullable = true, comment = "게시종료일자")
	private String postFnshDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	public int getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	public String getPostFnshDate() {
		return postFnshDate;
	}

	public void setPostFnshDate(String postFnshDate) {
		this.postFnshDate = postFnshDate;
	}

	public int getChgUserNo() {
		return chgUserNo;
	}

	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	public long getChgDtm() {
		return chgDtm;
	}

	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}

}

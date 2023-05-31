package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "FX_CM_BOARD", comment = "소통게시판테이블")
public class SelectBoardGridListDbo {

	@FxColumn(name = "BOARD_CNTS", size = 4000, nullable = true, comment = "게시내용")
	private String boardCnts;

	@FxColumn(name = "POST_STRT_DATE", size = 8, comment = "조회시작일자")
	private String postStrtDate;

	@FxColumn(name = "POST_FNSH_DATE", size = 8, comment = "조회종료일자")
	private String postFnshDate;

	public SelectBoardGridListDbo() {

	}

	public String getBoardCnts() {
		return boardCnts;
	}

	public void setBoardCnts(String boardCnts) {
		this.boardCnts = boardCnts;
	}

	public String getPostStrtDate() {
		return postStrtDate;
	}

	public void setPostStrtDate(String postStrtDate) {
		this.postStrtDate = postStrtDate;
	}

	public String getPostFnshDate() {
		return postFnshDate;
	}

	public void setPostFnshDate(String postFnshDate) {
		this.postFnshDate = postFnshDate;
	}

}

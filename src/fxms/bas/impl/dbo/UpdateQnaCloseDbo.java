package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_CM_QA_QUEST", comment = "소통QA질문테이블")
@FxIndex(name = "FX_CM_QA_QUEST__PK", type = INDEX_TYPE.PK, columns = { "QUEST_NO" })
public class UpdateQnaCloseDbo {

	@FxColumn(name = "QUEST_NO", size = 9, comment = "질문번호", sequence = "FX_SEQ_QUESTNO")
	private int questNo;

	@FxColumn(name = "CLOSE_YN", size = 1, comment = "마감여부")
	private boolean closeYn;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	public int getQuestNo() {
		return questNo;
	}

	public void setQuestNo(int questNo) {
		this.questNo = questNo;
	}

	public boolean isCloseYn() {
		return closeYn;
	}

	public void setCloseYn(boolean closeYn) {
		this.closeYn = closeYn;
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

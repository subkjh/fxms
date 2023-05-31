package fxms.bas.impl.dbo;

import fxms.bas.impl.dbo.all.FX_CM_QA_QUEST;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_CM_QA_ANSWR", comment = "소통QA질문테이블")
@FxIndex(name = "FX_CM_QA_ANSWR_FK", type = INDEX_TYPE.FK, columns = {
		"QUEST_NO" }, fkTable = "FX_CM_QA_QUEST", fkColumn = "QUEST_NO")
public class SelectQnaGridListDbo extends FX_CM_QA_QUEST {

	public static final String FX_SEQ_ANSWRNO = "FX_SEQ_ANSWRNO";
	@FxColumn(name = "ANSWR_NO", size = 9, comment = "답변번호", sequence = "FX_SEQ_ANSWRNO")
	private int answrNo;

	@FxColumn(name = "ANSWR_CNTS", size = 4000, comment = "답변내용")
	private String answrCnts;

	@FxColumn(name = "ANSWR_USER_NO", size = 9, nullable = true, comment = "답변사용자번호")
	private int answrUserNo;

	@FxColumn(name = "ANSWR_NAME", size = 50, comment = "답변자명")
	private String answrName;

	public int getAnswrNo() {
		return answrNo;
	}

	public void setAnswrNo(int answrNo) {
		this.answrNo = answrNo;
	}

	public String getAnswrCnts() {
		return answrCnts;
	}

	public void setAnswrCnts(String answrCnts) {
		this.answrCnts = answrCnts;
	}

	public int getAnswrUserNo() {
		return answrUserNo;
	}

	public void setAnswrUserNo(int answrUserNo) {
		this.answrUserNo = answrUserNo;
	}

	public String getAnswrName() {
		return answrName;
	}

	public void setAnswrName(String answrName) {
		this.answrName = answrName;
	}

}

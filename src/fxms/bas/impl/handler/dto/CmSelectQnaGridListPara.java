package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "VIRTUAL_TABLE", comment = "가상테이블")
public class CmSelectQnaGridListPara {

	@FxColumn(name = "CONTENTS", size = 100, nullable = true, comment = "Q&A 검색 내용")
	private String contents;

	@FxColumn(name = "START_DATE", size = 100, comment = "검색시간시작일자")
	private String startDate;

	@FxColumn(name = "END_DATE", size = 100, comment = "검색시간종료일자")
	private String endDate;

	@FxColumn(name = "QUEST_CL_CD", size = 100, comment = "질문구분코드")
	private String questClCd;

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getQuestClCd() {
		return questClCd;
	}

	public void setQuestClCd(String questClCd) {
		this.questClCd = questClCd;
	}

}

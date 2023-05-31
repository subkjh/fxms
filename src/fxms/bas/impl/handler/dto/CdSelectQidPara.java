package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "VIRTUAL_TABLE", comment = "가상테이블")
public class CdSelectQidPara {

	@FxColumn(name = "QID", size = 100, comment = "QID")
	private String qid;

	@FxColumn(name = "QID_FILE_NAME", size = 100, comment = "QID파일명")
	private String qidFileName;

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getQidFileName() {
		return qidFileName;
	}

	public void setQidFileName(String qidFileName) {
		this.qidFileName = qidFileName;
	}
	
	
}

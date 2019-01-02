package fxms.bas.dbo.user;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_UR_UGRP", comment = "운용자그룹테이블")
@FxIndex(name = "FX_UR_UGRP__PK", type = INDEX_TYPE.PK, columns = { "UGRP_NO" })
public class UserGroupGetDbo {

	@FxColumn(name = "UGRP_NO", size = 9, comment = "운용자그룹번호", sequence = "FX_SEQ_UGRPNO")
	private int ugrpNo;

	@FxColumn(name = "UGRP_NAME", size = 100, comment = "운용자그룹명")
	private String ugrpName;

	public int getUgrpNo() {
		return ugrpNo;
	}

	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	public String getUgrpName() {
		return ugrpName;
	}

	public void setUgrpName(String ugrpName) {
		this.ugrpName = ugrpName;
	}

}

package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UR_UGRP", comment = "사용자그룹테이블")
@FxIndex(name = "FX_UR_UGRP__PK", type = INDEX_TYPE.PK, columns = { "UGRP_NO" })
public class UpdateUserGroupPara {

	@FxColumn(name = "UGRP_NO", size = 9, comment = "사용자그룹번호", sequence = "FX_SEQ_UGRPNO")
	private int ugrpNo;

	@FxColumn(name = "UGRP_NAME", size = 100, comment = "사용자그룹명")
	private String ugrpName;

	@FxColumn(name = "UGRP_DESC", size = 200, nullable = true, comment = "사용자그룹설명")
	private String ugrpDesc;

	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호", defValue = "0")
	private Integer inloNo;

}

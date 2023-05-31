package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "FX_UR_UGRP", comment = "사용자그룹테이블")
public class InsertUserGroupPara {

	@FxColumn(name = "UGRP_NAME", size = 100, comment = "사용자그룹명")
	private String ugrpName;

	@FxColumn(name = "UGRP_DESC", size = 200, nullable = true, comment = "사용자그룹설명")
	private String ugrpDesc;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

}

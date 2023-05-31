package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "FX_UR_USER", comment = "사용자테이블")
public class InsertUserPara {

	@FxColumn(name = "USER_ID", size = 20, comment = "사용자ID")
	private String userId;

	@FxColumn(name = "USER_NAME", size = 50, comment = "사용자명")
	private String userName;

	@FxColumn(name = "USER_PWD", size = 255, comment = "사용자암호")
	private String userPwd;

	@FxColumn(name = "USER_MAIL", size = 100, nullable = true, comment = "사용자메일")
	private String userMail;

	@FxColumn(name = "USER_TEL_NO", size = 50, nullable = true, comment = "사용자전화번호")
	private String userTelNo;

	@FxColumn(name = "UGRP_NO", size = 9, comment = "사용자그룹번호", defValue = "0")
	private int ugrpNo = 0;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;
}

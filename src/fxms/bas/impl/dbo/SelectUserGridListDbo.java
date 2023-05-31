package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UR_USER", comment = "사용자테이블")
@FxIndex(name = "FX_UR_USER__PK", type = INDEX_TYPE.PK, columns = { "USER_NO" })
@FxIndex(name = "FX_UR_USER__FK_GRP", type = INDEX_TYPE.FK, columns = {
		"UGRP_NO" }, fkTable = "FX_UR_UGRP", fkColumn = "UGRP_NO")
public class SelectUserGridListDbo {

	@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo;

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

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private boolean useYn = true;

	@FxColumn(name = "USE_STRT_DATE", size = 8, nullable = true, comment = "사용시작일자", defValue = "20000101")
	private int useStrtDate = 20000101;

	@FxColumn(name = "USE_END_DATE", size = 8, nullable = true, comment = "사용종료일자", defValue = "39991231")
	private int useEndDate = 39991231;

	@FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접속NETWORK")
	private String accsNetwork;

	@FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접속NETMASK")
	private String accsNetmask;

}

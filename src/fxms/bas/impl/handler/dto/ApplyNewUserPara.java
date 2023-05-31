package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "FX_UR_USER_NEW_REQ", comment = "사용자신규요청테이블")
public class ApplyNewUserPara {

	@FxColumn(name = "APPLY_USER_ID", size = 20, comment = "신청사용자ID")
	private String applyUserId;

	@FxColumn(name = "APPLY_USER_NAME", size = 50, comment = "신청사용자명")
	private String applyUserName;

	@FxColumn(name = "APPLY_USER_MAIL", size = 100, comment = "신청사용자 메일")
	private String applyUserMail;

	@FxColumn(name = "APPLY_TEL_NO", size = 50, comment = "신청전화번호")
	private String applyTelNo;

	@FxColumn(name = "APPLY_INLO_NO", size = 9, comment = "신청설치위치번호")
	private int applyInloNo;

	@FxColumn(name = "USE_STRT_DATE", size = 8, nullable = true, comment = "사용시작일자", defValue = "20000101")
	private int useStrtDate = 20000101;

	@FxColumn(name = "USE_END_DATE", size = 8, nullable = true, comment = "사용종료일자", defValue = "39991231")
	private int useEndDate = 39991231;

	@FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접근NETWORK")
	private String accsNetwork;

	@FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접근NETMASK")
	private String accsNetmask;

}

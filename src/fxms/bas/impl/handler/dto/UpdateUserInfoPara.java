package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UR_USER", comment = "사용자테이블")
@FxIndex(name = "FX_UR_USER__PK", type = INDEX_TYPE.PK, columns = { "USER_NO" })
public class UpdateUserInfoPara {

	@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo;

	@FxColumn(name = "USER_NAME", size = 50, comment = "사용자명")
	private String userName;

	@FxColumn(name = "USER_MAIL", size = 100, nullable = true, comment = "사용자메일")
	private String userMail;

	@FxColumn(name = "USER_TEL_NO", size = 50, nullable = true, comment = "사용자전화번호")
	private String userTelNo;

	@FxColumn(name = "ACCS_NETWORK", size = 39, nullable = true, comment = "접속NETWORK")
	private String accsNetwork;

	@FxColumn(name = "ACCS_NETMASK", size = 39, nullable = true, comment = "접속NETMASK")
	private String accsNetmask;

}

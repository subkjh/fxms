package fxms.bas.impl.vo;

import subkjh.dao.def.FxColumn;

public class InloVo {

	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호", sequence = "FX_SEQ_INLONO")
	private int inloNo;

	@FxColumn(name = "UPPER_INLO_NO", size = 9, comment = "상위설치위치번호", defValue = "0")
	private int upperInloNo = 0;

	@FxColumn(name = "INLO_NAME", size = 100, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "INLO_ALL_NAME", size = 200, nullable = true, comment = "설치위치전체명")
	private String inloAllName;

	@FxColumn(name = "INLO_CL_CD", size = 30, comment = "설치위치분류코드")
	private String inloClCd;

	@FxColumn(name = "LTD", size = 15, nullable = true, comment = "위도")
	private double ltd;

	@FxColumn(name = "LND", size = 15, nullable = true, comment = "경도")
	private double lnd;
}

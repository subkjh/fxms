package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.PsApi;
import fxms.bas.vo.PsKind;
import subkjh.dao.def.Column;

public class PsDpo {

	public static final Column MO_NO = new Column("MO_NO", "Number", 19, 0, false, "관리대상번호");
	public static final Column MO_INSTANCE = new Column("MO_INSTANCE", "Varchar2", 100, 0, true, "인스턴스");
	public static final Column PS_DATE = new Column("PS_DATE", "Number", 14, 0, false, "통계일시");
	public static final Column DATA_COUNT = new Column("DATA_COUNT", "Number", 9, 0, false, "처리건수");
	public static final Column INS_DATE = new Column("INS_DATE", "Number", 14, 0, true, "생성일시");

	protected List<PsKind> getPsKinds() {
		List<PsKind> list = new ArrayList<>();
		for (PsKind psKind : PsApi.getApi().getPsKindList()) {
			list.add(psKind);
		}
		return list;
	}
}

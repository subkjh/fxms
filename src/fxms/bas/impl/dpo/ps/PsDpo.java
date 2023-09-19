package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.PsApi;
import fxms.bas.vo.PsKind;
import subkjh.dao.def.Column;

public class PsDpo {

	public static final Column MO_NO = new Column("MO_NO", "Number", 12, 0, false, "ManagedObject No.");
	public static final Column PS_DATE = new Column("PS_DATE", "Number", 14, 0, false, "Collection Date&Time");
	public static final Column DATA_COUNT = new Column("DATA_COUNT", "Number", 9, 0, false, "Data Count");
	public static final Column INS_DATE = new Column("INS_DATE", "Number", 14, 0, true, "Input Date&Time");

	protected List<PsKind> getPsKinds() {
		List<PsKind> list = new ArrayList<>();
		for (PsKind psKind : PsApi.getApi().getPsKindList()) {
			list.add(psKind);
		}
		return list;
	}
}

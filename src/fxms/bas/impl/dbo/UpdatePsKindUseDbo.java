package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_PS_STAT_KIND", comment = "성능통계종류테이블")
@FxIndex(name = "FX_PS_STAT_KIND__PK", type = INDEX_TYPE.PK, columns = { "PS_DATA_NAME" })
public class UpdatePsKindUseDbo {

	@FxColumn(name = "PS_DATA_NAME", size = 50, comment = "성능데이터명")
	private String psDataName;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private String useYn = "Y";

	@FxColumn(name = "CHG_USER_NO", size = 9, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, comment = "수정일시")
	private long chgDtm;

	public UpdatePsKindUseDbo(String psDataName, boolean use) {
		this.psDataName = psDataName;
		this.useYn = use ? "Y" : "N";
	}

	public String getPsDataName() {
		return psDataName;
	}

	public void setPsDataName(String psDataName) {
		this.psDataName = psDataName;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public int getChgUserNo() {
		return chgUserNo;
	}

	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	public long getChgDtm() {
		return chgDtm;
	}

	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}
	
	
}

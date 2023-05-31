package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_PS_ITEM", comment = "성능항목테이블")
@FxIndex(name = "FX_PS_ITEM__PK", type = INDEX_TYPE.PK, columns = { "PS_ID" })
public class UpdatePsItemUseDbo {

	@FxColumn(name = "PS_ID", size = 20, comment = "성능ID")
	private String psId;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private String useYn = "Y";

	@FxColumn(name = "CHG_USER_NO", size = 9, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, comment = "수정일시")
	private long chgDtm;

	public UpdatePsItemUseDbo(String psId, boolean use) {
		this.psId = psId;
		this.useYn = use ? "Y" : "N";
	}

	public String getPsId() {
		return psId;
	}

	public void setPsId(String psId) {
		this.psId = psId;
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

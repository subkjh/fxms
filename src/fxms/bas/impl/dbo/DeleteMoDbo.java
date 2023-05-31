package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * 관리대상을 삭제하는 MO<br>
 * 실제 삭제하지 않고 DEL_YN = 'Y'만 설정한다. 
 * 
 * @author subkjh
 *
 */
@FxTable(name = "FX_MO", comment = "관리대상테이블")
@FxIndex(name = "FX_MO__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
public class DeleteMoDbo {

	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호", sequence = "FX_SEQ_MONO")
	private long moNo;

	@FxColumn(name = "DEL_YN", size = 1, comment = "삭제여부")
	private String delYn = "Y";

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	public DeleteMoDbo() {

	}

	public long getMoNo() {
		return moNo;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
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

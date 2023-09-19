package fxms.bas.impl.dbo.all;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.02 18:01
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_V_xxx_RAW", comment = "성능수집원시값테이블")
@FxIndex(name = "FX_V_xxx_RAW__PK", type = INDEX_TYPE.PK, columns = { "PS_DTM", "MO_NO", "MO_INSTANCE" })
public class FX_V_xxx_RAW 	 {

	public FX_V_xxx_RAW() {
	}

	@FxColumn(name = "PS_DTM", size = 14, comment = "수집일시")
	private long psDtm;

	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
	private long moNo;

	@FxColumn(name = "XXX", size = 19, nullable = true, comment = "성능수집값")
	private double xxx;

	/**
	 * 수집일시
	 * 
	 * @return 수집일시
	 */
	public long getPsDtm() {
		return psDtm;
	}

	/**
	 * 수집일시
	 * 
	 * @param psDtm 수집일시
	 */
	public void setPsDtm(long psDtm) {
		this.psDtm = psDtm;
	}

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo MO번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * 성능수집값
	 * 
	 * @return 성능수집값
	 */
	public double getXxx() {
		return xxx;
	}

	/**
	 * 성능수집값
	 * 
	 * @param xxx 성능수집값
	 */
	public void setXxx(double xxx) {
		this.xxx = xxx;
	}
}

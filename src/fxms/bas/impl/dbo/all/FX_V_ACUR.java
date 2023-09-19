package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.12.13 16:06
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_V_ACUR", comment = "성능수집최신값")
@FxIndex(name = "FX_V_ACUR__PK", type = INDEX_TYPE.PK, columns = { "MO_NO", "PS_ID" })
public class FX_V_ACUR  {

	public FX_V_ACUR() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
	private long moNo;

	@FxColumn(name = "PS_ID", size = 50, comment = "성능ID")
	private String psId;

	@FxColumn(name = "PRE_COLL_VAL", size = 19, nullable = true, comment = "이전수집값")
	private Double preCollVal;

	@FxColumn(name = "CUR_COLL_VAL", size = 19, nullable = true, comment = "최근수집값")
	private Double curCollVal;

	@FxColumn(name = "PRE_COLL_DTM", size = 14, nullable = true, comment = "이전수집일시")
	private Long preCollDtm;

	@FxColumn(name = "CUR_COLL_DTM", size = 14, nullable = true, comment = "최근수집일시")
	private Long curCollDtm;

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
	 * 성능ID
	 * 
	 * @return 성능ID
	 */
	public String getPsId() {
		return psId;
	}

	/**
	 * 성능ID
	 * 
	 * @param psId 성능ID
	 */
	public void setPsId(String psId) {
		this.psId = psId;
	}

	/**
	 * 이전수집값
	 * 
	 * @return 이전수집값
	 */
	public Double getPreCollVal() {
		return preCollVal;
	}

	/**
	 * 이전수집값
	 * 
	 * @param preCollVal 이전수집값
	 */
	public void setPreCollVal(Double preCollVal) {
		this.preCollVal = preCollVal;
	}

	/**
	 * 최근수집값
	 * 
	 * @return 최근수집값
	 */
	public Double getCurCollVal() {
		return curCollVal;
	}

	/**
	 * 최근수집값
	 * 
	 * @param curCollVal 최근수집값
	 */
	public void setCurCollVal(Double curCollVal) {
		this.curCollVal = curCollVal;
	}

	/**
	 * 이전수집일시
	 * 
	 * @return 이전수집일시
	 */
	public Long getPreCollDtm() {
		return preCollDtm;
	}

	/**
	 * 이전수집일시
	 * 
	 * @param preCollDtm 이전수집일시
	 */
	public void setPreCollDtm(Long preCollDtm) {
		this.preCollDtm = preCollDtm;
	}

	/**
	 * 최근수집일시
	 * 
	 * @return 최근수집일시
	 */
	public Long getCurCollDtm() {
		return curCollDtm;
	}

	/**
	 * 최근수집일시
	 * 
	 * @param curCollDtm 최근수집일시
	 */
	public void setCurCollDtm(Long curCollDtm) {
		this.curCollDtm = curCollDtm;
	}
}

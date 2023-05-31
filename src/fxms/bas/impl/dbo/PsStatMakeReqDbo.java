package fxms.bas.impl.dbo;

import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * 성능을 다시 만들어 줄 것을 요청하는 내용
 * 
 * @author subkjh
 * 
 */
@FxTable(name = "FX_PS_STAT_CRE", comment = "성능통계생성테이블")
@FxIndex(name = "FX_PS_STAT_CRE__PK", type = INDEX_TYPE.PK, columns = { "PS_CRE_REQ_NO" })
public class PsStatMakeReqDbo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6159914238055807437L;

	@FxColumn(name = "PS_CRE_REQ_NO", size = 19, comment = "성능생성요청번호", sequence = "FX_SEQ_PSCREREQNO")
	private Long psCreReqNo;

	@FxColumn(name = "PS_TBL", size = 15, comment = "성능테이블")
	private String psTbl;

	@FxColumn(name = "PS_DATA_CD", size = 10, comment = "성능데이터코드")
	private String psDataCd;

	@FxColumn(name = "PS_DTM", size = 14, comment = "성능일시")
	private Long psDtm;

	public PsStatMakeReqDbo() {
	}

	public PsStatMakeReqDbo(String psTbl, String psDataCd, long psDtm) {
		this.psTbl = psTbl;
		this.psDataCd = psDataCd;
		this.psDtm = psDtm;
	}

	public String getKey() {
		return getPsTbl() + "." + getPsDataCd() + "." + getPsDtm();
	}

	public Long getPsCreReqNo() {
		return psCreReqNo;
	}

	public void setPsCreReqNo(Long psCreReqNo) {
		this.psCreReqNo = psCreReqNo;
	}

	public String getPsTbl() {
		return psTbl;
	}

	public void setPsTbl(String psTbl) {
		this.psTbl = psTbl;
	}

	public String getPsDataCd() {
		return psDataCd;
	}

	public void setPsDataCd(String psDataCd) {
		this.psDataCd = psDataCd;
	}

	public Long getPsDtm() {
		return psDtm;
	}

	public void setPsDtm(Long psDtm) {
		this.psDtm = psDtm;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("PS(");
		sb.append("TABLE(" + psTbl + ")");
		sb.append("TYPE(" + psDataCd + ")");
		sb.append("DATE(" + psDtm + ")");
		sb.append(")");

		return sb.toString();
	}

}

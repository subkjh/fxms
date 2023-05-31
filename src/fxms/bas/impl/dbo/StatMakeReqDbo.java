package fxms.bas.impl.dbo;

import fxms.bas.co.CoCode.CRE_ST_CD;
import fxms.bas.impl.dbo.all.FX_PS_STAT_CRE;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.def.FxOrder;

/**
 * 통계 생성 요청 DBO
 * 
 * @author subkjh
 *
 */
@FxOrder(columns = { "PS_DATE" })
public class StatMakeReqDbo extends FX_PS_STAT_CRE {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4912150934970958770L;

	public StatMakeReqDbo() {

	}

	public String getKey() {
		return getPsTbl() + "." + getPsDataCd() + "." + getPsDtm();
	}

	public StatMakeReqDbo(String psTable, String psDataCd, long psDate) {
		setPsDtm(psDate);
		setPsTbl(psTable);
		setPsDataCd(psDataCd);
		setPsCreReqDtm(DateUtil.getDtm());
		setCreStCd(CRE_ST_CD.Ready.getCode());
	}
}

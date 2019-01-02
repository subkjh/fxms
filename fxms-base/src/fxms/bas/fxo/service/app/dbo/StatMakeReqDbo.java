package fxms.bas.fxo.service.app.dbo;

import fxms.bas.api.FxApi;
import fxms.bas.dbo.FX_PS_STREQ_CUR;
import subkjh.bas.fxdao.define.FxOrder;

@FxOrder(columns = { "PS_DATE" })
public class StatMakeReqDbo extends FX_PS_STREQ_CUR {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4912150934970958770L;

	public static final String STATE_READY = "R";
	public static final String STATE_PROCESS = "P";
	public static final String STATE_COMPLETED = "C";

	public StatMakeReqDbo() {

	}

	public String getKey() {
		return getPsTable() + "." + getPsType() + "." + getPsDate();
	}

	public StatMakeReqDbo(String psTable, String psType, long psDate) {
		setPsDate(psDate);
		setPsTable(psTable);
		setPsType(psType);
		setRegDate(FxApi.getDate(0));
	}
}

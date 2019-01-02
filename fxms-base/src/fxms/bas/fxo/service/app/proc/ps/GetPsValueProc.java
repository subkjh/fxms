package fxms.bas.fxo.service.app.proc.ps;

import java.util.List;

import fxms.bas.api.MoApi;
import fxms.bas.define.PS_TYPE;
import fxms.bas.mo.Mo;
import fxms.bas.mo.exception.MoNotFoundException;
import fxms.bas.pso.TimeSeriesVo;
import fxms.bas.pso.ValueApi;
import subkjh.bas.user.UserProc;

public class GetPsValueProc extends UserProc<List<TimeSeriesVo>> {

	private Mo mo;
	private String psCode;
	private PS_TYPE pstype;
	private long startDate;
	private long endDate;

	public GetPsValueProc(long moNo, String psCode, String psType, long startDate, long endDate) throws Exception {
		mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}

		this.psCode = psCode;
		this.pstype = PS_TYPE.getPsType(psType);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	protected long getMoNo() {
		return mo.getMoNo();
	}

	@Override
	protected String getInPara() {
		return null;
	}

	@Override
	protected String getOutRet() {
		return null;
	}

	@Override
	protected List<TimeSeriesVo> process() throws Exception {
		return ValueApi.getApi().getValueList(mo.getMoNo(), psCode, pstype, startDate, endDate);
	}

}

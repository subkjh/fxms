package fxms.bas.fxo.service.app.proc.mo;

import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.mo.Mo;
import subkjh.bas.user.UserProc;

public class GetMoListProc extends UserProc<List<Mo>> {

	private Class<? extends Mo> classOfMo;
	private Map<String, Object> para;

	public GetMoListProc(Class<? extends Mo> classOfMo, Map<String, Object> para) {
		this.classOfMo = classOfMo;
		this.para = para;
	}

	@Override
	protected String getInPara() {
		return null;
	}

	@Override
	protected String getOutRet() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Mo> process() throws Exception {

		return (List<Mo>) MoApi.getApi().getMoList(classOfMo, para);

	}

}

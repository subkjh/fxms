package fxms.module.restapi.handler;

import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.define.PS_TYPE;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.exception.MoNotFoundException;
import fxms.bas.pso.ValueApi;
import fxms.module.restapi.vo.SessionVo;

public class PsHandler extends CommHandler {

	private PS_TYPE getPsType(Map<String, Object> parameters, String name) throws Exception {
		String psType = getString(parameters, "psType");
		PS_TYPE ret = PS_TYPE.getPsType(psType);
		if (ret == null) {
			throw new Exception(name + " is invalid value");
		}
		return ret;
	}

	public Object getPsList(SessionVo session, Map<String, Object> parameters) throws Exception {

		long moNo = getLong(parameters, "moNo");
		String psCode = getString(parameters, "psCode");
		PS_TYPE psType = getPsType(parameters, "psType");
		long startDate = getLong(parameters, "startDate");
		long endDate = getLong(parameters, "endDate");

		return ValueApi.getApi().getValueList(moNo, psCode, psType, startDate, endDate);
	}

	public Object getPsItemList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return PsApi.getApi().getPsItems();
	}

	public Object getPsRealList(SessionVo session, Map<String, Object> parameters) throws Exception {

		long moNo = getLong(parameters, "moNo");
		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}

		String psCode = getString(parameters, "psCode");

		MoService service = ServiceApi.getApi().getService(MoService.class, mo);
		return service.getValue(moNo, psCode.split(","));

	}
}

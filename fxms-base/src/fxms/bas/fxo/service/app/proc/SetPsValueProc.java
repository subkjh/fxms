package fxms.bas.fxo.service.app.proc;

import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.exception.MoNotFoundException;
import subkjh.bas.lang.Lang;
import subkjh.bas.log.Logger;
import subkjh.bas.user.UserProc;

public class SetPsValueProc extends UserProc<Mo> {

	private Mo mo;
	private Map<String, Object> para;

	public SetPsValueProc(long moNo, Map<String, Object> para) throws Exception {
		mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}

		this.para = para;
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
	protected Mo process() throws Exception {
		try {
			MoService moService = ServiceApi.getApi().getService(MoService.class, mo);
			moService.setValue(mo, "test", para);
			return mo;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw new Exception(Lang.get("설정중 오류가 발생하였습니다.") + " : " + e.getMessage());
		}
	}

}

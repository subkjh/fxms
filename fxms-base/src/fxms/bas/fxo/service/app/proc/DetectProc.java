package fxms.bas.fxo.service.app.proc;

import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.exception.MoNotFoundException;
import subkjh.bas.lang.Lang;
import subkjh.bas.log.Logger;
import subkjh.bas.user.UserProc;

public class DetectProc extends UserProc<Mo> {

	private Mo mo;

	public DetectProc(long moNo) throws Exception {
		mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}
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
			return moService.getConfig(mo.getMoNo());
		} catch (Exception e) {
			Logger.logger.error(e);
			throw new Exception(Lang.get("구성 수집중 오류가 발생하였습니다."));
		}
	}

}

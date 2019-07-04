package fxms.bas.impl.handler.func;

import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.module.restapi.vo.SessionVo;

public class MoAdd {

	public Object handle(SessionVo session, Map<String, Object> parameters, String moClass) throws Exception {

		Class<? extends Mo> classOfMo = MoApi.getApi().getMoClass(moClass);

		Mo mo = (Mo) classOfMo.newInstance();
		ObjectUtil.toObject(parameters, mo);
		mo.getProperties().putAll(parameters);


		Mo moAdded = MoApi.getApi().addMo(mo, "user-add", session.getUserNo());

		try {
			MoService moService = ServiceApi.getApi().getService(MoService.class, mo);
			if (moService == null) {
			} else {
				moService.requestSync(moAdded);
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		if (FxServiceImpl.fxService != null) {
			moAdded.setStatus(FxEvent.STATUS.added);
			FxServiceImpl.fxService.send(moAdded);
		}

		return moAdded;
	}
}

package fxms.module.restapi.handler.func;

import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.noti.FxEvent;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class MoAdd {

	public Object handle(SessionVo session, Map<String, Object> parameters, String moClass) throws Exception {

		Class<? extends Mo> classOfMo = MoApi.getApi().getMoClass(moClass);

		Mo mo = classOfMo.newInstance();
		ObjectUtil.toObject(parameters, mo);
		mo.getProperties().putAll(parameters);

		mo.setRegUserNo(session.getUserNo());
		mo.setChgUserNo(session.getUserNo());

		Mo moAdded = MoApi.getApi().addMo(mo, "user-add");

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

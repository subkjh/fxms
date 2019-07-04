package fxms.bas.impl.handler.func;

import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.exp.MoNotFoundException;
import fxms.module.restapi.vo.SessionVo;

public class MoUpdate {
	public Object handle(SessionVo session, Map<String, Object> parameters, long moNo) throws Exception {

		try {

			Mo mo = MoApi.getApi().getMo(moNo);

			if (mo == null) {
				throw new MoNotFoundException(moNo);
			}

			parameters.put("chgUserNo", session.getUserNo());
			parameters.put("chgDate", FxApi.getDate(0));

			MoApi.getApi().updateMo(mo, parameters);

			try {
				MoService moService = ServiceApi.getApi().getService(MoService.class, mo);
				if (moService != null) {
					moService.requestSync(mo);
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}

			ObjectUtil.toObject(parameters, mo);

			if (FxServiceImpl.fxService != null) {
				mo.setStatus(FxEvent.STATUS.changed);
				FxServiceImpl.fxService.send(mo);
			}

			return mo;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}
}

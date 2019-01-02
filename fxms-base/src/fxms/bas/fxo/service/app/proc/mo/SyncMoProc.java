package fxms.bas.fxo.service.app.proc.mo;

import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exception.MoNotFoundException;
import fxms.bas.noti.FxEvent;
import subkjh.bas.lang.Lang;
import subkjh.bas.log.Logger;
import subkjh.bas.user.UserProc;

public class SyncMoProc extends UserProc<List<Mo>> {

	private Mo mo;
	private boolean save;

	public SyncMoProc(long moNo, boolean save) throws Exception {
		mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}
		this.save = save;
	}

	@Override
	protected String getInPara() {
		return mo.getMoNo() + "";
	}

	@Override
	protected String getOutRet() {
		return null;
	}

	@Override
	protected List<Mo> process() throws Exception {

		MoService moService = ServiceApi.getApi().getService(MoService.class, mo);

		if (moService == null) {

			throw new Exception(Lang.get("지금은 {}의 구성정보를 수집할 수 없습니다.", mo));

		}

		mo.setSyncDate(FxApi.getDate(0));
		mo.setSyncUserNo(getUser().getUserNo());

		MoConfig children = MoApi.getApi().getMoConfig(mo);

		children = moService.getConfigChildren(children);

		if (save) {
			if (children != null && children.sizeAll() > 0) {
				MoApi.getApi().setMoChildren(children);
			}
		}

		if (FxServiceImpl.fxService != null) {
			mo.setStatus(FxEvent.STATUS.changed);
			try {
				FxServiceImpl.fxService.send(mo);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		Logger.logger.debug(children.getDebug());

		return (List<Mo>) children.getMoListAll();

	}

}
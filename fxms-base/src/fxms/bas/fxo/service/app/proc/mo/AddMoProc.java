package fxms.bas.fxo.service.app.proc.mo;

import java.util.Map;

import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.noti.FxEvent;
import subkjh.bas.log.Logger;
import subkjh.bas.user.UserProc;
import subkjh.bas.utils.ObjectUtil;

public class AddMoProc extends UserProc<Mo> {

	private Map<String, Object> para;
	private String moClass;

	public AddMoProc(String moClass, Map<String, Object> para) throws Exception {
		this.para = para;
		this.moClass = moClass;

		if (moClass == null)
			throw new Exception("moClass is empty");
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

		Class<?> classOfMo = MoApi.getApi().getMoClass(moClass.toString());

		Mo mo = (Mo) classOfMo.newInstance();

		ObjectUtil.toObject(para, mo);

		// first, add mo
		
		if( mo.getAlarmCfgNo() <= 0) {
			mo.setAlarmCfgNo(EventApi.getApi().getDefaultAlarmCfg(mo.getMoClass()));
		}

		mo.setRegUserNo(getUser().getUserNo());
		mo.setRegDate(FxApi.getDate(0));
		mo = MoApi.getApi().addMo(mo, "user-add");

		// second, find children

		MoService moService = ServiceApi.getApi().getService(MoService.class, mo);

		if (moService == null) {

			Logger.logger.info("{}의 담당 MoService가 없어 구성정보 수집을 생략합니다.", mo);

		} else {

			mo.setSyncDate(FxApi.getDate(0));
			mo.setSyncUserNo(getUser().getUserNo());

			MoConfig children = new MoConfig(mo);

			children = moService.getConfigChildren(children);

			if (children != null && children.sizeAll() > 0) {
				MoApi.getApi().setMoChildren(children);
			}
		}

		if (FxServiceImpl.fxService != null) {
			mo.setStatus(FxEvent.STATUS.added);
			try {
				FxServiceImpl.fxService.send(mo);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return mo;

	}

}

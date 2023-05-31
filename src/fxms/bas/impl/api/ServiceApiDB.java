package fxms.bas.impl.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.ServiceApi;
import fxms.bas.co.CoCode.FXSVC_ST_CD;
import fxms.bas.co.FxConfDao;
import fxms.bas.impl.dbo.FxServiceMoUpdateStatusAllDbo;
import fxms.bas.impl.dbo.FxServiceMoUpdateStatusDbo;
import fxms.bas.impl.dpo.mo.GetMoListDfo;
import fxms.bas.mo.FxServiceMo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

public class ServiceApiDB extends ServiceApi {

	@Override
	protected List<FxServiceMo> doServiceList(String projName) throws Exception {

		Map<String, Object> para = new HashMap<>();

		para.put("moClass", FxServiceMo.MO_CLASS);
		para.put("mngDiv", projName);
		para.put("useYn", "Y");

		List<FxServiceMo> list = new GetMoListDfo().selectMoList(para, FxServiceMo.class);

		Logger.logger.debug("{}", list);
		return list;
	}

	@Override
	protected void doSetAllServiceStatus(FXSVC_ST_CD serviceStatus) throws Exception {

		FxServiceMoUpdateStatusAllDbo dbo = new FxServiceMoUpdateStatusAllDbo(BasCfg.getIpAddress(), serviceStatus,
				DateUtil.getDtm());
		FxConfDao.getDao().updateOfClass(FxServiceMoUpdateStatusAllDbo.class, dbo);
	}

	@Override
	protected void doUpdateServiceStatus(String fxsvrIpAddr, String fxsvcName, long startDate, FXSVC_ST_CD fxsvcStCd,
			int rmiPort, int fxsvcPort) throws Exception {

		Logger.logger.trace("{}, {}, {}, {}", fxsvrIpAddr, fxsvcName, startDate, fxsvcStCd);

		try {

			FxServiceMo fxService = getServiceMo(fxsvrIpAddr, fxsvcName);

			if (fxService != null) {

				if (startDate != fxService.getStrtDtm() || !fxsvcStCd.name().equals(fxService.getFxsvcStCd())) {

					FxServiceMoUpdateStatusDbo data = new FxServiceMoUpdateStatusDbo(fxService.getMoNo(), startDate,
							fxsvcStCd, DateUtil.getDtm(), rmiPort, fxsvcPort);

					FxConfDao.getDao().updateOfClass(data.getClass(), data);

					fxService.setFxsvcStCd(fxsvcStCd.name());
					fxService.setStrtDtm(startDate);

					// 이 클래스에서는 다른 API를 사용하지 않는다.
//					EventApi.getApi().fireAlarm(fxService, DateUtil.getDtm(), ALARM_CODE.SERVICE_ON_NOTI.getAlcdNo(), null,							fxsvcStCd.name(), null, null);
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

}

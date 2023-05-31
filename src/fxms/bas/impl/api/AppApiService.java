package fxms.bas.impl.api;

import java.util.List;

import fxms.bas.api.AppApi;
import fxms.bas.api.FxApiServiceTag;
import fxms.bas.api.ServiceApi;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.service.AppService;
import fxms.bas.vo.PsStatReqVo;

/**
 * AppApi를 AppService를 이용하여 구현한 API
 * 
 * @author subkjh
 *
 */
public class AppApiService extends AppApi implements FxApiServiceTag {

	private AppService service;

	@Override
	public String checkStorage(String memo) throws Exception {
		AppService svc = getService();
		return svc.checkStorage(memo);
	}

	@Override
	public boolean requestMakeStat(List<PsStatReqVo> reqList) throws Exception {
		AppService svc = getService();
		return svc.requestMakeStat(reqList);
	}

	@Override
	public boolean responseMakeStat(PsStatReqVo req) throws Exception {
		AppService svc = getService();
		return svc.responseMakeStat(req);
	}

	private AppService getService() throws FxServiceNotFoundException, Exception {

		if (this.service == null) {
			this.service = ServiceApi.getApi().getService(AppService.class);
		}

		// ping을 해봐서 끊겼는지 확인한다.
		try {
			this.service.ping(getName());
		} catch (Exception e) {
			this.service = ServiceApi.getApi().getService(AppService.class);
		}

		return this.service;

	}

	@Override
	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws Exception {
		AppService svc = getService();
		return svc.generateStatistics(psTbl, psKindName, psDtm);
	}

}

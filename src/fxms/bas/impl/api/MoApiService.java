package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApiServiceTag;
import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.SyncMo;

/**
 * MoService를 이용하여 데이터를 가져오는 API
 * 
 * @author subkjh
 *
 */
public class MoApiService extends MoApi implements FxApiServiceTag {

	private MoService moService = null;

	public MoApiService() {
		try {
			setName(FxServiceImpl.serviceName + ":MoApi");
		} catch (Exception e) {
			setName("MoApiService");
		}
	}

	private synchronized MoService getMoService() throws FxServiceNotFoundException, Exception {

		if (this.moService == null) {
			this.moService = ServiceApi.getApi().getService(MoService.class);
		}

		// ping을 해봐서 끊겼는지 확인한다.
		try {
			this.moService.ping(getName());
		} catch (Exception e) {
			this.moService = ServiceApi.getApi().getService(MoService.class);
		}

		return this.moService;
	}

	@Override
	public Mo addMo(int userNo, String moClass, Map<String, Object> para, String reason, boolean broadcast)
			throws Exception {
		return getMoService().addMo(userNo, moClass, para, reason, broadcast);
	}

	@Override
	public Mo deleteMo(int userNo, long moNo, String reason, boolean broadcast) throws Exception {
		return getMoService().deleteMo(userNo, moNo, reason, broadcast);
	}

	@Override
	public Mo getMo(long moNo) throws MoNotFoundException, Exception {

		Mo mo = getMoCached(moNo);

		if (mo == null) {
			mo = getMoService().getMo(moNo);
			if (mo == null) {
				mo = NullMo;
			}
			setMoCached(moNo, mo);
		}

		if (mo.getMoNo() != NullMo.getMoNo()) {
			return mo;
		}

		throw new MoNotFoundException(moNo);

	}

	@Override
	public List<Mo> getMoList(Map<String, Object> para) throws Exception {
		return getMoService().getMoList(para);
	}

	@Override
	public <T extends Mo> List<T> getMoList(Map<String, Object> para, Class<T> classOfMo) throws Exception {
		return getMoService().getMoList(para, classOfMo);
	}

	@Override
	public Mo updateMo(int userNo, long moNo, Map<String, Object> para, boolean broadcast) throws Exception {
		return getMoService().updateMo(userNo, moNo, para, broadcast);
	}

	@Override
	public List<PsVoRaw> getRtValues(long moNo) throws Exception {
		return getMoService().getRtValues(moNo);
	}

	@Override
	public boolean setupMo(long moNo, String method, Map<String, Object> datas) throws Exception {
		return getMoService().setupMo(moNo, method, datas);
	}

	@Override
	public SyncMo sync(long moNo, boolean now, boolean update) throws Exception {
		return getMoService().sync(moNo, now, update);
	}

}

package fxms.nms.fxactor;

import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.mo.Mo;
import fxms.bas.poller.PollerMgr;
import fxms.nms.mo.NeIfMo;
import fxms.nms.mo.NeMo;

public class SnmpPollerMgr extends PollerMgr<NeMo> {

	public SnmpPollerMgr() {
		super(NeMo.class, SnmpPoller.class);
	}


	@Override
	protected List<NeMo> loadMoList(Class<NeMo> classOfMo, long moNo, Map<String, Object> parameters) {
		List<NeMo> list;
		List<NeIfMo> ifList;

		if (moNo > 0) {
			Mo mo = MoApi.getApi().getMo(moNo);
			if (mo.getUpperMoNo() > 0) {
				parameters.put("moNo", mo.getUpperMoNo());
				list = getPsApi().getMoAll(NeMo.class, parameters);
				parameters.remove("moNo");
				parameters.put("upperMoNo", mo.getUpperMoNo());
				ifList = getPsApi().getMoAll(NeIfMo.class, parameters);
			} else {
				parameters.put("moNo", moNo);
				list = getPsApi().getMoAll(NeMo.class, parameters);
				ifList = getPsApi().getMoAll(NeIfMo.class, parameters);
			}

		} else {
			list = getPsApi().getMoAll(NeMo.class, parameters);
			ifList = getPsApi().getMoAll(NeIfMo.class, parameters);
		}

		return makePollingMo(list, ifList);

	}

	private List<NeMo> makePollingMo(List<NeMo> neList, List<NeIfMo> ifList) {
		if (neList != null && ifList != null) {
			for (NeIfMo ifMo : ifList) {
				for (NeMo mo : neList) {
					if (ifMo.getUpperMoNo() == mo.getMoNo()) {
						mo.getMoConfig().addMo(ifMo, false);
						break;
					}
				}
			}
		}
		return neList;
	}
}

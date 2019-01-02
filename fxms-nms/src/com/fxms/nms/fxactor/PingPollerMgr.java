package com.fxms.nms.fxactor;

import com.fxms.nms.mo.pmo.IpPmo;

import fxms.bas.poller.PollerMgr;

public class PingPollerMgr extends PollerMgr<IpPmo> {

	public PingPollerMgr() {
		super(IpPmo.class, PingPoller.class);
	}

//	@Override
//	protected List<PollingMo> loadMo(Class<IpsPmo> classOfMo, Mo mo, Map<String, Object> parameters) {
//		List<PollingMo> list = getPsApi().getMo(IpPmo.class, mo, parameters);
//		return makeIpsMo(list);
//	}
//
//	@Override
//	protected List<PollingMo> loadMoListAll(Class<IpsPmo> classOfMo, Map<String, Object> parameters) {
//		List<PollingMo> list = getPsApi().getMoListAll(IpPmo.class, parameters);
//		return makeIpsMo(list);
//	}
//
//	private List<PollingMo> makeIpsMo(List<PollingMo> list) {
//		List<PollingMo> ret = new ArrayList<PollingMo>();
//
//		IpsPmo ips = null;
//
//		for (PollingMo mo : list) {
//
//			if (ips == null) {
//				ips = new IpsPmo();
//				ret.add(new PollingMo(ips));
//			}
//			
//			ips.getIpList().add((IpPmo) mo.getMo());
//
//			if (ips.getIpList().size() >= 10) {
//				ips = null;
//			}
//		}
//
//		return ret;
//	}
}

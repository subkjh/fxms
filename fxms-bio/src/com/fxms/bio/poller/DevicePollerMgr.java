package com.fxms.bio.poller;

import com.fxms.bio.mo.DeviceMo;

import fxms.bas.poller.PollerMgr;

public class DevicePollerMgr extends PollerMgr<DeviceMo> {

	public DevicePollerMgr() {
		super(DeviceMo.class, DevicePoller.class);
	}

}
package com.fxms.ws;

import java.rmi.RemoteException;

import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.api.EventApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.noti.FxEvent;

public class WsBroadcasterImpl extends FxServiceImpl implements WsBroadcaster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4242482962520465138L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(WsBroadcaster.class.getSimpleName(), WsBroadcasterImpl.class, args);
	}

	public WsBroadcasterImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		if (noti instanceof Alarm) {
			Alarm alarm = (Alarm) noti;
			if (alarm.isCleared() == false) {
				try {
					OccurAlarmDbo a = EventApi.getApi().doSelectAlarmHst(alarm.getAlarmNo());
					if (a != null) {
						super.onNotify(a);
						return;
					}
				} catch (Exception e) {
					logger.error(e);
				}

			}
		}

		super.onNotify(noti);
	}
}

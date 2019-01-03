package com.fxms.ws.fxactor;

import com.fxms.ws.vo.ClientVo;

import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.api.EventApi;
import fxms.bas.api.MoApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.noti.FxEvent;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;

public class AlarmWsBroadcaster extends FxBroadcaster {

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		if (getServer() == null) {
			return;
		}

		if (noti instanceof Alarm) {

			int count = -1;

			OccurAlarmDbo obj = null;

			obj = EventApi.getApi().doSelectAlarmHst(((Alarm) noti).getAlarmNo());

			if (obj != null) {
				
				count = 0;
				
				String msg = gson.toJson(obj);

				for (ClientVo client : getClientList()) {
					if (client.getUser().getUserType() == User.USER_TYPE.admin.getCode() //
							|| MoApi.getApi().isMemberLocation(obj.getInloNo(), client.getUser().getMngInloNo())) {
						try {
							client.getWebsocket().send(msg);
							count++;
						} catch (Exception e) {
							Logger.logger.error(e);
						}
					}
				}
			}

			FxServiceImpl.logger.trace("alarm-no={}, sent-client={}", ((Alarm) noti).getAlarmNo(), count);

		}

	}
}

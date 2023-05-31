package fxms.bas.ws.ao;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.InloApi;
import fxms.bas.event.FxEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.Inlo;
import fxms.bas.vo.MoStateEvt;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User.USER_TYPE_CD;

/**
 * 알람을 방송하는 스레드
 * 
 * @author subkjh
 *
 */
public class AlarmBroadServer extends FxBroadServer {

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		if (getServer() == null) {
			return;
		}

		FxServiceImpl.logger.debug("{}", noti);

		if (noti instanceof Alarm || noti instanceof MoStateEvt) {

			if (noti instanceof Alarm) {
				Alarm obj = AlarmApi.getApi().getHstAlarm(((Alarm) noti).getAlarmNo());
				if (obj != null) {
					broadcast(obj, obj.getInloNo());
				}
			} else if (noti instanceof MoStateEvt) {
				MoStateEvt obj = (MoStateEvt) noti;
				if (obj != null && obj.getMo() != null)
					broadcast(obj, obj.getMo().getInloNo());
			}

		} else {

			// super.onNotify(noti);

		}

	}

	private void broadcast(Object obj, int inloNo) {
		int count = 0;

		String msg = gson.toJson(obj);

		Inlo clientInlo;

		for (ClientVo client : getClientList()) {

			clientInlo = InloApi.getApi().getInlo(client.getSession().getInloNo());

			if (client.getSession().getUserTypeCd() == USER_TYPE_CD.SuperAdmin //
					|| (clientInlo != null && clientInlo.isContains(inloNo))) {
				try {
					client.getWebsocket().send(msg);
					count++;
				} catch (Exception e) {
					Logger.logger.error(e);
				}

			}
		}

		FxServiceImpl.logger.trace("{}, sent-client={}", msg, count);

	}
}

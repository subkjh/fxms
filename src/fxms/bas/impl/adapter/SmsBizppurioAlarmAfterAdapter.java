package fxms.bas.impl.adapter;

import java.util.List;

import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.co.AmUserVo;
import fxms.bas.fxo.adapter.AlarmAfterAdapter;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.impl.dpo.co.SendSmsDfo;
import fxms.bas.vo.ClearAlarm;
import fxms.bas.vo.OccurAlarm;

/**
 * bizppurio를 이용한 SMS 발송
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "AlarmService", descr = "비즈뿌리오를 이용한 SMS 발생")
public class SmsBizppurioAlarmAfterAdapter extends AlarmAfterAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3516355473049690693L;

	@Override
	public List<AmHstVo> followUp(OccurAlarm alarm, List<AmGroupVo> amGroupList) throws Exception {
		String msg = "경보발생 : " + alarm.getAlarmNo() + ":" + alarm.getAlarmMsg();
		for (AmGroupVo vo : amGroupList) {
			for (AmUserVo user : vo.getAmList()) {
				if (user.getAmTelno() != null) {
					smsSend(user.getAmTelno(), msg);
				}
			}
		}
		return null;
	}

	@Override
	public List<AmHstVo> followUp(ClearAlarm alarm, List<AmGroupVo> amGroupList) throws Exception {
		String msg = "경보해제 : " + alarm.getAlarmNo() + ":" + alarm.getRlseMemo();

		for (AmGroupVo vo : amGroupList) {
			for (AmUserVo user : vo.getAmList()) {
				if (user.getAmTelno() != null) {
					smsSend(user.getAmTelno(), msg);
				}
			}
		}
		return null;
	}

	private void smsSend(String telNo, String msg) throws Exception {
		new SendSmsDfo().sendSms(telNo, msg);
	}

	@Override
	public String getFpactCd() {
		return "SMS";
	}

}

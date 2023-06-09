package fxms.bas.ao.treat;

import java.util.List;

import fxms.bas.ao.vo.ClearAlarm;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;

/**
 * 경보가 발생했을 때 메일을 보내는 필터
 * 
 * @author SUBKJH-DEV
 *
 */
public class AlarmSendFilter extends TreatFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4191917974042081373L;

	public AlarmSendFilter() {
	}

//	private AmHstVo make(OccurAlarm alarm) {
//
//		AmHstVo vo = new AmHstVo();
//
//		vo.setAlarmNo(alarm.getAlarmNo());
//		vo.setAlcdName(alarm.getAlcdName());
//		vo.setAlcdNo(alarm.getAlcdNo());
//		vo.setClearYn(false);
//		vo.setMoNo(alarm.getMoNo());
//		vo.setMoAname(alarm.getMoAname());
//		vo.setRegDate(FxApi.getDate());
//		vo.setTreatName(alarm.getTreatName());
//
//		return vo;
//	}

	@Override
	public List<AmHstVo> treat(OccurAlarm a, List<AmGroupVo> amGroupList) throws Exception {

//		if (amGroupList != null) {
//
//			OccurAlarmDbo alarm = (OccurAlarmDbo) a;
//
//			List<AmHstVo> hstList = new ArrayList<AmHstVo>();
//
//			StringBuffer sb = new StringBuffer();
//			sb.append("<h3>mo-no=" + alarm.getMoNo() + "</h3>");
//			sb.append("<h3>mo-name=" + alarm.getMoName() + "</h3>");
//			sb.append("<h3>alarm-no=" + alarm.getAlarmNo() + "</h3>");
//			sb.append("<h3>alarm-name=" + alarm.getAlcdName() + "</h3>");
//			sb.append("<h3>alarm-msg=" + alarm.getAlarmMsg() + "</h3>");
//			SendMail sendmail = new SendMail();
//
//			boolean ret;
//			AmHstVo vo;
//
//			for (AmGroupVo group : amGroupList) {
//				for (AmUserVo user : group.getAmList()) {
//					if (user.getAmMail() != null) {
//						ret = sendmail.sendmail(user.getAmMail(), "FXMS Alarm Message", sb.toString());
//
//						vo = make(alarm);
//						hstList.add(vo);
//
//						vo.setUserNo(user.getUserNo());
//						vo.setAmGroupName(group.getAmGroupName());
//						vo.setAmGroupNo(user.getAmGroupNo());
//						vo.setErrYn(ret == false);
//					}
//				}
//			}
//
//			return hstList;
//
//		}

		return null;
	}

	@Override
	public List<AmHstVo> treat(ClearAlarm alarm, List<AmGroupVo> amGroupList) throws Exception {
		// if (amGroupList != null) {
		//
		// SendMail sendmail = new SendMail();
		//
		// for (AmGroupVo group : amGroupList) {
		// for (AmUserVo user : group.getAmList()) {
		// if (user.getAmMail() != null) {
		// sendmail.sendmail(user.getAmMail(), "경보해제", alarm.getAlarmMsg());
		// }
		// }
		// }
		// }

		return null;
	}

}

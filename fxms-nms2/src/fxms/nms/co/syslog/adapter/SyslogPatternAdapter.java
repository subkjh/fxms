package fxms.nms.co.syslog.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.AoCode;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.api.EventApi;
import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.Mo;
import fxms.nms.api.SyslogApi;
import fxms.nms.co.syslog.mo.SyslogMo;
import fxms.nms.co.syslog.mo.SyslogNode;
import fxms.nms.co.syslog.vo.SyslogEventLog;
import fxms.nms.co.syslog.vo.SyslogParsingResultVo;
import fxms.nms.co.syslog.vo.SyslogPattern;
import fxms.nms.co.syslog.vo.SyslogPatternFx;
import fxms.nms.co.syslog.vo.SyslogVo;
import subkjh.bas.co.log.Logger;

/**
 * 정의된 SYSLOG 패턴을 이용하여 경보를 발생하는 ACTOR
 * 
 * @author subkjh(김종훈)
 *
 */
public class SyslogPatternAdapter extends FxActorImpl implements SyslogAdapter {

	@Override
	public SyslogVo parse(SyslogNode syslognode, SyslogVo vo) throws Exception {

		SyslogMo node;
		if (syslognode instanceof SyslogMo) {
			node = (SyslogMo) syslognode;
		} else {
			return vo;
		}

		Logger.logger.trace("{} {}", node, vo);

		SyslogParsingResultVo p = SyslogApi.getApi().parse(node, vo);

		if (p == null) {
			return vo;
		}

		SyslogPatternFx pattern = (SyslogPatternFx) p.getPattern();
		SyslogPattern.LogStatus logStatus = p.getStatus();
		AlarmEvent event = null;
		Alarm alarm = null;
		String instance = null;
		boolean hasInstance = false;
		int alarmCode = 0;
		Mo mo = null;

		instance = SyslogApi.getApi().remakeInstance(instance);

		alarmCode = pattern.getAlarmCode();
		if (pattern.getAlarmCodeUse() > 0) {
			alarmCode = pattern.getAlarmCodeUse();
			if (instance == null)
				instance = "Syslog-" + pattern.getAlarmCode();
			else
				instance = instance + " Syslog-" + pattern.getAlarmCode();
		}

		if (p.getStatus() != SyslogPattern.LogStatus.nothing) {

			if (hasInstance) {
				mo = getMo(node, pattern, instance);
			}

			if (mo == null)
				mo = node;

			Logger.logger.trace("{} {} {} {}", logStatus, instance, node, mo, pattern);

			if (logStatus == SyslogPattern.LogStatus.clearAll) {
				alarm = EventApi.getApi().getAlarm(mo, hasInstance ? instance : null, alarmCode);
				if (alarm != null)
					EventApi.getApi().clear(alarm, System.currentTimeMillis(), AoCode.ClearReason.Auto);
			} else if (logStatus == SyslogPattern.LogStatus.clearOne) {
				alarm = EventApi.getApi().getAlarm(mo, hasInstance ? instance : null, alarmCode);
				if (alarm != null)
					EventApi.getApi().clear(alarm, System.currentTimeMillis(), AoCode.ClearReason.Auto);
			} else if (logStatus == SyslogPattern.LogStatus.occurIfNotExist) {
				alarm = EventApi.getApi().getAlarm(mo, hasInstance ? instance : null, alarmCode);
				if (alarm == null) {
					event = EventApi.getApi().makeEvent(mo, hasInstance ? instance : null, alarmCode, vo.getMsg(), 0, 0,
							0, pattern.getAlarmLevel(), null);
					event.setTreatName(pattern.getTreatName());
				}
			} else if (logStatus == SyslogPattern.LogStatus.occur) {
				event = EventApi.getApi().makeEvent(mo, instance, alarmCode, vo.getMsg(), 0, 0, 0,
						pattern.getAlarmLevel(), null);
				event.setTreatName(pattern.getTreatName());
			}

			else if (logStatus == SyslogPattern.LogStatus.notTrigger) {
				return null;
			}

			if (event != null) {
				try {
					alarm = EventApi.getApi().sendEvent(event);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

		if (SyslogApi.getApi().isSaveSyslog()) {

			SyslogEventLog syslogEvent = SyslogApi.getApi().makeSyslogEvent(mo, node, vo, //
					// 경보등급
					event != null ? event.getAlarmLevel() : 0, //
					// 경보코드
					alarmCode, //
					// 경보명
					pattern == null ? "" : pattern.getAlarmName(), //
					// 경보번호
					alarm != null ? alarm.getAlarmNo() : 0);

			syslogEvent.setMoInstance(instance);

			SyslogApi.getApi().insertLog(syslogEvent);
		}

		return null;
	}

	private Mo getMo(Mo node, SyslogPatternFx thr, String instance) {

		if (thr.isSetMo() == false || instance == null || instance.length() == 0)
			return null;

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNoUpper", node.getMoNo());
		para.put("moClass", thr.getMoClass());
		para.put(thr.getFieldMo(), instance);

		List<Mo> moList;
		try {
			moList = MoApi.getApi().getMoList(para);
			return moList != null && moList.size() > 0 ? moList.get(0) : null;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}
}

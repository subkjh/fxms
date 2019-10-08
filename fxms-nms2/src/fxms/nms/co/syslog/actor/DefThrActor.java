package fxms.nms.co.syslog.actor;

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
import fxms.nms.co.syslog.vo.SyslogThr;
import fxms.nms.co.syslog.vo.SyslogVo;
import subkjh.bas.co.log.Logger;

public class DefThrActor extends FxActorImpl implements SyslogAdapter {

	@Override
	public SyslogVo parse(SyslogNode syslogNode, SyslogVo vo) throws Exception {

		SyslogMo node = (SyslogMo) syslogNode;
		SyslogThr.LogStatus logStatus = null;
		SyslogThr thr = null;

		/** SyslogThreshold(syslog 경보 발생 조건)을 thresholds list에 담는다 */
		List<SyslogThr> thrList = SyslogApi.getApi().getThresholdList(node);

		Mo mo = node;
		AlarmEvent event = null;
		Alarm alarm = null;
		String instance = null;
		boolean hasInstance = false;
		int alarmCode = 0;

		if (thrList != null && thrList.size() > 0) {

			for (int i = 0, size = thrList.size(); i < size; i++) {
				thr = thrList.get(i);
				logStatus = thr.check(vo.getMsg());
				if (logStatus != SyslogThr.LogStatus.nothing) {
					instance = thr.getInstance(vo.getMsg());
					hasInstance = instance != null;
					if (instance == null && SyslogApi.getApi().isAllowSameEvent() == false) {
						instance = "No." + System.currentTimeMillis();
					}
					break;
				} else {
					thr = null;
				}
			}

			instance = SyslogApi.getApi().remakeInstance(instance);

			if (thr != null) {
				alarmCode = thr.getAlarmCode();
				if (thr.getAlarmCodeUse() > 0) {
					alarmCode = thr.getAlarmCodeUse();
					if (instance == null)
						instance = "Syslog-" + thr.getAlarmCode();
					else
						instance = instance + " Syslog-" + thr.getAlarmCode();
				}
			}

			if (logStatus != SyslogThr.LogStatus.nothing) {

				if (hasInstance) {
					mo = getMo(node, thr, instance);
				}

				if (mo == null)
					mo = node;

				Logger.logger.trace("{} {} {} {}", logStatus, instance, node, mo, thr);

				if (logStatus == SyslogThr.LogStatus.clearAll) {
					alarm = EventApi.getApi().getAlarm(mo, hasInstance ? instance : null, alarmCode);
					if (alarm != null)
						EventApi.getApi().clear(alarm, System.currentTimeMillis(), AoCode.ClearReason.Auto);
				} else if (logStatus == SyslogThr.LogStatus.clearOne) {
					alarm = EventApi.getApi().getAlarm(mo, hasInstance ? instance : null, alarmCode);
					if (alarm != null)
						EventApi.getApi().clear(alarm, System.currentTimeMillis(), AoCode.ClearReason.Auto);
				} else if (logStatus == SyslogThr.LogStatus.occurIfNotExist) {
					alarm = EventApi.getApi().getAlarm(mo, hasInstance ? instance : null, alarmCode);
					if (alarm == null) {
						event = EventApi.getApi().makeEvent(mo, hasInstance ? instance : null, alarmCode, vo.getMsg(),
								0, 0, 0, thr.getAlarmLevel(), null);
						event.setTreatName(thr.getTreatName());
					}
				} else if (logStatus == SyslogThr.LogStatus.occur) {
					event = EventApi.getApi().makeEvent(mo, instance, alarmCode, vo.getMsg(), 0, 0, 0,
							thr.getAlarmLevel(), null);
					event.setTreatName(thr.getTreatName());
				}

				else if (logStatus == SyslogThr.LogStatus.notTrigger) {
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

		} else {
			Logger.logger.trace("SyslogThr is not matched {}", vo.getIpAddress());
		}

		if (SyslogApi.getApi().isSaveSyslog()) {

			SyslogEventLog syslogEvent = SyslogApi.getApi().makeSyslogEvent(mo, node, vo, //
					// 경보등급
					event != null ? event.getAlarmLevel() : 0, //
					// 경보코드
					alarmCode, //
					// 경보명
					thr == null ? "" : thr.getAlarmName(), //
					// 경보번호
					alarm != null ? alarm.getAlarmNo() : 0);

			syslogEvent.setMoInstance(instance);

			SyslogApi.getApi().insertLog(syslogEvent);
		}

		return null;
	}

	private Mo getMo(Mo node, SyslogThr thr, String instance) {

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

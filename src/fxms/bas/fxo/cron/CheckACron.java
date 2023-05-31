package fxms.bas.fxo.cron;

import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.co.CoCode.MO_STATUS;
import fxms.bas.cron.Crontab;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.fxo.cron.vo.CollectInfoVo;
import fxms.bas.impl.dao.CheckACronQid;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 가용성(Availability) 확인<br>
 * 수집이 되지 않은 경우 다운으로 간주한다.<br>
 * 참고<br>
 * A(Availability) = MTTF / (MTTF + MTTR) * 100 또는 A = MTTF / MTBF * 100<br>
 * MTTR(Mean Time To Repair, 고장 시점부터 복구까지의 평균 시간): 장애로 인해 생기는 평균 다운 시간.<br>
 * MTTF(Mean Time To Failure, 복구 후 다음 고장까지의 평균 시간): 설비의 복구 시점부터 다음 고장까지, 동작시간의
 * 평균치 장애 없이 시스템을 가동하는 평균 시간.<br>
 * MTBF(Mean Time Between Failure): 설비의 고장 시점에서부터 다음 고장까지의 평균 시간으로, MTTF와 MTTR을
 * 더한 값이다.<br>
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "AppService", descr = "수집이 되지 않은 경우 다운으로 판단하여 알람을 발생한다.")
public class CheckACron extends Crontab {

	public static void main(String[] args) {
		Logger.logger.setLevel(LOG_LEVEL.trace);
		CheckACron c = new CheckACron();
		try {
			c.analysis();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FxAttr(name = "schedule", description = "실행계획", value = "* * * * *")
	private String schedule;

	private final CheckACronQid QID = new CheckACronQid();

	@SuppressWarnings("unchecked")
	public void analysis() throws Exception {
		QidDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(CheckACronQid.QUERY_XML_FILE));

		Map<String, Object> para = FxApi.makePara("moStatusPsId", PsApi.MO_STATUS_PS_ID);
		List<CollectInfoVo> list1 = null;
		List<CollectInfoVo> list2 = null;
		try {
			tran.start();
			list1 = tran.selectQid2Res(QID.select_not_collected_sensor_list, para);
			list2 = tran.selectQid2Res(QID.select_not_collected_node_list, para);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

		// 알람 확인
		Map<Long, Mo> moMap = MoApi.toMoNoMap(MoApi.getApi().getMoList(null));
		check(list1, moMap);
		check(list2, moMap);

		// OnOff상태 수집 기록
		try {
			PsVoRawList ret = new PsVoRawList(getClass().getSimpleName(), System.currentTimeMillis());
			makePsValue(list1, ret, moMap);
			makePsValue(list2, ret, moMap);
			ValueApi.getApi().addValue(ret, false);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	public String getGroup() {
		return "CollectAnalysis";
	}

	@Override
	public void start() throws Exception {

		analysis();

	}

	@Override
	protected String getSchedule() {
		return schedule;
	}

	private void check(List<CollectInfoVo> list, Map<Long, Mo> moMap) {
		ALARM_LEVEL alarmLevel;
		Mo mo;

		for (CollectInfoVo vo : list) {

			alarmLevel = vo.getAlarmLevel();

			mo = moMap.get(vo.getMoNo());

			Logger.logger.trace("alarmLevel={}, mo={}", alarmLevel, mo);

			if (mo == null)
				continue;

			if (alarmLevel == ALARM_LEVEL.Clear) {
				AlarmApi.getApi().clearAlarm(mo, null, vo.getAlcdNo(), ALARM_RLSE_RSN_CD.Release, "수집됨",
						System.currentTimeMillis(), User.USER_NO_SYSTEM);
			} else {
				AlarmOccurEvent event = AlarmApi.getApi().makeAlarmEvent(mo, null, vo.getAlcdNo(), alarmLevel, null,
						null);
				if (event != null) {

					Alarm curAlarm = AlarmApi.getApi().getCurAlarm(mo, null, vo.getAlcdNo());

					// 알람이 없거나 등급이 다르면 알람을 발생한다.
					if (curAlarm == null || curAlarm.getAlarmLevel() != alarmLevel.getAlarmLevel()) {
						event.setAlarmCfgNo(vo.getAlarmCfgNo());
						try {
							AlarmApi.getApi().fireAlarm(mo.getMoNo(), vo.getAlcdNo(), null);
						} catch (Exception e) {
							Logger.logger.error(e);
						}
					}
					
				}
			}
		}
	}

	private void makePsValue(List<CollectInfoVo> list, PsVoRawList ret, Map<Long, Mo> moMap) {

		ALARM_LEVEL alarmLevel;

		for (CollectInfoVo vo : list) {
			alarmLevel = vo.getAlarmLevel();

			if (alarmLevel == ALARM_LEVEL.Clear) {
				ret.add(new PsVoRaw(vo.getMoNo(), PsApi.MO_STATUS_PS_ID, MO_STATUS.Online.getNo()));
			} else {
				ret.add(new PsVoRaw(vo.getMoNo(), PsApi.MO_STATUS_PS_ID, MO_STATUS.Offline.getNo()));
			}
		}

	}
}

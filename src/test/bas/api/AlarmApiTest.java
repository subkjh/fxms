package test.bas.api;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.AlarmApiService;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmClearEvent;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

public class AlarmApiTest {

	public static void main(String[] args) {

		FxCfg.isTest = true;
		Logger.logger.setLevel(LOG_LEVEL.trace);
		AlarmApiTest test = new AlarmApiTest();
		try {
			test.test();
		} catch (Exception e) {
		}
	}

	private AlarmApi api = new AlarmApiService();

	void test() throws Exception {

		System.out.println("cfg\t" + FxmsUtil.toJson(api.getAlCfgs(null)));
		System.out.println("code\t" + FxmsUtil.toJson(api.getAlCds()));

		long startDate = DateUtil.getDtm(System.currentTimeMillis() - 3600000L);
		long endDate = DateUtil.getDtm();

		Alarm alarm = api.fireAlarm(1002134, 10000, null);
		System.out.println("fire\t" + FxmsUtil.toJson(alarm));
		alarm = api.getCurAlarm(alarm.getAlarmNo());
		System.out.println("cur\t" + FxmsUtil.toJson(alarm));
		System.out.println("get\t" + FxmsUtil.toJson(api.getHstAlarm(alarm.getAlarmNo())));
		System.out.println("gets\t"
				+ FxmsUtil.toJson(api.getHstAlarms(startDate, endDate, FxApi.makePara("alarmNo", alarm.getAlarmNo()))));
		alarm = api.clearAlarm(new AlarmClearEvent(alarm.getAlarmNo(), System.currentTimeMillis(),
				ALARM_RLSE_RSN_CD.ByUser, "test", 2));
		System.out.println("clear\t" + FxmsUtil.toJson(alarm));

	}

	public void test2() {

		Map<String, Object> etcData = new HashMap<>();
		etcData.put("alarmKey", "fems_12345");
		api.fireAlarm(null, null, ALARM_CODE.test_alarm.getAlcdNo(), null, null, etcData);

	}
}

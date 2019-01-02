package fxms.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.client.log.Logger;

public class FoClient extends FxmsClient {

	public FoClient() throws Exception {
		super("125.7.128.42", 10005);
		// super("localhost", 10005);
		login("SYSTEM", "SYSTEM");
	}

	public static void main(String[] args) throws Exception {
		FoClient client = new FoClient();
		// client.reloadAlarmCode();
		// client.reloadMoAll();
		for (int i = 0; i < 10; i++) {
			client.cycle();
			Thread.sleep(5000);
			client.testFire();
			Thread.sleep(5000);
		}
	}

	private List<String> reloadAlarmCode() {

		List<String> alarmNameList = new ArrayList<String>();

		try {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("targetMoClass", "all");
			List<Map<String, Object>> alarmCodeList = testRetList("ao", "get-alarm-code-list", para);
			for (Map<String, Object> map : alarmCodeList) {
				alarmNameList.add(map.get("alcdName").toString());
			}
			return alarmNameList;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	private List<Long> reloadMoAll() {

		List<Long> list = new ArrayList<Long>();

		try {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("moClass", "NE");
			List<Map<String, Object>> mapList = testRetList("mo", "get-mo-list", para);
			for (Map<String, Object> map : mapList) {
				list.add((((Number) map.get("moNo")).longValue()));
			}
			return list;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	void testFire() throws Exception {
		List<String> alarmCodeList = this.reloadAlarmCode();
		List<Long> moList = this.reloadMoAll();
		int index = 0;

		for (String alarmName : alarmCodeList) {
			
			index++;
			
			for (Long moNo : moList) {
				this.fireAlarm(moNo, alarmName, "test event...");
			}

			if (index >= 100)
				break;
		}

	}

	void cycle() throws Exception {
		List<Map<String, Object>> list = getAlarmCur();
		for (Map<String, Object> a : list) {
			// this.ackAlarm(((Number)a.get("alarmNo")).longValue());
			this.clearAlarm(((Number) a.get("alarmNo")).longValue());

			// return;
			// Thread.sleep(3000);
		}
	}

	void getAlarmCfg() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("moClass", "MO");
		para.put("alarmCfgNo", "1000");
		List<Map<String, Object>> list = testRetList("ao", "get-alarm-cfg-list", para);
		print(list);
	}

	Map<String, Object> getAlarmCfg(int alarmCfgNo) throws Exception {
		Map<String, Object> mem;
		Map<String, Object> item = new HashMap<String, Object>();
		if (alarmCfgNo > 0) {
			item.put("alarmCfgNo", alarmCfgNo);
		}
		item.put("alarmCfgName", "alarm-cfg-" + (System.currentTimeMillis() / 1000L));
		item.put("moClass", "MO");

		List<Map<String, Object>> memList = new ArrayList<Map<String, Object>>();
		item.put("memList", memList);

		mem = new HashMap<String, Object>();
		memList.add(mem);
		mem.put("alcdNo", ALARM_CODE.CO2_OVER);
		mem.put("alarmLevel", 1);
		mem.put("compareVal", 80);

		mem = new HashMap<String, Object>();
		memList.add(mem);
		mem.put("alcdNo", ALARM_CODE.CO2_UNDEF);
		mem.put("alarmLevel", 2);
		mem.put("compareVal", 30);

		mem = new HashMap<String, Object>();
		memList.add(mem);
		mem.put("alcdNo", ALARM_CODE.TEMP_OVER);
		mem.put("alarmLevel", 2);
		mem.put("compareVal", 30);

		mem = new HashMap<String, Object>();
		memList.add(mem);
		mem.put("alcdNo", ALARM_CODE.TEMP_UNDER);
		mem.put("alarmLevel", 2);
		mem.put("compareVal", 18);

		mem = new HashMap<String, Object>();
		memList.add(mem);
		mem.put("alcdNo", ALARM_CODE.PH_OVER);
		mem.put("alarmLevel", 2);
		mem.put("compareVal", 8);

		mem = new HashMap<String, Object>();
		memList.add(mem);
		mem.put("alcdNo", ALARM_CODE.PH_UNDER);
		mem.put("alarmLevel", 2);
		mem.put("compareVal", 4);
		return item;

	}

	List<Map<String, Object>> getAlarmCur() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("moNo", "530000");
		// para.put("alcdNo", "22101");
		List<Map<String, Object>> list = testRetList("ao", "get-alarm-cur", para);
		print(list);

		return list;
	}

	void updateAlarmCfg() throws Exception {
		Map<String, Object> item = getAlarmCfg(1000);
		testRetObj("ao", "update-alarm-cfg", item);
	}

	void ackAlarm(long alarmNo) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("alarmNo", alarmNo);
		testRetObj("ao", "ack-alarm", para);
	}

	void fireAlarm(long moNo, String alarmName, String alarmMsg) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", moNo);
		para.put("alarmName", alarmName);
		para.put("alarmMsg", alarmMsg);
		testRetObj("ao", "fire-alarm", para);
	}

	void clearAlarm(long alarmNo) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("alarmNo", alarmNo);
		testRetObj("ao", "clear-alarm", para);
	}

	void addAlarmCfg() throws Exception {
		Map<String, Object> item = getAlarmCfg(0);
		testRetObj("ao", "add-alarm-cfg", item);
	}

	void getAlarmHst() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("moNo", 530000);
		para.put("ocuMsdate >=", System.currentTimeMillis() - 3600000L);
		para.put("ocuMsdate >=", System.currentTimeMillis() - 300000L);
		para.put("ocuMsdate <=", System.currentTimeMillis());
		List<Map<String, Object>> list = testRetList("ao", "get-alarm-hst", para);
		print(list);
	}

}
package fxms.client;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FxmsClientTest {

	public static void main(String[] args) throws Exception {
		FxmsClientTest tester = new FxmsClientTest();
		// tester.getPsList(ClientCode.psType.raw);
		// tester.selectQid();
		// tester.getDiagList();
		// tester.getInloList();
		// tester.getMoList("DEVICE");
		// tester.getUserTree();
		// tester.sendFxEvent();
//		tester.testDownload();
//		tester.testLs();
		tester.fireAlarm();
	}

	private FxmsClient client;

	public FxmsClientTest() throws Exception {
		// client = new FxmsClient("localhost", 10005);
		client = new FxmsClient("125.7.128.42", 10005);
		client.login("astabio", "astabio");
	}

	private int getLength(Object obj) {
		if (obj == null) {
			return 0;
		}

		try {
			return obj.toString().getBytes("euc-kr").length;
		} catch (UnsupportedEncodingException e) {
			return obj.toString().length();
		}
	}

	private void print(List<Map<String, Object>> mapList) {
		if (mapList == null || mapList.size() == 0)
			return;

		Map<String, Object> map = mapList.get(0);
		String nameArr[] = map.keySet().toArray(new String[map.size()]);
		Arrays.sort(nameArr);

		int max[] = new int[nameArr.length];

		for (int i = 0; i < max.length; i++) {
			max[i] = nameArr[i].length();
		}

		for (Map<String, Object> e : mapList) {
			for (int i = 0; i < nameArr.length; i++) {
				max[i] = Math.max(max[i], (getLength(e.get(nameArr[i]))));
			}
		}

		StringBuffer line = new StringBuffer();
		line.append("+-");
		for (int i = 0; i < max.length; i++) {
			for (int n = 0; n < max[i]; n++)
				line.append("-");
			line.append("-+");
			if (i < max.length - 1)
				line.append("-");
		}

		System.out.println(line);
		System.out.print("| ");
		for (int i = 0; i < nameArr.length; i++) {
			System.out.format("%-" + max[i] + "s", nameArr[i]);
			if (i < nameArr.length)
				System.out.print(" | ");
		}
		System.out.println("\n" + line);

		for (Map<String, Object> e : mapList) {
			System.out.print("| ");
			for (int i = 0; i < nameArr.length; i++) {
				System.out.format("%-" + max[i] + "s", e.get(nameArr[i]));
				if (i < nameArr.length)
					System.out.print(" | ");
			}
			System.out.println();
		}

		System.out.println(line);

		System.out.println("  " + mapList.size() + " rows");
		System.out.println(line);
	}

	private void print(String tag, Map<String, Object> treeMap) {

		System.out.println(tag + treeMap);

		List<Map<String, Object>> children = (List<Map<String, Object>>) treeMap.get("children");
		if (children != null && children.size() > 0) {
			for (Map<String, Object> e : children) {
				print(tag + " tree \t", e);
			}
		} else {

			List<Map<String, Object>> moList = (List<Map<String, Object>>) treeMap.get("moList");
			if (moList != null && moList.size() > 0) {
				System.out.println(tag + ">>> " + treeMap);
				for (Map<String, Object> e : moList) {
					System.out.println(tag + e);
				}
			}
			Map<String, Map<String, Object>> locationMap = (Map<String, Map<String, Object>>) treeMap
					.get("locationMap");
			if (locationMap != null && locationMap.size() > 0) {
				for (Map<String, Object> location : locationMap.values()) {
					print(tag + " loc \t", location);
				}
			}
		}
	}

	void addDeviceMo() throws Exception {
		Map<String, Object> item = getDeviceMo(0);
		client.testRetObj("mo", "add-mo", item);
	}

	void addInLo() throws Exception {
		Map<String, Object> item = getFX_CF_INLO(200020);
		client.testRetObj("mo", "add-inlo", item);
	}

	void addPbrMo() throws Exception {
		Map<String, Object> item = getPbrMo();
		client.testRetObj("mo", "add-mo", item);
	}

	void deleteAlarmCfg() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("alarmCfgNo", 10160);
		client.testRetObj("ao", "delete-alarm-cfg", para);
	}

	void deleteInLo() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("inloNo", 200160);
		client.testRetObj("mo", "delete-inlo", para);
	}
	
	void deleteMo(long moNo) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", moNo);
		client.testRetObj("mo", "delete-mo", para);
	}

	void deleteMos() throws Exception {
		long moNos[] = new long[] { 1000101, 1000035, 1000103, 1000105 };

		for (long moNo : moNos) {
			try {
				deleteMo(moNo);
			} catch (Exception e) {
			}
		}
	}

	void detectMo() throws Exception {

		// ExtService <-> MoService <-> AgentAdapter <-> HoleManager <->
		// HoleAgent

		Map<String, Object> item = getGwMo();
		item.put("moClass", "GW");

		client.testRetObj("mo", "test-mo", item);
	}

	void fireAlarm() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", 1040143);
		para.put("alcdNo", 21002);
		para.put("alcdNo", 10000);
		para.put("alarmMsg", "Test");
		para.put("moInstance", System.currentTimeMillis());
		client.testRetObj("ao", "fire-alarm", para);
	}

	void getAlarmCodeList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("targetMoClass", "SENSOR");
		List<Map<String, Object>> list = client.testRetList("ao", "get-alarm-code-list", para);

		print(list);

	}

	void getAttrValueList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("qid", "SELECT_COMPANY_LIST");
		List<Map<String, Object>> list = client.testRetList("cd", "get-attr-value-list", para);

		print(list);

	}

	void getCodeList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("cdType", "CONTAINER_TYPE");
		List<Map<String, Object>> list = client.testRetList("cd", "get-code-list", para);

		print(list);

	}

	void getDataList() throws Exception {
		List<String> dataList = new ArrayList<String>();
		dataList.add("fxms.bas.dbo.FX_DI_MAIN");
		dataList.add("fxms.bas.dbo.FX_DI_LINE");
		dataList.add("fxms.bas.dbo.FX_DI_NODE");

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("dataClassList", dataList);
		Object ret = client.testRetObj("cd", "get-data-list", para);

		System.out.println(ret);
	}

	Map<String, Object> getDeviceMo(long moNo) throws Exception {

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("moClass", ClientCode.moClass.DEVICE);
		item.put("alarmCfgNo", 1000);
		item.put("mngYn", true);
		item.put("moAname", "CON-TEST.TEMP.1");
		item.put("moName", "CON-TEST.TEMP.1");
		item.put("upperMoNo", 1000090);
		item.put("moNo", moNo);

		item.put("deviceType", ClientCode.deviceType.TEMP);
		item.put("deviceIpaddr", null);
		item.put("gwMoNo", 200130);
		item.put("deviceIdInGw", "CON-TEST.TEMP.1");

		return item;

	}

	void getDiagList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		Object ret = client.testRetObj("cd", "get-diag-list", para);
		System.out.println(ret);
	}

	Map<String, Object> getFX_CF_INLO(int inloNo) throws Exception {

		Map<String, Object> item = new HashMap<String, Object>();

		item.put("inloNo", inloNo);
		item.put("inloFlag", null);
		item.put("inloFname", "강원");
		item.put("inloName", "강원");
		item.put("inloType", ClientCode.inloType.PLANT);
		item.put("upperInloNo", 200120);

		return item;

	}
	//
	//
	// @SuppressWarnings("unchecked")
	// public List<Map<String, Object>> getMoList(String moClass, Map<String,
	// Object> para) throws Exception {
	//
	// Map<String, Object> parameters = new HashMap<String, Object>();
	// parameters.put("mo-class", moClass);
	// parameters.putAll(para);
	//
	// Map<String, Object> pdu = getReq();
	// pdu.put("method", "get-mo-list");
	// pdu.put("parameters", parameters);
	//
	// String request = gson.toJson(pdu);
	//
	// String result = post(getUrl("mo"), request);
	// Map<String, Object> res = parseResult(result);
	//
	// return (List<Map<String, Object>>) res.get("result");
	// }
	//
	// @SuppressWarnings("unchecked")
	// public List<Map<String, Object>> getAlarmCfgList(String moClass,
	// Map<String, Object> para) throws Exception {
	//
	// Map<String, Object> parameters = new HashMap<String, Object>();
	// parameters.put("mo-class", moClass);
	// if (para != null) {
	// parameters.putAll(para);
	// }
	// Map<String, Object> pdu = getReq();
	// pdu.put("method", "get-alarm-cfg-list");
	// pdu.put("parameters", parameters);
	//
	// String request = gson.toJson(pdu);
	//
	// String result = post(getUrl("ao"), request);
	// Map<String, Object> res = parseResult(result);
	//
	// return (List<Map<String, Object>>) res.get("result");
	// }

	Map<String, Object> getGwMo() throws Exception {

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("moClass", ClientCode.moClass.GW);
		item.put("alarmCfgNo", 1000);
		item.put("mngYn", true);
		item.put("moAname", "JB-GW");
		item.put("moName", "JB-GW");
		item.put("upperMoNo", 0);
		item.put("gwIpaddr", "20.10.0.1");
		item.put("gwPort", 4242);
		item.put("gwType", "소노넷");
		item.put("inloNo", 200130);
		item.put("inloMemo", "전북대");

		return item;

	}

	void getInloList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("inloType", ClientCode.inloType.COUNTRY);
		para.put("inloType", ClientCode.inloType.COMPANY);
		para.put("upperInloNo", 200090);
		List<Map<String, Object>> list = client.testRetList("mo", "get-inlo-list", para);

		print(list);
	}

	void getMoConfig() throws Exception {

		// ExtService <-> MoService <-> AgentAdapter <-> HoleManager <->
		// HoleAgent

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("moNo", 1000144);

		client.testRetObj("mo", "get-mo-config", item);
	}

	void getMoList(String moClass) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moClass", moClass);
		para.put("containerId like ", "CON%");
		List<Map<String, Object>> list = client.testRetList("mo", "get-mo-list", para);

		print(list);

	}

	Map<String, Object> getPbrMo() throws Exception {

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("alarmCfgNo", 0);
		item.put("mngYn", true);
		item.put("moAname", "PBR-9");
		item.put("moName", "PBR-9");
		item.put("containerId", "JB-CON-001");
		item.put("upperMoNo", 1000124);
		item.put("pbrType", "Test");
		item.put("moClass", ClientCode.moClass.PBR);

		return item;

	}

	void getPsItemList() throws Exception {
		List<Map<String, Object>> list = client.testRetList("ps", "get-ps-item-list", null);
		print(list);
	}

	void getPsList(ClientCode.psType pstype) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", 1000139);
		para.put("psCode", ClientCode.psCode.HUMI);
		para.put("psType", pstype);
		para.put("startDate", FxmsClient.getDate(System.currentTimeMillis() - 3600000L));
		para.put("endDate", FxmsClient.getDate(System.currentTimeMillis()));
		List<Map<String, Object>> list = client.testRetList("ps", "get-ps-list", para);

		printPsList(list);

	}

	void getPsRealList() throws Exception {

		// ExtService <-> MoService <-> AgentAdapter <-> HoleManager <->
		// HoleAgent

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("moNo", 1000144);
		item.put("psCode", "TEMP,CO2");

		List<Map<String, Object>> list = client.testRetList("ps", "get-ps-real-list", item);
		print(list);
	}

	void getUserTree() throws Exception {
		Map<String, Object> item = client.testRetObj("usr", "get-user-tree-item-list", null);

		Map<String, Object> treeMap = (Map<String, Object>) item.get("30015");
		print("", treeMap);

		// System.out.println(item);
	}

	void printPsList(List<Map<String, Object>> list) {
		if (list == null || list.size() == 0) {
			System.out.println("size = 0");
			return;
		}
		System.out.println("size = " + list.size());

		List<String> keyList = new ArrayList<String>(list.get(0).keySet());
		Collections.sort(keyList);

		for (Map<String, Object> map : list) {
			System.out.println("moNo | moInstance | psCode");
			System.out.println(map.get("moNo") + " | " + map.get("moInstance") + " | " + map.get("psCode"));
			List<Double> timeList = (List<Double>) map.get("timeList");
			List<Number> valueList = (List<Number>) map.get("valueList");
			for (int i = 0; i < timeList.size(); i++) {
				System.out.println(timeList.get(i).longValue() + " | " + valueList.get(i));
			}
		}

	}

	void selectQid() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("qid-file-name", "ui-container-list.xml");
		para.put("qid", "SELECT_CONTAINER_LIST");
		para.put("cdType", "AAA");
		Map<String, Object> ret = client.testRetObj("cd", "get-qid-select", para);

		System.out.println(ret.get("columns").getClass());
		System.out.println(ret.get("datas").getClass());
	}

	void sendFxEvent() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("type", "ReloadSignal");
		para.put("target", "111");
		Object ret = client.testRetObj("cd", "send-fx-event", para);

		System.out.println(ret);
	}

	void setAlarmResaon() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("alarmNo", "10077700");
		para.put("reasonNo", "100");
		para.put("reasonName", "테스트");
		para.put("reasonMemo", "개발테스트");
		client.testRetObj("ao", "set-alarm-reason", para);
	}

	void setupMo() throws Exception {

		// ExtService <-> MoService <-> AgentAdapter <-> HoleManager <->
		// HoleAgent

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("sensor-value", 99);
		item.put("sensor-ps-code", "TEMP");
		item.put("moNo", 1000144);
		item.put("method", "set-sensor-value");

		// item = new HashMap<String, Object>();
		// List<String> script = new ArrayList<String>();
		// script.add("loop");
		// script.add("PUMP.0 0");
		// script.add("sleep 5");
		// script.add("PUMP.1 1");
		// script.add("sleep 100");
		// script.add("PUMP.1 0");
		// script.add("sleep 5");
		// script.add("PUMP.0 1");
		// script.add("sleep 500");
		//
		// item.put("script", script);
		// item.put("moNo", 1000144);
		// item.put("method", "set-cycle-job");

		client.testRetObj("mo", "setup-mo", item);
	}

	void testDownload() throws Exception {
		client.download("deploy/lib/fxms-base.jar", new File("a.jar"));
	}

	void testLs() throws Exception {
		System.out.println(client.ls("deploy/ui"));
	}

	void updateDeviceMo(long moNo) throws Exception {
		Map<String, Object> item = getDeviceMo(moNo);
		client.testRetObj("mo", "update-mo", item);
	}

	void updateInLo() throws Exception {
		Map<String, Object> item = getFX_CF_INLO(200160);
		// item.put("inloFname", "띵스파이어");
		// item.put("inloName", "띵스파이어");
		client.testRetObj("mo", "update-inlo", item);
	}

	void updatePbrMo() throws Exception {
		Map<String, Object> item = getPbrMo();
		item.put("moNo", 1000043);
		item.put("pbrType", "Red");
		client.testRetObj("mo", "update-mo", item);
	}

	// @SuppressWarnings("unchecked")
	// public List<Map<String, Object>> getPsList(long moNo, String psCode,
	// String psType, long startDate, long endDate)
	// throws Exception {
	//
	// Map<String, Object> parameters = new HashMap<String, Object>();
	// parameters.put("moNo", moNo);
	// parameters.put("psCode", psCode);
	// parameters.put("psType", psType);
	// parameters.put("startDate", startDate);
	// parameters.put("endDate", endDate);
	//
	// Map<String, Object> pdu = getReq();
	// pdu.put("method", "ps-list");
	// pdu.put("parameters", parameters);
	//
	// String request = gson.toJson(pdu);
	//
	// String result = post(getUrl("ps"), request);
	// Map<String, Object> res = parseResult(result);
	//
	// return (List<Map<String, Object>>) res.get("result");
	// }
}

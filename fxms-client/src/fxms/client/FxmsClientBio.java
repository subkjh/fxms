package fxms.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FxmsClientBio extends FxmsClient {

	public static void main(String[] args) throws Exception {
		FxmsClientBio tester = new FxmsClientBio();
		// tester.selectContainer();
		// tester.selectSensorAll();
		// tester.addContainerMo("TEST-CON-002");
		// tester.getCurBatch();
//		 tester.getNewBatchId();
		// tester.addBatch();
//		 tester.addBatchData();
		tester.getBatchData();
//		tester.getBatchIds();

	}

	public FxmsClientBio() throws Exception {
		super("125.7.128.42", 10005);
		// login("SYSTEM", "SYSTEM");
		login("astabio", "astabio");
		// login("subkjh", "rlawhdgns");
	}

	// void addContainerMo(String name) throws Exception {
	// Map<String, Object> item = getContainerMo(0, name);
	// client.testRetObj("mo", "add-mo", item);
	// }

	void selectContainer() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("containerId", "JB-CON-001");
		List<Map<String, Object>> list = testRetList("bio", "get-container-list", para);
		print(list);
	}

	void selectSensorAll() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("containerId", "JB-CON-001");
		List<Map<String, Object>> list = testRetList("bio", "get-sensor-all", para);
		print(list);
	}

	void getCurBatch() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("containerId", "JB-CON-001");
		List<Map<String, Object>> list = testRetList("bio", "get-cur-batch", para);
		print(list);
	}

	void getNewBatchId() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("containerId", "JB-CON-001");
		Map<String, Object> ret = testRetObj("bio", "get-new-batch-id", para);
		System.out.println(ret);
	}

	void addBatch() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("batchId", "L180723004");
		para.put("pbrMoNo", "1040188");
		para.put("prdcCode", "Hematococcus");
		Map<String, Object> ret = testRetObj("bio", "add-batch", para);
		System.out.println(ret);
	}

	void addBatchData() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("batchId", "L180723004");
		para.put("pbrMoNo", "1040188");
		para.put("prdcCode", "Hematococcus");
		para.put("biomass", Math.random() * 1000);
		para.put("yyyymmdd", "20180723");
		para.put("n", Math.random() * 1000);
		para.put("p", Math.random() * 1000);
		para.put("endYn", "N");
		Map<String, Object> ret = testRetObj("bio", "add-batch-data", para);
		System.out.println(ret);
	}

	void getBatchData() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		List<String> idList = new ArrayList<String>();
		idList.add("L180723003");
		idList.add("L180723004");

		para.put("batchId", idList);
		Map<String, Object> ret = testRetObj("bio", "get-batch-data-by-id", para);
		System.out.println(ret);

		para.clear();
		para.put("startDate", 20180701000000L);
		para.put("endDate", 20180731235959L);
		para.put("prdcCode", "Hematococcus");
		para.put("pbrMoNo", 1040188);
		ret = testRetObj("bio", "get-batch-data-by-term", para);
		System.out.println(ret);

		para.clear();
		ret = testRetObj("bio", "get-last-batch-data", para);
		System.out.println(ret);
	}
	

	void getBatchIds() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", 20180701000000L);
		para.put("prdcCode", "Hematococcus");
		Map<String, Object> ret = testRetObj("bio", "get-batch-ids", para);
		System.out.println(ret);
	}

	void addGwMo() throws Exception {
		Map<String, Object> item = getGwMo();
		testRetObj("bio", "add-mo-gw", item);
	}

	Map<String, Object> getGwMo() throws Exception {

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("moClass", ClientCode.moClass.GW);
		item.put("alarmCfgNo", 1000);
		item.put("mngYn", true);
		item.put("moAname", "JB-GW");
		item.put("moName", "JB-GW");

		// item.put("upperMoNo", 0);

		item.put("gwIpaddr", "20.10.0.1");
		item.put("gwPort", 4242);
		item.put("gwType", "소노넷");
		item.put("inloNo", 200130);
		item.put("inloMemo", "전북대");

		return item;

	}

	void addContainerMo(String name) throws Exception {
		Map<String, Object> item = getContainerMo(0, name);
		testRetObj("bio", "add-mo-container", item);
	}

	void updateContainerMo(long moNo, String name) throws Exception {
		Map<String, Object> item = getContainerMo(moNo, name);
		testRetObj("mo", "update-mo", item);
	}

	void updateBatch() throws Exception {
		Map<String, Object> item = getContainerMo(1, "test");
		testRetObj("bio", "update-batch-data", item);
	}

	Map<String, Object> getContainerMo(long moNo, String name) throws Exception {

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("alarmCfgNo", 1000);
		item.put("mngYn", true);
		item.put("moAname", name);
		item.put("moName", name);
		// item.put("upperMoNo", 0);
		if (moNo > 0)
			item.put("moNo", moNo);
		item.put("moClass", ClientCode.moClass.CONTAINER);
		item.put("containerId", name);
		item.put("containerType", ClientCode.containerType.ft12B.getCode());
		item.put("inloMemo", "A동 옥상");
		item.put("inloNo", 200130);
		item.put("gwMoNo", 1111);
		item.put("installYmd", "20180314");

		return item;

	}

	void addMtrl() throws Exception {
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("companyInloNo", 111);
		item.put("mtrlCode", "TEST-001");
		item.put("mtrlName", "TEST-001-NAME");
		item.put("mtrlDescr", "TEST-001-DESC");
		item.put("mtrlUnit", "TEST-001-UNIT");

		testRetObj("bio", "add-mtrl", item);

	}
}

package fxms.client.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.client.FxmsClient;

public class BioClient extends FxmsClient {

	private String batchId = "L180130003";
	private long batchNo = 0L;

	public static void main(String[] args) throws Exception {
		BioClient bio = new BioClient();
//		bio.cycle();
		bio.deleteBatch();
	}
	
	public BioClient() throws Exception {
		super("localhost", 10005);
		// client = new FxmsClient("125.7.128.42", 10005);
		login("SYSTEM", "SYSTEM");
	}

	public void addBatch() throws Exception {
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("batchId", batchId);
		item.put("prdcCode", "H01");
		item.put("companyInloNo", 200090);
		item.put("curNDays", 0);
		item.put("batchSrtDate", FxmsClient.getDate(0));
		item.put("batchEndDate", 0);

		Map<String, Object> ret = testRetObj("bio", "add-batch", item);
		batchNo = ((Number) ret.get("batchNo")).longValue();
		
		System.out.println(batchNo);
	}

	public void addBatchFlow() throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		parameters.put("list", list);

		for (int i = 1; i <= 15; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("batchNo", batchNo);
			item.put("nDays", i);
			item.put("pbrMoNo", (i < 8 ? 1000147 : 1000148));
			item.put("alarmCfgNo", 1000 + i);
			item.put("daysMemo", "초기데이터");
			list.add(item);

		}

		List<Map<String, Object>> retList = testRetList("bio", "add-batch-flow", parameters);
		print(retList);
	}

	public void addBatchIn() throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		parameters.put("list", list);
		parameters.put("batchNo", batchNo);

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("batchNo", batchNo);
		item.put("nDays", 1);
		item.put("mtrlCode", "CO2");
		item.put("mtrlAmt", 340);
		item.put("mtrlUcst", 0);
		item.put("inMemo", "TEST");
		list.add(item);

		List<Map<String, Object>> retList = testRetList("bio", "add-batch-in", parameters);
		print(retList);

	}

	public void addBatchOut() throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		parameters.put("list", list);
		parameters.put("batchNo", batchNo);

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("batchNo", batchNo);
		item.put("nDays", 1);
		item.put("prdcCode", "H01");
		item.put("outAmt", 120);
		item.put("outMemo", "TEST");
		list.add(item);

		List<Map<String, Object>> retList = testRetList("bio", "add-batch-out", parameters);
		print(retList);

	}
	public void addMtrl() throws Exception {
		Map<String, Object> item = getCdMtrl();
		testRetObj("bio", "add-mtrl", item);
	}

	public void cycle() throws Exception {
		this.addBatch();
		Thread.sleep(10000);
		this.setBatchFlow();
		Thread.sleep(10000);
		this.setBatchIn();
		Thread.sleep(10000);
		this.setBatchOut();
		Thread.sleep(10000);
		this.deleteBatch();
	}

	public void deleteBatch() throws Exception {
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("batchId", batchId);
		testRetObj("bio", "delete-batch", item);
	}

	public void setBatchFlow() throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		parameters.put("list", list);
		parameters.put("batchNo", batchNo);

		for (int i = 1; i <= 15; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("batchNo", batchNo);
			item.put("nDays", i);
			item.put("pbrMoNo", (i < 8 ? 1000147 : 1000148));
			item.put("alarmCfgNo", 1000 + i);
			item.put("daysMemo", "초기데이터");
			list.add(item);

		}

		List<Map<String, Object>> retList = testRetList("bio", "set-batch-flow", parameters);
		print(retList);
	}

	public void setBatchIn() throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		parameters.put("list", list);
		parameters.put("batchNo", batchNo);

		for (int i = 1; i <= 15; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("batchNo", batchNo);
			item.put("nDays", i);
			item.put("mtrlCode", "CO2");
			item.put("mtrlAmt", Math.random() * 1000);
			item.put("mtrlUcst", 0);
			item.put("inMemo", "TEST");
			list.add(item);
		}

		List<Map<String, Object>> retList = testRetList("bio", "set-batch-in", parameters);
		print(retList);

	}

	public void setBatchOut() throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		parameters.put("list", list);
		parameters.put("batchNo", batchNo);

		for (int i = 1; i <= 15; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("batchNo", batchNo);
			item.put("nDays", i);
			item.put("prdcCode", "H01");
			item.put("outAmt", Math.random() * 1000);
			item.put("outMemo", "TEST");
			list.add(item);
		}

		List<Map<String, Object>> retList = testRetList("bio", "set-batch-out", parameters);
		print(retList);

	}

	public void updateMtrl() throws Exception {
		Map<String, Object> item = getCdMtrl();
		testRetObj("bio", "update-mtrl", item);
	}

	Map<String, Object> getCdMtrl() throws Exception {

		Map<String, Object> item = new HashMap<String, Object>();
		item.put("companyInloNo", 200090);
		item.put("mtrlCode", "CO2");
		item.put("mtrlName", "이산화탄소");
		item.put("mtrlDescr", "");
		item.put("mtrlUnit", "cc");

		return item;

	}

}

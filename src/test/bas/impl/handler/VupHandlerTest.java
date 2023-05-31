package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.ws.handler.client.FxHttpClient;
import subkjh.bas.co.utils.DateUtil;

public class VupHandlerTest {
	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();

		try {
			FxHttpClient c = FxHttpClient.getFxmsClient();
			c.login("subkjh", "1111");
			para.clear();

//			moNo=1000001&psId=INLPG&psDataCd=RAW&startPsDate=20220608000000&endPsDate=20220608085959

			para.put("point_pid", "P010602001008");
//			para.put("ps_kind_name", "MIN5");
			para.put("collected_date_start", DateUtil.getYmd()+"");
			para.put("collected_date_end", DateUtil.getYmd()+"");

			c.call("vup/select-value-list", para);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(50000);
			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

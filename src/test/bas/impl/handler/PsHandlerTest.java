package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.ws.handler.client.FxHttpClient;
import subkjh.bas.co.utils.DateUtil;

public class PsHandlerTest {
	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();

		try {
			FxHttpClient c = FxHttpClient.getFxmsClient();
			c.login("SOIL", "1111");
			para.clear();

//			moNo=1000001&psId=INLPG&psDataCd=RAW&startPsDate=20220608000000&endPsDate=20220608085959

			para.put("moNo", 100003);
			para.put("psId", "TEMP");
			para.put("psStatFuncList", "SUM");
			para.put("psDataCd", "MIN5");
			para.put("startPsDate", DateUtil.getDtm(System.currentTimeMillis() - (86400000L + 3600000L)));
			para.put("endPsDate", DateUtil.getDtm(System.currentTimeMillis() - 3600000L));

			c.call("ps/select-ps-value-list", para);
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

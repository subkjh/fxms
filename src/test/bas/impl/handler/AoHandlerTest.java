package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.ws.handler.client.FxHttpClient;

public class AoHandlerTest {
	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();

		try {
			FxHttpClient c = FxHttpClient.getFxmsClient();
			c.login("SOIL", "1111");
			para.clear();

//			para.put("alarmNo", 10013613);
			para.put("occurDtm >= ", 20220525000000L);

			c.call("ao/get-alarm-hst-list", para);

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

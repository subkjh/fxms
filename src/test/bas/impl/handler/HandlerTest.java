package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.FxResponse;
import fxms.bas.ws.handler.client.FxHttpClient;

public class HandlerTest {

	FxHttpClient c;
	private final String uri;

	public HandlerTest(String host, String uri) throws Exception {
		c = new FxHttpClient(host, 10005);
		c.login("subkjh", "1111");
		this.uri = uri;
	}

	void testMo() throws Exception {

	}

	void testPs() throws Exception {
		Map<String, Object> para = makePara("moNo", 123456, "psId", "MoStatus");
		Object ret1 = c.call("ps/get-values", para);

		para = makePara("moNo", 1002287);
		Object ret2 = c.call("ps/get-values", para);

		System.out.println(FxmsUtil.toJson(ret1));
		System.out.println(FxmsUtil.toJson(ret2));
	}

	protected void logout() {
		Map<String, Object> para = new HashMap<String, Object>();

		try {
			c.call("user/logout", para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected FxResponse call(String method, Map<String, Object> para) {

		try {
			FxResponse response = c.call(uri + "/" + method, para);
			System.out.println(response);
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 입력된 인자를 이용하여 Class Map을 생성한다.
	 * 
	 * @param parameters
	 * @return
	 */
	protected Map<String, Object> makePara(Object... parameters) {

		Map<String, Object> para = new HashMap<String, Object>();

		if (parameters == null || parameters.length < 2) {
			return para;
		}

		for (int i = 0; i < parameters.length; i += 2) {
			para.put(String.valueOf(parameters[i]), parameters[i + 1]);
		}

		return para;
	}
}

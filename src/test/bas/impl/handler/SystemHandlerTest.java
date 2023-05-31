package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.handler.vo.FxResponse;

public class SystemHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {
			SystemHandlerTest c = new SystemHandlerTest();

			c.selectVarList();

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

	public SystemHandlerTest() throws Exception {
		super("localhost", "system");
	}

	public void selectVarList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("varGrpName", "성능조회");
		call("select-var-list", para);
	}

	public FxResponse getVarList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("varGrpName", "성능조회");
		return call("select-var-list", para);
	}

}

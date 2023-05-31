package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

public class CodeHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {
			CodeHandlerTest c = new CodeHandlerTest();

			c.selectCodeList();

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

	public CodeHandlerTest() throws Exception {
		super("localhost", "code");
	}

	public void selectCodeList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("cdClass", "MO_CLASS");
		call("select-code-list", para);
	}

}

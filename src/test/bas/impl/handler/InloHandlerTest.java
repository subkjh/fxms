package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

public class InloHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {

			InloHandlerTest c = new InloHandlerTest();
			c.insertInlo();

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

	public InloHandlerTest() throws Exception {
		super("localhost", "location");
	}

	void insertInlo() {
		Map<String, Object> para = new HashMap<String, Object>();

		para.put("upperInloNo", 200009);
		para.put("ownerInloNo", 2000);
		para.put("inloName", "손오공");
		para.put("inloClCd", "PERSON");

		para.put("inloTypeCd", "10");
		para.put("inloLevelCd", "10");
		para.put("addr", "서울특별시 용산구 한강대로96길 27");
		para.put("cntcrName", "김종훈");
		para.put("telNum", "02-4728-xxxx");
		para.put("faxNum", "");

		call("insert-inlo", para);

	}

	void deleteInlo() {
		call("delete-inlo", makePara("inloNo", "200040"));

	}

	void selectInloClCdList() {
		call("select-inlo-cl-cd-list", makePara());
	}

	public void selectMyInloList() throws Exception {
		call("select-my-inlo-list", makePara("inloClCd", "OFFICE"));
//		call("select-my-inlo-list", makePara());
	}

	public void selectInloOnlineStateList() throws Exception {
		call("select-inlo-online-state-list", makePara("inloClCd", "PLANT", "moClass", "SENSOR"));

	}

	public void selectInloAlarmStateList() throws Exception {
//		call("select-inlo-alarm-state-list", makePara("inloClCd", "PLANT", "moClass", "SENSOR"));
		call("select-inlo-alarm-state-list", makePara());
	}

}

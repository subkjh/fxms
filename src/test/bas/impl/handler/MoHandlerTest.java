package test.bas.impl.handler;

import java.util.Map;

public class MoHandlerTest extends HandlerTest {

	public static void main(String[] args) {

		try {

			MoHandlerTest c = new MoHandlerTest();
//			c.selectMoOnlineStateList();
//			c.selectMoAlarmStateList();
			c.testMo();

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

	public MoHandlerTest() throws Exception {
		super("localhost", "mo");
	}

	public void deleteMo() throws Exception {

	}

	public void detect() throws Exception {

	}

	public void getMo() throws Exception {

	}

	public void getMoConfig() throws Exception {

	}

	public void getMoCount() throws Exception {

	}

	public void getMoList() throws Exception {

	}

	public void insertMo() throws Exception {
	}

	public void selectMoAlarmStateList() throws Exception {

//		inloNo 설치위치번호 Y
//		moClass MO클래스 Y

		Map<String, Object> para = makePara("moClass", "SENSOR", "inloNo", 6000);
		call("select-mo-alarm-state-list", para);

	}

	public void selectMoOnlineStateList() throws Exception {

//		inloNo 설치위치번호 Y
//		moClass MO클래스 Y

		Map<String, Object> para = makePara("moClass", "SENSOR", "inloNo", 6000);
		call("select-mo-online-state-list", para);
	}

	public void setupMo() throws Exception {

	}

	public void testMo() throws Exception {
		Map<String, Object> para = makePara("moNo", 1000003);
		call("test-mo", para);

	}

	public void updateMo() throws Exception {

	}

}

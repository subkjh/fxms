package test.bas.api;

import fxms.bas.api.AppApi;

public class AppApiTest {

	public static void main(String[] args) {

		AppApiTest test = new AppApiTest();

		try {
			test.makeTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void makeTable() throws Exception {
		AppApi.getApi().checkStorage("test");
	}

	void getMoAll() throws Exception {

//		Map<String, Object> para = new HashMap<String, Object>();
//
//		List<EquipMo> moList = MoApi.getApi().selectMoListWithChildren(para, EquipMo.class,
//				new Class<?>[] { PortPingMo.class, StoragePingMo.class });
//
//		for (EquipMo mo : moList) {
//			System.out.println(mo.getSyncMo().getDebug());
//			System.out.println(mo.getSyncMo().getChildren(PortPingMo.class));
//			System.out.println(mo.getSyncMo().getChildren(StoragePingMo.class));
//		}

	}
}

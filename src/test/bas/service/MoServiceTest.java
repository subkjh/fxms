package test.bas.service;

import java.rmi.RemoteException;

import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.MoService;
import fxms.bas.fxo.service.MoServiceImpl;
import fxms.bas.fxo.service.UserService;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.api.MoApiService;
import subkjh.bas.co.utils.ObjectUtil;

public class MoServiceTest {
	public static void main(String[] args) throws RemoteException, FxServiceNotFoundException, Exception {

		MoServiceTest test = new MoServiceTest();

		test.testStart();

//		test.test3();

	}

	void test3() throws Exception {

		ServiceApi.getApi().setServiceUrl(MoService.class, "rmi://localhost:63810/" + MoService.class.getSimpleName());

		MoApi api = new MoApiService();

	}

	void test2() throws Exception {
		ServiceApi.getApi().setServiceUrl(UserService.class,
				"rmi://10.0.1.11:63810/" + UserService.class.getSimpleName());

		SessionVo vo = ServiceApi.getApi().getService(UserService.class).login("subkjh", "1111", null, "aa");
		System.out.println(ObjectUtil.toMap(vo));
	}

	void testStart() throws Exception {

		FxServiceImpl.start(MoService.class.getSimpleName(), MoServiceImpl.class,
				new String[] { ".", "fxservice.port=0" });

	}
}

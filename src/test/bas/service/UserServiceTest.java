package test.bas.service;

import java.rmi.RemoteException;

import fxms.bas.api.ServiceApi;
import fxms.bas.api.UserApi;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.UserService;
import fxms.bas.fxo.service.UserServiceImpl;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.api.UserApiService;
import subkjh.bas.co.utils.ObjectUtil;

public class UserServiceTest {
	public static void main(String[] args) throws RemoteException, FxServiceNotFoundException, Exception {

		UserServiceTest test = new UserServiceTest();

		test.testStart();
		
//		test.test3();

	}
	
	void test3() throws Exception
	{

		ServiceApi.getApi().setServiceUrl(UserService.class,
				"rmi://localhost:63810/" + UserService.class.getSimpleName());

		UserApi api = new UserApiService();
		SessionVo vo = api.login("subkjh", "1111", null, "aa");
		FxmsUtil.print(vo);
		Thread.sleep(10000);
		vo = api.reissueJwt(vo.getRefreshToken(), vo.getAccessToken());
		FxmsUtil.print(vo);
		
	}

	void test2() throws Exception {
		ServiceApi.getApi().setServiceUrl(UserService.class,
				"rmi://10.0.1.11:63810/" + UserService.class.getSimpleName());

		SessionVo vo = ServiceApi.getApi().getService(UserService.class).login("subkjh", "1111", null, "aa");
		System.out.println(ObjectUtil.toMap(vo));
	}

	void testStart() throws Exception {

		FxServiceImpl.start(UserService.class.getSimpleName(), UserServiceImpl.class,
				new String[] { ".", "fxservice.port=0" });

	}
}

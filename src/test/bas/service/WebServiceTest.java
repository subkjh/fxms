package test.bas.service;

import fxms.bas.api.ServiceApi;
import fxms.bas.api.UserApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.UserService;
import fxms.bas.fxo.service.WebService;
import fxms.bas.fxo.service.WebServiceImpl;
import fxms.bas.impl.api.UserApiDfo;
import fxms.bas.impl.dpo.co.InitLangDpo;

public class WebServiceTest {

	public static void main(String[] args) {

		new InitLangDpo().initLang();

		UserApi.api = new UserApiDfo();

		ServiceApi.getApi().setServiceUrl(UserService.class,
				"rmi://10.0.1.11:63810/" + UserService.class.getSimpleName());

		// ServiceApi.getApi().setServiceUrl(ValueService.class,
		// "rmi://10.0.1.11:63810/" + ValueService.class.getSimpleName());

		FxServiceImpl.start(WebService.class.getSimpleName(), WebServiceImpl.class,
				new String[] { ".", "fxservice.port=0" });
	}
}

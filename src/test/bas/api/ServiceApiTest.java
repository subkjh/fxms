package test.bas.api;

import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.FxCfg;

public class ServiceApiTest {

	public static void main(String[] args) {
		FxCfg.setProjectName("vup");
		ServiceApi api = ServiceApi.getApi();

		System.out.println(api.getServiceList());

	}

}

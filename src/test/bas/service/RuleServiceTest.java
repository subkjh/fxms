package test.bas.service;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.rule.RuleService;
import fxms.rule.RuleServiceImpl;

public class RuleServiceTest {
	public static void main(String[] args) {
//		ServiceApi.getApi().setServiceUrl(UserService.class, "rmi://10.0.1.11:63810/" + UserService.class.getSimpleName());
		FxServiceImpl.start(RuleService.class.getSimpleName(), RuleServiceImpl.class, new String[] { ".", "fxservice.port=0"} );
	}
}

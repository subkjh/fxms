package test.bas.api;

import fxms.bas.api.OpApi;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

public class OpApiTest {
	
	public static void main(String[] args) {

		Logger.logger.setLevel(LOG_LEVEL.trace);
		
		System.out.println(		OpApi.getApi().getOpCode("none"));
		System.out.println(		OpApi.getApi().setOpId(1, "none", true));
		System.out.println(		OpApi.getApi().getOpCode("none"));
		System.out.println(		OpApi.getApi().setOpId(1, "none", false));
		System.out.println(		OpApi.getApi().getOpCode("none"));
	}
}

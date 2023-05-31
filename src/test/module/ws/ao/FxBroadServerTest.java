package test.module.ws.ao;

import fxms.bas.fxo.FxCfg;
import fxms.bas.ws.ao.FxBroadServer;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

public class FxBroadServerTest {
	public static void main(String[] args) {

		FxCfg.DB_CONFIG = "VUPDB";
		FxCfg.DB_PSVALUE = FxCfg.DB_CONFIG;
		FxCfg.isTest = true;
		Logger.logger.setLevel(LOG_LEVEL.trace);

		FxBroadServer test = new FxBroadServer();
		test.startMember();

	}
}

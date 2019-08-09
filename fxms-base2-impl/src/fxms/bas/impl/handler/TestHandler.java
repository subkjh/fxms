package fxms.bas.impl.handler;

import java.util.Map;

import fxms.module.restapi.CommHandler;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.co.log.Logger;

public class TestHandler extends CommHandler {

	public Object test(SessionVo session, Map<String, Object> parameters) throws Exception {
		long ms = (long) (Math.random() * 100000L);

		Logger.logger.debug("sleep={}", ms);

		Thread.sleep(ms);

		return parameters;
	}

	public Object echo(SessionVo session, Map<String, Object> parameters) throws Exception {
		return parameters;
	}
}

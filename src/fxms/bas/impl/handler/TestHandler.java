package fxms.bas.impl.handler;

import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.AlarmHandlerQid;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

public class TestHandler extends BaseHandler {

	public Object echo(SessionVo session, Map<String, Object> parameters) throws Exception {
		return parameters;
	}

	public Object test(SessionVo session, Map<String, Object> parameters) throws Exception {
		long ms = (long) (Math.random() * 100000L);

		Logger.logger.debug("sleep={}", ms);

		Thread.sleep(ms);

		return parameters;
	}
	
	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao();
	}
}

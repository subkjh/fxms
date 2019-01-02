package fxms.bas.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.BasCfg;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;

public class MoHandlerDao {

	private DbTrans getTran() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createDbTrans(BasCfg.getHomeDeployConfSql("mo-handler.xml"));
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> selectMoCount(Map<String, Object> para) throws Exception {

		DbTrans tran = getTran();

		try {
			tran.start();
			List<Map<String, Object>> mapList = tran.selectQid2Res("SELECT_MO_COUNT_LIST", para);
			Map<String, Integer> ret = new HashMap<String, Integer>();
			for (Map<String, Object> map : mapList) {
				ret.put(map.get("MO_CLASS").toString(), ((Number)map.get("MO_COUNT")).intValue());
			}
			return ret;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}
}

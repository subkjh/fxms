package test.dao;

import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dao.AlarmCfgHandlerQid;
import subkjh.bas.BasCfg;
import subkjh.dao.QidDaoEx;

public class DaoTest {
	
	public static void main(String[] args) throws Exception {
		DaoTest test = new DaoTest();
		test.test();
	}

	void test() throws Exception {
		AlarmCfgHandlerQid qid = new AlarmCfgHandlerQid();
		QidDaoEx dao = QidDaoEx.open(BasCfg.getHome(AlarmCfgHandlerQid.QUERY_XML_FILE));
		List<?> list = dao.selectQid2Res(qid.select_alarm_cfg_simple_list, FxApi.makePara("userNo", 2));
		System.out.println(FxmsUtil.toJson(list));
		dao.close();
	}
}

package fxms.bas.dao;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.BasCfg;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;

public class LoginHandlerDao {

	private DbTrans getTran() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createDbTrans(BasCfg.getHomeDeployConfSql("login-handler.xml"));
	}

	/**
	 * 운용자 관할 MO를 생성한다.
	 * 
	 * @param userNo
	 *            운용자 번호
	 * @throws Exception
	 */
	public void makeUserMo(int userNo) throws Exception {

		DbTrans tran = getTran();
		String qid = "MAKE_USER_MO__";
		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("userNo", userNo);
			para.put("regDate", FxApi.getDate());

			int index = 1;
			while (tran.getSqlBean(qid + index) != null) {
				tran.execute(qid + index, para);
				index++;
			}

			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}

package fxms.bas.impl.dao;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class AppServiceDao {

	private AppServiceQid QID = new AppServiceQid();

	public static void main(String[] args) {
		try {
			new AppServiceDao().updateAutoLogout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AppServiceDao() {
	}

	/**
	 * 행위가 없는 세션을 로그아웃 처리한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public int updateAutoLogout() throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao(AppServiceQid.QUERY_XML_FILE);

		try {
			tran.start();
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("loginDtmStart", DateUtil.getDtm(System.currentTimeMillis() - 86400000L * 7));
			para.put("loginDtmEnd", DateUtil.getDtm(System.currentTimeMillis() - 3600000L));

			int ret = tran.execute(QID.update_auto_logout, para);
			tran.commit();
			
			Logger.logger.info("update.auto.logout={}, para={}", ret, para.toString());
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}
}

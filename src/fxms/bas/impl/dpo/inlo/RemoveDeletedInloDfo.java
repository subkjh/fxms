package fxms.bas.impl.dpo.inlo;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class RemoveDeletedInloDfo implements FxDfo<Void, Integer> {

	public static void main(String[] args) {
		try {
			RemoveDeletedInloDfo dfo = new RemoveDeletedInloDfo();
			dfo.removeInlo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer call(FxFact fact, Void data) throws Exception {
		return removeInlo();
	}

	public int removeInlo() throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			tran.executeSql(
					"delete from FX_CF_INLO_MEM where INLO_NO in ( select a.INLO_NO from FX_CF_INLO a where a.DEL_YN = 'Y' )");
			tran.executeSql(
					"delete from FX_CF_INLO_MEM where LOWER_INLO_NO in ( select a.INLO_NO from FX_CF_INLO a where a.DEL_YN = 'Y' )");
			tran.executeSql(
					"update FX_UR_USER set INLO_NO = 0 where INLO_NO in ( select a.INLO_NO from FX_CF_INLO a where a.DEL_YN = 'Y' )");
			tran.executeSql(
					"update FX_MO set INLO_NO = 0 where INLO_NO in ( select a.INLO_NO from FX_CF_INLO a where a.DEL_YN = 'Y' )");
			int ret = tran.executeSql("delete from FX_CF_INLO where DEL_YN = 'Y'");

			tran.commit();
			return ret;

		} catch (Exception e) {
			tran.rollback();
			Logger.logger.error(e);
			throw e;

		} finally {
			tran.stop();
		}
	}

}

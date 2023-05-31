package fxms.bas.impl.dpo.inlo;

import fxms.bas.api.FxApi;
import fxms.bas.exp.InloNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 삭제 플레그를 설정한다.
 * 
 * @author subkjh
 *
 */
public class SetDeleteFlagDfo implements FxDfo<Void, Boolean> {

	@Override
	public Boolean call(FxFact fact, Void data) throws Exception {

		int inloNo = fact.getInt("inloNo");
		String inloName = fact.getString("inloName");
		return setDeleteFlag(inloNo, inloName);
	}

	/**
	 * 
	 * @param inloNo
	 * @param inloName
	 * @return
	 * @throws Exception
	 */
	public boolean setDeleteFlag(int inloNo, String inloName) throws InloNotFoundException, Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_CF_INLO old = tran.selectOne(FX_CF_INLO.class, FxApi.makePara("inloNo", inloNo, "inloName", inloName));
			if (old != null) {

				int moCount = tran.selectCount(FX_MO.class, FxApi.makePara("delYn", "N", "inloNo in",
						" ( select LOWER_INLO_NO from FX_CF_INLO_MEM where INLO_NO = " + inloNo + " )"));

				int inloCount = tran.selectCount(FX_CF_INLO.class, FxApi.makePara("delYn", "N", "inloNo in",
						" ( select LOWER_INLO_NO from FX_CF_INLO_MEM where INLO_NO = " + inloNo + " and LOWER_DEPTH > 0 )"));

				int userCount = tran.selectCount(FX_UR_USER.class, FxApi.makePara("inloNo in",
						" ( select LOWER_INLO_NO from FX_CF_INLO_MEM where INLO_NO = " + inloNo + " )"));

				if (moCount > 0 || inloCount > 0 || userCount > 0) {
					throw new Exception(Lang.get(
							"There are sub-locations or management object under the location you want to delete."));
				}

				old.setDelYn("Y");
				tran.updateOfClass(FX_CF_INLO.class, old);
				tran.commit();
				return true;
			} else {
				throw new InloNotFoundException(inloName);
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

}

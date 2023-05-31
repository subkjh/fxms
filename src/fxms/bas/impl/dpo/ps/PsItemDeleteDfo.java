package fxms.bas.impl.dpo.ps;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 삭제처리된 성능항목을 저장소에서 제거한다.
 * 
 * @author subkjh
 *
 */
public class PsItemDeleteDfo implements FxDfo<Void, Boolean> {

	public static void main(String[] args) {
		PsItemDeleteDfo dfo = new PsItemDeleteDfo();
		dfo.removeDeletedPsItem();
	}

	@Override
	public Boolean call(FxFact fact, Void data) throws Exception {
		return null;
	}

	public void removeDeletedPsItem() {
		try {
			ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

			try {
				tran.start();

				tran.executeSql(
						"delete from FX_PS_STAT_FUNC where PS_ID in ( select a.PS_ID from FX_PS_ITEM a where a.USE_YN = 'N' )");
				tran.executeSql(
						"delete from FX_PS_STAT_CRE where PS_TBL in ( select a.PS_TBL from FX_PS_ITEM a where a.USE_YN = 'N' )");
				tran.executeSql("delete from FX_PS_ITEM where USE_YN = 'N'");

				tran.commit();

			} catch (Exception e) {
				tran.rollback();
				Logger.logger.error(e);
				throw e;

			} finally {
				tran.stop();
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

}

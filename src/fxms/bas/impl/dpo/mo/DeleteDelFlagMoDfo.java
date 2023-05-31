package fxms.bas.impl.dpo.mo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dbo.all.FX_AL_STAT_MO;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dbo.all.FX_V_ACUR;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.exp.TableNotFoundException;

public class DeleteDelFlagMoDfo implements FxDfo<Void, Integer> {

	public static void main(String[] args) {
		DeleteDelFlagMoDfo dfo = new DeleteDelFlagMoDfo();
		try {
			dfo.removeDeletedMo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer call(FxFact fact, Void data) throws Exception {
		return removeDeletedMo();
	}

	public int removeDeletedMo() throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			Map<String, Object> para = new HashMap<>();
			tran.start();
			int delCount = 0;

			List<FX_MO> list = tran.select(FX_MO.class, FxApi.makePara("delYn", "Y"));
			for (FX_MO mo : list) {

				para.put("moNo", mo.getMoNo());

				tran.deleteOfClass(FX_AL_ALARM_CUR.class, para);
				tran.deleteOfClass(FX_AL_ALARM_HST.class, para);
//					tran.deleteOfClass(FX_MX_WORK_HST.class, para);
				tran.deleteOfClass(FX_V_ACUR.class, para);
				tran.deleteOfClass(FX_AL_STAT_MO.class, para);

				if (mo.getMoClass().equals("MO") == false) {
					try {
						tran.deleteOfClass(MoDpo.getDboClass(mo.getMoClass()), para);
					} catch (TableNotFoundException e) {
					}
				} else {
					tran.deleteOfClass(FX_MO.class, para);
				}

				delCount++;

				tran.commit();
			}

			return delCount;

		} catch (Exception e) {
			tran.rollback();
			Logger.logger.error(e);
			throw e;

		} finally {
			tran.stop();
		}

	}
}

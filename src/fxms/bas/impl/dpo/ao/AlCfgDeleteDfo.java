package fxms.bas.impl.dpo.ao;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.UseObjectException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_CFG;
import fxms.bas.impl.dbo.all.FX_AL_CFG_MEM;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.AlCfgDeleteDto;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 알람 발생 조건 복사
 * 
 * @author subkjh
 *
 */
public class AlCfgDeleteDfo implements FxDfo<AlCfgDeleteDto, Boolean> {

	@Override
	public Boolean call(FxFact fact, AlCfgDeleteDto data) throws Exception {
		return delete(data);
	}

	public Boolean delete(AlCfgDeleteDto data) throws Exception {

		Map<String, Object> para = FxApi.makePara("alarmCfgNo", data.getAlarmCfgNo());

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			para.put("alarmCfgName", data.getAlarmCfgName());
			FX_AL_CFG cfg = tran.selectOne(FX_AL_CFG.class, para);
			if (cfg == null) {
				throw new Exception("alarm-cfg not found");
			}

			para.remove("alarmCfgName");
			int setCnt = tran.selectCount(FX_MO.class, para);
			if (setCnt > 0) {
				throw new UseObjectException("alarm-cfg", setCnt);
			}

			tran.deleteOfClass(FX_AL_CFG_MEM.class, para);
			tran.deleteOfClass(FX_AL_CFG.class, para);
			tran.commit();
			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}

package fxms.bas.impl.dpo.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.AlCfgNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_CFG;
import fxms.bas.impl.dbo.all.FX_AL_CFG_MEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmCfgMem;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.database.DBManager;

/**
 * 테스트 알람을 발생하는 DFO
 * 
 * @author subkjh
 *
 */
public class AlCfgSelectDfo implements FxDfo<Map<String, Object>, List<AlarmCfg>> {

	public static void main(String[] args) {

		AlCfgSelectDfo dfo = new AlCfgSelectDfo();
		FxFact fact = new FxFact();
		try {
			dfo.call(fact, FxApi.makePara("moClass", "SENSOR"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<AlarmCfg> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectAlarmCfg(data);
	}

	public List<AlarmCfg> selectAlarmCfg(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			List<FX_AL_CFG> cfgList = tran.select(FX_AL_CFG.class, para);
			List<FX_AL_CFG_MEM> memList = tran.select(FX_AL_CFG_MEM.class, para);

			List<AlarmCfg> list = new ArrayList<AlarmCfg>();
			for (FX_AL_CFG e : cfgList) {
				list.add(new AlarmCfg(e.getAlarmCfgNo(), e.getAlarmCfgName(), e.getMoClass(), e.getMoType(),
						e.isBasAlarmCfgYn()));
			}

			for (FX_AL_CFG_MEM e : memList) {
				for (AlarmCfg cfg : list) {
					if (cfg.getAlarmCfgNo() == e.getAlarmCfgNo()) {
						cfg.add(new AlarmCfgMem(e.getAlcdNo(), e.getAlCriCmprVal(), e.getAlMajCmprVal(),
								e.getAlMinCmprVal(), e.getAlWarCmprVal(), e.getReptTimes(), e.getFpactCd()));
						break;
					}
				}
			}

			return list;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public FX_AL_CFG selectAlCfg(int alCfgNo) throws Exception {

		FX_AL_CFG cfg = ClassDaoEx.SelectData(FX_AL_CFG.class, FxApi.makePara("alCfgNo", alCfgNo));
		if (cfg == null) {
			throw new AlCfgNotFoundException(alCfgNo);
		}

		return cfg;

	}
}

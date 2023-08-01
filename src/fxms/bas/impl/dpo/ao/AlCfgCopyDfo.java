package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_CFG;
import fxms.bas.impl.dbo.all.FX_AL_CFG_MEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.AlCfgCopyDto;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;


/**
 * 알람 발생 조건 복사
 * 
 * @author subkjh
 *
 */
public class AlCfgCopyDfo implements FxDfo<AlCfgCopyDto, Integer> {

	@Override
	public Integer call(FxFact fact, AlCfgCopyDto data) throws Exception {
		return copyAlarmCfg(data);
	}

	public int copyAlarmCfg(AlCfgCopyDto data) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("alarmCfgNo", data.getAlarmCfgNo());

			List<FX_AL_CFG> cfgList = tran.selectDatas(FX_AL_CFG.class, para);
			List<FX_AL_CFG_MEM> memList = tran.selectDatas(FX_AL_CFG_MEM.class, para);

			FX_AL_CFG item = cfgList.get(0);

			int alarmCfgNo = tran.getNextVal(FX_AL_CFG.FX_SEQ_ALARMCFGNO, Integer.class);
			item.setAlarmCfgNo(alarmCfgNo);
			item.setAlarmCfgName(data.getNewAlarmCfgName());
			item.setRegUserNo(item.getRegUserNo());
			item.setRegDtm(DateUtil.getDtm());
			item.setChgDtm(item.getRegDtm());
			item.setChgUserNo(item.getRegUserNo());

			tran.insertOfClass(item.getClass(), item);

			if (memList != null && memList.size() > 0) {
				for (FX_AL_CFG_MEM e : memList) {
					e.setAlarmCfgNo(alarmCfgNo);
				}
				tran.insertOfClass(FX_AL_CFG_MEM.class, memList);
			}

			tran.commit();

			return alarmCfgNo;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}
